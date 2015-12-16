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

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.*;

public class JsonAccessToken extends AbstractOAuth2AccessToken implements AccessToken {

    private JsonObject rootResponse;

    private final String intent;

    private final String smartStyleUrl;

    private final String encounterId;

    private final String patientId;

    private final String locationId;

    private final boolean needPatientBanner;

    private final String resource;

    private final String idToken;

    private Map<String, Object> claimsMap = new HashMap<>();

    public JsonAccessToken(JsonObject rootResponse, String accessToken, String tokenType, String expires, String scope,
                           String intent, String smartStyleUrl, String patientId, String encounterId, String locationId,
                           boolean needPatientBanner, String resource, String refreshToken, String idToken) {
        super(accessToken, tokenType, expires, scope, refreshToken, idToken);
        Validate.notNull(rootResponse, "The rootResponse must not be null");
        this.rootResponse = rootResponse;
        this.intent = intent;
        this.smartStyleUrl = smartStyleUrl;
        this.encounterId = encounterId;
        this.patientId = patientId;
        this.locationId = locationId;
        this.needPatientBanner = needPatientBanner;
        this.resource = resource;
        this.idToken = idToken;
    }

    @Override
    public JsonObject getRootResponse() {
        return rootResponse;
    }

    @Override
    public String getIntent() {
        return intent;
    }

    @Override
    public String getSmartStyleUrl() {
        return smartStyleUrl;
    }

    @Override
    public String getEncounterId() {
        return encounterId;
    }

    @Override
    public String getPatientId() {
        return patientId;
    }

    @Override
    public String getLocationId() {
        return locationId;
    }

    @Override
    public boolean needPatientBanner() {
        return needPatientBanner;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public List<NameValuePair> asNameValuePairList() {
        // create a list of all the non-null values to be transferred to the refresh token
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        if (StringUtils.isNoneEmpty(intent)) {
            nameValuePairs.add(new BasicNameValuePair(INTENT, intent));
        }

        if (StringUtils.isNoneEmpty(smartStyleUrl)) {
            nameValuePairs.add(new BasicNameValuePair(SMART_STYLE_URL, smartStyleUrl));
        }

        if (StringUtils.isNoneEmpty(encounterId)) {
            nameValuePairs.add(new BasicNameValuePair(ENCOUNTER, encounterId));
        }

        if (StringUtils.isNoneEmpty(patientId)) {
            nameValuePairs.add(new BasicNameValuePair(PATIENT, patientId));
        }

        if (StringUtils.isNoneEmpty(locationId)) {
            nameValuePairs.add(new BasicNameValuePair(LOCATION, locationId));
        }

        if (needPatientBanner) {
            nameValuePairs.add(new BasicNameValuePair(NEED_PATIENT_BANNER, "true"));
        }

        if (StringUtils.isNoneEmpty(resource)) {
            nameValuePairs.add(new BasicNameValuePair(RESOURCE, resource));
        }

        return nameValuePairs;
    }

    public void setClaimsMap(Map<String, Object> claimsMap) {
        this.claimsMap = claimsMap;
    }

    public Map<String, Object> getClaimsMap() {
        return Collections.unmodifiableMap(claimsMap);
    }
}
