package org.hspconsortium.client.auth.impl;

import org.hspconsortium.client.auth.AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class DefaultAccessToken implements AccessToken {

    private String tokenValue;
    private String patientId;

    public DefaultAccessToken(String tokenValue, String patientId) {
        this.tokenValue = tokenValue;
        this.patientId = patientId;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }

    @Override
    public Set<String> getScope() {
        return null;
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return null;
    }

    @Override
    public String getTokenType() {
        return null;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public Date getExpiration() {
        return null;
    }

    @Override
    public int getExpiresIn() {
        return 0;
    }

    @Override
    public String getValue() {
        return this.tokenValue;
    }

    @Override
    public String getPatientId() {
        return this.patientId;
    }
}
