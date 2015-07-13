/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAccessTokenRequest implements AccessTokenRequest {

    private Map<String, String> tokenRequestParams = new HashMap<String, String>();
    private String clientId;
    private String clientSecret;

    protected AbstractAccessTokenRequest(String clientId, String clientSecret, AccessTokenGrantType grantType) {
        this.tokenRequestParams.put("grant_type", grantType.getParamValue());
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.getMergedParameters();
    }

    private Map<String, String> getMergedParameters(){
        Map<String, String> mergedParams = new HashMap<String, String>(this.tokenRequestParams);
        Map<String, String> additionalParams = getAdditionalParameters();
        if(additionalParams != null){
            mergedParams.putAll(getAdditionalParameters());
        }
        return mergedParams;
    }

    public abstract Map<String, String> getAdditionalParameters();


}
