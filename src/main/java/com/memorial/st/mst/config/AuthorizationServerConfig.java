package com.memorial.st.mst.config;

import com.memorial.st.mst.domain.client.service.ClientEntityRepository;
import com.memorial.st.mst.domain.client.service.DBRegisteredClientRepository;
import com.memorial.st.mst.utils.KeyPairGeneratorUtils;
import com.memorial.st.mst.utils.MstKeyManager;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AuthorizationServerConfig extends WebSecurityConfigurerAdapter {

    private final ClientEntityRepository clientEntityRepository;

    @Bean
    public RegisteredClientRepository registeredClientRepository(DBRegisteredClientRepository dbRegisteredClientRepository) {
        return dbRegisteredClientRepository;
    }

    @Bean
    public MstKeyManager keyManager() throws Exception {
        // RSA 키 페어를 생성합니다. 실제 환경에서는 안전한 키 저장소를 사용하세요.
        KeyPair keyPair = KeyPairGeneratorUtils.generateRsaKeyPair(2048);
        return new MstKeyManager(keyPair);
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource(MstKeyManager keyManager) {
        RSAPublicKey publicKey = keyManager.getPublicKey();
        RSAKey rsaKey = new RSAKey.Builder(publicKey).keyID("mst-key-id").build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://localhost:8080")
                .tokenEndpoint("/oath2/token")
                .build();
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        http
                .requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .apply(authorizationServerConfigurer)
                .oauth2AuthorizationServer(authorizationServer ->
                        authorizationServer.providerSettings(providerSettings())
                );
        return http.build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll() // 모든 요청에 대한 접근을 허용
                .and()
                .csrf().disable() // CSRF 보호 비활성화
                .formLogin().disable() // 폼 로그인 비활성화
                .httpBasic().disable() // HTTP Basic 인증 비활성화
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }

    @Bean
    public JwtDecoder jwtDecoder(MstKeyManager keyManager) {
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(keyManager.getJWKSet());
        JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWSKeySelector<SecurityContext> jwsKeySelector = new JWSVerificationKeySelector<>(jwsAlgorithm, jwkSource);
        jwtProcessor.setJWSKeySelector(jwsKeySelector);
        return new NimbusJwtDecoder(jwtProcessor);
    }
}
