/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hspconsortium.client.auth.AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.Serializable;
import java.util.*;

public class DefaultAccessToken implements Serializable, AccessToken {

    private DefaultOAuth2AccessToken oAuth2AccessToken;
    private String patientId;

    public DefaultAccessToken(String tokenValue, String patientId) {
        oAuth2AccessToken = new DefaultOAuth2AccessToken(tokenValue);
        this.patientId = patientId;
    }

    public DefaultAccessToken(String tokenValue, String patientId, String tokenType,
                              String expires, String scope, String refreshToken) {
        this(tokenValue, patientId);
        oAuth2AccessToken.setTokenType(tokenType);
        oAuth2AccessToken.setExpiration(createExpirationDate(expires));
        oAuth2AccessToken.setScope(createScopeSet(scope));
        oAuth2AccessToken.setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
    }

    private Date createExpirationDate(String expiresIn) {
        return DateUtils.addSeconds(new Date(), Integer.parseInt(expiresIn));
    }

    private Set<String> createScopeSet(String scope) {
        String[] scopeArray = StringUtils.split(scope, " ");
        return new HashSet<String>(Arrays.asList(scopeArray));
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return oAuth2AccessToken.getAdditionalInformation();
    }

    @Override
    public Set<String> getScope() {
        return oAuth2AccessToken.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return oAuth2AccessToken.getRefreshToken();
    }

    @Override
    public String getTokenType() {
        return oAuth2AccessToken.getTokenType();
    }

    @Override
    public boolean isExpired() {
        return oAuth2AccessToken.isExpired();
    }

    @Override
    public Date getExpiration() {
        return oAuth2AccessToken.getExpiration();
    }

    @Override
    public int getExpiresIn() {
        return oAuth2AccessToken.getExpiresIn();
    }

    @Override
    public String getValue() {
        return oAuth2AccessToken.getValue();
    }

    @Override
    public String getPatientId() {
        return this.patientId;
    }
}
