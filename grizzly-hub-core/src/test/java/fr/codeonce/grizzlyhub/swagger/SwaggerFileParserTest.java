package fr.codeonce.grizzlyhub.swagger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;

@SpringBootTest
public class SwaggerFileParserTest {
 
	private static final Logger logger = LoggerFactory.getLogger(SwaggerFileParserTest.class);
	private final String apiUrl = "https://extendsclass.com/api/json-storage/bin";

	@Autowired
    private WebApplicationContext webApplicationContext;
	@Autowired
	ISwaggerParser swaggerParser;

	@Test
	public void testPersistContainer(@Autowired MongoTemplate mongoTemplate) throws IOException {
		Swagger swagger = new Swagger();
		mongoTemplate.save(swagger);
		logger.info("check persisted swagger by id: " + swagger.getId());
		assertNotNull(swagger.getId());
		
	}
	
	
	
	@Test
	public void storeYaml() throws JSONException, IOException, Exception {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("url construction");
		File swaggerYaml = ResourceUtils.getFile("classpath:swagger/swagger.yaml");
		String fileContent = new String(
			      Files.readAllBytes(swaggerYaml.toPath()));
		logger.info("content to convert "  + fileContent);
		
		RestTemplate restTamplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(fileContent);
		ResponseEntity<String> result;
		String resultUri;
		
		try {
			
			resultUri = restTamplate.postForObject(apiUrl, request, String.class);
			stopWatch.stop();

			
			logger.info("info watch: {}", stopWatch.getTotalTimeMillis());
			logger.info("uri result: {}", resultUri);
			@SuppressWarnings("unchecked")
			Map<String, String> mapResult = new ObjectMapper().readValue(resultUri, Map.class);
			logger.info("uri result: {}", mapResult.get("uri"));
			assertNotNull(mapResult.get("uri"));
			
		} catch (HttpClientErrorException.BadRequest e) {
			String responseBodyAsString = e.getResponseBodyAsString();
			logger.error(responseBodyAsString);
		} 
	}
	
	@Test
	public void storeJson() throws JSONException, IOException, Exception {
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("url construction");
		String openApiUrl = "https://petstore.swagger.io/v2/swagger.json";
		String content = swaggerParser.getBody(openApiUrl);
		
		logger.info("content to convert "  + content);
		
		RestTemplate restTamplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(content);
		ResponseEntity<String> result;
		String resultUri;
		
		try {
			resultUri = restTamplate.postForObject(apiUrl, request, String.class);
			stopWatch.stop();

			
			logger.info("info watch: {}", stopWatch.getTotalTimeMillis());
			logger.info("uri result: {}", resultUri);
			@SuppressWarnings("unchecked")
			Map<String, String> mapResult = new ObjectMapper().readValue(resultUri, Map.class);
			logger.info("uri result: {}", mapResult.get("uri"));
			assertNotNull(mapResult.get("uri"));
			
		} catch (HttpClientErrorException.BadRequest e) {
			String responseBodyAsString = e.getResponseBodyAsString();
			logger.error(responseBodyAsString);
		} 
	}
	
	@Test 
	public void getTypeFromYaml() throws IOException{
		File swaggerYaml = ResourceUtils.getFile("classpath:swagger/swagger.yaml");
		String fileContent = new String(
			      Files.readAllBytes(swaggerYaml.toPath()));
		String[] result = fileContent.split("\n", 2);
		String json = convertYamlToJson(fileContent);
		assertNotNull(json);
		
	}
	
	String convertYamlToJson(String yaml) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
	    Object obj = yamlReader.readValue(yaml, Object.class);

	    ObjectMapper jsonWriter = new ObjectMapper();
	    return jsonWriter.writeValueAsString(obj);
	}
}
