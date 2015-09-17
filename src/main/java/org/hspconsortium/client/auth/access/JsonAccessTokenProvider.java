/*
 * #%L
 * hsp-client
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.hspconsortium.client.auth.access;

import ca.uhn.fhir.context.FhirContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonAccessTokenProvider implements AccessTokenProvider<JsonAccessToken> {
    private final FhirContext fhirContext;

    public JsonAccessTokenProvider(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    public JsonAccessToken getAccessToken(String tokenEndpointUrl, AccessTokenRequest request) {
        String clientId = request.getClientId();
        Credentials<?> clientSecretCredentials = request.getCredentials();

        List<NameValuePair> paramPairs = new ArrayList<>();
        Map<String, String> parameters = request.getParameters();
        if (parameters != null) {
            for (String param : parameters.keySet()) {
                paramPairs.add(new BasicNameValuePair(param, parameters.get(param)));
            }
        }

        JsonObject rootResponse = post(tokenEndpointUrl, clientId, clientSecretCredentials, paramPairs);
        return buildAccessToken(rootResponse, null);
    }

    @Override
    public JsonAccessToken refreshAccessToken(String tokenEndpointUrl, AccessTokenRequest request, AccessToken accessToken) {
        String clientId = request.getClientId();
        Credentials<?> clientSecretCredentials = request.getCredentials();

        JsonObject rootResponse = post(tokenEndpointUrl, clientId, clientSecretCredentials, accessToken.asNameValuePairList());
        return buildAccessToken(rootResponse, new String[]{});
    }

    protected JsonAccessToken buildAccessToken(JsonObject rootResponse, String[] params) {
        return new JsonAccessToken(
                rootResponse,
                getResponseElement(AccessToken.ACCESS_TOKEN, rootResponse),
                getResponseElement(AccessToken.TOKEN_TYPE, rootResponse),
                getResponseElement(AccessToken.EXPIRES_IN, rootResponse),
                getResponseElement(AccessToken.SCOPE, rootResponse),
                getResponseElement(AccessToken.INTENT, rootResponse),
                getResponseElement(AccessToken.SMART_STYLE_URL, rootResponse),
                getResponseElement(AccessToken.PATIENT, rootResponse),
                getResponseElement(AccessToken.ENCOUNTER, rootResponse),
                getResponseElement(AccessToken.LOCATION, rootResponse),
                Boolean.parseBoolean(getResponseElement(AccessToken.NEED_PATIENT_BANNER, rootResponse)),
                getResponseElement(AccessToken.RESOURCE, rootResponse),
                getResponseElement(AccessToken.REFRESH_TOKEN, rootResponse)
        );
    }

    protected JsonObject post(String serviceUrl, String clientId, Credentials clientCredentials, List<NameValuePair> transferParams) {
        HttpPost postRequest = new HttpPost(serviceUrl);
        postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");

        if (clientCredentials instanceof ClientSecretCredentials) {
            Object credentialsObj = clientCredentials.getCredentials();
            if (credentialsObj instanceof String) {
                String credentialsStr = (String) credentialsObj;
                if (StringUtils.isNotBlank(clientId) && StringUtils.isNotBlank(credentialsStr)) {
                    setAuthorizationHeader(postRequest, clientId, credentialsStr);
                } else {
                    throw new RuntimeException("Confidential client authorization requires clientId and client secret.");
                }
            } else {
                throw new IllegalArgumentException("Credentials not supported");
            }
        } else if (clientCredentials instanceof JWTCredentials) {
            ((JWTCredentials) clientCredentials).setAudience(serviceUrl);
        } else {
            throw new IllegalArgumentException("Credentials type not supported");
        }

        return processRequest(postRequest, transferParams);
    }

    protected static void setAuthorizationHeader(HttpRequest request, String clientId, String clientSecret) {
        String authHeader = String.format("%s:%s", clientId, clientSecret);
        String encoded = Base64.encode(authHeader.getBytes());
        request.addHeader("Authorization", String.format("Basic %s", encoded));
    }

    protected JsonObject processRequest(HttpEntityEnclosingRequestBase request, List<NameValuePair> parameters) {
        JsonObject rootResponse = null;
        try {
            request.setEntity(new UrlEncodedFormEntity(parameters));
            HttpResponse response = this.fhirContext.getRestfulClientFactory().getHttpClient().execute(request);
            int status = response.getStatusLine().getStatusCode();

            if (status != 200) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                throw new RuntimeException(String.format("There was a problem attempting to get the access token.\nResponse Status : %s .\nResponse Detail :%s."
                        , response.getStatusLine()
                        , responseString));
            }

            try {
                JsonParser parser = new JsonParser();
                rootResponse = (JsonObject) parser.parse(new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException io_ex) {
                throw new RuntimeException("There was a problem attempting to get the access token", io_ex);
            }
            return rootResponse;
        } catch (IOException ioe) {
            throw new RuntimeException("Error sending HTTP Post Payload", ioe);
        }
    }

    protected String getResponseElement(String elementKey, JsonObject rootResponse) {
        JsonElement jsonElement = rootResponse.get(elementKey);
        if (jsonElement != null) {
            return jsonElement.getAsString();
        }
        return null;
    }

}
