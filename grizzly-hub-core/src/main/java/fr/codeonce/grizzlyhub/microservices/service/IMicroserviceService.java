package fr.codeonce.grizzlyhub.microservices.service;

import java.util.List;

import org.springframework.data.domain.Page;

import fr.codeonce.grizzlyhub.microservices.domain.Microservice;

public interface IMicroserviceService {
	public Page<Microservice> findAll(int page,int pageSize);

	public Microservice save(Microservice microservice);

	public Microservice findById(String id);

	public void delete(String id);

	public Microservice update(Microservice microservice, String id);
	public List<Microservice> findAllByKeyWord(String keyword);

	List<Microservice> findAllByType(int page, int pageSize);

	Microservice findByTitle(String title);

}
