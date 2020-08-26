package fr.codeonce.grizzlyhub.notification.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.codeonce.grizzlyhub.auth.service.util.EmailService;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;
import fr.codeonce.grizzlyhub.notification.domain.Notification;
import fr.codeonce.grizzlyhub.notification.domain.NotificationForMicroservice;
import fr.codeonce.grizzlyhub.notification.domain.NotificationForMicroserviceRepository;
import fr.codeonce.grizzlyhub.notification.domain.NotificationRepository;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDiffDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerDto;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerParser;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerService;

@Service
public class NotificationServiceImpl implements INotificationService {
	@Autowired
	NotificationForMicroserviceRepository notificationForMicroserviceRepository;
	@Autowired
	private ISwaggerParser swaggerParser;
	@Autowired
	private ISwaggerService swaggerService;
	@Autowired
	private SwaggerRepository swaggerRepository;
	@Autowired
	private ISubscriptionService subscriptionService;
	@Autowired
	private SubscriptionRepository subscriptionRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private MicroserviceRepository microserviceRepository;

	@Autowired
	private SimpMessagingTemplate template;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Qualifier("webApplicationContext")
	@Autowired
	private ResourceLoader resourceloader;

	@Value("${frontUrl}")
	private String url;
	
	@Override
	public NotificationForMicroservice addNotifForMicroservice(Subscription subscription,
			String microserviceTitle,String commentaire,String owner) {
		NotificationForMicroservice notifForMicroservice = new NotificationForMicroservice(microserviceTitle,false,false,addSeconds(new Date(), getLastDate(subscription)),SecurityContextUtil.getCurrentUserEmail(),subscription.getId());
		return notificationForMicroserviceRepository.save(notifForMicroservice);

	}

