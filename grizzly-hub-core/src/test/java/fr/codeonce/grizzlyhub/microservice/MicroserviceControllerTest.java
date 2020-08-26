package fr.codeonce.grizzlyhub.microservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.codeonce.grizzlyhub.microservices.controller.MicroserviceController;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.service.IMicroserviceService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(value= MicroserviceController.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MicroserviceControllerTest {

	private static final Logger log = LoggerFactory.getLogger(MicroserviceController.class);

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	@Autowired
	private MockMvc mockMvc;

//	    @Mock
//	    private IMicroserviceService microService;
//	@Test
//	public void testCreate() throws Exception {
//		Microservice microservice = new Microservice();
//		microservice.setTitle("micro1");
//		microservice.setDescription("desc1");
//		ObjectMapper mapper = new ObjectMapper();
//		log.info(mapToJson((microservice)));
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/microservice/create").contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON).content(mapToJson((microservice)))).andExpect(status().isOk());
//	}
//
//
//	@Test
//	public void testFindAll() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/api/microservice/all")).andExpect(status().isOk());
//	}
//
//	@Test
//	public void testDelete() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.delete("/api/microservice/delete/1")).andExpect(status().isOk());
//	}
//
//	private String mapToJson(Object object) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		return objectMapper.writeValueAsString(object);
//	}
//	
//	    @Test
//	    public void findById() throws Exception {
//	        mockMvc.perform(MockMvcRequestBuilders.get("/api/microservice/find/1")).andExpect(status().isOk());
//	    }


}
//
//RunWith(JUnit4.class)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.properties")
//@ActiveProfiles("test")
//@WebMvcTest(value= PrmLevelController.class)
//public class PrmLevelControllerTest {
//    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//
//    @Autowired
//    private MockMvc mockMvc;
//    
//
//    @Mock
//    private IMicroserviceService microService;
//
//    @Test
//    public void testFindAll() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/microservice/all")).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}
//    @InjectMocks
//    private PrmLevelController prmLevelController;
//  ;
//    @Test
//    public void testCreate() throws Exception {
//        PrmLevelDto prmLevelDto= new PrmLevelDto();
//        prmLevelDto.setLevelName("junior");
//
//        mockMvc.perform( MockMvcRequestBuilders
//                .post("/level/create")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapToJson(prmLevelDto))
//                .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testFindAll() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/level/all")).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void testDelete() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.delete("/level/delete/1")).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Test
//    public void findById() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/level/find/1")).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(prmLevelController).build();
//    }
//
//
//    @Test
//    public void testUpdate() throws Exception {
//        PrmLevelDto prmLevelDto= new PrmLevelDto();
//        prmLevelDto.setLevelName("senior");
//
//        mockMvc.perform( MockMvcRequestBuilders
//                .put("/level/update/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapToJson(prmLevelDto))
//                .accept(MediaType.APPLICATION_JSON)
//        ).andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    private String mapToJson(Object object) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(object);
//    }
//}