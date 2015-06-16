/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.platform.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WebAuthorizer {
    JsonParser parser = new JsonParser();

    private HttpClient httpClient = new DefaultHttpClient();

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void authorize(HttpServletRequest request, HttpServletResponse response, String launchID, String fhirServiceURL) {
        String authEndpoint = metadata(fhirServiceURL);
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", authEndpoint);
    }

    private String metadata(String fhirServiceURL) {
        HttpGet getRequest = new HttpGet(fhirServiceURL+"/metadata?_format=json");

        HttpResponse response = null;
        try {
            response = httpClient.execute(getRequest);
            HttpEntity httpEntity = response.getEntity();
            InputStream is = httpEntity.getContent();

            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer);
            String jsonString = writer.toString();


            JsonObject jsonObj = (JsonObject)parser.parse(jsonString);
            jsonObj = jsonObj.get("rest").getAsJsonArray().get(0).getAsJsonObject();
            jsonObj = jsonObj.get("security").getAsJsonObject();
            JsonArray jsonArray = jsonObj.get("extension").getAsJsonArray();
            for ( int i = 0; i < jsonArray.size(); i++) {
                jsonObj = jsonArray.get(i).getAsJsonObject();
                if (jsonObj.get("url").getAsString().equalsIgnoreCase("http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#authorize")){
                    return jsonObj.get("valueString").getAsString();
                }
            }
        } catch(IOException ioe) {
            throw new RuntimeException("Error sending Http Request", ioe);
        }
        return "";
    }

}
