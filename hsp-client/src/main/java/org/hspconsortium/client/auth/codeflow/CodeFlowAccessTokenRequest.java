package org.hspconsortium.client.auth.codeflow;

import org.hspconsortium.client.auth.AbstractAccessTokenRequest;
import org.hspconsortium.client.auth.AccessTokenGrantType;

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
