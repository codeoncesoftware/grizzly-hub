package fr.codeonce.grizzlyhub.microservices.controller;




import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.*;
import fr.codeonce.grizzlyhub.microservices.service.IMicroserviceService;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;
import fr.codeonce.grizzlyhub.notification.service.INotificationService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription")
@EnableScheduling
public class SubscriptionController {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private ISubscriptionService subscriptionService;
    
    @Autowired
    private INotificationService notificationService;
    
    @Autowired
    private MicroserviceRepository microserviceRepository;
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @PostMapping("/subscribe")
    public Subscription subscribe(@RequestBody SubscriptionDto subscriptionDto) {
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        return(subscriptionService.subscribe(subscriptionDto.getMicroserviceId() , subscriptionDto.getEnvironment() , subscriptionDto.getFrequence(),subscriptionDto.getUserEmail(), subscriptionDto.getChanges(), subscriptionDto.getEmailOption()));

    }


    @DeleteMapping("/unsubscribe")
    public void unsubscribe(@RequestParam String userEmail, @RequestParam String microserviceId){
        subscriptionService.unsubscribe(userEmail, microserviceId);
    }

	@GetMapping("/all")
	public Page<Subscription> findAllSubscriptions(@RequestParam(name="pageSize")int pageSize ,@RequestParam(name="page")int page) {
		return subscriptionService.findAll(page, pageSize);

	}

    @GetMapping("/allbyuser")
    public List<Subscription> findAllSubscriptionsByUserEmail(){
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        return subscriptionService.findAllByUserEmail(currentEmail);
    }

    
    @GetMapping("/microserviceByUser")
    public List<Microservice> findAllByUserEmail(){
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        List<Microservice> microservices = new ArrayList<Microservice>();
        subscriptionService.findAllByUserEmail(currentEmail).forEach(sub->{
        	microservices.add(microserviceRepository.findById(sub.getMicroserviceId()).get());
        });
        return microservices;
    }
    
    @GetMapping("/allFrequency")
    public Map<String, List<Subscription>> findAllSubscriptionsByFrequency() {
        return subscriptionService.findAllByFrequency();
    }

	@GetMapping("/find/{id}")
	public Subscription findById(@PathVariable(name = "id") String id)
			throws MalformedURLException, JSONException, IOException {
		return subscriptionService.findById(id);
	}


    @PostMapping("/create")
    public Subscription saveSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        Subscription subscription = subscriptionMapper.mapToDomain(subscriptionDto);
        return subscriptionService.save(subscription);
    }

    @PutMapping("/update/{id}")
    public Subscription update(@RequestBody SubscriptionDto subscriptionDto, @PathVariable(name = "id") String id) {
        Subscription subscription = subscriptionMapper.mapToDomain(subscriptionDto);
        return subscriptionService.update(subscription, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSubscription(@PathVariable(name = "id") String id) {
        subscriptionService.delete(id);
    }

    @GetMapping("/findbyuser/{id}")
    public Subscription findByUser(@PathVariable String id){
        String currentEmail = SecurityContextUtil.getCurrentUserEmail();
        log.info("here");
        return subscriptionService.findByUser(currentEmail, id);
    }

    @GetMapping("/findByMicroserviceId/{microserviceId}")
    public List<String> findByMicroserviceId(@PathVariable String microserviceId){
        return subscriptionService.findUsersByMicroservice(microserviceId);
    }
    
    @GetMapping("/isSubscribed/{userEmail}/{microserviceId}")
    public boolean existsByUserEmailAndMicroserviceId(@PathVariable String userEmail , @PathVariable String microserviceId){
        return subscriptionRepository.existsByUserEmailAndMicroserviceId(userEmail, microserviceId);
    }
    @DeleteMapping("/delete/{userEmail}/{microserviceId}")
    public String deleteSubscriptionByEmailAndId(@PathVariable String userEmail , @PathVariable String microserviceId) {
        return subscriptionRepository.deleteByUserEmailAndMicroserviceId(userEmail, microserviceId);
    }
}
