package fr.codeonce.grizzlyhub.swagger.domain;

public class SwaggerDiffDto {

	private String oldSwaggerId;
	private String newSwaggerId;

	public String getOldSwaggerId() {
		return oldSwaggerId;
	}

	public void setOldSwaggerId(String oldSwaggerId) {
		this.oldSwaggerId = oldSwaggerId;
	}

	public String getNewSwaggerId() {
		return newSwaggerId;
	}

	public void setNewSwaggerId(String newSwaggerId) {
		this.newSwaggerId = newSwaggerId;
	}

	public SwaggerDiffDto(String oldSwaggerId, String newSwaggerId) {
		super();
		this.oldSwaggerId = oldSwaggerId;
		this.newSwaggerId = newSwaggerId;
	}
	
}
