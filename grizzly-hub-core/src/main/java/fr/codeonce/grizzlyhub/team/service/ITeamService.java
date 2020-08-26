package fr.codeonce.grizzlyhub.team.service;

import java.util.List;

import fr.codeonce.grizzlyhub.team.domain.Team;

public interface ITeamService {
	public List<Team> findAllTeams();
	public Team addTeam(Team team, String adminEmail);
	public Team updateTeam(Team team, String id);
	public List<Team> findByOrganisationId(String organisationId);
	public void deleteTeam(String id);
	public Team findById(String id);
	public void deleteMemberFromTeam(String idTeam, String idMember);

}
