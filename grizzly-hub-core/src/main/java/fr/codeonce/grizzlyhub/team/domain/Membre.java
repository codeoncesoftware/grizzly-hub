package fr.codeonce.grizzlyhub.team.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "membre")
public class Membre {

	private String id;
	private String email;
	private String role;
	private List<String> teamIds;
	private String organisationId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getTeamIds() {
		return teamIds;
	}
	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
	public String getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}
	public Membre() {
	}
	public Membre(String email, String role, List<String> teamIds, String organisationId) {
		this.email = email;
		this.role = role;
		this.teamIds = teamIds;
		this.organisationId = organisationId;
	}
	
	
	
}
