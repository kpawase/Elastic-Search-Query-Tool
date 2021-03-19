package com.querytool.service.impl;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.querytool.exception.ServiceException;
import com.querytool.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

	@Autowired
	RestHighLevelClient client;

	@Value("${index.name}")
	private String indexName;

	/**
	 * This method checks for index is present or not.
	 * 
	 */
	@Override
	public boolean checkIndexExists() throws ServiceException {

		LOGGER.debug(":::: Invoking checkIndexExists method");
		try {

			GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);

			if (client.indices().exists(getIndexRequest, RequestOptions.DEFAULT))
				return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

			throw new ServiceException(e.getMessage());
		}
		return false;
	}

	@Override
	public Set<String> getIndexKeys() throws ServiceException {
		try {

			if (!client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT))
				return new HashSet<String>();

			GetMappingsRequest getMappingsRequest = new GetMappingsRequest();
			getMappingsRequest.indices(indexName);

			GetMappingsResponse getMappingsResponse = client.indices().getMapping(getMappingsRequest,
					RequestOptions.DEFAULT);
			Map<String, MappingMetaData> allMappings = getMappingsResponse.mappings();

			System.out.println("indexName" + indexName);

			MappingMetaData indexMapping = allMappings.get(indexName);
			Map<String, Object> mapping = indexMapping.sourceAsMap();
			LinkedHashMap<String, Object> maps = (LinkedHashMap<String, Object>) mapping.get("properties");

			return maps.keySet();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			
			throw new ServiceException(e.getMessage());
		}

	}
}
