package fr.codeonce.grizzlyhub.swagger.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.deepoove.swagger.diff.SwaggerDiff;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.qdesrame.openapi.diff.OpenApiCompare;
import com.qdesrame.openapi.diff.model.ChangedOpenApi;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerType;
import io.swagger.models.Path;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;

@Service
public class SwaggerParserImpl implements ISwaggerParser {

	private static final Logger log = LoggerFactory.getLogger(SwaggerParserImpl.class);

	@Autowired
	SwaggerRepository swaggerRepository;
	
	@Autowired
	IJsonStoreService jsonStore;
	
	@Value("${frontUrlForSwagger}")
	private String url;

	@Override
	public String getVersion(String cont) throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonBody = mapper.readTree(cont);

		return jsonBody.findValue("info").findValue("version").asText();
	}

	@Override
	public SwaggerType getType(String cont) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonBody = mapper.readTree(cont);
		log.info("json body " + jsonBody.has("openapi"));
		if (jsonBody.has("openapi")) {
			return SwaggerType.OpenApi;
		} else if (jsonBody.has("swagger")) {
			return SwaggerType.Swagger;
		} else
			throw new NoSuchElementException("Url invalid !");
	}
	

	@Override
	public String getBody(String url) throws Exception {
		String cont = "";
		String swaggerUrl = url.replace("swagger-ui.html", "v2/api-docs");
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		try {
			String content = IOUtils.toString(new URL(swaggerUrl), Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new Exception("l'url: " + swaggerUrl + " est invalide");
		}
		String content = IOUtils.toString(new URL(swaggerUrl), Charset.forName("UTF-8"));
		if (!content.isEmpty()) {
			Object obj = yamlReader.readValue(content, Object.class);
			ObjectMapper jsonWriter = new ObjectMapper();
			cont = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		}
		return cont;
	}

	@Override
	public String getDiff(SwaggerDiffDto swagerDiffDto) throws Exception {
		String oldUrl = swaggerRepository.findById(swagerDiffDto.getOldSwaggerId()).get().getUrl();
		String newUrl = swaggerRepository.findById(swagerDiffDto.getNewSwaggerId()).get().getUrl();

		Swagger oldSwagger = swaggerRepository.findById(swagerDiffDto.getOldSwaggerId()).get();
		Swagger newSwagger = swaggerRepository.findById(swagerDiffDto.getNewSwaggerId()).get();

		SwaggerType oldSwaggerType = null;
		oldSwaggerType = getType(getBody(oldUrl));
		SwaggerType newSwaggerType = null;
		newSwaggerType = getType(getBody(newUrl));
		log.info("Swagger Types " + oldSwaggerType + " " + newSwaggerType);
		ObjectMapper mapper = new ObjectMapper();

		if (!oldSwaggerType.equals(newSwaggerType)) {
			throw new NoSuchElementException("Swagger have not the same type !");
		} else if (oldSwaggerType.equals(SwaggerType.Swagger) && newSwaggerType.equals(SwaggerType.Swagger)) {
			ObjectMapper jsonMapper = new ObjectMapper();

			SwaggerDiff swaggerDiff = SwaggerDiff.compareV2(mapper.readTree(oldSwagger.getContent()),
					mapper.readTree(newSwagger.getContent()));
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(swaggerDiff);

		} else if (oldSwaggerType.equals(SwaggerType.OpenApi) && newSwaggerType.equals(SwaggerType.OpenApi)) {

			ChangedOpenApi openApiDiff = OpenApiCompare.fromContents(oldSwagger.getContent(), newSwagger.getContent());
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(openApiDiff);
		}
		throw new NullPointerException("Swagger Type unknown");
	}

	@Override
	public String getDiffForCompare(SwaggerDiffDto swagerDiffDto) throws Exception {
		String oldUrl = swaggerRepository.findById(swagerDiffDto.getOldSwaggerId()).get().getUrl();
		String newUrl = swaggerRepository.findById(swagerDiffDto.getNewSwaggerId()).get().getUrl();
		SwaggerType oldSwaggerType = getType(getBody(oldUrl));
		SwaggerType newSwaggerType = getType(getBody(newUrl));
		log.info("Swagger Types " + oldSwaggerType + " " + newSwaggerType);
		ObjectMapper mapper = new ObjectMapper();
		if (!oldSwaggerType.equals(newSwaggerType)) {
			throw new NoSuchElementException("Swagger have not the same type !");
		} else if (oldSwaggerType.equals(SwaggerType.Swagger) && newSwaggerType.equals(SwaggerType.Swagger)) {
			SwaggerDiff swaggerDiff = SwaggerDiff.compareV2(oldUrl, newUrl);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(swaggerDiff);
		} else if (oldSwaggerType.equals(SwaggerType.OpenApi) && newSwaggerType.equals(SwaggerType.OpenApi)) {
			ChangedOpenApi openApiDiff = OpenApiCompare.fromLocations(oldUrl, newUrl);
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(openApiDiff);
		}
		throw new NullPointerException("Swagger Type unknown");
	}
	public String checkEndpointChanges(SwaggerDiffDto swagerDiffDto, List<String> options)
			throws IOException, Exception {
		Boolean changed = false;
		String message = "";
		String diff = getDiff(swagerDiffDto);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonBody = mapper.readTree(diff);
		System.out.println(options);
		if (options.equals(null)) {
			return "";
		} else {
			if (options.contains("endpoints")) {
				if (!jsonBody.get("newEndpoints").isEmpty()) {
					return "new";
				}
				if (!jsonBody.get("missingEndpoints").isEmpty()) {
					return "missing";
				}
			}
			if (options.contains("params")) {
				if (!jsonBody.get("changedEndpoints").isEmpty())  {
					return "params";
				}
			}
		}
		return message;
	}

	@Override
	public List<Swagger> getSwaggersFromFiles(MultipartFile[] files, String[] environments, String microserviceId)
			throws Exception {
		List<Swagger> swaggerList = new ArrayList<>();
		
		if (files.length != environments.length || files.length == 0 || environments.length == 0) {
			throw new IllegalArgumentException("file or environment missing");
		} else {
			for (int i = 0; i < files.length; i++) {
				Swagger swagger = new Swagger();
				ByteArrayInputStream stream = new ByteArrayInputStream(files[i].getBytes());
				String cont = IOUtils.toString(stream, "UTF-8");
				ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
				Object obj = yamlReader.readValue(cont, Object.class);
				ObjectMapper jsonWriter = new ObjectMapper();
				String string = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
				log.info("file input: {}", string);
				swagger.setMicroserviceId(microserviceId);
				swagger.setEnvironment(environments[i]);
				swagger.setContent(string);
				swagger.setSwaggerType(getType(string));
				swagger.setVersion(getVersion(string));
				Swagger savedSwagger = swaggerRepository.save(swagger);
				savedSwagger.setUrl(url+"/api/microservice/content/"+savedSwagger.getId());
				swaggerRepository.save(savedSwagger);
				swaggerList.add(swagger);
			}
		}

		return swaggerList;
	}
	
	@Override
	public boolean checkFile(MultipartFile file) throws IOException {
		
		ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
		String string = IOUtils.toString(stream);
//		log.info(string);
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = yamlReader.readValue(string, Object.class);
		ObjectMapper jsonWriter = new ObjectMapper();
		String cont = jsonWriter.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		SwaggerType type = getType(cont);
		return type != null ? true : false;
	}
	
	public String convertYamlToJson(String yaml) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
	    Object obj = yamlReader.readValue(yaml, Object.class);

	    ObjectMapper jsonWriter = new ObjectMapper();
	    return jsonWriter.writeValueAsString(obj);
	}

}
