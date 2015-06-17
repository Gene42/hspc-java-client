package org.hspconsortium.client.auth.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.AccessTokenRequest;


import java.io.IOException;
import java.io.InputStreamReader;

public class DefaultAccessTokenProvider implements AccessTokenProvider {

    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String PATIENT_ID_KEY = "patientId";

    @Override
    public AccessToken getAccessToken(String tokenEndpointUrl, AccessTokenRequest request) {
        String clientId = request.getClientId();
        String clientSecret = request.getClientSecret();

        HttpResponse response = post(tokenEndpointUrl, clientId, clientSecret, request.serialize().getBytes());
        JsonParser parser = new JsonParser();
        JsonObject rootResponse = null;
        try {
            rootResponse = (JsonObject)parser.parse(new InputStreamReader(response.getEntity().getContent()));

            return new DefaultAccessToken(
                    getResponseElement(ACCESS_TOKEN_KEY, rootResponse),
                    getResponseElement(PATIENT_ID_KEY, rootResponse)
                    );
        } catch (IOException io_ex) {
            throw new RuntimeException("There was a problem attempting to get the access token", io_ex);
        }
    }

    protected HttpResponse post(String serviceUrl, String clientId, String clientSecret, byte[] payload){
        HttpPost postRequest = new HttpPost(serviceUrl);
        if(StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(clientSecret)){
            setAuthorizationHeader(postRequest, clientId, clientSecret);
        }
        else{
            throw new RuntimeException("Confidential client authorization requires clientId and client secret.");
        }
        return sendPayload(postRequest, payload);
    }

    protected static void setAuthorizationHeader(HttpRequest request, String clientId, String clientSecret){
        String authHeader = String.format("%s:%s", clientId, clientSecret);
        String encoded = new Base64().encode(authHeader.getBytes());
        request.addHeader("Authorization", String.format("Basic %s", new String(encoded)));
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");

    }

    protected HttpResponse sendPayload(HttpEntityEnclosingRequestBase request, byte[] payload) {
        HttpResponse response = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            request.setEntity(new ByteArrayEntity(payload));
            response = httpclient.execute(request);
        } catch(IOException ioe) {
            throw new RuntimeException("Error sending HTTP Post Payload", ioe);
        }
        return response;
    }

    private String getResponseElement(String elementKey, JsonObject rootResponse){
        JsonElement jsonElement = rootResponse.get(elementKey);
        if(jsonElement != null){
            return jsonElement.getAsString();
        }
        return null;
    }

}
