package fr.codeonce.grizzlyhub.swagger.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import fr.codeonce.grizzlyhub.microservices.controller.MicroserviceController;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.microservices.service.IMicroserviceService;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerCheckDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerService;

@RestController
@RequestMapping("/api/swagger")
public class SwaggerController {

	private static final Logger log = LoggerFactory.getLogger(SwaggerController.class);

	@Autowired
	private ISwaggerService swaggerService;
	@Autowired
	private SwaggerRepository SwaggerRepository;
	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private IMicroserviceService microService;

	@Autowired
	private ISwaggerParser swaggerParser;

//1
	@GetMapping("/swaggers")
	public List<Swagger> getSwaggers() {
		return SwaggerRepository.findAll();
	}

	// 2
	@GetMapping("/all-swaggers")
	public Page<Swagger> findAllSwaggers(@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "page") int page) {
		return swaggerService.getAllSwaggers(page, pageSize);
	}

	// 3
	@DeleteMapping("/deleteSwagger/{id}")
	public void deleteSwagger(@PathVariable(name = "id") String id) {
		swaggerService.delete(id);
	}

	// 4
	@GetMapping(value = "/{microserviceId}/swaggers", produces = "application/json")
	public List<Swagger> getVersions(@PathVariable String microserviceId) {
		log.info("id parameter: " + microserviceId);
		try {
			return swaggerService.getSwaggersWithLastVersionFromMicroservice(microserviceId);
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Microservice not found");
		}
	}

	// 5
	@GetMapping(value = "/{microserviceId}/{env}/swaggersVersions", produces = "application/json")
	public List<Swagger> getVersionsByEnvironment(@PathVariable String microserviceId, @PathVariable String env) {
		try {
			return swaggerService.getSwaggersVersionsFromEnvironmentAndMicroservice(env, microserviceId);
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Microservice not found");
		}
	}

	// 6
	@GetMapping(value = "/{microserviceId}/swaggersByEnv", produces = "application/json")
	public List<Swagger> getEnvs(@PathVariable String microserviceId) {
		try {
			return swaggerService.getSwaggersWithLastEnvironmentFromMicroservice(microserviceId);
		} catch (Exception e) {
			log.info("{}", e);
		}
		return null;
	}

	// 7
	@GetMapping(value = "/swaggersEnv", produces = "application/json")
	public List<Swagger> getVersionsByEnv(@RequestParam(name = "env") String env,
			@RequestParam(name = "microserviceId") String microserviceId) {

		return swaggerService.getSwaggersFromEnvironmentAndMicroservice(env, microserviceId);
	}

	// 8
	@PostMapping(value = "/add-swagger", consumes = "application/json", produces = "application/json")
	public Swagger addSwagger(@RequestBody SwaggerDto swaggerDto) throws Exception {

		log.info("Request params " + swaggerDto.getUrl());
		try {
			return swaggerService.saveSwagger(swaggerDto);
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid url");
		}
	}

	// 9
	@PostMapping("/swagger-diff")
	public String swaggerDiff(@RequestBody SwaggerDiffDto swaggerDiffDto) throws IOException {
		log.info("Params " + swaggerDiffDto.getNewSwaggerId() + " " + swaggerDiffDto.getNewSwaggerId());
		try {
			return swaggerParser.getDiff(swaggerDiffDto);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	// 10
	@PostMapping("/checkEndpoints")
	public String check(@RequestBody SwaggerDiffDto swaggerDiffDto) throws IOException {
		log.info("Params " + swaggerDiffDto.getNewSwaggerId() + " " + swaggerDiffDto.getNewSwaggerId());
		try {
			List<String> options = new ArrayList<String>();
			options.add("endpoints");
			System.out.println(options);
			return swaggerParser.checkEndpointChanges(swaggerDiffDto, options);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping(value = "/{microserviceId}/all-swaggers-ById", produces = "application/json")
	public List<Swagger> getAllSwaggers(@PathVariable String microserviceId) {
		log.info("id parameter: " + microserviceId);
		try {
			return swaggerService.getSwaggersFromMicroservice(microserviceId);
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Microservice not found");
		}
	}
	
	// 11
	@GetMapping(value = "/public/{microserviceId}/all-swaggers-ById", produces = "application/json")
	public List<Swagger> getAllSwaggersPublic(@PathVariable String microserviceId) {
		log.info("id parameter: " + microserviceId);
		try {
			return swaggerService.getSwaggersFromMicroservice(microserviceId);
		} catch (NullPointerException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Microservice not found");
		}
	}

	// 12
	@PostMapping(value = "/checkSwagger", consumes = { "multipart/form-data" })
	public SwaggerCheckDto checkSwagger(@RequestPart SwaggerCheckDto swagger,
			@RequestPart(required = false) MultipartFile file) throws IOException {
		try {
			log.info("params: {}", swagger.getEnvironment());
			log.info("url: {}", swagger.getUrl());
			if (swagger.getUrl() != null)
				swaggerParser.getBody(swagger.getUrl());
			if (file != null && !swaggerParser.checkFile(file))
				throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Upload swagger file");
			return swagger;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
		}
	}

	// 13
	@GetMapping("/swaggerById/{id}")
	public Swagger swaggerById(@PathVariable String id) {
		return SwaggerRepository.findById(id).get();
	}

}
