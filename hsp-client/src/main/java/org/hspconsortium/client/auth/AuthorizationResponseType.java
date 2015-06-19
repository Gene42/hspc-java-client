package org.hspconsortium.client.auth;

public enum AuthorizationResponseType {
    CODE("code");

    private String paramValue;

    private AuthorizationResponseType(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamValue() {
        return paramValue;
    }
}
