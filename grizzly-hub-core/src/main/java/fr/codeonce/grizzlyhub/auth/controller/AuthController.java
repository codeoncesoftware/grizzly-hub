package fr.codeonce.grizzlyhub.auth.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import fr.codeonce.grizzlyhub.auth.domain.user.Invitation;
import fr.codeonce.grizzlyhub.auth.domain.user.InvitationRepository;
import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.auth.domain.user.token.TokenRepository;
import fr.codeonce.grizzlyhub.auth.service.auth.AuthService;
import fr.codeonce.grizzlyhub.auth.service.auth.LoginDto;
import fr.codeonce.grizzlyhub.auth.service.user.UserDto;
import fr.codeonce.grizzlyhub.auth.service.user.UserService;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;
import fr.codeonce.grizzlyhub.team.domain.Membre;
import fr.codeonce.grizzlyhub.team.domain.Team;
import fr.codeonce.grizzlyhub.team.service.ITeamService;
import fr.codeonce.grizzlyhub.team.service.MemberServiceImpl;


@RestController
@CrossOrigin(origins ="localhost:4200")
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	ITeamService teamService;
	
	@Autowired
	private InvitationRepository invitationRepository;
	
	@Autowired
	private MemberServiceImpl memberService;

	/**
	 * Authenticate the user using email and password
	 * 
	 * @param login
     * @param req
	 * @return token
	 * @throws IOException
	 */
	@PostMapping("/login")
	public String login(@RequestBody LoginDto login, HttpServletRequest req) throws IOException {
		log.info("start login with : {}", login.getEmail());
		return authService.login(login.getEmail(), login.getPassword());
	}

	/**
	 * Save a new User after Registration
	 * 
	 * @param userDto
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/signup")
	public UserDto signup(@RequestBody UserDto userDto, HttpServletRequest req) throws IOException {
		log.info("request signup {}", userDto.getEmail());
		log.info("Lang : {}", req.getHeader("Accept-Language"));
		UserDto user = userService.addUser(userDto);
		userService.confirmRegistration(user.getEmail(), req.getHeader("Accept-Language"));
		
		Optional<Invitation> inv = invitationRepository.findByUserEmail(user.getEmail());
		if(inv.isPresent()) {
			Membre m = new Membre();
			m.setEmail(inv.get().getUserEmail());
			m.setOrganisationId(inv.get().getOrganizationId());
			m.setRole("consumer");
			memberService.saveMembre(m);
		}
		
		return user;
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/github/login")
	public String githubLogin(@RequestParam String code, HttpServletRequest req, HttpServletResponse response)
			throws IllegalAccessException, IOException {
		return authService.loginWithGithub(code, req);
	}

	/**
	 * Check the Email's Unicity
	 * 
	 * @param email
	 * @param req
	 * @return
	 */
	@GetMapping("/check/{email}")
	public boolean existsByEmail(@PathVariable String email, HttpServletRequest req) {
		log.info("request check email: {}", email);
		return userService.existsByEmail(email);
	}

	/**
	 * Verify the received Token and enable the associated user email
	 * 
	 * @param token
	 * @param res
	 * @throws IOException
	 */
	@GetMapping("/confirm/email/{token}")
	public void confirmEmail(@PathVariable String token, HttpServletResponse res) throws IOException {
		log.info("request to confirm email token received");
		userService.confirmEmail(token);
	}



	/**
	 * Checks the Token Validity
	 * 
	 * @param token
	 * @return
	 */
	@GetMapping("/check/token/{token}")
	public boolean checkToken(@PathVariable String token) {
		log.info("request to check Token validaty");
		return userService.checkToken(token);
	}

	

}
