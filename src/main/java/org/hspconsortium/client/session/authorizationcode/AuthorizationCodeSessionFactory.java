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
package org.hspconsortium.client.session.authorizationcode;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang.Validate;
import org.hspconsortium.client.auth.access.AccessToken;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.UserInfo;
import org.hspconsortium.client.auth.authorizationcode.AuthorizationCodeRequest;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.session.FhirSessionContext;
import org.hspconsortium.client.session.FhirSessionContextHolder;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.SessionKeyRegistry;

public class AuthorizationCodeSessionFactory<C extends Credentials> {
    private final FhirContext hapiFhirContext;

    private final String sessionKey;

    private final AccessTokenProvider accessTokenProvider;

    private final String clientId;

    private final C clientCredentials;

    private final String redirectUri;

    private FhirSessionContextHolder fhirSessionContextHolder;

    public AuthorizationCodeSessionFactory(FhirContext hapiFhirContext, SessionKeyRegistry sessionKeyRegistry,
                                           String sessionKey,
                                           FhirSessionContextHolder fhirSessionContextHolder,
                                           AccessTokenProvider accessTokenProvider,
                                           String clientId,
                                           C clientCredentials,
                                           String redirectUri) {
        this.hapiFhirContext = hapiFhirContext;
        // register the session key or fail if it isn't unique
        sessionKeyRegistry.registerSessionKey(sessionKey);
        this.sessionKey = sessionKey;
        this.fhirSessionContextHolder = fhirSessionContextHolder;
        this.accessTokenProvider = accessTokenProvider;
        this.clientId = clientId;
        this.clientCredentials = clientCredentials;
        this.redirectUri = redirectUri;
    }

    public void registerInContext(String oauthState, AuthorizationCodeRequest authorizationCodeRequest) {
        FhirSessionContext fhirSessionContext = fhirSessionContextHolder.get(oauthState);
        if (fhirSessionContext == null) {
            fhirSessionContext = new FhirSessionContext(oauthState);
            fhirSessionContextHolder.put(oauthState, fhirSessionContext);
        }
        fhirSessionContext.setAuthorizationCodeRequest(authorizationCodeRequest);
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public Session createSession(String oauthState, String authorizationCode) {
        // verify that this authorizationCodeRequest matches according to the oauthState
        // (meaning, the authorization request came from this application)
        FhirSessionContext fhirSessionContext = fhirSessionContextHolder.get(oauthState);
        AuthorizationCodeRequest authorizationCodeRequest = fhirSessionContext.getAuthorizationCodeRequest();
        Validate.notNull(authorizationCodeRequest, "AuthorizationCodeRequest not found for oauthState: " + oauthState);
        Validate.isTrue(clientId.equals(authorizationCodeRequest.getClientId()), "AuthorizationCodeRequest.clientId does not match");

        // gain an access token for use by the session
        String fhirServiceUrl = authorizationCodeRequest.getFhirEndpoints().getFhirServiceApi();
        String tokenEndpoint = authorizationCodeRequest.getFhirEndpoints().getTokenEndpoint();
        String userInfoEndpoint = authorizationCodeRequest.getFhirEndpoints().getUserInfoEndpoint();
        AuthorizationCodeAccessTokenRequest<C> authorizationCodeAccessTokenRequest =
                new AuthorizationCodeAccessTokenRequest<>(clientId, clientCredentials, authorizationCode, redirectUri);

        AccessToken accessToken = accessTokenProvider.getAccessToken(tokenEndpoint, authorizationCodeAccessTokenRequest);

        // obtain the userInfo
        UserInfo userInfo = null;
        if (accessToken.getIdTokenStr() != null) {
            userInfo = accessTokenProvider.getUserInfo(userInfoEndpoint, accessToken);
        }

        return new Session(hapiFhirContext, fhirServiceUrl, accessToken, userInfo);
    }
}
