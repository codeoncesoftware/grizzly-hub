package fr.codeonce.grizzlyhub.notification.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONException;

import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.notification.domain.Notification;
import fr.codeonce.grizzlyhub.notification.domain.NotificationForMicroservice;

public interface INotificationService {

	public void checkChangee() throws MalformedURLException, JSONException, IOException;

	public Notification addNotif(Subscription subscription, String idOldSwagger, String idNewSwagger, String microserviceTitle,
			String message,String owner,String environment);

	public void sendNotification();

	public List<Notification> findNotifications(String email, int page, int pageSize);

	public Notification updateNotification(Notification notification, String id);

	public void deleteNotifications(String email);

	public void allSeen(String email);

	//public List<Notification> findLatestNotifications(String email);

	public void deleteNotification(String id);

	public String formatSwagger(String content);

	public NotificationForMicroservice addNotifForMicroservice(Subscription subscription, String microserviceTitle,
			String commentaire, String owner);

}
