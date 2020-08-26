package fr.codeonce.grizzlyhub.swagger.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class SwaggerToShow {
	private String swaggerType;
	private String swaggerVersion;
	private String swaggerName;
	private boolean inProd;
	private JsonNode swaggerAuth;
	private Date lastUpdated;
	public String getSwaggerType() {
		return swaggerType;
	}
	public void setSwaggerType(String swaggerType) {
		this.swaggerType = swaggerType;
	}
	public String getSwaggerVersion() {
		return swaggerVersion;
	}
	public void setSwaggerVersion(String swaggerVersion) {
		this.swaggerVersion = swaggerVersion;
	}
	public String getSwaggerName() {
		return swaggerName;
	}
	public void setSwaggerName(String swaggerName) {
		this.swaggerName = swaggerName;
	}
	public boolean isInProd() {
		return inProd;
	}
	public void setInProd(boolean inProd) {
		this.inProd = inProd;
	}
	public JsonNode getSwaggerAuth() {
		return swaggerAuth;
	}
	public void setSwaggerAuth(JsonNode swaggerAuth) {
		this.swaggerAuth = swaggerAuth;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
