package org.hspconsortium.client.auth.codeflow;

import org.hspconsortium.client.auth.AbstractAccessTokenRequest;
import org.hspconsortium.client.auth.AccessTokenGrantType;

import java.util.HashMap;
import java.util.Map;

public class CodeFlowAccessTokenRequest extends AbstractAccessTokenRequest {

    private Map<String, String> tokenRequestParams = new HashMap<String, String>();

    public CodeFlowAccessTokenRequest(String clientId, String clientSecret, String code, String redirectUri) {
        super(clientId, clientSecret, AccessTokenGrantType.AUTHORIZATION_CODE);
        if (code != null) {
            this.tokenRequestParams.put("code", code);
        }
        this.tokenRequestParams.put("redirect_uri", redirectUri);
        this.tokenRequestParams.put("client_id", clientId);
    }

    @Override
    public Map<String, String> getAdditionalParameters() {
        return this.tokenRequestParams;
    }
}
