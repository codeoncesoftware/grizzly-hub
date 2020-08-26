package fr.codeonce.grizzlyhub.auth.domain.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "core", ignoreUnknownFields = false)
@Component
@Primary
public class GrizzlyCoreProperties {

    private Oauth2 oauth2 = new Oauth2();

    public Oauth2 getOauth2() {
        return oauth2;
    }
    
    public static class Oauth2 {

    	
        private String clientId;

        private String clientSecret;

        private String grantType;

        private String scope;

        private int tokenValidity;

        private int refreshTokenValidity;

        private String jwtKey;

        private String url ;


        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public int getTokenValidity() {
            return tokenValidity;
        }

        public void setTokenValidity(int tokenValidity) {
            this.tokenValidity = tokenValidity;
        }

        public int getRefreshTokenValidity() {
            return refreshTokenValidity;
        }

        public void setRefreshTokenValidity(int refreshTokenValidity) {
            this.refreshTokenValidity = refreshTokenValidity;
        }

        public String getJwtKey() {
            return jwtKey;
        }

        public void setJwtKey(String jwtKey) {
            this.jwtKey = jwtKey;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
