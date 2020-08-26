package fr.codeonce.grizzlyhub.microservices.domain;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.codeonce.grizzlyhub.swagger.domain.Swagger;

@Document(collection = "microservice")
public class Microservice {

	@Id
	private String id;

	@TextIndexed
	private String title;

	@TextIndexed
	private String description;
	
	private List<Faq> faq;
	
	private List<Documentation> documentation;
		
	private String type ;
	
	private List<String> teamIds;
	
	private String owner;
	
	private List<Swagger> swaggersVersions;

	@LastModifiedDate
	private Date lastUpdateDate;

	@CreatedDate
	private Date creationDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreatedDate() {
		return creationDate;
	}

	public void setCreatedAt(Date createdAt) {
		this.creationDate = createdAt;
	}

	public Microservice() {

	}
	

	public List<Faq> getFaq() {
		return faq;
	}

	public void setFaq(List<Faq> faq) {
		this.faq = faq;
	}

	public List<Documentation> getDocumentation() {
		return documentation;
	}

	public void setDocumentation(List<Documentation> documentation) {
		this.documentation = documentation;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	

	public List<String> getTeamIds() {
		return teamIds;
	}

	public void setTeamIds(List<String> teamIds) {
		this.teamIds = teamIds;
	}
	
	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public List<Swagger> getSwaggersVersions() {
		return swaggersVersions;
	}

	public void setSwaggersVersions(List<Swagger> swaggersVersions) {
		this.swaggersVersions = swaggersVersions;
	}

	public Microservice(String id, String title, String description, Date lastUpdateDate, Date creationDate) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.lastUpdateDate = lastUpdateDate;
		this.creationDate = creationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Microservice [id=" + id + ", title=" + title + ", description=" + description + ", lastUpdateDate="
				+ lastUpdateDate + ", creationDate=" + creationDate + "]";
	}

}
