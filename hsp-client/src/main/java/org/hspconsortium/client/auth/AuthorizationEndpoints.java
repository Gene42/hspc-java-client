package org.hspconsortium.client.auth;

public class AuthorizationEndpoints {

    private String authorizationEndpoint;
    private String tokenEndpoint;

    public AuthorizationEndpoints(String authorizationEndpoint, String tokenEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
}
