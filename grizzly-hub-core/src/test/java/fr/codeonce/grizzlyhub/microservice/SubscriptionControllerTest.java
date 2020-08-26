package fr.codeonce.grizzlyhub.microservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.codeonce.grizzlyhub.microservices.controller.MicroserviceController;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionDto;
import fr.codeonce.grizzlyhub.microservices.domain.SubscriptionRepository;
import fr.codeonce.grizzlyhub.swagger.domain.Swagger;
import fr.codeonce.grizzlyhub.swagger.domain.SwaggerRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SubscriptionControllerTest {
    private static final Logger log = LoggerFactory.getLogger(SubscriptionControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwaggerRepository swaggerRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

//    @Test
//    public void testSubscribe() throws Exception {
//        SubscriptionDto subscription = new SubscriptionDto();
//        ArrayList<String> list = new ArrayList<String>();
//        Swagger swagger = new Swagger();
//        swagger.setMicroserviceId("10");
//        swagger.setEnvironment("Prod");
//        swaggerRepository.save(swagger);
//        list.add("Prod");
//        list.add("Dev");
//        subscription.setEnvironment(list);
//        subscription.setFrequence(100L);
//        subscription.setMicroserviceId("10");
//        log.info(mapToJson((subscription)));
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/subscription/subscribe").contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON).content(mapToJson((subscription)))).andExpect(status().isOk());
//    }
//
//    @Test
//    public void testCreate() throws Exception {
//        SubscriptionDto subscriptionDto = new SubscriptionDto();
//        subscriptionDto.setId("10");
//        log.info(mapToJson((subscriptionDto)));
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/subscription/create").contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON).content(mapToJson((subscriptionDto)))).andExpect(status().isOk());
//    }
//
//
//
//    @Test
//    public void testFindAll() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscription/all")).andExpect(status().isOk());
//    }
//
//    @Test
//    public void testDelete() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/subscription/delete/10")).andExpect(status().isOk());
//    }
//
//    @Test
//    public void findById() throws Exception {
//        Subscription subscription = new Subscription();
//        subscription.setId("10");
//        subscriptionRepository.save(subscription);
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/subscription/find/10")).andExpect(status().isOk());
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        Subscription subscription = new Subscription();
//        subscription.setId("10");
//        subscriptionRepository.save(subscription);
//        SubscriptionDto newSubscription = new SubscriptionDto();
//        ArrayList<String> list = new ArrayList<String>();
//        list.add("Prod");
//        list.add("Dev");
//        newSubscription.setEnvironment(list);
//        newSubscription.setFrequence(3600L);
//        newSubscription.setMicroserviceId("10");
//        log.info(mapToJson((newSubscription)));
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/subscription/update/10").contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON).content(mapToJson((newSubscription)))).andExpect(status().isOk());
//    }
//


    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }


}
