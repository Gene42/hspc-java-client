/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.impl;

import org.hspconsortium.client.AbstractFhirClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.codeflow.CodeFlowAccessTokenRequest;
import org.hspconsortium.client.auth.context.FhirClientContext;
import org.hspconsortium.client.auth.context.FhirClientContextHolder;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class CodeFlowFhirClient extends AbstractFhirClient {

    public CodeFlowFhirClient(HttpServletRequest request, String clientSecret) {
        Map<String,String[]> paramMap = request.getParameterMap();
        setFhirClientContext( FhirClientContextHolder.getFhirClientContext(paramMap.get("state")[0]) );
        this.client = hapiFhirContext.newRestfulGenericClient(fhirClientContext.getFhirApi());

        if (fhirClientContext.getAccessToken() == null) {
            CodeFlowAccessTokenRequest tokenRequest = new CodeFlowAccessTokenRequest(fhirClientContext.getClientId(),
                    clientSecret, paramMap.get("code")[0], fhirClientContext.getRedirectUri());
            AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
            AccessToken accessToken = tokenProvider.getAccessToken(fhirClientContext.getAuthorizationEndpointsProvider().getAuthorizationEndpoints().getTokenEndpoint(), tokenRequest);
            fhirClientContext.setAccessToken(accessToken);
        }
        this.client.registerInterceptor(new BearerTokenAuthorizationHeaderInterceptor(fhirClientContext.getAccessToken()));
    }

}
