package com.memorial.st.mst.controller.client;

import com.memorial.st.mst.controller.client.model.ClientRegistrationRequest;
import com.memorial.st.mst.controller.client.model.ClientResponse;
import com.memorial.st.mst.controller.client.model.ClientUpdateRequest;
import com.memorial.st.mst.domain.client.ClientEntity;
import com.memorial.st.mst.domain.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRegistrationRequest request) {
        ClientEntity clientEntity = clientService.registerClient(request.getClientName());
        return ResponseEntity.ok(new ClientResponse(clientEntity));
    }

    @PostMapping("/update/{clientId}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable String clientId,
                                                     @RequestBody ClientUpdateRequest request) {
        ClientEntity updatedClient = clientService.updateClient(clientId, request);
        return ResponseEntity.ok(new ClientResponse(updatedClient));
    }
}
