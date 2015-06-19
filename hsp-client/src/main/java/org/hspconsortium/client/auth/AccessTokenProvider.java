package org.hspconsortium.client.auth;

public interface AccessTokenProvider {

    AccessToken getAccessToken(String tokenEndpointUrl, AccessTokenRequest request);

}
