/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.platform.util;

import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hspconsortium.client.auth.AuthorizationEndpoints;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.DefaultAuthorizationEndpointsProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebAuthorizer {

    private JsonParser parser = new JsonParser();

    private HttpClient httpClient = new DefaultHttpClient();

    private AuthorizationEndpointsProvider authorizationEndpointsProvider = new DefaultAuthorizationEndpointsProvider();

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response, String launchID, String fhirServiceURL) {
        String authEndpoint = metadata(fhirServiceURL);
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", authEndpoint);
    }

    private String metadata(String fhirServiceURL) {
        AuthorizationEndpoints authEndpoints = authorizationEndpointsProvider.getAuthorizationEndpoints(fhirServiceURL);
        return authEndpoints.getAuthorizationEndpoint();
    }

}
