package com.querytool.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.querytool.exception.ServiceException;
import com.querytool.model.QueryDto;
import com.querytool.model.ResponseDto;
import com.querytool.service.IndexService;
import com.querytool.service.QueryService;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/")
public class QueryToolController {

	
	@Autowired
	IndexService indexService;
	
	@Autowired
	QueryService queryService;
	
	
	@PostMapping(value = "/query",produces = "application/json" , consumes = "application/json")
	public ResponseDto query(@RequestBody QueryDto queryDto) throws ServiceException {
		
		
		return new ResponseDto(HttpStatus.SC_OK, "Query Executed Successfully", queryService.queryImpl(queryDto));
	}
	
	@PostMapping(value = "/jsonquery",produces = "application/json" , consumes = "application/json")
	public ResponseDto submitJsonQuery(@RequestBody QueryDto queryDto) throws ServiceException { 


		return new ResponseDto(HttpStatus.SC_OK, "Query Executed Successfully", queryService.submitJsonQuery(queryDto));
		
	}

	
}
	