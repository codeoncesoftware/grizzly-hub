package fr.codeonce.grizzlyhub.swagger.domain;

public class SwaggerDto {

	private String url;
	private String environment;
	private String microserviceId;
	private String owner;
	
	

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getMicroserviceId() {
		return microserviceId;
	}

	public void setMicroserviceId(String microserviceId) {
		this.microserviceId = microserviceId;
	}

	public SwaggerDto(String url, String environment, String microserviceId,String owner) {
		this.url = url;
		this.environment = environment;
		this.microserviceId = microserviceId;
		this.owner=owner;
	}
	
	

}
