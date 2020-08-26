package fr.codeonce.grizzlyhub.swagger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.codeonce.grizzlyhub.auth.service.util.EmailService;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.microservices.service.IMicroserviceService;
import fr.codeonce.grizzlyhub.notification.domain.Notification;
import fr.codeonce.grizzlyhub.notification.service.INotificationService;
import fr.codeonce.grizzlyhub.swagger.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SwaggerServiceImpl implements ISwaggerService {

	private static final Logger log = LoggerFactory.getLogger(SwaggerServiceImpl.class);

	@Value("${frontUrlForSwagger}")
	private String url;

	@Autowired
	ISwaggerParser swaggerParser;

	@Autowired
	SwaggerRepository swaggerRepository;

	@Autowired
	SubscriptionRepository SubscriptionRepository;
	
	@Autowired
	MicroserviceRepository microserviceRepository;

	@Autowired
	IMicroserviceService microserviceService;

	@Autowired
	INotificationService notificationService;

	@Autowired
	IJsonStoreService jsonStore;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Qualifier("webApplicationContext")
	@Autowired
	private ResourceLoader resourceloader;

	@Value("${frontUrl}")
	private String frontUrl;

	@Override
	public Swagger saveSwagger(SwaggerDto swaggerDto) throws Exception {
		Swagger swagger = new Swagger();
		swagger.setOwner(SecurityContextUtil.getCurrentUserEmail());
		swagger.setVersion(swaggerParser.getVersion(swaggerParser.getBody(swaggerDto.getUrl())));
		swagger.setSwaggerType(swaggerParser.getType(swaggerParser.getBody(swaggerDto.getUrl())));
		swagger.setEnvironment(swaggerDto.getEnvironment());
		swagger.setUrl(swaggerDto.getUrl());
		swagger.setMicroserviceId(swaggerDto.getMicroserviceId());
		String content = swaggerParser.getBody(swaggerDto.getUrl());
		swagger.setContent(content);
		Swagger savedSwagger = swaggerRepository.save(swagger);
		return swaggerRepository.save(savedSwagger);
	}

	@Override
	public Swagger saveSwaggerForNotification(SwaggerDto swaggerDto) throws Exception {
		Swagger swagger = new Swagger();
		swagger.setVersion(swaggerParser.getVersion(swaggerParser.getBody(swaggerDto.getUrl())));
		swagger.setSwaggerType(swaggerParser.getType(swaggerParser.getBody(swaggerDto.getUrl())));
		swagger.setEnvironment(swaggerDto.getEnvironment());
		swagger.setUrl(swaggerDto.getUrl());
		swagger.setOwner(swaggerDto.getOwner());
		swagger.setMicroserviceId(swaggerDto.getMicroserviceId());
		String content = swaggerParser.getBody(swaggerDto.getUrl());
		swagger.setContent(content);
		Swagger savedSwagger = swaggerRepository.save(swagger);
		return swaggerRepository.save(savedSwagger);
	}

	@Override
	public Page<Swagger> getAllSwaggers(int page, int pageSize) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
		return swaggerRepository.findAll(firstPageWithTwoElements);
	}

	@Override
	public List<Swagger> getSwaggersFromMicroservice(String microserviceId) {
		return swaggerRepository.findAllByMicroserviceIdOrderByLastUpdateDate(microserviceId);
	}

	@Override
	public List<Swagger> getSwaggersToShow(String microserviceId) {
		List<Swagger> swaggers = swaggerRepository
				.findAllByMicroserviceIdOrderByLastUpdateDateDescEnvironmentDesc(microserviceId);
		List<Swagger> swaggersToSend = new ArrayList<Swagger>();
		if (swaggers.size() != 0) {
			swaggersToSend.add(swaggers.get(0));
			for (int i = 1; i < swaggers.size(); i++) {
				if (!swaggers.get(i - 1).getEnvironment().equals(swaggers.get(i).getEnvironment())) {
					swaggersToSend.add(swaggers.get(i));
				}
			}
		}

		return swaggersToSend;
	}

	@Override
	public SwaggerToShow getSwaggerFromMicroservice(String microserviceId) {
		ObjectMapper mapper = new ObjectMapper();
		List<Swagger> swaggers = getSwaggersToShow(microserviceId);
		SwaggerToShow swaggerToShow = new SwaggerToShow();
		swaggerToShow.setSwaggerType(swaggers.get(0).getSwaggerType().toString());
		swaggerToShow.setSwaggerVersion(swaggers.get(0).getVersion());
		swaggerToShow.setInProd(false);
		swaggers.forEach(swagger -> {
			try {
				if(swagger.getEnvironment().equalsIgnoreCase("PROD")) {
					swaggerToShow.setInProd(true);
				}
				JsonNode jsonBody = mapper.readTree(swagger.getContent());
				swaggerToShow.setSwaggerName(jsonBody.get("info").get("title").asText());
				if (swagger.getSwaggerType().toString().equals("Swagger")) {
					log.info("{}", jsonBody.get("info").get("title"));
					swaggerToShow.setSwaggerAuth(jsonBody.get("securityDefinitions"));
				} else if (swagger.getSwaggerType().toString().equals("OpenApi")) {
					swaggerToShow.setSwaggerAuth(jsonBody.get("components").get("securitySchemes"));
				}
				//add last updated date of last swagger
				swaggerToShow.setLastUpdated(getLastModifiedDateSwagger(swaggers));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (swagger.getEnvironment().trim().equals("prod")) {
				swaggerToShow.setInProd(true);
			}
		});
		return swaggerToShow;
	}

	@Override
	public List<Swagger> getSwaggersFromEnvironmentAndMicroservice(String environment, String microserviceId) {
		return swaggerRepository.findAllByEnvironmentAndMicroserviceIdOrderByLastUpdateDate(environment,
				microserviceId);
	}

	@Override
	public List<Swagger> getSwaggersVersionsFromEnvironmentAndMicroservice(String environment, String microserviceId) {
		List<Swagger> swaggers = swaggerRepository
				.findAllByEnvironmentAndMicroserviceIdOrderByVersionDescLastUpdateDateDesc(environment, microserviceId);
		List<Swagger> swaggersToSend = new ArrayList<Swagger>();
		if (swaggers.size() != 0) {
			swaggersToSend.add(swaggers.get(0));
			for (int i = 1; i < swaggers.size(); i++) {
				if (!swaggers.get(i - 1).getVersion().equals(swaggers.get(i).getVersion())) {
					swaggersToSend.add(swaggers.get(i));
				}
			}
		}

		return swaggersToSend;
	}

	@Override
	public List<Swagger> getSwaggersWithLastEnvironmentFromMicroservice(String microserviceId) {
		List<Swagger> swaggers = swaggerRepository
				.findAllByMicroserviceIdOrderByEnvironmentDescLastUpdateDateDesc(microserviceId);
        Collections.reverse(swaggers);

		swaggers.forEach(el->log.info(el.getId()));
		List<Swagger> swaggersToSend = new ArrayList<Swagger>();
		if (swaggers.size() != 0) {
			swaggersToSend.add(swaggers.get(0));
			for (int i = 1; i < swaggers.size(); i++) {
				if (!swaggers.get(i - 1).getEnvironment().equals(swaggers.get(i).getEnvironment())) {
					swaggersToSend.add(swaggers.get(i));
				}
			}
		}

		return swaggersToSend;
	}

	@Override
	public List<Swagger> getSwaggersWithLastVersionFromMicroservice(String microserviceId) {
		List<Swagger> swaggers = swaggerRepository
				.findAllByMicroserviceIdOrderByVersionDescLastUpdateDateDesc(microserviceId);
		List<Swagger> swaggersToSend = new ArrayList<Swagger>();
		if (swaggers.size() != 0) {
			swaggersToSend.add(swaggers.get(0));
			for (int i = 1; i < swaggers.size(); i++) {
				if (!swaggers.get(i - 1).getVersion().equals(swaggers.get(i).getVersion())) {
					swaggersToSend.add(swaggers.get(i));
				}
			}

		}
		return swaggersToSend;
	}

	@Override
	public Swagger getSwaggerById(String id) {
		Optional<Swagger> swagger = swaggerRepository.findById(id);
		if (swagger.isPresent()) {
			return swagger.get();
		}
		throw new NullPointerException("Swagger doesn't exist");
	}

	public Swagger getLastSwaggerByEnvironmentAndMicroservice(String environment, String microserviceId) {
		return getSwaggersFromEnvironmentAndMicroservice(environment, microserviceId)
				.get(getSwaggersFromEnvironmentAndMicroservice(environment, microserviceId).size() - 1);
	}

	@Override
	public Swagger updateSwagger(SwaggerDto swaggerDto, String id) throws Exception {

		Optional<Swagger> swagger = swaggerRepository.findById(id);
		if (swagger.isPresent())
			swagger.get().setVersion(swaggerParser.getVersion(swaggerParser.getBody(swaggerDto.getUrl())));
		String content = swaggerParser.getBody(swaggerDto.getUrl());
		swagger.get().setContent(content);
		Swagger savedSwagger = swaggerRepository.save(swagger.get());
		return swaggerRepository.save(savedSwagger);

	}

	@Override
	public List<Swagger> getSwaggerByLastModifiedDate() {
		return swaggerRepository.findAll().stream().sorted(Comparator.comparing(Swagger::getLastUpdateDate))
				.collect(Collectors.toList());
	}

	public Date getLastModifiedDateSwagger(List<Swagger> swaggers) {
		return swaggers.stream().sorted(Comparator.comparing(Swagger::getLastUpdateDate))
				.collect(Collectors.toList()).get(swaggers.size()-1).getLastUpdateDate();
	}

	@Override
	public void delete(String id) {
		swaggerRepository.deleteById(id);
	}

	@Override
	public SwaggerListDto addMicroServiceAndSwaggers(SwaggerListDto swaggerListDto) throws Exception {
		Microservice newMicroservice = swaggerListDto.getMicroservice();
		newMicroservice = microserviceService.save(newMicroservice);
		log.info("new microservice id " + newMicroservice.getId());
		List<Swagger> listSwagger = new ArrayList<>();
		List<Swagger> swaggerFromFiles = new ArrayList<>();

		for (Swagger s : swaggerListDto.getSwaggers()) {
			Swagger swagger = new Swagger(s.getUrl(), s.getEnvironment());
			swagger = updateNewSwagger(swagger, newMicroservice.getId());
			listSwagger.add(swagger);
		}

		swaggerListDto.setSwaggers(listSwagger);
		log.info("list of arrays " + swaggerListDto);
		return swaggerListDto;
	}

	public Swagger updateNewSwagger(Swagger swagger, String microserviceId) throws Exception {
		String content = swaggerParser.getBody(swagger.getUrl());
		if (!content.equals("")) {
			swagger.setVersion(swaggerParser.getVersion(swaggerParser.getBody(swagger.getUrl())));
			swagger.setSwaggerType(swaggerParser.getType(swaggerParser.getBody(swagger.getUrl())));
			swagger.setEnvironment(swagger.getEnvironment());
			swagger.setUrl(swagger.getUrl());
			swagger.setMicroserviceId(microserviceId);
			swagger.setContent(content);
			swagger.setOwner(SecurityContextUtil.getCurrentUserEmail());
			swagger.setContent(content);
			Swagger savedSwagger = swaggerRepository.save(swagger);
			return swaggerRepository.save(savedSwagger);
		} else {
			log.info("invalide");
			return null;
		}

	}

	@Override
	public SwaggerListDto updateSwaggerList(SwaggerListDto swaggerListDto) throws Exception {

		Microservice newMicroservice = swaggerListDto.getMicroservice();
		StopWatch watch1 = new StopWatch();
		watch1.start("task1");
		Microservice existingMicroservice = microserviceService.findById(newMicroservice.getId());
		microserviceRepository.save(newMicroservice);
		SubscriptionRepository.findAllByMicroserviceId(newMicroservice.getId()).forEach(subscription -> {
			String currentUserEmail = SecurityContextUtil.getCurrentUserEmail();
			if(!subscription.getUserEmail().equalsIgnoreCase(currentUserEmail) && subscription.getChanges().contains("ui modifications")) {
				Notification notification = notificationService.addNotif(subscription, null, null,
						newMicroservice.getTitle(), swaggerListDto.getModification(), newMicroservice.getOwner(), null);
				if(subscription.getEmailOption().booleanValue()){
					String title = newMicroservice.getTitle();
					String comments = swaggerListDto.getModification();
					try {
						sendNotifViaEmail(comments, subscription.getUserEmail(), title);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});
		watch1.stop();
		log.info("timing :{}", watch1.getTotalTimeMillis());

		if (existingMicroservice != null) {
			StopWatch watch3 = new StopWatch();
			watch3.start("task3");
			List<Swagger> oldList = getSwaggersFromMicroservice(existingMicroservice.getId());

			for (Swagger old : oldList) {
				Boolean remove = true;

				for (Swagger newS : swaggerListDto.getSwaggers()) {

					if (old.getId().equals(newS.getId()) && newS.getId() != null)
						remove = false;
				}
				if (remove.equals(true))
					delete(old.getId());
			}
			StopWatch watch2 = new StopWatch();
			watch2.start("task2");
			swaggerListDto.getSwaggers().forEach(newSwagger -> {
				if (newSwagger.getCreationDate() == null) {
					try {
						newSwagger = updateNewSwagger(newSwagger, existingMicroservice.getId());

					} catch (Exception e) {
						throw new NoSuchElementException(e.getMessage());
					}
				}
			});
			watch2.stop();
			log.info("timing :{}", watch2.getTotalTimeMillis());
			log.info("swagger list updated", swaggerListDto);
			watch3.stop();
			log.info("timing3 :{}", watch3.getTotalTimeMillis());
			return swaggerListDto;

		}

		return null;
	}

	private void sendNotifViaEmail(String comments, String userEmail, String title) throws IOException {
		String subject = title + " microservice has been changed";

		String content = getOutputContent("notification.html", "templates/notification",
				comments);

		emailService.send(content, subject, userEmail);
	}

	private String getOutputContent(String templateFileName, String variablesPath, String comments)
			throws IOException {
		Context context = new Context();
		variablesPath += "-en.json";
		comments += " has been changed.";

		@SuppressWarnings("unchecked")
		Map<String, Object> variables = new ObjectMapper()
				.readValue(resourceloader.getResource("classpath:" + variablesPath).getInputStream(), HashMap.class);

		// Set THYMELEAF Variables from JSON File
		context.setVariables(variables);
		context.setVariable("url", url);
		context.setVariable("message", comments);
		context.setVariable("prod", false);
		return templateEngine.process(templateFileName, context);
	}

	@Override
	public List<Swagger> addSwaggerFiles(MultipartFile[] files, String[] environments, Microservice microservice)
			throws Exception {
		microservice = microserviceService.save(microservice);

		List<Swagger> parsedSwaggers = swaggerParser.getSwaggersFromFiles(files, environments, microservice.getId());

		return parsedSwaggers;
	}

}
