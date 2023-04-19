package com.memorial.st.mst.controller.client;

import com.memorial.st.mst.controller.client.model.ClientRegistrationRequest;
import com.memorial.st.mst.domain.client.ClientEntity;
import com.memorial.st.mst.domain.client.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClientControllerTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testRegisterClientService() {
        String clientName = "testClient";
        ClientEntity clientEntity = clientService.registerClient(clientName);

        assertThat(clientEntity.getClientName()).isEqualTo(clientName);
        assertThat(clientEntity.getId()).isNotNull();
        assertThat(clientEntity.getClientId()).isNotNull();
        assertThat(clientEntity.getClientSecret()).isNotNull();
    }

    @Test
    public void testRegisterClientController() {
        String clientName = "testClient";
        ClientRegistrationRequest request = new ClientRegistrationRequest();
        request.setClientName(clientName);

        ResponseEntity<Void> response = testRestTemplate.postForEntity("/api/client/register", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
