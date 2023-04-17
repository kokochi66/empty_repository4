package com.memorial.st.mst.controller.oauth;

import com.memorial.st.mst.domain.client.service.OAuth2TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private OAuth2TokenService oAuth2TokenService;


    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestParam("grant_type") String grantType,
                                   @RequestParam("scope") String scope,
                                   @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        String[] clientCredentials  = oAuth2TokenService.decodeClientCredentials(authorization);
        String clientId = clientCredentials[0];
        String clientSecret = clientCredentials[1];

        return new ResponseEntity<>(oAuth2TokenService.issueAccessToken(grantType, clientId, clientSecret, scope));
    }

}
