package fr.codeonce.grizzlyhub.swagger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IJsonStoreService {
	public String storeJson(String json) throws JsonMappingException, JsonProcessingException;
}
