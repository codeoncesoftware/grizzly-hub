package fr.codeonce.grizzlyhub.team.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "organisation")
public class Organisation {
	@Id
	private String id;
	private String email;
	private String description;
	private String name;
	private long totalMembers;
	private long totalTeams;
	@LastModifiedDate
	private Date lastUpdateDate;

	@CreatedDate
	private Date creationDate;
	
	
	

	public long getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(long totalNumbers) {
		this.totalMembers = totalNumbers;
	}

	public long getTotalTeams() {
		return totalTeams;
	}

	public void setTotalTeams(long totalTeams) {
		this.totalTeams = totalTeams;
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

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Organisation() {
		// TODO Auto-generated constructor stub
	}

	public Organisation(String id, String email, String description, String name) {
		this.id = id;
		this.email = email;
		this.description = description;
		this.name = name;
	}
	
	
	
	
}
