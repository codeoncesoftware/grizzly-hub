package fr.codeonce.grizzlyhub.team.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "team")
public class Team {
	@Id
	private String id;
	private String name;
	private String description;
	private String trigramme; 
	private String organisationId;
	private long totalMembers;
	private String owner;

	@LastModifiedDate
	private Date lastUpdateDate;

	@CreatedDate
	private Date creationDate;
	
	public long getTotalMembers() {
		return totalMembers;
	}
	public void setTotalMembers(long totalMembers) {
		this.totalMembers = totalMembers;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	
	public String getTrigramme() {
		return trigramme;
	}
	public void setTrigramme(String trigramme) {
		this.trigramme = trigramme;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Team() {
		// TODO Auto-generated constructor stub
	}
	public Team( String name, String description, String organisationId) {
		this.name = name;
		this.description = description;
		this.organisationId = organisationId;
	}


	
}
