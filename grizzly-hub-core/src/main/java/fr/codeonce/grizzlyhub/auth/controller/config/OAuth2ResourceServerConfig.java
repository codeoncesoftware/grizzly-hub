package fr.codeonce.grizzlyhub.auth.controller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	public OAuth2ResourceServerConfig(TokenStore tokenStore) {
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().and().csrf().disable().headers().frameOptions().disable().and().httpBasic().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and().authorizeRequests()
				// .antMatchers(SecurityParams.PUBLIC_ROUTES).permitAll()
				// .antMatchers("/**").permitAll();
				// PUBLIC API
//				.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()//
				.antMatchers("/api/auth/**").permitAll().antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/api/microservice/content/**").permitAll()
//				.antMatchers("/api/resource/public/**").permitAll()//
//				.antMatchers("/api/dbsource/public/**").permitAll()//
//				.antMatchers("/api/container/public/**").permitAll()//
//				.antMatchers("/api/swagger/**").permitAll()//
//				.antMatchers("/github/login").permitAll();//
				// SECURED API
				.antMatchers("/api/microservice/allPublic").anonymous()
				.antMatchers("/api/microservice/findByName/**").anonymous()
				.antMatchers("/api/microservice/public/**").anonymous()
				.antMatchers("/api/swagger/public/**").anonymous()
				.and().authorizeRequests().antMatchers("/api/**")
				.authenticated();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("code_once_rm");
	}
}
