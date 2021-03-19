package com.querytool.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.querytool.constants.QueryTypeConstants;
import com.querytool.exception.ServiceException;
import com.querytool.model.Query;
import com.querytool.model.QueryDto;
import com.querytool.service.QueryService;
import com.querytool.util.CommonUtils;

@Service
public class QueryServiceImpl implements QueryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryServiceImpl.class);

	@Autowired
	RestHighLevelClient client;

	@Value("${index.name}")
	private String indexName;

	/**
	 * This method build and executes the elastic search query based on parameters
	 * defined by user.
	 * 
	 * @throws ServiceException
	 * 
	 */
	@Override
	public List<Map<String,String>> queryImpl(QueryDto dto) throws ServiceException {

		LOGGER.debug(":::: Invoking queryImpl");

		LOGGER.debug(":::: Query Parameters: " + dto.toString());

		try {

			BoolQueryBuilder builder = new BoolQueryBuilder();

			Iterator<Query> iterator = dto.getQueries().iterator();

			// Building boolean query.

			while (iterator.hasNext()) {
				Query query = iterator.next();

				if (query.getMatchConstrain().equalsIgnoreCase(QueryTypeConstants.must)) {

					builder.must(getQueryBuilder(query));

				} else if (query.getMatchConstrain().equalsIgnoreCase(QueryTypeConstants.mustNot)) {

					builder.mustNot(getQueryBuilder(query));

				} else if (query.getMatchConstrain().equalsIgnoreCase(QueryTypeConstants.should)) {

					builder.should(getQueryBuilder(query));
				}
			}

			SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

			if (dto.getLimit() > 0)
				sourceBuilder.size(dto.getLimit());

			SearchRequest searchRequest = new SearchRequest(indexName).source(sourceBuilder.query(builder));

			// Query execution

			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			List<Map<String,String>> queryResult = new ArrayList<>();
			
			ObjectReader reader =  CommonUtils.getObjectMapper().readerFor(Map.class);

			Arrays.stream(searchResponse.getHits().getHits()).forEach(hits -> {

					try {
						queryResult.add(reader.readValue(hits.getSourceAsString()));
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}

			);

			return queryResult;

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());

		}

	}

	/**
	 * This method builds and returns the sub queries.
	 * 
	 * @param query
	 * @return
	 */
	private QueryBuilder getQueryBuilder(Query query) {

		LOGGER.debug(":::: Building Sub query for:" + query.toString());

		QueryBuilder builder = null;

		if (query.getQueryType().equalsIgnoreCase(QueryTypeConstants.matchAll)) {

			builder = QueryBuilders.matchAllQuery();

		} else if (query.getQueryType().equalsIgnoreCase(QueryTypeConstants.match)) {

			builder = QueryBuilders.matchQuery(query.getFieldName(), query.getFieldValue());

		} else if (query.getQueryType().equalsIgnoreCase(QueryTypeConstants.term)) {

			builder = QueryBuilders.termQuery(query.getFieldName(), query.getFieldValue());

		} else if (query.getQueryType().equalsIgnoreCase(QueryTypeConstants.wildCard)) {

			builder = QueryBuilders.wildcardQuery(query.getFieldName(), query.getFieldValue().toString());
		}

		return builder;

	}

	/**
	 * This method can be used to execute raw JSON query provided by User.
	 * 
	 * @throws ServiceException
	 * 
	 */
	@Override
	public String submitJsonQuery(QueryDto queryDto) throws ServiceException {

		LOGGER.debug(":::: Executing Raw json query: " + queryDto.getQueryString());

		try {

			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
			try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
					.createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), null, queryDto.getQueryString())) {
				searchSourceBuilder.parseXContent(parser);
			}


			SearchRequest searchRequest = new SearchRequest(indexName).source(searchSourceBuilder);

			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

			return CommonUtils.getObjectMapper().writeValueAsString(searchResponse);

		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);

			throw new ServiceException(e.getMessage());

		}

	}

}
