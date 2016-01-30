/*
 * #%L
 * hspc-client
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
package org.hspconsortium.client.session;

import org.hspconsortium.client.auth.authorizationcode.AuthorizationCodeRequest;

import java.io.Serializable;

public class FhirSessionContext implements Serializable {

    private String oauthState;

    private AuthorizationCodeRequest authorizationCodeRequest;

    public FhirSessionContext(String oauthState) {
        this.oauthState = oauthState;
    }

    public String getOauthState() {
        return oauthState;
    }

    public AuthorizationCodeRequest getAuthorizationCodeRequest() {
        return authorizationCodeRequest;
    }

    public void setAuthorizationCodeRequest(AuthorizationCodeRequest authorizationCodeRequest) {
        this.authorizationCodeRequest = authorizationCodeRequest;
    }

}
