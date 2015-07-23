/*
 *
 *  * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 *
 */

package org.hspconsortium.client.session;

import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.AuthorizationRequest;
import org.hspconsortium.client.auth.codeflow.CodeFlowAccessTokenRequest;
import org.hspconsortium.client.auth.codeflow.CodeFlowAuthorizer;
import org.hspconsortium.client.auth.context.FhirSessionContextHolder;
import org.hspconsortium.client.auth.context.impl.SimpleFhirSessionContextHolder;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface SessionFactory {

    public FhirSession session();
    public void requestAccessToken(String oauthState, String authorizationCode);
    public void requestAccessToken(HttpServletRequest request);
    public void authorize(HttpServletRequest request, HttpServletResponse response);

    public static class Impl implements SessionFactory {

        private final String clientId;
        private final String redirectUri;
        private final String scope;
        private final String clientSecret;

        private FhirSessionContextHolder fhirSessionContextHolder = new SimpleFhirSessionContextHolder();
        private AccessTokenProvider accessTokenProvider = new DefaultAccessTokenProvider();
        private CodeFlowAuthorizer codeFlowAuthorizer;

        private Session session;

        public Impl(String clientId, String redirectUri, String clientSecret, String scope){
            this.clientId = clientId;
            this.redirectUri = redirectUri;
            this.clientSecret = clientSecret;
            this.scope = scope;
        }

        public void setCodeFlowAuthorizer(CodeFlowAuthorizer codeFlowAuthorizer) {
            this.codeFlowAuthorizer = codeFlowAuthorizer;
        }

        public void setFhirSessionContextHolder(FhirSessionContextHolder fhirSessionContextHolder) {
            this.fhirSessionContextHolder = fhirSessionContextHolder;
        }

        public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
            this.accessTokenProvider = accessTokenProvider;
        }

        @Override
        public FhirSession session() {
            return this.session;
        }

        @Override
        public void requestAccessToken(HttpServletRequest request) {
            Map<String, String[]> params = request.getParameterMap();
            String oauthState = params.get("state")[0];
            String authorizationCode = params.get("code")[0];

            requestAccessToken(oauthState, authorizationCode);
        }

        @Override
        public void authorize(HttpServletRequest request, HttpServletResponse response) {
            codeFlowAuthorizer.authorize(request, response, clientId, scope, redirectUri);
        }

        @Override
        public void requestAccessToken(String oauthState, String authorizationCode) {
            AuthorizationRequest authorizationRequest = fhirSessionContextHolder.getAuthorizationRequest(oauthState);

            AccessToken accessToken = fhirSessionContextHolder.getAccessToken(oauthState);
            if (accessToken == null) {
                CodeFlowAccessTokenRequest tokenRequest = new CodeFlowAccessTokenRequest(authorizationRequest.getClientId(),
                        clientSecret, authorizationCode, authorizationRequest.getRedirectUri());
                accessToken = accessTokenProvider.getAccessToken(
                        authorizationRequest.getAuthorizationEndpoints().getTokenEndpoint(),
                        tokenRequest);
                fhirSessionContextHolder.addAccessToken(oauthState, accessToken);
            }

            this.session = new Session(authorizationRequest.getAuthorizationEndpoints().getFhirServiceApi(), accessToken);
        }
    }
}
