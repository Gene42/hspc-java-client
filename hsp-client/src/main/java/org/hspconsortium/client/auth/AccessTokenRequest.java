package org.hspconsortium.client.auth;

public interface AccessTokenRequest {

    public String getClientId();
    public String getClientSecret();
    public String serialize();

}
