package com.querytool.service;

import java.util.Set;

import com.querytool.exception.ServiceException;

public interface IndexService {

	public boolean checkIndexExists() throws ServiceException;
	
	public Set<String> getIndexKeys() throws ServiceException;
	
}
