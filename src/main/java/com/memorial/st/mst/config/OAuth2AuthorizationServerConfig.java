package com.memorial.st.mst.config;

import com.memorial.st.mst.utils.KeyPairGeneratorUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;

import javax.net.ssl.KeyManager;
import java.security.KeyPair;
import java.util.UUID;

@Configuration
public class OAuth2AuthorizationServerConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-id")
                .clientSecret("client-secret")
                .clientAuthenticationMethod(new ClientAuthenticationMethod("basic"))
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8080/login/oauth2/code/callback")
                .scopes(scopes -> {
                    scopes.add("read");
                    scopes.add("write");
                })
//                .clientSettings(clientSettings -> clientSettings(true))      0.2.0 에서는 기본값으로 사용자 동의를 요구하도록 설정 되어있음
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public KeyManager keyManager() {
        // RSA 키 페어를 생성합니다. 실제 환경에서는 안전한 키 저장소를 사용하세요.
        KeyPair keyPair = KeyPairGeneratorUtils.generateRsaKeyPair(2048);
        return new StaticKeyManager(keyPair);
    }

    @Bean
    public JwtEncoder jwtEncoder(KeyManager keyManager) {
        return new NimbusJwtEncoder(keyManager);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(RegisteredClientRepository registeredClientRepository) {
        return new InMemoryOAuth2AuthorizationService(registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(RegisteredClientRepository registeredClientRepository) {
        return new InMemoryOAuth2AuthorizationConsentService(registeredClientRepository);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://localhost:8080")
                .build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}
