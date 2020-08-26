package fr.codeonce.grizzlyhub.microservices.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {

    List<Subscription> findAllByUserEmail(String userEmail);
    Optional<Subscription> findByUserEmailAndMicroserviceId(String userEmail, String microserviceId);
    List<Subscription> findAllByMicroserviceId(String microserviceId);
    boolean existsByUserEmailAndMicroserviceId(String userEmail , String microserviceId); 
    String deleteByUserEmailAndMicroserviceId(String userEmail , String microserviceId); 


}
