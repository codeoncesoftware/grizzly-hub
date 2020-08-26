package fr.codeonce.grizzlyhub.auth.domain.user.token;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verification-token")
public class VerificationToken {

	@Id
	private String id;
	private String token;
	@CreatedDate
	private Date createdDate;
	@Indexed(unique = true)
	private String userEmail;

	public VerificationToken(String userEmail) {
		super();
		this.userEmail = userEmail;
		this.token = DigestUtils.sha256Hex(userEmail + DigestUtils.sha256Hex("co%de01/"));
	}

	public String getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public static String generateToken(String email) {
		return DigestUtils.sha256Hex(email + DigestUtils.sha256Hex("co%de01/"));
	}

}
