package fr.codeonce.grizzlyhub.microservices.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;


@Repository
public interface MicroserviceRepository extends MongoRepository<Microservice, String> {
	
	public boolean existsByTitleIgnoreCase(String title);
	public List<Microservice> findAllByTeamIds(String teamIds);
	public Page<Microservice> findAllByType(String type,Pageable pageable);
	public Microservice findByTitle(String title);


}
