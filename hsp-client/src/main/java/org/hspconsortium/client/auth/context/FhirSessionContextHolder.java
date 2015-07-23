package org.hspconsortium.client.auth.context;

import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AuthorizationRequest;

/**
 */
public interface FhirSessionContextHolder {

    AuthorizationRequest getAuthorizationRequest(String oauthState);
    void addAuthorizationRequest(AuthorizationRequest authorizationRequest);
    void removeAuthorizationRequest(AuthorizationRequest authorizationRequest);

    AccessToken getAccessToken(String oauthState);
    void addAccessToken(String oauthState, AccessToken accessToken);
    void removeAccessToken(String oauthState);
}