	private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private Integer getLastDate(Subscription subscription) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime createDate = LocalDateTime.ofInstant(subscription.getCreatedDate().toInstant(),
				ZoneId.systemDefault());
		long diff = ChronoUnit.SECONDS.between(createDate, now);
		return (int) ((int) diff % subscription.getFrequence());
	}

	private static Date addSeconds(Date date, Integer seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}

	@Override
	public Notification addNotif(Subscription subscription, String idOldSwagger, String idNewSwagger,
			String microserviceTitle,String commentaire,String owner,String environment) {
		Notification notification = new Notification(idOldSwagger, idNewSwagger, subscription.getId(), false, false,
				microserviceTitle,commentaire, owner,environment);
		notification.setIdSubscription(subscription.getId());
		notification.setSendingDate(addSeconds(new Date(), getLastDate(subscription)));
		return notificationRepository.save(notification);

	}

	@Override
	public String formatSwagger(String content) {
		return content.replace("\r\n", "").replace("\n", "").replace("  ", "").replace(" : ", ":").replace("[ {", "[{")
				.replace("} ]", "}]").replace(":[ ", ":[").replace(" ],", "],").replace("}, {", "},{")
				.replace(", ", ",");
	}
	

	@Override
	@Scheduled(cron = "*/1 * * * * *")
	public void checkChangee() throws MalformedURLException, JSONException, IOException {
		subscriptionRepository.findAll().forEach(subscription -> {
			subscription.getEnvironment().forEach(environment -> {
				Swagger lastSwagger = swaggerRepository.findFirstByEnvironmentAndMicroserviceIdOrderByLastUpdateDateDesc(
						environment, subscription.getMicroserviceId());
				if (lastSwagger != null) {
					try {
//						log.info("id {} sub {} ",lastSwagger.getId(),subscription.getUserEmail());
						String content = swaggerParser.getBody(lastSwagger.getUrl());
						if (!formatSwagger(content).equals(formatSwagger(lastSwagger.getContent()))) {
							SwaggerDto swaggerDto = new SwaggerDto(lastSwagger.getUrl(),lastSwagger.getEnvironment(),subscription.getMicroserviceId(),lastSwagger.getOwner());
							Swagger newSwagger = swaggerService.saveSwaggerForNotification(swaggerDto);
							SwaggerDiffDto swaggerDiffDto = new SwaggerDiffDto(lastSwagger.getId(), newSwagger.getId());
							if (!swaggerParser.checkEndpointChanges(swaggerDiffDto, subscription.getChanges()).equals("")) {
								subscriptionRepository.findAllByMicroserviceId(subscription.getMicroserviceId())
										.forEach(sub -> {
											try {
												String title = microserviceRepository.findById(sub.getMicroserviceId())
														.get().getTitle();
												String lastSwaggerEnvironment = lastSwagger.getEnvironment();
												String comments = swaggerParser.checkEndpointChanges(swaggerDiffDto, sub.getChanges());
												addNotif(sub, lastSwagger.getId(), newSwagger.getId(),
														title,comments,lastSwagger.getOwner(),lastSwaggerEnvironment);
												if(sub.getEmailOption().booleanValue()) {
													sendNotifViaEmail(comments, sub.getUserEmail(), title, lastSwaggerEnvironment);
												}

											}  catch (Exception e) {
												log.error("Exception: {}", e);
											}
										});
							}
						}
					} catch (Exception e) {
						log.error("Exception: {}", e);
					}
				}
			});
		});
	}

	public void sendNotifViaEmail(String comments, String userEmail, String title, String env) throws IOException {
		String subject = title + " microservice has been changed in the " + env + " environment";
		boolean prod = false;
		if(env.equalsIgnoreCase("prod")) prod = true;
		String content = getOutputContent("notification.html", "templates/notification",
				comments, prod);
		emailService.send(content, subject, userEmail);

	}

	private String getOutputContent(String templateFileName, String variablesPath, String comments, boolean prod)
			throws IOException {
		Context context = new Context();
		// Choose Language
		//if (lang.contains("fr")) {
		//	variablesPath += "-fr.json";
		//} else {
		variablesPath += "-en.json";
		//}
		if(comments.equalsIgnoreCase("new")) {
			comments = "An endpoint has been added.";
		} else if(comments.equalsIgnoreCase("missing")) {
			comments = "An endpoint has been deleted.";
		} else {
 			comments = "An endpoint has been updated.";
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> variables = new ObjectMapper()
				.readValue(resourceloader.getResource("classpath:" + variablesPath).getInputStream(), HashMap.class);

		// Set THYMELEAF Variables from JSON File
		context.setVariables(variables);
		context.setVariable("url", url);
		context.setVariable("message", comments);
		context.setVariable("prod", prod);
		context.setVariable("prodAlert", "There are changes in the Production Environment!");
		return templateEngine.process(templateFileName, context);
	}

	@Override
	@Scheduled(cron = "*/1 * * * * *")
	public void sendNotification() {
		notificationRepository.findAllBySent(false).forEach(notification -> {
			if (new Date().after(notification.getSendingDate())) {
				notification.setSent(true);
				String email = subscriptionRepository.findById(notification.getIdSubscription()).get().getUserEmail();
				log.info("email : {} ", email);
				notificationRepository.save(notification);
				String path = "/topic/notification?email=" + email ;
				template.convertAndSend(path, notification);
				log.info("sent");
			}
		});
		;
	}

	public List<Notification> findNotifications(String email, int pageSize, int page) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
		List<Subscription> subscriptions = subscriptionService.findAllByUserEmail(email);
		List<Notification> notifications = new ArrayList<Notification>();
		subscriptions.forEach(subscription -> {
			notifications.addAll(notificationRepository.findByIdSubscriptionAndSent(subscription.getId(), true,
					firstPageWithTwoElements));
		});
		return notifications;
	}

	private List<Notification> findAllNotifications(String email) {
		List<Subscription> subscriptions = subscriptionService.findAllByUserEmail(email);
		log.info(String.valueOf(subscriptions));
		List<Notification> notifications = new ArrayList<Notification>();
		subscriptions.forEach(subscription -> {
			notifications.addAll(notificationRepository.findByIdSubscriptionAndSent(subscription.getId(), true));
		});
		return notifications;
	}

	/*public List<Notification> findLatestNotifications(String email){
		List<Notification> notifications = findNotifications(email);
		Collections.sort(notifications, new Comparator<Notification>() { public int compare(Notification a, Notification b) { return a.getCreationDate().compareTo(b.getCreationDate()); } });
		List<Notification> latestNotifications = notifications.stream().limit(6).collect(Collectors.toList());
		return latestNotifications;
	}*/

	@Override
	public void allSeen(String email) {
		findAllNotifications(email).forEach(notification -> {
			notification.setOpened(true);
			log.info(String.valueOf(notification.getOpened()));
			notificationRepository.save(notification);
		});
	}

	@Override
	public Notification updateNotification(Notification notification, String id) {
		notification.setId(notificationRepository.findById(id).get().getId());
		return notificationRepository.save(notification);
	}

	@Override
	public void deleteNotifications(String email) {
		findAllNotifications(email).forEach(notification -> {
			notificationRepository.delete(notification);
		});
	}

	@Override
	public void deleteNotification(String id){
		notificationRepository.delete(notificationRepository.findById(id).get());
	}

}
