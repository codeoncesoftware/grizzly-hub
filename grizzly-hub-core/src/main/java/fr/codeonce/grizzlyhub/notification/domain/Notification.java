package fr.codeonce.grizzlyhub.notification.domain;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notification")
public class Notification {

	@Id
	private String id;

	private String commentaire;

	private String idOldSwagger;

	private String idNewSwagger;

	private String idSubscription;

	private String microserviceTitle;

	private Boolean opened;
	
	private Boolean sent;

	private Date sendingDate;
	
	private String owner ;
	
	private String environment;
	
	
	
	

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getMicroserviceTitle() {
		return microserviceTitle;
	}

	public void setMicroserviceTitle(String microserviceTitle) {
		this.microserviceTitle = microserviceTitle;
	}

	public Date getSendingDate() {
		return sendingDate;
	}

	public void setSendingDate(Date sendingDate) {
		this.sendingDate = sendingDate;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	@LastModifiedDate
	private Date lastUpdateDate;

	@CreatedDate
	private Date creationDate;

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getIdOldSwagger() {
		return idOldSwagger;
	}

	public void setIdOldSwagger(String idOldSwagger) {
		this.idOldSwagger = idOldSwagger;
	}

	public String getIdNewSwagger() {
		return idNewSwagger;
	}

	public void setIdNewSwagger(String idNewSwagger) {
		this.idNewSwagger = idNewSwagger;
	}

	public String getIdSubscription() {
		return idSubscription;
	}

	public void setIdSubscription(String idSubscription) {
		this.idSubscription = idSubscription;
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

	public Notification() {
	}

	public Notification(String idOldSwagger, String idNewSwagger, String idSubscription, Boolean opened, Boolean sent, String microserviceTitle,String commentaire,String owner,String environment) {
		this.sent=sent;
		this.opened = opened;
		this.idOldSwagger = idOldSwagger;
		this.idNewSwagger = idNewSwagger;
		this.idSubscription = idSubscription;
		this.microserviceTitle = microserviceTitle;
		this.commentaire= commentaire;
		this.owner=owner;
		this.environment=environment;
	}

}
