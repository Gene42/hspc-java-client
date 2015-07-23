/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.context.impl;

import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AuthorizationRequest;
import org.hspconsortium.client.auth.context.FhirSessionContextHolder;

import java.util.HashMap;
import java.util.Map;

public class SimpleFhirSessionContextHolder implements FhirSessionContextHolder {

    /* NOTE: This is a simple FhirSessionContextHolder which holds context in memory in the current JVM and is not
     * a robust, production level FhirSessionContextHolder.
     */

    private static final Map<String, AuthorizationRequest> globalAuthorizationRequestMap = new HashMap<String, AuthorizationRequest>();
    private static final Map<String, AccessToken> globalAccessTokenMap = new HashMap<String, AccessToken>();

    @Override
    public AuthorizationRequest getAuthorizationRequest(String oauthState){
        return globalAuthorizationRequestMap.get(oauthState);
    }

    @Override
    public void addAuthorizationRequest(AuthorizationRequest authorizationRequest){
        if(authorizationRequest != null){
            globalAuthorizationRequestMap.put(authorizationRequest.getOauthState(), authorizationRequest);
        }
    }

    @Override
    public void removeAuthorizationRequest(AuthorizationRequest authorizationRequest){
        globalAuthorizationRequestMap.remove(authorizationRequest.getOauthState());
    }

    @Override
    public AccessToken getAccessToken(String oauthState){
        return globalAccessTokenMap.get(oauthState);
    }

    @Override
    public void addAccessToken(String oauthState, AccessToken accessToken){
        if(accessToken != null){
            globalAccessTokenMap.put(oauthState, accessToken);
        }
    }

    @Override
    public void removeAccessToken(String oauthState){
        globalAccessTokenMap.remove(oauthState);
    }
}
