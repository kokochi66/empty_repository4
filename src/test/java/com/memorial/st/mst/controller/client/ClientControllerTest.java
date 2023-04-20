package com.memorial.st.mst.controller.client;

import com.memorial.st.mst.controller.client.model.ClientRegistrationRequest;
import com.memorial.st.mst.controller.client.model.ClientResponse;
import com.memorial.st.mst.controller.client.model.ClientUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegisterAndUpdate() {
        // Arrange
        String clientName = "testClient";
        ClientRegistrationRequest registrationRequest = new ClientRegistrationRequest();
        registrationRequest.setClientName(clientName);

        ResponseEntity<ClientResponse> registrationResponse = restTemplate.postForEntity(
                "/api/client/register", registrationRequest, ClientResponse.class);

        assertThat(registrationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registrationResponse.getBody()).isNotNull();
        assertThat(registrationResponse.getBody().getId()).isNotNull();
        assertThat(registrationResponse.getBody().getClientId()).isNotNull();
        assertThat(registrationResponse.getBody().getClientName()).isEqualTo(clientName);

        ClientResponse existingClient = registrationResponse.getBody();

        Set<String> newClientAuthMethods = new HashSet<>();
        newClientAuthMethods.add("new_method");
        Set<String> newGrantTypes = new HashSet<>();
        newGrantTypes.add("new_grant_type");
        Set<String> newScopes = new HashSet<>();
        newScopes.add("new_scope");
        Set<String> newRedirectUris = new HashSet<>();
        newRedirectUris.add("https://new.example.com");

        ClientUpdateRequest request = new ClientUpdateRequest();
        request.setClientAuthenticationMethods(newClientAuthMethods);
        request.setAuthorizationGrantTypes(newGrantTypes);
        request.setScopes(newScopes);
        request.setRedirectUris(newRedirectUris);

        // Act
        ResponseEntity<ClientResponse> response = restTemplate.postForEntity(
                "/api/client/update/" + existingClient.getClientId(),
                request, ClientResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getClientAuthenticationMethods()).isEqualTo(newClientAuthMethods);
        assertThat(response.getBody().getAuthorizationGrantTypes()).isEqualTo(newGrantTypes);
        assertThat(response.getBody().getScopes()).isEqualTo(newScopes);
        assertThat(response.getBody().getRedirectUris()).isEqualTo(newRedirectUris);
    }

}
