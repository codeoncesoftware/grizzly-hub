package fr.codeonce.grizzlyhub.auth.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import fr.codeonce.grizzlyhub.auth.domain.config.GrizzlyCoreProperties;
import fr.codeonce.grizzlyhub.auth.service.auth.MongoUserDetailsService;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private GrizzlyCoreProperties properties;

	public OAuth2AuthorizationServerConfig(GrizzlyCoreProperties properties) {
		this.properties = properties;
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(properties.getOauth2().getJwtKey());
		return tokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private MongoUserDetailsService userDetailsService;

		private final AuthenticationManager authenticationManager;

		private final TokenStore tokenStore;

		@Autowired
		private final GrizzlyCoreProperties properties;

		private final AccessTokenConverter accessTokenConverter;

		@Autowired
		private PasswordEncoder passwordEncoder;

		public AuthorizationServerConfiguration(
				@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager,
				TokenStore tokenStore, GrizzlyCoreProperties properties, AccessTokenConverter accessTokenConverter) {

			this.authenticationManager = authenticationManager;
			this.tokenStore = tokenStore;
			this.properties = properties;
			this.accessTokenConverter = accessTokenConverter;
		}

		@Bean
		protected AuthorizationCodeServices authorizationCodeServices() {
			return new InMemoryAuthorizationCodeServices();
		}

		@Bean
		public ApprovalStore approvalStore() {
			return new InMemoryApprovalStore();
		}

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authorizationCodeServices(authorizationCodeServices()).approvalStore(approvalStore())
					.tokenStore(tokenStore).accessTokenConverter(accessTokenConverter)
					.authenticationManager(authenticationManager).userDetailsService(userDetailsService);
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer.allowFormAuthenticationForClients();
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient(properties.getOauth2().getClientId())
					.secret(passwordEncoder.encode(properties.getOauth2().getClientSecret()))
					.accessTokenValiditySeconds(properties.getOauth2().getTokenValidity())
					.refreshTokenValiditySeconds(properties.getOauth2().getRefreshTokenValidity())
					.authorizedGrantTypes(properties.getOauth2().getGrantType().split(","))
					.scopes(properties.getOauth2().getScope().split(",")).resourceIds("code_once_rm");

		}

	}
}