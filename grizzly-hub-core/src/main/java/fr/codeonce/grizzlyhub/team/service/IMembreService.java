package fr.codeonce.grizzlyhub.team.service;

import java.util.List;
import java.util.Set;

import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Organisation;
import fr.codeonce.grizzlyhub.team.domain.Team;

public interface IMembreService {

	public Membre saveMembre(Membre membre);
	public List<Membre> findAllMembres();
	public List<Membre> findByOrganisationId(String organisationId);
	public Membre findById(String id);
	public Membre updateMembre(Membre membre , String id);
	public void deleteMembre(String id);
	public List<Membre> findByTeamId(String teamId) ;
	public List<Organisation> findOrganisationByEmail(String email) throws Exception  ;
	public List<Team> findTeamByEmail(String email) throws Exception ;
	public void deletAllMemebersOfOrganisation(String organisationId);
	public Boolean checkMembreInOrganisation(String email);
	public Boolean membreExistsInUsers(String email);
	public Boolean membreExistsInMembers(String email);
	public Membre addMemberToTeam(String teamId, String email);
	public Membre findMemberByEmail(String email);
	public List<Membre> findTeamsMembers(List<String> teamsIds);
	public Set<Microservice> findAllMembersSubscriptions(List<Membre> members);
	public boolean checkUserIsAdmin(String currentEmail);

}
