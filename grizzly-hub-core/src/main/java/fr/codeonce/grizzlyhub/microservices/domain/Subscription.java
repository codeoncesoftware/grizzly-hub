package fr.codeonce.grizzlyhub.microservices.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Subscription")
public class Subscription {

    @Id
    private String id;

    private List<String> environment;

    //private List<String> swaggerId;

    private Long frequence;

    private String microserviceId;

    private String userEmail;
    
    private List<String> changes;

    private Boolean emailOption;

    @CreatedDate
    private Date creationDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

    public List<String> getChanges() {
		return changes;
	}

	public void setChanges(List<String> changes) {
		this.changes = changes;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public List<String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(List<String> environment) {
        this.environment = environment;
    }

    public Long getFrequence() {
        return frequence;
    }

    public void setFrequence(Long frequence) {
        this.frequence = frequence;
    }

    public String getMicroserviceId() {
        return microserviceId;
    }

    public void setMicroserviceId(String microserviceId) {
        this.microserviceId = microserviceId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getCreatedDate() {
        return creationDate;
    }

    public void setCreatedDate(Date createdAt) {
        this.creationDate = createdAt;
    }


    public Boolean getEmailOption() {
        return emailOption;
    }

    public void setEmailOption(Boolean emailOption) {
        this.emailOption = emailOption;
    }

//    public List<String> getSwaggerId() {
//        return swaggerId;
//    }
//
//    public void setSwaggerId(List<String> swaggerId) {
//        this.swaggerId = swaggerId;
//    }

    public Subscription() {

    }


    public Subscription(String microserviceId, List<String> environment, Long frequence, String userEmail , List<String> changes, Boolean emailOption) {
        this.microserviceId = microserviceId;
        this.environment = environment;
        this.frequence = frequence;
        this.userEmail = userEmail;
        this.changes = changes;
        this.emailOption = emailOption;
    }

}


