package fr.codeonce.grizzlyhub.microservices.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.swagger.service.ISwaggerService;

@Service
public class MicroserviceService implements IMicroserviceService {

	private static final Logger log = LoggerFactory.getLogger(MicroserviceService.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	MicroserviceRepository microserviceRepository;
	@Autowired
	ISwaggerService swaggerService;

	@Autowired
	UserRepository userRepository;

	@Override
	public Page<Microservice> findAll(int page, int pageSize) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
		List<Microservice> micros = microserviceRepository.findAll(firstPageWithTwoElements).getContent();
		micros.forEach(micro -> {
			log.info(" micros {}", micro.getId());
			micro.setSwaggersVersions(swaggerService.getSwaggersWithLastEnvironmentFromMicroservice(micro.getId()));
		});
		Page<Microservice> pageMicro = new PageImpl<>(micros, firstPageWithTwoElements, micros.size());
		return pageMicro;
	}
	
	@Override
	public List<Microservice> findAllByType(int page, int pageSize) {
		Pageable firstPageWithTwoElements = PageRequest.of(page, pageSize);
		List<Microservice> micros = microserviceRepository.findAllByType("public",firstPageWithTwoElements).getContent();
		micros.forEach(micro -> {
			log.info(" micros {}", micro.getId());
			micro.setSwaggersVersions(swaggerService.getSwaggersWithLastEnvironmentFromMicroservice(micro.getId()));
		});
		//Page<Microservice> pageMicro = new PageImpl<>(micros, firstPageWithTwoElements, micros.size());
		return micros;
	}

	@Override
	public Microservice save(Microservice microservice) {
		//microservice.setOwner(SecurityContextUtil.getCurrentUserEmail());
		Microservice micro = microserviceRepository.save(microservice);
		Subscription subscription = new Subscription();	
		return micro;
	}

	@Override
	public Microservice findById(String id) {
		Optional<Microservice> microservice = microserviceRepository.findById(id);
		if (microservice.isPresent()) {
			return microservice.get();
		}
		throw new NullPointerException("Microservice not found");
	}
	
	@Override
	public Microservice findByTitle(String title) {
		Microservice microservice = microserviceRepository.findByTitle(title);
		microservice.setSwaggersVersions(swaggerService.getSwaggersWithLastEnvironmentFromMicroservice(microservice.getId()));
		return microservice;


	}

	@Override
	public void delete(String id) {
		swaggerService.getSwaggersFromMicroservice(id).forEach(swagger -> {
			swaggerService.delete(swagger.getId());
		});
		microserviceRepository.deleteById(id);
	}

	@Override
	public Microservice update(Microservice microservice, String id) {
		Optional<Microservice> oldMicroservice = microserviceRepository.findById(id);
		if (oldMicroservice.isPresent()) {
			microservice.setId(id);
			return microserviceRepository.save(microservice);
		}
		throw new NullPointerException("Microservice not found");

	}

	@Override
	public List<Microservice> findAllByKeyWord(String keyword) {
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny(keyword);
		Query query = TextQuery.queryText(criteria).sortByScore();
		List<Microservice> microservices = mongoTemplate.find(query, Microservice.class);
		if (mongoTemplate.find(query, Microservice.class).isEmpty()) {
			Query secondquery = new Query();
			secondquery.addCriteria(Criteria.where("title").regex(keyword, "i"));
			microservices = mongoTemplate.find(secondquery, Microservice.class);
			log.info(String.valueOf(microservices));
		}
		return microservices;
	}
}
