package fr.codeonce.grizzlyhub.microservices.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;

import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;

public interface ISubscriptionService {

	public Page<Subscription> findAll(int page, int pageSize);

	public Subscription save(Subscription subscription);

	public Subscription findById(String id);

	public void delete(String id);

	public Subscription subscribe(String id, List<String> env, Long frequence, String email, List<String> changes, Boolean emailOption);

	public Subscription update(Subscription subscription, String id);

	Map<String, List<Subscription>> findAllByFrequency();

	public Map<String, List<Subscription>> getByFrequency(List<Subscription> findAll);

	public CompletableFuture<List<Microservice>> getSubscribedMicroservicesForConnectedUser(String email);

	public List<Subscription> findAllByUserEmail(String userEmail);

	public List<String> findUsersByMicroservice(String microserviceId);

	public void unsubscribe(String userEmil, String microserviceId);

	public Subscription findByUser(String email, String id);
}
