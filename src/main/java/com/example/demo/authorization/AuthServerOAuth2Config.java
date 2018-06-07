package com.example.demo.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Important articles for implementing Spring security:
 *
 * https://tools.ietf.org/html/rfc6749
 * https://spring.io/guides/topicals/spring-security-architecture/
 * https://stackoverflow.com/questions/44671457/what-is-the-use-of-enablewebsecurity-in-spring
 * https://projects.spring.io/spring-security-oauth/docs/Home.html
 * https://projects.spring.io/spring-security-oauth/docs/oauth2.html
 * https://www.oauth.com/oauth2-servers/the-resource-server/
 *
 * https://piotrminkowski.wordpress.com/tag/spring-security/
 *
 *
 * If you define a @Configuration with @EnableWebSecurity anywhere in your application it will switch off the default
 * webapp security settings in Spring Boot (but leave the Actuator’s security enabled).
 *
 *
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {

    private static final String CLIEN_ID = "devglan-client";
    private static final String CLIENT_SECRET = "$2a$04$e/c1/RfsWuThaWFCrcCuJeoyvwCV0URN/6Pn9ZFlrtIWaU/vj/BfG";
    private static final String GRANT_TYPE_PASSWORD = "password";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String IMPLICIT = "implicit";
    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";
    private static final String TRUST = "trust";
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60;
    private static final int FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;


    @Autowired
    private TokenStore tokenStore;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient(CLIEN_ID)
                .secret(CLIENT_SECRET)
                .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS).
                refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);

    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
    }
}

