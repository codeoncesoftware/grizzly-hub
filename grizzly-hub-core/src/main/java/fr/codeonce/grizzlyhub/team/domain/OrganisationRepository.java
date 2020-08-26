package fr.codeonce.grizzlyhub.team.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganisationRepository extends MongoRepository<Organisation,String> {
	public Organisation findByEmail(String teamId);
	public Boolean existsByNameIgnoreCase(String name);

}
