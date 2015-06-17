package org.hspconsortium.client.auth;

public enum AccessTokenGrantType {
    AUTHORIZATION_CODE("authorization_code"),
    CLIENT_CREDENTIALS("client_credentials");

    private String paramValue;

    private AccessTokenGrantType(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamValue() {
        return paramValue;
    }
}
