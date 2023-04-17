package com.memorial.st.mst.controller.oauth;

import com.memorial.st.mst.controller.oauth.model.AccessTokenResponse;
import com.memorial.st.mst.domain.client.service.OAuth2TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private OAuth2TokenService oAuth2TokenService;


    @PostMapping("/token")
    public AccessTokenResponse token(@RequestParam("grant_type") String grantType,
                                     @RequestParam("scope") String scope,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        String[] clientCredentials  = oAuth2TokenService.decodeClientCredentials(authorization);
        String clientId = clientCredentials[0];
        String clientSecret = clientCredentials[1];

        return oAuth2TokenService.issueAccessToken(grantType, clientId, clientSecret, scope);
    }

}
