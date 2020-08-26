package fr.codeonce.grizzlyhub.team.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<Membre, String> {
public List<Membre> findAllByOrganisationId(String organisationId);
public List<Membre> findAllByTeamIds(String teamIds);
public Membre findByEmail(String Email);
public long countByOrganisationId(String organisationId);
public long countByTeamIds(String TeamId);
public Boolean existsByEmail(String email);
public Boolean existsByOrganisationIdAndEmail(String organisationId,String email);
public List<Membre> findAllByTeamIdsIn(List<String> teamIds);

}
