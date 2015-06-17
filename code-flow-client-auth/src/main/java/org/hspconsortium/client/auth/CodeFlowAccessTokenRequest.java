package org.hspconsortium.client.auth;

import java.util.Map;

public class CodeFlowAccessTokenRequest extends AbstractAccessTokenRequest {

    public CodeFlowAccessTokenRequest(String clientId, String clientSecret, AccessTokenGrantType grantType) {
        super(clientId, clientSecret, AccessTokenGrantType.AUTHORIZATION_CODE);
    }

    @Override
    public Map<String, String> getAdditionalParameters() {
        return null;
    }
}
