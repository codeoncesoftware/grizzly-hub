package fr.codeonce.grizzlyhub.microservices.domain;

public class Documentation {
	private String endpoint;
	private String doc;
	private String operation;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}





	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	
	public Documentation(String endpoint, String doc, String operation) {
		this.endpoint = endpoint;
		this.doc = doc;
		this.operation = operation;
	}

	public Documentation() {
		super();
		// TODO Auto-generated constructor stub
	}

}
