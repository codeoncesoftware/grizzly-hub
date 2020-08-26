package fr.codeonce.grizzlyhub.team.service;

import java.io.IOException;
import java.util.*;

import fr.codeonce.grizzlyhub.microservices.controller.MicroserviceController;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import fr.codeonce.grizzlyhub.microservices.domain.Subscription;
import fr.codeonce.grizzlyhub.microservices.service.ISubscriptionService;
import fr.codeonce.grizzlyhub.microservices.service.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.notification.service.NotificationServiceImpl;
import fr.codeonce.grizzlyhub.team.domain.MemberRepository;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Organisation;
import fr.codeonce.grizzlyhub.team.domain.OrganisationRepository;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.domain.TeamRepository;

@Service
public class MemberServiceImpl implements IMembreService {

	@Autowired
	MicroserviceRepository microserviceRepository;

	@Autowired
	ISubscriptionService subscriptionService;

	@Autowired
	MemberRepository membreRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrganisationRepository organisationRepository;
	@Autowired
	TeamRepository teamRepository;
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Override
	public Membre saveMembre(Membre membre) {
		membre.setTeamIds(new ArrayList<String>());
		return membreRepository.save(membre);
	}

	@Override
	public Boolean membreExistsInUsers(String email) {
		return userRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public Boolean membreExistsInMembers(String email) {
		return membreRepository.existsByEmail(email);
	}

	@Override
	public List<Membre> findAllMembres() {
		return membreRepository.findAll();
	}

	@Override
	public Membre findById(String id) {
		return membreRepository.findById(id).get();
	}

	@Override
	public Membre updateMembre(Membre membre, String id) {
		membre.setId(id);
		return membreRepository.save(membre);
	}

	@Override
	public void deleteMembre(String id) {
		membreRepository.deleteById(id);
	}

	@Override
	public List<Membre> findByOrganisationId(String organisationId) {
		return membreRepository.findAllByOrganisationId(organisationId);

	}

	@Override
	public List<Membre> findByTeamId(String teamIds) {
		return membreRepository.findAllByTeamIds(teamIds);

	}

	@Override
	public List<Organisation> findOrganisationByEmail(String email) throws Exception {
		List<Organisation> organisations = new ArrayList<Organisation>();
		Membre member = membreRepository.findByEmail(email);
		try {
			organisations.add(organisationRepository.findById(member.getOrganisationId()).get());
		} catch (NullPointerException e) {
			throw new Exception("cet utilisateur n'est inscrit à aucune organisation");
		}
		return organisations;
	}

	@Override
	public List<Team> findTeamByEmail(String email) throws Exception {
		List<Team> teams = new ArrayList<Team>();
		Membre member = membreRepository.findByEmail(email);
		try {
			member.getTeamIds().forEach(teamId -> teams.add(teamRepository.findById(teamId).get()));
		} catch (NullPointerException e) {
			throw new Exception("cet utilisateur n'est inscrit à aucune équipe");
		}
		return teams;

	}

	@Override
	public void deletAllMemebersOfOrganisation(String organisationId) {

		membreRepository.findAllByOrganisationId(organisationId)
				.forEach(member -> membreRepository.deleteById(member.getId()));
	}

	@Override
	public Boolean checkMembreInOrganisation(String email) {
		if (membreRepository.existsByEmail(email)) {
			Membre member = membreRepository.findByEmail(email);
			return organisationRepository.existsById(member.getOrganisationId());
		} else
			return false;

	}
	
	@Override
	public Membre addMemberToTeam(String teamId, String email) {
		Membre member = membreRepository.findByEmail(email);
		List<String> teams = member.getTeamIds();
		teams.add(teamId);
		return membreRepository.save(member);
		
	}

	@Override
	public Membre findMemberByEmail(String email){
		return membreRepository.findByEmail(email);
	}

	@Override
	public List<Membre> findTeamsMembers(List<String> teamsIds){
		List<Membre> members = membreRepository.findAllByTeamIdsIn(teamsIds);
		return members;
	}

	@Override
	public Set<Microservice> findAllMembersSubscriptions(List<Membre> members){
		List<Subscription> subscriptionList = new ArrayList<Subscription>();
		Set<Microservice> microservices = new TreeSet<Microservice>(new Comparator<Microservice>(){
			@Override
			public int compare(Microservice m1 , Microservice m2){
				return m1.getId().compareTo(m2.getId());
			}
		});
		for (Membre member : members) {
			List<Subscription> subscriptions = subscriptionService.findAllByUserEmail(member.getEmail());
			subscriptionList.addAll(subscriptions);
		}
		for(Subscription subscription : subscriptionList){
			if(microserviceRepository.findById(subscription.getMicroserviceId()).isPresent()){
				microservices.add(microserviceRepository.findById(subscription.getMicroserviceId()).get());
			}
		}
		return microservices;
	}

	@Override
	public boolean checkUserIsAdmin(String currentEmail) {
		// TODO Auto-generated method stub
		Membre member = membreRepository.findByEmail(currentEmail);
		if(member.getRole().equals("admin")) {
			return true;
		}else {
			return false;
		}
		
		
	}



}
