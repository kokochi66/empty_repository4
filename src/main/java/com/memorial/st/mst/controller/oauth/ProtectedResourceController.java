package com.memorial.st.mst.controller.oauth;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProtectedResourceController {

    @GetMapping("/protected-resource")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public String getProtectedResource() {
        return "This is a protected resource!";
    }
}
