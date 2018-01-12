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

package org.hspconsortium.client.auth.authorizationcode;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.StateProvider;
import org.hspconsortium.client.controller.FhirEndpoints;
import org.hspconsortium.client.controller.FhirEndpointsProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class AuthorizationCodeRequestBuilder {

    private static final String ISSUER_PARAMETER = "iss";
    private static final String LAUNCH_ID_PARAMETER = "launch";

    private FhirEndpointsProvider fhirEndpointsProvider;
    private StateProvider stateProvider;

    public AuthorizationCodeRequestBuilder(FhirEndpointsProvider fhirEndpointsProvider, StateProvider stateProvider) {
        this.fhirEndpointsProvider = fhirEndpointsProvider;
        this.stateProvider = stateProvider;
    }

    public AuthorizationCodeRequest buildAuthorizationCodeRequest(HttpServletRequest request, String clientId,
                                                                  String scope, String redirectUri) {
        Map paramMap = request.getParameterMap();
        String launchId = null;
        if (paramMap.containsKey(LAUNCH_ID_PARAMETER)) {
             launchId = ((String[]) paramMap.get(LAUNCH_ID_PARAMETER))[0];
        }
        String fhirServiceURL = ((String[]) paramMap.get(ISSUER_PARAMETER))[0];

        return buildAuthorizationCodeRequest(fhirServiceURL, launchId, clientId, scope, redirectUri);
    }

    public AuthorizationCodeRequest buildAuthorizationCodeRequest(String fhirServiceURL, String launchId, String clientId,
                                                                  String scope, String redirectUri) {
        Validate.notNull(fhirServiceURL, "the fhirServiceURL must not be null");
        Validate.notNull(clientId, "the clientId must not be null");
        Validate.notNull(scope, "the scope must not be null");
        Validate.notNull(redirectUri, "the redirectUri must not be null");

        Scopes scopes = new Scopes();
        scopes.add(new SimpleScope(scope));
        FhirEndpoints authEndpoints = fhirEndpointsProvider.getEndpoints(fhirServiceURL);

        // create authorization request
        return new AuthorizationCodeRequest(authEndpoints, clientId, "code", scopes, launchId,
                redirectUri, stateProvider.getNewState());
    }

    public AuthorizationCodeRequest buildStandAloneAuthorizationCodeRequest(String fhirServiceURL, String clientId,
                                                                  String scope, String redirectUri) {
        Validate.notNull(fhirServiceURL, "the fhirServiceURL must not be null");
        Validate.notNull(clientId, "the clientId must not be null");
        Validate.notNull(scope, "the scope must not be null");
        Validate.notNull(redirectUri, "the redirectUri must not be null");

        Scopes scopes = new Scopes();
        scopes.add(new SimpleScope(scope));
        FhirEndpoints authEndpoints = fhirEndpointsProvider.getEndpoints(fhirServiceURL);

        // create authorization request
        return new AuthorizationCodeRequest(authEndpoints, clientId, "code", scopes,
                redirectUri, stateProvider.getNewState());
    }

    public String endSession(String fhirServiceURL, String redirectUri, HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        FhirEndpoints authEndpoints = fhirEndpointsProvider.getEndpoints(fhirServiceURL);
        String logoutEndpoint = authEndpoints.getAuthorizationEndpoint().replace("authorize", "logout");
        return "redirect:" + logoutEndpoint + "?hspcRedirectUrl=" + redirectUri;
    }
}
