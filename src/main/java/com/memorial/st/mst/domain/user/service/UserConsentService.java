package com.memorial.st.mst.domain.user.service;

import com.memorial.st.mst.controller.user.model.UserConsentRequest;
import com.memorial.st.mst.domain.user.MstUserConsent;
import com.memorial.st.mst.domain.user.service.repository.MstUserConsentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserConsentService {

    @Autowired
    private MstUserConsentRepository userConsentRepository;

    public Boolean createConsent(UserConsentRequest consentRequest) {
        for (String scope : consentRequest.getScopes()) {
            userConsentRepository.save(MstUserConsent.builder()
                    .userId(consentRequest.getUserId())
                    .clientEntityId(consentRequest.getClientEntityId())
                            .scope(scope)
                            .modDate(LocalDateTime.now())
                            .createdDate(LocalDateTime.now())
                    .build());
        }
        return true;
    }
}
