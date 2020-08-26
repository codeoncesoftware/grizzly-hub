package fr.codeonce.grizzlyhub.swagger.domain;

import org.springframework.web.multipart.MultipartFile;

public class SwaggerFileDto {

	private String environment;
	private String url;
	private MultipartFile file;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public SwaggerFileDto() {
		super();
	}

}
