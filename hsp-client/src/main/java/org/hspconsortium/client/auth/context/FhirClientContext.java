/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.context;

import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.StateProvider;
import org.hspconsortium.client.auth.impl.DefaultAuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.impl.DefaultStateProvider;

public final class FhirClientContext {

    private final AuthorizationEndpointsProvider authorizationEndpointsProvider;
    private final StateProvider stateProvider;

    private final String fhirApi;
    private final String launchId;
    private final String clientId;
    private final Scopes scopes;
    private final String redirectUri;
    private final String state;
    private AccessToken accessToken;

    public FhirClientContext(String fhirApi, String launchId,String clientId,
                             Scopes scopes, String redirectUri) {
        this.fhirApi     = fhirApi;
        this.launchId    = launchId;
        this.clientId    = clientId;
        this.scopes      = scopes;
        this.redirectUri = redirectUri;
        this.authorizationEndpointsProvider = new DefaultAuthorizationEndpointsProvider(fhirApi);
        this.stateProvider = new DefaultStateProvider();
        this.state       = stateProvider.getNewState();
    }

    public String getFhirApi() {
        return fhirApi;
    }

    public String getLaunchId() {
        return launchId;
    }

    public String getClientId() {
        return clientId;
    }

    public Scopes getScopes() {
        return scopes;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getState() {
        return state;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AuthorizationEndpointsProvider getAuthorizationEndpointsProvider() {
        return authorizationEndpointsProvider;
    }

    public StateProvider getStateProvider() {
        return stateProvider;
    }
}
