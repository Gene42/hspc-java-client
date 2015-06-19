package org.hspconsortium.client.auth;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractAccessTokenRequest implements AccessTokenRequest {

    private static final String REQUEST_PARAM_FORMAT = "%s=%s";

    private Map<String, String> tokenRequestParams = new HashMap<String, String>();
    private String clientId;
    private String clientSecret;

    protected AbstractAccessTokenRequest(String clientId, String clientSecret, AccessTokenGrantType grantType) {
        this.tokenRequestParams.put("grant_type", grantType.getParamValue());
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String serialize(){
        Map<String, String> mergedParams = new HashMap<String, String>(this.tokenRequestParams);
        Map<String, String> additionalParams = getAdditionalParameters();
        if(additionalParams != null){
            mergedParams.putAll(getAdditionalParameters());
        }
        List<String> params = new ArrayList<String>();

        for(String param : mergedParams.keySet()){
            params.add(String.format(REQUEST_PARAM_FORMAT, param, mergedParams.get(param)));
        }

        String body = StringUtils.join(params, "&");

        try {
            body = URLEncoder.encode(body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return body;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public abstract Map<String, String> getAdditionalParameters();


}
