package org.hspconsortium.client.auth.credentialsflow;

import org.hspconsortium.client.auth.AbstractAccessTokenRequest;
import org.hspconsortium.client.auth.AccessTokenGrantType;
import org.hspconsortium.client.auth.Scopes;

import java.util.HashMap;
import java.util.Map;

public class ClientCredentialsAccessTokenRequest extends AbstractAccessTokenRequest {

    private Map<String, String> tokenRequestParams = new HashMap<String, String>();

    public ClientCredentialsAccessTokenRequest(String clientId, String clientSecret, Scopes scopes) {
        super(clientId, clientSecret, AccessTokenGrantType.CLIENT_CREDENTIALS);
        if(scopes != null){
            this.tokenRequestParams.put("scope", scopes.asParamValue());
        }
    }

    @Override
    public Map<String, String> getAdditionalParameters() {
        return this.tokenRequestParams;
    }

}
