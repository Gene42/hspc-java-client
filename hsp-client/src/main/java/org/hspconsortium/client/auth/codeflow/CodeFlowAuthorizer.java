/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.codeflow;

import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hspconsortium.client.auth.AuthorizationEndpoints;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.impl.DefaultAuthorizationEndpointsProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CodeFlowAuthorizer {

    private JsonParser parser = new JsonParser();

    private HttpClient httpClient = new DefaultHttpClient();

    private AuthorizationEndpointsProvider authorizationEndpointsProvider = new DefaultAuthorizationEndpointsProvider();

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response, String clientId, String scope, String redirectUri, String launchId, String fhirServiceURL) {
        String authEndpoint = metadata(fhirServiceURL);
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", authEndpoint+
                "?client_id="+clientId +
                "&response_type=code" +
                "&scope="+scope + " launch:" + launchId +
                "&redirect_uri=" + redirectUri +
                "&state=d10fd891-59cf-d135-6658-165e861b038d"
        );
    }

    private String metadata(String fhirServiceURL) {
        AuthorizationEndpoints authEndpoints = authorizationEndpointsProvider.getAuthorizationEndpoints(fhirServiceURL);
        return authEndpoints.getAuthorizationEndpoint();
    }

}
