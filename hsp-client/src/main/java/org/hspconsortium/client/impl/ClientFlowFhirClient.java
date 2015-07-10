package org.hspconsortium.client.impl;

import org.hspconsortium.client.AbstractFhirClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.codeflow.CodeFlowAccessTokenRequest;
import org.hspconsortium.client.auth.context.FhirClientContext;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ClientFlowFhirClient extends AbstractFhirClient {

    public ClientFlowFhirClient(HttpServletRequest request, String clientSecret) {
        Map<String,String[]> paramMap = request.getParameterMap();
        FhirClientContext context = (FhirClientContext) request.getSession().getAttribute(FhirClientContext.FHIR_CLIENT_CONTEXT);
        this.client = hapiFhirContext.newRestfulGenericClient(context.getFhirApi());

        CodeFlowAccessTokenRequest tokenRequest = new CodeFlowAccessTokenRequest(context.getClientId(), clientSecret, paramMap.get("code")[0], context.getRedirectUri());
        AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken(context.getAuthorizationEndpointsProvider().getAuthorizationEndpoints().getTokenEndpoint(), tokenRequest);
        context.setAccessToken(accessToken);

        this.client.registerInterceptor(new BearerTokenAuthorizationHeaderInterceptor(accessToken));
    }

}
