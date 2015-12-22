/*
 * #%L
 * hsp-client
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
package org.hspconsortium.client.session.clientcredentials;

import ca.uhn.fhir.context.FhirContext;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.access.AccessToken;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.UserInfo;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.hspconsortium.client.session.Session;

public class ClientCredentialsSessionFactory<C extends Credentials> {
    private final FhirContext hapiFhirContext;

    private final AccessTokenProvider accessTokenProvider;

    private final FhirEndpointsProvider fhirEndpointsProvider;

    private final String fhirServiceUrl;

    private final String clientId;

    private final C clientCredentials;

    private final Scopes scopes;

    public ClientCredentialsSessionFactory(FhirContext hapiFhirContext, AccessTokenProvider accessTokenProvider,
                                           FhirEndpointsProvider fhirEndpointsProvider,
                                           String fhirServiceUrl,
                                           String clientId,
                                           C clientCredentials,
                                           Scopes scopes) {
        this.hapiFhirContext = hapiFhirContext;
        this.accessTokenProvider = accessTokenProvider;
        this.fhirEndpointsProvider = fhirEndpointsProvider;
        this.fhirServiceUrl = fhirServiceUrl;
        this.clientId = clientId;
        this.clientCredentials = clientCredentials;
        this.scopes = scopes;
    }

    public Session createSession() {
        final String tokenEndpoint = fhirEndpointsProvider.getEndpoints(fhirServiceUrl).getTokenEndpoint();
        final String userInfoEndpoint = fhirEndpointsProvider.getEndpoints(fhirServiceUrl).getUserInfoEndpoint();
        if (clientCredentials instanceof JWTCredentials) {
            ((JWTCredentials) clientCredentials).setAudience(tokenEndpoint);
        }
        ClientCredentialsAccessTokenRequest<C> clientCredentialsAccessTokenRequest =
                new ClientCredentialsAccessTokenRequest<>(clientId, clientCredentials, scopes);

        AccessToken accessToken = accessTokenProvider.getAccessToken(
                tokenEndpoint,
                clientCredentialsAccessTokenRequest);

        // obtain the userInfo
        UserInfo userInfo = null;
        if (accessToken.getIdTokenStr() != null) {
            userInfo = accessTokenProvider.getUserInfo(userInfoEndpoint, accessToken);
        }

        return new Session(hapiFhirContext, fhirServiceUrl, accessToken, userInfo);
    }
}
