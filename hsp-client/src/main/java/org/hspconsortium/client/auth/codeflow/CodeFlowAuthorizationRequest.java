/*
 *
 *  * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 *
 */

package org.hspconsortium.client.auth.codeflow;

import org.hspconsortium.client.auth.AuthorizationEndpoints;
import org.hspconsortium.client.auth.AuthorizationRequest;
import org.hspconsortium.client.auth.Scopes;

import java.io.Serializable;

public class CodeFlowAuthorizationRequest implements AuthorizationRequest, Serializable {

    private final AuthorizationEndpoints authorizationEndpoints;
    private final String clientId;
    private final String responseType;
    private final Scopes scopes;
    private final String launchId;
    private final String redirectUri;
    private final String oauthState;

    CodeFlowAuthorizationRequest(AuthorizationEndpoints authorizationEndpoints,
                                 String clientId,
                                 String responseType,
                                 Scopes scopes,
                                 String launchId,
                                 String redirectUri,
                                 String oauthState) {
        this.authorizationEndpoints = authorizationEndpoints;
        this.clientId = clientId;
        this.responseType = responseType;
        this.scopes = scopes;
        this.launchId = launchId;
        this.redirectUri = redirectUri;
        this.oauthState = oauthState;
    }

    @Override
    public AuthorizationEndpoints getAuthorizationEndpoints() {
        return authorizationEndpoints;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getResponseType() {
        return responseType;
    }

    @Override
    public Scopes getScopes() {
        return scopes;
    }

    @Override
    public String getLaunchId() {
        return launchId;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public String getOauthState() {
        return oauthState;
    }
}
