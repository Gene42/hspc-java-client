/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.impl;

import org.hspconsortium.client.AbstractFhirClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.credentialsflow.ClientCredentialsAccessTokenRequest;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
import org.hspconsortium.client.auth.impl.DefaultAuthorizationEndpointsProvider;

public class CredentialsFlowFhirClient extends AbstractFhirClient  {

    public CredentialsFlowFhirClient(String fhirApi, String clientId, String clientSecret, Scopes scopes) {
        this.client = hapiFhirContext.newRestfulGenericClient(fhirApi);

        AuthorizationEndpointsProvider authorizationEndpointsProvider = new DefaultAuthorizationEndpointsProvider(fhirApi);
        ClientCredentialsAccessTokenRequest tokenRequest = new ClientCredentialsAccessTokenRequest(clientId, clientSecret, scopes);
        AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken(authorizationEndpointsProvider.getAuthorizationEndpoints().getTokenEndpoint(), tokenRequest);

        this.client.registerInterceptor(new BearerTokenAuthorizationHeaderInterceptor(accessToken));
    }

}
