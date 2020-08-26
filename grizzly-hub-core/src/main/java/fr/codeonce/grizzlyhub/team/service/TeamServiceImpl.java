package fr.codeonce.grizzlyhub.team.service;

import java.util.List;

import fr.codeonce.grizzlyhub.auth.service.util.GlobalExceptionUtil;
import fr.codeonce.grizzlyhub.microservices.domain.Microservice;
import fr.codeonce.grizzlyhub.microservices.domain.MicroserviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.codeonce.grizzlyhub.auth.controller.AuthController;
import fr.codeonce.grizzlyhub.team.domain.MemberRepository;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.domain.TeamRepository;

@Service
public class TeamServiceImpl implements ITeamService {
	@Autowired
	TeamRepository teamRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	IMembreService membreService;
	@Autowired
	MicroserviceRepository microserviceRepository;

	private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);

	@Override
	public List<Team> findAllTeams() {
		List<Team> teams = teamRepository.findAll();
		teamRepository.findAll();
		teams.forEach(team -> {
			team.setTotalMembers(memberRepository.countByTeamIds(team.getId()));
		});
		return teams;
	}

	@Override
	public Team addTeam(Team team, String adminEmail) {
		team.setTotalMembers(team.getTotalMembers()+1);
		team.setOwner(adminEmail);
		Team teamCreated = teamRepository.save(team);

		Membre member = memberRepository.findByEmail(adminEmail);
		member.getTeamIds().add(teamCreated.getId());
		memberRepository.save(member);
		return teamCreated;
	}

	@Override
	public Team updateTeam(Team team, String id) {
		team.setId(id);
		return teamRepository.save(team);

	}

	@Override
	public List<Team> findByOrganisationId(String organisationId) {
		log.info("orgid: {}", organisationId);
		return teamRepository.findByOrganisationId(organisationId);
	}

	@Override
	public void deleteMemberFromTeam(String idTeam , String email) {
			Membre member = memberRepository.findByEmail(email);
			List<String> teams = member.getTeamIds();
			teams.remove(idTeam);
			member.setTeamIds(teams);
			memberRepository.save(member);
	}
	
	@Override
	public void deleteTeam(String id) {
		membreService.findByTeamId(id).forEach(member -> {
			deleteMemberFromTeam(id,member.getEmail());
		});
		microserviceRepository.findAllByTeamIds(id).forEach(ms -> {
			deleteMicroserviceFromTeam(id,ms.getId());
		});
		teamRepository.deleteById(id);
	}

	private void deleteMicroserviceFromTeam(String id, String msId) {
		Microservice ms = microserviceRepository.findById(msId)
				.orElseThrow(GlobalExceptionUtil.notFoundException(Microservice.class, msId));
		List<String> teams = ms.getTeamIds();
		teams.remove(id);
		ms.setTeamIds(teams);
		ms.setType("private");
		microserviceRepository.save(ms);
	}

	@Override
	public Team findById(String id) {
		 Team team = teamRepository.findById(id).get();
			team.setTotalMembers(memberRepository.countByTeamIds(team.getId()));
		 return team;
	}

}
