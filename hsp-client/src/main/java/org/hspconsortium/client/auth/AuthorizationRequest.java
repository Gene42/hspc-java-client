/*
 *
 *  * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 *
 */

package org.hspconsortium.client.auth;

/**
 */
public interface AuthorizationRequest {

    public AuthorizationEndpoints getAuthorizationEndpoints();
    public String getClientId();
    public String getResponseType();
    public Scopes getScopes();
    public String getLaunchId();
    public String getRedirectUri();
    public String getOauthState();

}
