/*
 * #%L
 * hspc-client
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.hspconsortium.client.auth.access;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.io.Serializable;
import java.util.*;

abstract public class AbstractOAuth2AccessToken implements Serializable, AccessToken {
    protected final OAuth2AccessToken oAuth2AccessToken;

    public AbstractOAuth2AccessToken(String accessToken, String tokenType,
                                     String expires, String scope, String refreshToken, final String idToken) {
        Validate.notNull(accessToken, "AccessToken must not be null");
        Validate.notNull(tokenType, "TokenType must not be null");
        Validate.notNull(scope, "Scope must not be null");

        this.oAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setTokenType(tokenType);
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setExpiration(createExpirationDate(expires));
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setScope(createScopeSet(scope));
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setRefreshToken(new DefaultOAuth2RefreshToken(refreshToken));
        ((DefaultOAuth2AccessToken)oAuth2AccessToken).setAdditionalInformation(
                new HashMap<String, Object>(){{put(AccessToken.ID_TOKEN, idToken);}}
        );
    }

    private Date createExpirationDate(String expiresIn) {
        return (expiresIn == null ? null : DateUtils.addSeconds(new Date(), Integer.parseInt(expiresIn)));
    }

    private Set<String> createScopeSet(String scope) {
        String[] scopeArray = StringUtils.split(scope, " ");
        return new HashSet<>(Arrays.asList(scopeArray));
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

    public String getIdTokenStr() {
        return (String)(oAuth2AccessToken).getAdditionalInformation().get(AccessToken.ID_TOKEN);
    }

    public IdToken getIdToken() {
        return new IdToken(getIdTokenStr());
    }

}
