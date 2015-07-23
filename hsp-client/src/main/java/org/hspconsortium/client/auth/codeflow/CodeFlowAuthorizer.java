/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.codeflow;

import org.hspconsortium.client.auth.*;
import org.hspconsortium.client.auth.context.FhirSessionContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CodeFlowAuthorizer {

    static final String ISSUER_PARAMETER = "iss";
    static final String LAUNCH_ID_PARAMETER = "launch";

    private FhirSessionContextHolder fhirSessionContextHolder;
    private AuthorizationEndpointsProvider authorizationEndpointsProvider;
    private StateProvider stateProvider;

    public void setFhirSessionContextHolder(FhirSessionContextHolder fhirSessionContextHolder) {
        this.fhirSessionContextHolder = fhirSessionContextHolder;
    }

    public void setAuthorizationEndpointsProvider(AuthorizationEndpointsProvider authorizationEndpointsProvider) {
        this.authorizationEndpointsProvider = authorizationEndpointsProvider;
    }

    public void setStateProvider(StateProvider stateProvider) {
        this.stateProvider = stateProvider;
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

        AuthorizationRequest authRequest = createAuthorizationRequest(fhirServiceURL, launchId, clientId, scope, redirectUri);

        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", authRequest.getAuthorizationEndpoints().getAuthorizationEndpoint() +
                "?client_id=" + authRequest.getClientId() +
                "&response_type=" + authRequest.getResponseType() +
                "&scope=" + authRequest.getScopes().asParamValue() + " launch:" + authRequest.getLaunchId() +
                "&redirect_uri=" + authRequest.getRedirectUri() +
                "&state=" + authRequest.getOauthState()
        );
    }

    private void authorize(String fhirServiceURL, String launchId, String clientId, String scope, String redirectUri) {
        AuthorizationRequest authRequest = createAuthorizationRequest(fhirServiceURL, launchId, clientId, scope, redirectUri);

        //TODO redirect to a server for authorization

    }

    private CodeFlowAuthorizationRequest createAuthorizationRequest(String fhirServiceURL, String launchId, String clientId, String scope, String redirectUri) {
        Scopes scopes = new Scopes();
        scopes.add(new SimpleScope(scope));
        AuthorizationEndpoints authEndpoints = authorizationEndpointsProvider.getAuthorizationEndpoints(fhirServiceURL);

        CodeFlowAuthorizationRequest authRequest =
                new CodeFlowAuthorizationRequest(authEndpoints, clientId, "code", scopes, launchId,
                        redirectUri, stateProvider.getNewState());
        fhirSessionContextHolder.addAuthorizationRequest(authRequest);

        return authRequest;
    }
}
