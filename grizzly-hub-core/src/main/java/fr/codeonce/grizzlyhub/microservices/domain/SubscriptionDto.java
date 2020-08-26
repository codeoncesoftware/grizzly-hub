package fr.codeonce.grizzlyhub.microservices.domain;

import java.util.List;

public class SubscriptionDto {

    private String id;

    private List<String> environment;

    private Long frequence;

    private String microserviceId;
    
    private List<String> changes;
    
    private String userEmail;

    private Boolean emailOption;

    public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public List<String> getChanges() {
		return changes;
	}

	public void setChanges(List<String> changes) {
		this.changes = changes;
	}

	public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public List<String> getEnvironment(){
        return environment;
    }

    public void setEnvironment(List<String> environment){
        this.environment = environment;
    }

    public String getMicroserviceId(){
        return microserviceId;
    }

    public void setMicroserviceId(String microserviceId){
        this.microserviceId = microserviceId;
    }

    public Long getFrequence(){
        return frequence;
    }

    public void setFrequence(Long frequence){
        this.frequence = frequence;
    }

    public Boolean getEmailOption() {
        return emailOption;
    }

    public void setEmailOption(Boolean emailOption) {
        this.emailOption = emailOption;
    }
}
