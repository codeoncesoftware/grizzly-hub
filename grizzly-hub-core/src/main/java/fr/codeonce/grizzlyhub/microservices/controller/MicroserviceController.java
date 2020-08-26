package fr.codeonce.grizzlyhub.microservices.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;

import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.microservices.service.IMicroserviceService;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerCheckDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerListDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerToShow;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerService;

@RestController
@RequestMapping("/api/microservice")
public class MicroserviceController {

	@Autowired
	private Environment env;

	private static final Logger log = LoggerFactory.getLogger(MicroserviceController.class);

	@Autowired
	private MicroserviceRepository microserviceRepository;

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

	@Autowired
	private ISubscriptionService subscriptionService;

	@GetMapping("/search/{keyword}")
	public List<Microservice> findByKeyWord(@PathVariable String keyword) {
		System.out.println();
		return microService.findAllByKeyWord(keyword)
				.stream().filter(m -> m.getType().equalsIgnoreCase("public") | m.getOwner().equalsIgnoreCase(SecurityContextUtil.getCurrentUserEmail()))
				.collect(Collectors.toList());
	}

	@GetMapping("/content/{id}")
	public JsonNode getContent(@PathVariable String id) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(SwaggerRepository.findById(id).get().getContent());
	}

	// 1
	@GetMapping("/swaggers")
	public List<Swagger> getSwaggers(HttpServletRequest request) throws MalformedURLException {
		// public String getURLBase(HttpServletRequest request) throws
		// MalformedURLException {
		String path = env.getProperty("frontUrl");
		log.info(path);
		URL requestURL = new URL(request.getRequestURL().toString());
		String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
		log.info(requestURL.getProtocol() + "://" + requestURL.getHost() + port);

		// }
		return SwaggerRepository.findAll();
	}

	@GetMapping("/all")
	public ResponseEntity<Page<Microservice>> findAllMicroservices(@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "page") int page) {
		return ResponseEntity.ok(microService.findAll(page, pageSize));
	}
	
	@GetMapping("/public/allPublic")
	public List<Microservice> findAllPublicMicroservices(@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "page") int page) {
		return microService.findAllByType(page,pageSize);
	}


	// 2
	@GetMapping("/all-swaggers")
	public Page<Swagger> findAllSwaggers(@RequestParam(name = "pageSize") int pageSize,
			@RequestParam(name = "page") int page) {
		return swaggerService.getAllSwaggers(page, pageSize);
	}

	@GetMapping("/find/{id}")
	public Microservice findById(@PathVariable(name = "id") String id) {
		return microService.findById(id);
	}

	@PostMapping("/create")
	public ResponseEntity<Microservice> saveMicroservice(@RequestBody Microservice microservice) {
		return ResponseEntity.ok(microService.save(microservice));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Microservice> update(@RequestBody Microservice microservice,
			@PathVariable(name = "id") String id) {
		return ResponseEntity.ok(microService.update(microservice, id));
	}

	@DeleteMapping("/delete/{id}")
	public void deleteMicroservice(@PathVariable(name = "id") String id) {
		subscriptionRepository.findAllByMicroserviceId(id).forEach(subscription -> {
			subscriptionService.delete(subscription.getId());
		});
		SwaggerRepository.findAllByMicroserviceId(id).forEach(swagger -> {
			log.info("swagger {}", id);
			swaggerService.delete(id);
		});
		microService.delete(id);
	}

//3
	@DeleteMapping("/deleteSwagger/{id}")
	public void deleteSwagger(@PathVariable(name = "id") String id) {
		swaggerService.delete(id);
	}

//4
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
	
	// 6
	@GetMapping(value = "/{microserviceId}/swaggerDetails", produces = "application/json")
	public SwaggerToShow getswaggerDetails(@PathVariable String microserviceId) {
		try {
			return swaggerService.getSwaggerFromMicroservice(microserviceId);
		} catch (Exception e) {
			log.info("{}", e);
		}
		return null;
	}
	
	@GetMapping(value = "/public/{microserviceId}/swaggerDetails", produces = "application/json")
	public SwaggerToShow getswaggerDetailsPublic(@PathVariable String microserviceId) {
		try {
			return swaggerService.getSwaggerFromMicroservice(microserviceId);
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

	@PostMapping(value = "/add-swaggers", consumes = { "multipart/form-data" }, produces = "application/json")
	public SwaggerListDto addSwagger(@RequestPart SwaggerListDto data,
			@RequestPart(required = false) MultipartFile[] files, @RequestParam(required = false) String[] environments)
			throws Exception {

		log.info("Request params " + data.getMicroservice().getTitle());
		try {
			StopWatch watch1 = new StopWatch();
			watch1.start("task2");
			SwaggerListDto result = swaggerService.addMicroServiceAndSwaggers(data);
			watch1.stop();
			log.info("timing :{}", watch1.getTotalTimeMillis());
			if (files.length != 0 && environments.length != 0) {
				List<Swagger> parsedSwaggers = swaggerService.addSwaggerFiles(files, environments,
						data.getMicroservice());
				result.getSwaggers().addAll(parsedSwaggers);
			}

			return result;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
		}

	}

	@PutMapping(value = "/update-all-swaggers", consumes = { "multipart/form-data" }, produces = "application/json")
	public SwaggerListDto updateSwaggerList(@RequestPart SwaggerListDto data,
			@RequestPart(required = false) MultipartFile[] files, @RequestParam(required = false) String[] environments)
			throws JSONException, IOException {

		log.info("Request params " + data.getMicroservice().getTitle());
		try {
			SwaggerListDto result = swaggerService.updateSwaggerList(data);
			if (files.length != 0 && environments.length != 0) {
				List<Swagger> parsedSwaggers = swaggerService.addSwaggerFiles(files, environments,
						data.getMicroservice());
				result.getSwaggers().addAll(parsedSwaggers);
			}
			return result;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
	
	@PostMapping("/compare-diff")
	public String compareDiff(@RequestBody SwaggerDiffDto swaggerDiffDto) throws IOException {
		log.info("Params " + swaggerDiffDto.getNewSwaggerId() + " " + swaggerDiffDto.getNewSwaggerId());
		try {
			return swaggerParser.getDiffForCompare(swaggerDiffDto);
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

	// 11
	@GetMapping(value = "/{microserviceId}/all-swaggers-ById", produces = "application/json")
	public List<Swagger> getAllSwaggers(@PathVariable String microserviceId) {
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
			throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "hellooo");
		}
	}

	@GetMapping("/subscribed-microservices")
	public CompletableFuture<List<Microservice>> subscribedMicroservices() {
		try {
			String email = SecurityContextUtil.getCurrentUserEmail();
			return subscriptionService.getSubscribedMicroservicesForConnectedUser(email);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/only/{title}")
	public Boolean onlyOrganisation(@PathVariable String title) {
		return microserviceRepository.existsByTitleIgnoreCase(title);
	}

	// 13
	@GetMapping("/swaggerById/{id}")
	public Swagger swaggerById(@PathVariable String id) {
		return SwaggerRepository.findById(id).get();
	}
	
	@GetMapping("/public/findByName/{title}")
	public Microservice findByTitle(@PathVariable String title) {
		return microService.findByTitle(title);
	}

	
}
