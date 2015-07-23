/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

import java.io.Serializable;

public class AuthorizationEndpoints implements Serializable {

    private final String fhirServiceApi;
    private final String authorizationEndpoint;
    private final String tokenEndpoint;

    public AuthorizationEndpoints(String fhirServiceApi, String authorizationEndpoint, String tokenEndpoint) {
        this.fhirServiceApi = fhirServiceApi;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getFhirServiceApi() {
        return fhirServiceApi;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
}
