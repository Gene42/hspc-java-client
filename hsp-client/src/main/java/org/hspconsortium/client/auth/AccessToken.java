package org.hspconsortium.client.auth;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface AccessToken extends OAuth2AccessToken {

    String getPatientId();

}
