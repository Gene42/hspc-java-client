package org.hspconsortium.client.auth.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hspconsortium.client.auth.AuthorizationEndpoints;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class DefaultAuthorizationEndpointsProvider implements AuthorizationEndpointsProvider {

    private static final String REST_CONFORMANCE_DEF = "rest";
    private static final String SECURITY_CONFORMANCE_DEF = "security";
    private static final String AUTH_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#authorize";
    private static final String TOKEN_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#token";
    private JsonParser parser = new JsonParser();
    private final String fhirServiceUrl;

    private AuthorizationEndpoints authorizationEndpoints;

    private HttpClient httpClient = new DefaultHttpClient();

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public DefaultAuthorizationEndpointsProvider(String fhirServiceUrl) {
        this.fhirServiceUrl = fhirServiceUrl;
    }

    @Override
    public AuthorizationEndpoints getAuthorizationEndpoints() {
        if (authorizationEndpoints != null) {
            return authorizationEndpoints;
        }

        HttpGet getRequest = new HttpGet(fhirServiceUrl+"/metadata?_format=json");
        String authEndpoint = null;
        String tokenEndpoint = null;

        HttpResponse response = null;
        try {
            response = httpClient.execute(getRequest);
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();

            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            String jsonString = writer.toString();

            JsonObject jsonObj = (JsonObject)parser.parse(jsonString);
            jsonObj = jsonObj.get(REST_CONFORMANCE_DEF).getAsJsonArray().get(0).getAsJsonObject();
            jsonObj = jsonObj.get(SECURITY_CONFORMANCE_DEF).getAsJsonObject();
            JsonArray jsonArray = jsonObj.get("extension").getAsJsonArray();
            for ( int i = 0; i < jsonArray.size(); i++) {
                jsonObj = jsonArray.get(i).getAsJsonObject();
                if (jsonObj.get("url").getAsString().equalsIgnoreCase(AUTH_ENDPOINT_EXTENSION)){
                    authEndpoint = jsonObj.get("valueString").getAsString();
                }
                else if(jsonObj.get("url").getAsString().equalsIgnoreCase(TOKEN_ENDPOINT_EXTENSION)){
                    tokenEndpoint = jsonObj.get("valueString").getAsString();
                }
            }
        } catch(IOException ioe) {
            throw new RuntimeException("Error sending Http Request", ioe);
        }
        setAuthorizationEndpoints(authEndpoint, tokenEndpoint);
        return authorizationEndpoints;
    }

    private void setAuthorizationEndpoints(String authEndpoint, String tokenEndpoint) {
        if (authorizationEndpoints == null) {
            synchronized (this) {
                if (authorizationEndpoints == null ) {
                    authorizationEndpoints = new AuthorizationEndpoints(authEndpoint, tokenEndpoint);
                }
            }
        }
    }

}
