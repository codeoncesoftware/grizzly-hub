package fr.codeonce.grizzlyhub.auth.service.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.codeonce.grizzlyhub.auth.domain.user.Invitation;
import fr.codeonce.grizzlyhub.auth.domain.user.InvitationRepository;
import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.auth.domain.user.token.TokenRepository;
import fr.codeonce.grizzlyhub.auth.domain.user.token.VerificationToken;
import fr.codeonce.grizzlyhub.auth.service.util.EmailService;
import fr.codeonce.grizzlyhub.auth.service.util.GlobalExceptionUtil;

@PropertySource("classpath:application.yml")
@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);


	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	private InvitationRepository invitationRepository;

	@Qualifier("webApplicationContext")
	@Autowired
	private ResourceLoader resourceloader;

	@Value("${frontUrl}")
	private String url;

	/**
	 * Adds a user to the database using a given userDto
	 * 
	 * @param userDto
	 * @return UserDto
	 * @throws IOException
	 */
	public UserDto addUser(UserDto userDto) throws IOException {

		User userToSave = userMapper.mapToDomain(userDto);
		userToSave.setFirstTime(true);
		String email = userToSave.getEmail();
		if (userRepository.existsByEmailIgnoreCase(email)) {
			throw GlobalExceptionUtil.duplicateNameFound(User.class, email).get();
		} else {
			userToSave.setPassword(DigestUtils.sha256Hex(userToSave.getPassword() + DigestUtils.sha256Hex("co%de01/")));
			User savedUser = userRepository.save(userToSave);
			return userMapper.mapToDto(savedUser);
		}

	}




	public UserDto updateUser(UserDto userDto) {
		String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

		//if (userDto.getEmail().equals(currentEmail)) {
			return this.userRepository.findByEmail(userDto.getEmail()).map(user -> {
				user.setFirstName(userDto.getFirstName());
				user.setLastName(userDto.getLastName());
				user.setPhone(userDto.getPhone());
				user.setFirstTime(userDto.isFirstTime());
				return this.userMapper.mapToDto(this.userRepository.save(user));
//		}
				}).orElseThrow();
//		} else {
//			throw new NoSuchElementException("User is not registered. Please contact us to resolve this issue.");
//		

	}

	/**
	 * Returns a specific user using his email
	 * 
	 * @param email
	 * @return
	 */
	public UserDto getUser(String email) {
		return userMapper.mapToDto(userRepository.findByEmail(email)
				.orElseThrow(GlobalExceptionUtil.notFoundException(User.class, email)));
	}

	/**
	 * Returns all the users in the database
	 * 
	 * @return List<UserDto>
	 */
	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream().map(u -> userMapper.mapToDto(u)).collect(Collectors.toList());
	}

	/**
	 * Verifies the UNICITY of the user email
	 * 
	 * @param email
	 * @return boolean
	 */
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmailIgnoreCase(email);
	}

	/**
	 * Confirm a Given Email Address
	 * 
	 * @param token
	 * @return
	 */
	public void confirmEmail(String token) {
		Optional<VerificationToken> verifToken = this.tokenRepository.findByToken(token);
		if (verifToken.isPresent()) {
			this.tokenRepository.delete(verifToken.get());
			// Verify if Token Not Expired
			Date date = new Date();
			Date tokenDate = verifToken.get().getCreatedDate();
			if (TimeUnit.MILLISECONDS.toMinutes((date.getTime() - tokenDate.getTime())) > 3600) {
				return; // Send Expired Token Exception
			}
			Optional<User> userOp = this.userRepository.findByEmail(verifToken.get().getUserEmail());
			if (userOp.isPresent()) {
				// Activate User
				User user = userOp.get();
				user.setEnabled(true);
				this.userRepository.save(user);
			}
		}
	}

	public void confirmRegistration(String userEmail, String lang) throws IOException {
		VerificationToken token = new VerificationToken(userEmail);
		this.tokenRepository.save(token);

		String subject = "";
		if(lang.contains("en")) {
			subject = "Email Confirmation";
		}
		else {
			subject = "Confirmation de votre email";
		}		String confirmUrl = url + "/confirm/email/" + token.getToken();
		// Get HTML After Processing the THYMELEAF File
		String content = getOutputContent("confirm-registration.html", "templates/confirm-registration", lang,
				confirmUrl);

		emailService.send(content, subject, userEmail);

	}

	/**
	 * Send an Email with Token for Password Reset
	 * 
	 * @param email
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void sendResetPassword(String email, String lang) {

		this.userRepository.findByEmail(email).ifPresentOrElse(user -> {
			StringBuilder tokenValue = new StringBuilder();

			this.tokenRepository.findByUserEmail(email).ifPresentOrElse(tok -> {
				tok.setToken(tokenValue.append(VerificationToken.generateToken(email)).toString());
				this.tokenRepository.save(tok);
			}, () -> {
				VerificationToken token = new VerificationToken(email);
				tokenValue.append(token.getToken());
				this.tokenRepository.save(token);
			});
			// Send Exception if a Token with That Email Already Exists
			String subject = "";
			if(lang.contains("en")) {
				subject = "Password Reset";
			}
			else {
				subject = "Réinitialisation du Mot de passe";
			}
			
			String resetUrl = url + "/reset/" + tokenValue.toString();
			try {
				String content = getOutputContent("reset-password.html", "templates/reset-password", lang, resetUrl);
				emailService.send(content, subject, email);
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
		}, () -> {
			throw new BadCredentialsException("4013"); // Code 3: Email not Registered
		});

	}

	/**
	 * Parse the THYMELEAF template, inject the given data, URL for the Button
	 * 
	 * @param templateFileName
	 * @param variablesPath
	 * @param url
	 * @return the output HTML as String
	 * @throws IOException
	 */
	private String getOutputContent(String templateFileName, String variablesPath, String lang, String url)
			throws IOException {
		Context context = new Context();
		// Choose Language
		if (lang.contains("fr")) {
			variablesPath += "-fr.json";
		} else {
			variablesPath += "-en.json";
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> variables = new ObjectMapper()
				.readValue(resourceloader.getResource("classpath:" + variablesPath).getInputStream(), HashMap.class);

		// Set THYMELEAF Variables from JSON File
		context.setVariables(variables);
		context.setVariable("url", url);
		return templateEngine.process(templateFileName, context);
	}

	// NEEDS TO BE REFACTORED
	public void logout(HttpServletRequest req) {
		try {
			req.logout();
		} catch (ServletException e) {
			log.error("an error occurred!", e);
		}
	}

	/**
	 * Verifies if the password matches the email
	 * 
	 * @param email
	 * @param oldPassword
	 * @return
	 */
	public boolean verifyOldPassword(String email, String oldPassword) {
		return this.userRepository.existsByEmailAndPassword(email,
				DigestUtils.sha256Hex(oldPassword + DigestUtils.sha256Hex("co%de01/")));
	}

	public boolean checkToken(String token) {
		return this.tokenRepository.findByToken(token).isPresent();
	}

	public void resetPassword(String token, String password) {
		tokenRepository.findByToken(token).ifPresent(verifToken -> {
			if (isValidToken(verifToken)) {
				userRepository.findByEmail(verifToken.getUserEmail()).ifPresent(user -> {
					setUserPwd(user, password);
					tokenRepository.deleteById(verifToken.getId());
				});
			}
		});
	}

	public boolean updateUserPwd(String oldPwd, String newPwd) {
		return this.userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
				.map(user -> {
					if (verifyOldPassword(user.getEmail(), oldPwd)) {
						setUserPwd(user, newPwd);
						return true;
					}
					return false;
				}).orElse(false);
	}

	private boolean isValidToken(VerificationToken verifToken) {
		Date date = new Date();
		Date tokenDate = verifToken.getCreatedDate();
		return TimeUnit.MILLISECONDS.toMinutes((date.getTime() - tokenDate.getTime())) <= 3600;
	}

	private void setUserPwd(User user, String newPwd) {
		user.setPassword(DigestUtils.sha256Hex(newPwd + DigestUtils.sha256Hex("co%de01/")));
		userRepository.save(user);
	}

	public String getName(String email) {
		return this.userRepository.findByEmail(email).map(User::getEmail).orElseThrow();
	}

	public void sendInvitation(List<String> userEmails, String lang, String orgId, String orgName ) throws IOException {

		String subject = "";
		if(lang.contains("en")) {
			subject = "Invitation to join " + orgName + " on GrizzlyHUB";
		}
		else {
			subject = "Invitation pour joindre " + orgName + " sur GrizzlyHUB";
		}
		String confirmUrl = url + "/sign-up";
		// Get HTML After Processing the THYMELEAF File
		String content = getOutputContent("invitation.html", "templates/invitation", lang,
				confirmUrl);
		for (String userEmail : userEmails) {
			emailService.send(content, subject, userEmail);
			createInvitation(orgId, orgName, userEmail);

		}
	}

	private void createInvitation(String orgId, String orgName, String userEmail) {
		Invitation i = new Invitation();
		i.setOrganizationId(orgId);
		i.setOrgnizationName(orgName);
		i.setUserEmail(userEmail);
		this.invitationRepository.save(i);
	}

}
