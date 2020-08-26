package fr.codeonce.grizzlyhub.auth.service.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class SecurityContextUtil {
	
	private SecurityContextUtil() {
		super();
	}

	public static void setUpSecurityContext(String userLogin) {
		Authentication authentication = new PreAuthenticatedAuthenticationToken(userLogin, "N/A");
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}
	
	public static String getCurrentUserEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
