package fr.codeonce.grizzlyhub.notification.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationForMicroserviceRepository extends MongoRepository<NotificationForMicroservice, String> {

}
