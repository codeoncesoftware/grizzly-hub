package fr.codeonce.grizzlyhub.swagger.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.codeonce.grizzlyhub.notification.service.INotificationService;

@Service
public class JsonStorage implements IJsonStoreService {

	private static final Logger logger = LoggerFactory.getLogger(JsonStorage.class);

	private final String apiUrl = "https://api.jsonbin.io/b";
	
	@Autowired
	private INotificationService notificationService;

	@Override
	public String storeJson(String content) throws JsonMappingException, JsonProcessingException {
		

		StopWatch stopWatch = new StopWatch();
		stopWatch.start("url construction");
		RestTemplate restTamplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type" ,  "application/json");
		headers.add("secret-key", "$2b$10$X70M4uEyidy.yleLB4yDtO3d9.hGbXE11ZewZnzs1Y2cjItHnMzsm");
		
		logger.info("content {}" , content);
		//logger.info("content saver {}" , notificationService.formatSwagger(content));
		HttpEntity<String> request = new HttpEntity<>(notificationService.formatSwagger(content) , headers);
		String resultUri;

		try {

			resultUri = restTamplate.postForObject(apiUrl, request, String.class);
			stopWatch.stop();

			logger.info("info watch: {}", stopWatch.getTotalTimeMillis());
			logger.info("uri result: {}", resultUri);
			@SuppressWarnings("unchecked")
			Map<String, String> mapResult = new ObjectMapper().readValue(resultUri, Map.class);
			logger.info("uri result: {}", mapResult.get("uri"));
			return mapResult.get("uri");

		} catch (HttpClientErrorException.BadRequest e) {
			String responseBodyAsString = e.getResponseBodyAsString();
			logger.error(responseBodyAsString);
		}

		throw new IllegalStateException("an error occurred with rest call : TODO");
	}

	
}
