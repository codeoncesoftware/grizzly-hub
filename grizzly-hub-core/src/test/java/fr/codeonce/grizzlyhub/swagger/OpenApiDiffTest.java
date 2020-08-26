package fr.codeonce.grizzlyhub.swagger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;
import com.qdesrame.openapi.diff.output.MarkdownRender;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;

@SpringBootTest
public class OpenApiDiffTest {

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ISwaggerParser swaggerParser; 
	
	private static final Logger log = LoggerFactory.getLogger(OpenApiDiffTest.class);
	private static final String oldUrl = "https://idratherbewriting.com/learnapidoc/docs/rest_api_specifications/openapi_openweathermap.yml";
	private static final String newUrl = "https://petstore3.swagger.io/api/v3/openapi.json";
	
//	@Test
//	public void testOpenApiDiffLibrary() throws MalformedURLException, IOException {
//		 ChangedOpenApi diff = OpenApiCompare.fromLocations(oldUrl, newUrl);
//		 String render = new MarkdownRender().render(diff);
//		 log.info("Open Api render diff ........." + render);
//		 assertNotNull(render);
//		
//	}
//	@Test
//	public void TestOpenApiDiffService() throws  Exception {
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
//	}
	
}
