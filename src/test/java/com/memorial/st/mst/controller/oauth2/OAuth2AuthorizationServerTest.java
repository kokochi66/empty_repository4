package com.memorial.st.mst.controller.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.memorial.st.mst.domain.client.service.DBRegisteredClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OAuth2AuthorizationServerTest {

    @Autowired
    private DBRegisteredClientRepository dbRegisteredClientRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RegisteredClientRepository registeredClientRepository;
    @Autowired
    private JwtDecoder jwtDecoder;

    private RegisteredClient registeredClient;

    @BeforeEach
    public void setUp() {
        registeredClient = RegisteredClient.withId("test-client")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/callback")
                .scope("read")
                .scope("write")
                .build();
        // Save the registered client
        dbRegisteredClientRepository.save(registeredClient);
    }

    @Test
    public void saveAndFindRegisteredClient() {


        // Retrieve the registered client by client ID
        RegisteredClient retrievedClient = dbRegisteredClientRepository.findById(registeredClient.getId());

        // Assert that the retrieved client is the same as the saved client
        assertThat(retrievedClient).isNotNull();
        assertThat(retrievedClient.getId()).isEqualTo(registeredClient.getId());
        assertThat(retrievedClient.getClientId()).isEqualTo(registeredClient.getClientId());
        assertThat(retrievedClient.getClientSecret()).isEqualTo(registeredClient.getClientSecret());
        assertThat(retrievedClient.getClientAuthenticationMethods()).isEqualTo(registeredClient.getClientAuthenticationMethods());
        assertThat(retrievedClient.getAuthorizationGrantTypes()).isEqualTo(registeredClient.getAuthorizationGrantTypes());
        assertThat(retrievedClient.getRedirectUris()).isEqualTo(registeredClient.getRedirectUris());
        assertThat(retrievedClient.getScopes()).isEqualTo(registeredClient.getScopes());
    }

    @Test
    public void requestTokenTest() throws Exception {
        // Retrieve the registered client
        RegisteredClient registeredClient = registeredClientRepository.findByClientId("client-id");

        // Request the token
        MvcResult result = mockMvc.perform(post("/oauth2/token")
                        .param("grant_type", "client_credentials")
                        .param("scope", "read")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((registeredClient.getClientId() + ":" + registeredClient.getClientSecret()).getBytes()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the token response
        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        String accessToken = jsonResponse.get("access_token").asText();

        // Verify the token
        Jwt jwt = jwtDecoder.decode(accessToken);

        // Assertions
        assertThat((String) jwt.getClaim("scope")).isEqualTo("read");
    }
}
