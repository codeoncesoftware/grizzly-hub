package fr.codeonce.grizzlyhub.notification.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
	List<Notification> findAllByOpenedAndSent(Boolean opened,Boolean sent);
	List<Notification> findAllByOpened(Boolean opened,Pageable pageable);
	List<Notification> findAllBySent(Boolean sent);
	List<Notification> findByIdSubscription(String idSubscription,Pageable pageable);
	List<Notification> findByIdSubscriptionAndSent(String idSubscription,Boolean sent,Pageable pageable);
	List<Notification> findByIdSubscriptionAndSent(String idSubscription,Boolean sent);
}
