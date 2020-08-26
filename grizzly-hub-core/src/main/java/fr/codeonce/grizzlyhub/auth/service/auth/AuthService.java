package fr.codeonce.grizzlyhub.auth.service.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



import fr.codeonce.grizzlyhub.auth.domain.config.GrizzlyCoreProperties;
import fr.codeonce.grizzlyhub.auth.domain.user.User;
import fr.codeonce.grizzlyhub.auth.domain.user.UserRepository;
import fr.codeonce.grizzlyhub.auth.service.user.UserDto;
import fr.codeonce.grizzlyhub.auth.service.user.UserService;
import fr.codeonce.grizzlyhub.auth.service.util.SecurityContextUtil;

@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private GrizzlyCoreProperties properties;

	@Autowired
	private UserRepository userRepository;


	@Autowired
	private UserService userService;

	@Value("${server.port}")
	private String port;

	@Value("${github.client.clientId}")
	private String githubClientId;

	@Value("${github.client.clientSecret}")
	private String githubClientSecret;

	@Value("${github.client.accessTokenUri}")
	private String githubAccessTokenUri;

	@Value("${github.resource.userInfoUri}")
	private String githubUserInfoUri;

	@Value("${github.resource.salt}")
	private String githubSalt;

	/**
	 * Authenticate the user using email and password
	 * 
	 * @param email
	 * @param password
	 * @return token
	 */
	public String login(String email, String password) {

		Optional<User> opUser = this.userRepository.findByEmail(email);
		if (opUser.isPresent() && !opUser.get().isEnabled()) {
			throw new BadCredentialsException("4012"); // Code 2: account not activated
		}
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("url construction");

		String url = prepareToken(email, password);

		RestTemplate restTamplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<String> result;

		stopWatch.stop();

		try {
			stopWatch.start("The login process");
			result = restTamplate.exchange(url, HttpMethod.POST, request, String.class);
			stopWatch.stop();

			log.debug(stopWatch.prettyPrint());

			SecurityContextUtil.setUpSecurityContext(email);
			UserDto currentUser = userService.getUser(email);
			if (currentUser.isFirstTime()) {
				currentUser.setFirstTime(false);
				currentUser.setPassword(null);
				userService.updateUser(currentUser);
			}
			@SuppressWarnings("unchecked")
			Map<String, String> loginRes = new ObjectMapper().readValue(result.getBody(), Map.class);
			loginRes.put("email", email);
			return new ObjectMapper().writeValueAsString(loginRes);

		} catch (HttpClientErrorException.BadRequest e) {
			String responseBodyAsString = e.getResponseBodyAsString();
			if (responseBodyAsString.contains("Bad credentials")) {
				throw new BadCredentialsException("4011"); // Code 1: invalid credentials
			}

		} catch (IOException e) {
			throw new IllegalArgumentException("Cannot create Project Example");
		}

		throw new IllegalStateException("an error occurred with rest call : TODO");
	}

	private String prepareToken(String email, String password) {
		String clientId = this.properties.getOauth2().getClientId();
		String clientSecret = this.properties.getOauth2().getClientSecret();
		String grantType = this.properties.getOauth2().getGrantType();
		String jwtKey = this.properties.getOauth2().getJwtKey();

		return "http://localhost:" + port + "/oauth/token?client_id=" + clientId + "&client_secret=" + clientSecret
				+ "&grant_type=" + grantType + "&username=" + email + "&password="
				+ DigestUtils.sha256Hex(password + DigestUtils.sha256Hex("co%de01/")) + "&jwt_key=" + jwtKey;

	}

	@SuppressWarnings("unchecked")
	public String loginWithGithub(String code, HttpServletRequest req) throws IllegalAccessException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> args = new HashMap<>();
		args.put("code", code);
		args.put("client_id", githubClientId);
		args.put("client_secret", githubClientSecret);
		Map<String, String> res = restTemplate.postForObject(githubAccessTokenUri, args, Map.class);
		if (!ObjectUtils.allNotNull(res.get("access_token"))) {
			throw new IllegalAccessException("Cannot access Github Account");
		}
		res = restTemplate.getForObject(githubUserInfoUri + "?access_token=" + res.get("access_token"), Map.class);
		log.info(String.valueOf(res));
		Optional<User> opUser = userRepository.findByEmail(String.valueOf(res.get("id")) );
		User githubUser;
		if (opUser.isPresent()) {
			githubUser = opUser.get();
			if (!githubUser.getPassword().equals(DigestUtils.sha256Hex(githubSalt + String.valueOf(res.get("id")) + DigestUtils.sha256Hex("co%de01/")))) {
				throw new IllegalStateException("4014"); // The linked Github email already linked with an account
			}
		} else {
			githubUser = createNewGithubUser(res);

		}
		userRepository.save(githubUser);
		return login(String.valueOf(res.get("id")) , githubSalt + String.valueOf(res.get("id")) );
	}

	private User createNewGithubUser(Map<String, String> res) {
		User githubUser = new User();
		githubUser.setFirstName(res.get("login"));
		githubUser.setLastName(res.get("login"));
		githubUser.setEmail(String.valueOf(res.get("id")) );
		githubUser
				.setPassword(DigestUtils.sha256Hex(githubSalt + String.valueOf(res.get("id"))  + DigestUtils.sha256Hex("co%de01/")));
		githubUser.setEnabled(true);
		githubUser.setFirstTime(true);
		return githubUser;
	}

}
