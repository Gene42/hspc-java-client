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

package org.hspconsortium.client.controller;

import org.apache.commons.lang3.Validate;

import java.io.Serializable;

public class FhirEndpoints implements Serializable {

    private final String fhirServiceApi;
    private final String authorizationEndpoint;
    private final String tokenEndpoint;

    public FhirEndpoints(String fhirServiceApi, String authorizationEndpoint, String tokenEndpoint) {
        Validate.notNull(fhirServiceApi, "the fhirServiceApi must not be null");
        Validate.notNull(authorizationEndpoint, "the authorizationEndpoint must not be null");
        Validate.notNull(tokenEndpoint, "the tokenEndpoint must not be null");

        this.fhirServiceApi = fhirServiceApi;
        this.authorizationEndpoint = authorizationEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getFhirServiceApi() {
        return fhirServiceApi;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }
}
