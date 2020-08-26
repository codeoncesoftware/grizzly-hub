package fr.codeonce.grizzlyhub.swagger.domain;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class SwaggerCheckDto {
	String url;
	String environment;
	
	private File file;
	
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
//	public MultipartFile getFile() {
//		return file;
//	}
//	public void setFile(MultipartFile file) {
//		this.file = file;
//	}
	public SwaggerCheckDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
