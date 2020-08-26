package fr.codeonce.grizzlyhub.swagger.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.codeonce.grizzlyhub.microservices.domain.Microservice;

public class SwaggerListDto {
	private Microservice microservice;

	private List<Swagger> swaggers = new ArrayList<>();
	private String modification ; 
//
//	@Autowired
//	private List<SwaggerFileDto> files;

	private List<Swagger> newSwaggers = new ArrayList<>();


	public Microservice getMicroservice() {
		return microservice;
	}

	public void setMicroservice(Microservice microservice) {
		this.microservice = microservice;
	}

	public List<Swagger> getSwaggers() {
		return swaggers;
	}

	public String getModification() {
		return modification;
	}

	public void setModification(String modification) {
		this.modification = modification;
	}

	public void setSwaggers(List<Swagger> swaggers) {
		this.swaggers = swaggers;
	}

	public void addSwagger(Swagger swagger) {
		this.swaggers.add(swagger);
	}
//
//	public List<SwaggerFileDto> getFiles() {
//		return files;
//	}
//
//	public void setFiles(List<SwaggerFileDto> files) {
//		this.files = files;
//	}

	public List<Swagger> getNewSwaggers() {
		return newSwaggers;
	}

	public void setNewSwaggers(List<Swagger> newSwaggers) {
		this.newSwaggers = newSwaggers;
	}
	
	

}
