/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.codeflow;

import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.context.FhirClientContext;
import org.hspconsortium.client.auth.context.FhirClientContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CodeFlowAuthorizer {

    static final String ISSUER_PARAMETER = "iss";
    static final String LAUNCH_ID_PARAMETER = "launch";

    private FhirClientContext fhirContext;

    public void authorize(HttpServletRequest request, HttpServletResponse response, String clientId, String scope) {
        authorize(request, response, clientId, scope, null);
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response, String clientId, String scope, String redirectUri) {
        Map paramMap = request.getParameterMap();
        String launchId = ((String[]) paramMap.get(LAUNCH_ID_PARAMETER))[0];
        String fhirServiceURL = ((String[]) paramMap.get(ISSUER_PARAMETER))[0];

        if (redirectUri == null) {
            String url = request.getRequestURL().toString();
            String dispatcher = request.getPathInfo();
            int index = url.indexOf(dispatcher);
            if (index > -1 ) {
                redirectUri = url.substring(0,index);
            }
        }

        Scopes scopes = new Scopes();
        scopes.add(new SimpleScope(scope));
        fhirContext = new FhirClientContext(fhirServiceURL, launchId, clientId, scopes, redirectUri);
        FhirClientContextHolder.addFhirClientContext(fhirContext);

        String authEndpoint = fhirContext.getAuthorizationEndpointsProvider()
                .getAuthorizationEndpoints()
                .getAuthorizationEndpoint();

        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", authEndpoint +
                "?client_id=" + clientId +
                "&response_type=code" +
                "&scope=" + scopes.asParamValue() + " launch:" + launchId +
                "&redirect_uri=" + redirectUri +
                "&state=" + fhirContext.getState()
        );
    }
}
