package com.querytool.service;

import java.util.List;
import java.util.Map;

import com.querytool.exception.ServiceException;
import com.querytool.model.QueryDto;

public interface QueryService {
	
public List<Map<String,String>>  queryImpl(QueryDto dto) throws ServiceException;

public String submitJsonQuery(QueryDto queryDto) throws ServiceException;



}
