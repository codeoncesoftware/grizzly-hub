package fr.codeonce.grizzlyhub.swagger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import fr.codeonce.grizzlyhub.swagger.domain.SwaggerType;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;

@SpringBootTest
public class SwaggerPersistTest {

	private static final Logger log = LoggerFactory.getLogger(SwaggerPersistTest.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ISwaggerParser swaggerParser;

	@SuppressWarnings("unlikely-arg-type")
	@DisplayName("Test Spring @Autowired Integration")
	@Test
	public void persist() throws Exception {
		Swagger container = new Swagger();
		mongoTemplate.save(container, "container");
		assertNotNull(container.getId());
	}

	@Test
	public void testContainerItemPersist() {
		Swagger item = new Swagger();
		mongoTemplate.save(item);
		assertNotNull(item.getId());
	}

	@Test
	public void testItemPersist() {
		Swagger swagger = new Swagger();
		Microservice microService = new Microservice();
		mongoTemplate.save(microService);
		swagger.setMicroserviceId(microService.getId());
		mongoTemplate.save(swagger); // update swagger
		Boolean exist = swagger.getMicroserviceId() == microService.getId();
		assertTrue(exist);
	}
	
	@Test
	public void testOpenApiTypeCheck() throws Exception {
		String openApiUrl = "https://petstore3.swagger.io/api/v3/openapi.json";
		String content = swaggerParser.getBody(openApiUrl);
		String version = swaggerParser.getVersion(content);
		Swagger swagger = new Swagger();
		swagger.setContent(content);
		swagger.setVersion(version);
		mongoTemplate.save(swagger);
		log.info("OpenApi Type from Url check: " + swagger.getContent());
		assertEquals(swagger.getContent(), content);
		assertNotNull(swagger.getId());
	}

	@Test
	public void testSwaggerTypeCheck() throws  Exception {
		String swaggerUrl = "https://petstore.swagger.io/v2/swagger.json";
		String content = swaggerParser.getBody(swaggerUrl);
		Swagger swagger = new Swagger();
		log.info("Swagger Type from Url check : " + content);
		assertNotNull(swagger);
	}

}
