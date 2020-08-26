package fr.codeonce.grizzlyhub.notification.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notificationForMicroservice")
public class NotificationForMicroservice {
	@Id
	private String id;
	
	private String microserviceTitle;
	
	private String idSubscription;

	private Boolean opened;
	
	private Boolean sent;

	private Date sendingDate;
	
	private String owner ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
	public String getIdSubscription() {
		return idSubscription;
	}

	public void setIdSubscription(String idSubscription) {
		this.idSubscription = idSubscription;
	}

	public String getMicroserviceTitle() {
		return microserviceTitle;
	}

	public void setMicroserviceTitle(String microserviceTitle) {
		this.microserviceTitle = microserviceTitle;
	}

	public Boolean getOpened() {
		return opened;
	}

	public void setOpened(Boolean opened) {
		this.opened = opened;
	}

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	public Date getSendingDate() {
		return sendingDate;
	}

	public void setSendingDate(Date sendingDate) {
		this.sendingDate = sendingDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public NotificationForMicroservice() {
		// TODO Auto-generated constructor stub
	}

	public NotificationForMicroservice(String microserviceTitle, Boolean opened, Boolean sent, Date sendingDate,
			String owner,String idSubscription) {
		this.microserviceTitle = microserviceTitle;
		this.opened = opened;
		this.sent = sent;
		this.sendingDate = sendingDate;
		this.owner = owner;
		this.idSubscription = idSubscription;
	}
	
	
	}
