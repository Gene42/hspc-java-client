/*
 * #%L
 * Health Services Platform Consortium - HSPC Client
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
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonAccessTokenTest {

    JsonObject jsonObject = new JsonObject();

    JsonAccessToken jsonAccessToken = null;

    @Before
    public void setUp() {
        jsonAccessToken = new JsonAccessToken(
                jsonObject,
                "accessToken",
                "tokenType",
                "10",
                "scope",
                "intent",
                "smartStyleUrl",
                "patientId",
                "encounterId",
                "locationId",
                true,
                "resource",
                "refreshToken",
                "idToken"
        );
    }

    @Test
    public void testGetRootResponse() throws Exception {
        assertEquals(jsonObject, jsonAccessToken.getRootResponse());
    }

    @Test
    public void testGetIntent() throws Exception {
        assertEquals("intent", jsonAccessToken.getIntent());
    }

    @Test
    public void testGetSmartStyleUrl() throws Exception {
        assertEquals("smartStyleUrl", jsonAccessToken.getSmartStyleUrl());
    }

    @Test
    public void testGetEncounterId() throws Exception {
        assertEquals("encounterId", jsonAccessToken.getEncounterId());
    }

    @Test
    public void testGetPatientId() throws Exception {
        assertEquals("patientId", jsonAccessToken.getPatientId());
    }

    @Test
    public void testGetLocationId() throws Exception {
        assertEquals("locationId", jsonAccessToken.getLocationId());
    }

    @Test
    public void testNeedPatientBanner() throws Exception {
        assertEquals(true, jsonAccessToken.needPatientBanner());
    }

    @Test
    public void testGetResource() throws Exception {
        assertEquals("resource", jsonAccessToken.getResource());
    }

    @Test
    public void testAsNameValuePairList() throws Exception {
        int i=0;
        assertEquals("intent", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("smartStyleUrl", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("encounterId", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("patientId", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("locationId", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("true", jsonAccessToken.asNameValuePairList().get(i++).getValue());
        assertEquals("resource", jsonAccessToken.asNameValuePairList().get(i).getValue());
    }
}