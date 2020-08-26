package fr.codeonce.grizzlyhub.swagger.service;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerType;

public interface ISwaggerParser {
	//public String getVersion(JsonNode jsonBody);

	public SwaggerType getType(String cont) throws JsonMappingException, JsonProcessingException;
	public String getBody(String url) throws JSONException, IOException, Exception;

	public String getVersion(String cont) throws JsonMappingException, JsonProcessingException;
	public String getDiff(SwaggerDiffDto swagerDiffDto) throws IOException, Exception;	
	
	public String checkEndpointChanges(SwaggerDiffDto swagerDiffDto ,List<String> options) throws IOException, Exception;	
	
	public List<Swagger> getSwaggersFromFiles(MultipartFile[] files, String[] environments, String microserviceId) throws Exception;
	
	public boolean checkFile(MultipartFile file) throws IOException;
	public String getDiffForCompare(SwaggerDiffDto swagerDiffDto) throws IOException,Exception ;
	
}
