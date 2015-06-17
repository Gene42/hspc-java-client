package org.hspconsortium.client.auth;

public interface AuthorizationEndpointsProvider {
    AuthorizationEndpoints getAuthorizationEndpoints(String serviceUrl);
}
