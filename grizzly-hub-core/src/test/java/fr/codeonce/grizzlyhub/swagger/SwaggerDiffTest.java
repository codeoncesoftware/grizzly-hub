package fr.codeonce.grizzlyhub.swagger;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.deepoove.swagger.diff.SwaggerDiff;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;



@SpringBootTest
public class SwaggerDiffTest {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ISwaggerParser swaggerParser; 
	
	private static final Logger log = LoggerFactory.getLogger(SwaggerDiffTest.class);
	private static final String oldUrl = "http://petstore.swagger.io/v2/swagger.json";
	private static final String newUrl = "http://petstore.swagger.io/v2/swagger.json";
	
/*	@Test
	public void testSwaggerDiff() throws IOException {
		SwaggerDiff diff = SwaggerDiff.compareV2(oldUrl, newUrl);
		log.info("diff" + diff);
		assertNotNull(diff);
	}*/
//	
//	@Test
//	public void TestOpenApiDiffService() throws Exception {
//		Swagger oldSwagger = new Swagger();
//		oldSwagger.setUrl(oldUrl);
//		Swagger newSwagger = new Swagger();
//		newSwagger.setUrl(newUrl);
//		mongoTemplate.save(oldSwagger);
//		mongoTemplate.save(newSwagger);
//		assertNotNull(newSwagger.getId());
//		assertNotNull(oldSwagger.getId());
//		SwaggerDiffDto diffDto = new SwaggerDiffDto(oldSwagger.getId(), newSwagger.getId());
//		String diff = swaggerParser.getDiff(diffDto);
//		log.info("Render diff from service ........." + diff);
//		assertNotNull(diff);
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode json = mapper.readTree(diff);
//		assertEquals(json.findValue("oldVersion"), json.findValue("newVersion"));
//	}
//	
//	@Test
//	public JsonNode getJsonFromFile() throws IOException {
//		Path resourceDirectory = Paths.get("src","test","resources", "swagger");
//		File file = new File(resourceDirectory.toFile(), "petshop.json");
//		log.info("exists ? " + file.exists());
//		JsonNode json = getJsonNodeFromFile(file);
//		log.info("json" + json);
//		return json ;
//	}
//	
//	public JsonNode getJsonNodeFromFile(File file) throws IOException {
//		// Prepare the Swagger Object
//		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
//		Object obj = yamlReader.readValue(file, Object.class);
//		log.info("output object from file" + obj);
//		
//		ObjectMapper jsonWriter = new ObjectMapper();
//		ObjectMapper mapper = new ObjectMapper();
//		JsonNode actualObj = mapper.readTree(jsonWriter.writeValueAsString(obj));
//		return actualObj;
//	}
	
	
}
