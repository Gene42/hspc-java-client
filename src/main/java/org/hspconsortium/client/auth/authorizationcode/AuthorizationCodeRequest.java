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

package org.hspconsortium.client.auth.authorizationcode;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.client.controller.FhirEndpoints;
import org.hspconsortium.client.auth.Scopes;

import java.io.Serializable;

public class AuthorizationCodeRequest implements org.hspconsortium.client.auth.AuthorizationRequest, Serializable {

    private final FhirEndpoints fhirEndpoints;
    private final String clientId;
    private final String responseType;
    private final Scopes scopes;
    private final String launchId;
    private final String redirectUri;
    private final String oauthState;

    AuthorizationCodeRequest(FhirEndpoints fhirEndpoints,
                             String clientId,
                             String responseType,
                             Scopes scopes,
                             String launchId,
                             String redirectUri,
                             String oauthState) {
        Validate.notNull(fhirEndpoints, "the authorizationEndpoints must not be null");
        Validate.notNull(clientId, "the clientId must not be null");
        Validate.notNull(responseType, "the responseType must not be null");
        Validate.notNull(scopes, "the scopes must not be null");
        Validate.notNull(launchId, "the launchId must not be null");
        Validate.notNull(redirectUri, "the redirectUri must not be null");
        Validate.notNull(oauthState, "the oauthState must not be null");

        this.fhirEndpoints = fhirEndpoints;
        this.clientId = clientId;
        this.responseType = responseType;
        this.scopes = scopes;
        this.launchId = launchId;
        this.redirectUri = redirectUri;
        this.oauthState = oauthState;
    }

    @Override
    public FhirEndpoints getFhirEndpoints() {
        return fhirEndpoints;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getResponseType() {
        return responseType;
    }

    @Override
    public Scopes getScopes() {
        return scopes;
    }

    @Override
    public String getLaunchId() {
        return launchId;
    }

    @Override
    public String getRedirectUri() {
        return redirectUri;
    }

    @Override
    public String getOauthState() {
        return oauthState;
    }
}
