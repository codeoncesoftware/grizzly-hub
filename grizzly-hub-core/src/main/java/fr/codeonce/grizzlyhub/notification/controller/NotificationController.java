package fr.codeonce.grizzlyhub.notification.controller;

import fr.codeonce.grizzlyhub.microservices.controller.SubscriptionController;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;
import fr.codeonce.grizzlyhub.microservices.service.SubscriptionService;
import fr.codeonce.grizzlyhub.notification.domain.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.net.HttpHeaders;

import fr.codeonce.grizzlyhub.notification.domain.Notification;
import fr.codeonce.grizzlyhub.notification.service.INotificationService;
import fr.codeonce.grizzlyhub.notification.service.NotificationServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SimpMessagingTemplate template;

	@Autowired
	private ISubscriptionService subscriptionService;
	
	@Autowired
	private INotificationService notificationService;

	private Notification notifications = new Notification();


	@GetMapping("/notify")
	public void getNotification() {
		notifications.setCommentaire("hello");
		notificationRepository.save(notifications);
		template.convertAndSend("/topic/notification", notifications);
	}

	@DeleteMapping("delete/{id}")
	public void deleteNotification(@PathVariable String id){
		notificationService.deleteNotification(id);
	}
	
	@GetMapping("/findByEmail")
	 public List<Notification> findNotifications(@RequestParam(name="email") String email,@RequestParam(name="pageSize")int pageSize ,@RequestParam(name="page")int page){
		return notificationService.findNotifications(email,pageSize,page);
	}
	
	@PutMapping("/update/{id}")
	public Notification updateNotification(@RequestBody Notification notification,@PathVariable String id) {
		return notificationService.updateNotification(notification,id);
	}
	
	@DeleteMapping("/clearAll/{email}")
	public void clearAll(@PathVariable String email) {
		notificationService.deleteNotifications(email);
	}
	
	@PutMapping("/allSeen/{email}")
	public void allSeen(@PathVariable String email) {
		notificationService.allSeen(email);
	}

}
