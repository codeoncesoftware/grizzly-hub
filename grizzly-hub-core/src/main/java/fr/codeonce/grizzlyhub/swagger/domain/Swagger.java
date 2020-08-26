package fr.codeonce.grizzlyhub.swagger.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.String;

import fr.codeonce.grizzlyhub.swagger.domain.SwaggerType;

@Document(collection = "swagger")
public class Swagger {

	@Id
	private String id;

	private SwaggerType swaggerType; //readable only 

	private String version;

	private String url;

	private String microserviceId;

	private String environment;

	private String content;
	
	private String owner;
	
	@LastModifiedDate
	private Date lastUpdateDate;

	@CreatedDate
	private Date creationDate;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SwaggerType getSwaggerType() {
		return swaggerType;
	}

	public void setSwaggerType(SwaggerType swaggerType) {
		this.swaggerType = swaggerType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMicroserviceId() {
		return microserviceId;
	}

	public void setMicroserviceId(String microserviceId) {
		this.microserviceId = microserviceId;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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


	@JsonCreator
	public Swagger(String id, SwaggerType swaggerType, String version, String url, String microserviceId, String environment,
			String content, Date lastUpdateDate, Date creationDate) {
	super();
	this.id = id;
	this.swaggerType = swaggerType;
	this.version = version;
	this.url = url;
	this.microserviceId = microserviceId;
	this.environment = environment;
	this.content = content;
	this.lastUpdateDate = lastUpdateDate;
	this.creationDate = creationDate;
}

	public Swagger() {
		super();
	}

	@Override
	public String toString() {
		return "Swagger [id=" + id + ", swaggerType=" + swaggerType + ", version=" + version + ", url=" + url
				+ ", microserviceId=" + microserviceId + ", environment=" + environment + ", content=" + content
				+ ", lastUpdateDate=" + lastUpdateDate + ", creationDate=" + creationDate + "]";
	}

	public Swagger(String url, String environment) {
		this.url = url;
		this.environment = environment;
	}
	
	

}
