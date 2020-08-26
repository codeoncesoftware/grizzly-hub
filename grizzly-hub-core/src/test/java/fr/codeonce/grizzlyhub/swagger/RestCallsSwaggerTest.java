package fr.codeonce.grizzlyhub.swagger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
public class RestCallsSwaggerTest {

	private static final Logger log = LoggerFactory.getLogger(RestCallsSwaggerTest.class);

	@Autowired
	MockMvc mvc;

//	@Test
//	public void testDeserialization() throws Exception {
//		mvc.perform(get("/api/microservice/all").contentType("application/json")).andExpect(status().isOk());
//	}
//	
//	


}
