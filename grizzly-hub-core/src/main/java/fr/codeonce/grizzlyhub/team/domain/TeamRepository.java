package fr.codeonce.grizzlyhub.team.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
	public long countByOrganisationId(String organisationId);

	public List<Team> findByOrganisationId(String organisationId);
}
