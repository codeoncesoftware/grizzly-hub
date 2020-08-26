package fr.codeonce.grizzlyhub.microservices.domain;

public class Faq {
	
	
	private String response;
	
	private String questionTitle;


	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public Faq() {

	}

	public Faq(String response, String questionTitle) {
		this.response = response;
		this.questionTitle = questionTitle;
	}
	
}
