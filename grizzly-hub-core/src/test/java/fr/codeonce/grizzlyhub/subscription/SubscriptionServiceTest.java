package fr.codeonce.grizzlyhub.subscription;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;

@SpringBootTest
public class SubscriptionServiceTest {

	
	@Autowired
	ISubscriptionService subscriptionService;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	private static final String email = "test@test.com";
	private static final String frequence = "hourly";
	
	@BeforeAll
	public Microservice getMicroservice() {
		Microservice microservice = new Microservice();
		microservice.setTitle("new microservice subscription");
		return mongoTemplate.save(microservice) ;
		
//		Subscription s = new Subscription(microservice.getId(), envs , "hourly", SecurityContextUtil.getCurrentUserEmail());
		
	}
	
//	@Test
//	public void testGetCurrentUserSubscriptions() {
//		List<String> envs = new ArrayList<>();
//		envs.add("dev");
//		subscriptionService.subscribe(getMicroservice().getId(), envs, frequence, email);
//		Query query = new Query();
//	    query.limit(1);
//	    query.with(new Sort(Sort.Direction.DESC, "id"));
//	    Subscription sub = mongoTemplate.findOne(query, Subscription.class);
//	    
//		assertNotNull(mongoTemplate.findAll(Subscription.class));
//	}
}
