/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.example;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.codeflow.CodeFlowAccessTokenRequest;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
import org.hspconsortium.client.auth.impl.DefaultClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class AuthRedirectEndpoint {

    private String clientId;
    private String tokenRedirectUri;

    @javax.annotation.Resource(name="clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @javax.annotation.Resource(name="authRedirectUri")
    public void setTokenRedirectUri(String tokenRedirectUri) {
        this.tokenRedirectUri = tokenRedirectUri;
    }

    @RequestMapping(value = "/auth-redirect", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String[]> paramMap = request.getParameterMap();
        paramMap.entrySet();

        CodeFlowAccessTokenRequest tokenRequest = new CodeFlowAccessTokenRequest(clientId, "secret", paramMap.get("code")[0], tokenRedirectUri );
        AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken("http://localhost:8080/hsp-auth/token", tokenRequest);
        accessToken.getTokenType();

        DefaultClient defaultClient = new DefaultClient("http://localhost:8080/hsp-api/data");
        Bundle results = defaultClient.search().forResource(Patient.class).where(Patient.FAMILY.matches().value("Wilson")).execute();
        results.getEntries();
    }

}
