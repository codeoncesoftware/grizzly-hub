package fr.codeonce.grizzlyhub.microservices.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;

//assumes the current class is called MyLogger

@Service
public class SubscriptionService implements ISubscriptionService {


    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SwaggerRepository swaggerRepository;


	private static final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

    @Override
    public Map<String,List<Subscription>> findAllByFrequency(){
        return getByFrequency(subscriptionRepository.findAll());
    }
	@Autowired
	private MicroserviceRepository microserviceRepository;


    public Page<Subscription> findAll(int page,int pageSize) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
        return subscriptionRepository.findAll(firstPageWithTwoElements);
    }

    @Override
    public List<Subscription> findAllByUserEmail(String userEmail){
        return subscriptionRepository.findAllByUserEmail(userEmail);
    }
    
    public Map<String,List<Subscription>> getByFrequency(List<Subscription> subscriptions){
    	Map<String, List<Subscription>> subscriptionsByFrequency = new HashMap<String, List<Subscription>>(); 
    	List<Subscription> dailySubscriptions = new ArrayList<Subscription>();
    	List<Subscription> hourlySubscriptions = new ArrayList<Subscription>();
    	List<Subscription> weeklySubscriptions = new ArrayList<Subscription>();
    	subscriptions.forEach(subscription->{
    		if (subscription.getFrequence().equals("Hourly")) {
    			hourlySubscriptions.add(subscription);
			}
    		else     		if (subscription.getFrequence().equals("Daily")) {
    			dailySubscriptions.add(subscription);
			}
    		else weeklySubscriptions.add(subscription);
    	});
    	subscriptionsByFrequency.put("Hourly", hourlySubscriptions);
    	subscriptionsByFrequency.put("Daily", dailySubscriptions);
    	subscriptionsByFrequency.put("Weekly", weeklySubscriptions);
    	return subscriptionsByFrequency;
    }

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription findById(String id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        if (subscription.isPresent()) {
            return subscription.get();
        }
        throw new NullPointerException("Subscription not found");
    }

    @Override
    public void delete(String id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public Subscription subscribe(String id, List<String> env, Long frequence, String currentEmail , List<String> changes, Boolean emailOption) {
        if(subscriptionRepository.findByUserEmailAndMicroserviceId(currentEmail,id).isPresent()){
            subscriptionRepository.deleteById(subscriptionRepository.findByUserEmailAndMicroserviceId(currentEmail,id).get().getId());
        }
        ArrayList<String> idList = new ArrayList<String>();
        List<Swagger> list = swaggerRepository.findAllByMicroserviceIdOrderByLastUpdateDate(id);
        if (list.isEmpty() != true) {
            for (Swagger swagger : list) {
            	System.out.println(env);
                String environment = env.stream().filter(e ->
                        swagger.getEnvironment().equals(e)).findAny().orElse(null);
                if (environment != null) {
                    idList.add(swagger.getId());
                }
            }
            Subscription subscription = new Subscription(id, env, frequence,  currentEmail , changes, emailOption);
            subscriptionRepository.save(subscription);
            return subscriptionRepository.findByUserEmailAndMicroserviceId(currentEmail,id).get();
        }
        else {throw new NullPointerException("Swaggers not found");}
    }


    public Subscription update(Subscription subscription,String id){
        Optional<Subscription> oldSubscription = subscriptionRepository.findById(id);
        if (oldSubscription.isPresent()) {
            subscription.setId(id);
            return subscriptionRepository.save(subscription);
        }
        throw new NullPointerException("Subscription not found");
    }


    @Override
    public void unsubscribe(String userEmail, String microserviceId){
        Optional<Subscription> subscription = subscriptionRepository.findByUserEmailAndMicroserviceId(userEmail, microserviceId);
        if (subscription.isPresent()){
            delete(subscription.get().getId());
        }
    }

	@Async
	@Override
	public CompletableFuture<List<Microservice>> getSubscribedMicroservicesForConnectedUser(String email) {
		
		log.info("Current authenticated user " + email);
		List<Microservice> listM = new ArrayList<>();
		subscriptionRepository.findAllByUserEmail(email)
				.forEach(s -> microserviceRepository.findById(s.getMicroserviceId()).ifPresent(ms -> listM.add(ms)));

		return CompletableFuture.completedFuture(listM);
	}

	@Override
    public Subscription findByUser(String email, String id){
        Optional<Subscription> subscription = subscriptionRepository.findByUserEmailAndMicroserviceId(email, id);
        if( subscription.isPresent()){
            return subscription.get();
        } else {
            return null;
        }
    }

	@Override
	public List<String> findUsersByMicroservice(String microserviceId) {
		List<String> users = new ArrayList<String>();
		StopWatch watch = new StopWatch();
		watch.start("url construction");
		subscriptionRepository.findAllByMicroserviceId(microserviceId).forEach(subscription->users.add(subscription.getUserEmail()));		
		watch.stop();
		log.info("timing {}", watch.getTotalTimeMillis());
		return users;
	}
		
}






