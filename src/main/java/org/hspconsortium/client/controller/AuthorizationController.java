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

package org.hspconsortium.client.controller;

import org.apache.commons.lang.Validate;
import org.hspconsortium.client.auth.authorizationcode.AuthorizationCodeRequestBuilder;
import org.hspconsortium.client.auth.authorizationcode.AuthorizationCodeRequest;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.authorizationcode.AuthorizationCodeSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class AuthorizationController<C extends Credentials> {

    private final AuthorizationCodeSessionFactory<C> authorizationCodeSessionFactory;

    private final String appEntryPoint;

    private final String clientId;

    private final String redirectUri;

    private final String scope;

    private AuthorizationCodeRequestBuilder authorizationCodeRequestBuilder;

    @Inject
    public AuthorizationController(AuthorizationCodeSessionFactory<C> authorizationCodeSessionFactory,
                                   String appEntryPoint, String clientId,
                                   String redirectUri, String scope,
                                   AuthorizationCodeRequestBuilder authorizationCodeRequestBuilder) {
        Validate.notNull(authorizationCodeSessionFactory, "AuthorizationCodeSessionFactory is required");
        Validate.notNull(appEntryPoint, "AppEntryPoint is required");
        Validate.notNull(clientId, "ClientId is required");
        Validate.notNull(redirectUri, "RedirectUri is required");
        Validate.notNull(scope, "Scope is required");
        Validate.notNull(authorizationCodeRequestBuilder, "AuthorizationCodeAuthorizer is required");

        this.authorizationCodeSessionFactory = authorizationCodeSessionFactory;
        this.appEntryPoint = appEntryPoint;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.authorizationCodeRequestBuilder = authorizationCodeRequestBuilder;
    }

    /**
     * A request from the EHR is issued to launch the application
     *
     * @param request  Request application launch
     * @param response Response is a redirect back to the EHR for authorization
     */
    @RequestMapping(value = "/launch", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        AuthorizationCodeRequest authorizationCodeRequest = authorizationCodeRequestBuilder
                .buildAuthorizationCodeRequest(request, clientId, scope, redirectUri);

        // remember the fhirSessionContext based on the state (for request-callback association)
        authorizationCodeSessionFactory.registerInContext(authorizationCodeRequest.getOauthState(), authorizationCodeRequest);

        // build the response redirect
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(authorizationCodeRequest.getFhirEndpoints().getAuthorizationEndpoint());
        stringBuilder.append("?client_id=");
        stringBuilder.append(authorizationCodeRequest.getClientId());
        stringBuilder.append("&response_type=");
        stringBuilder.append(authorizationCodeRequest.getResponseType());
        stringBuilder.append("&scope=");
        stringBuilder.append(authorizationCodeRequest.getScopes().asParamValue());
        if (authorizationCodeRequest.getLaunchId() != null) {
            stringBuilder.append("&launch=");
            stringBuilder.append(authorizationCodeRequest.getLaunchId());
        }
        stringBuilder.append("&redirect_uri=");
        stringBuilder.append(authorizationCodeRequest.getRedirectUri());
        stringBuilder.append("&aud=");
        stringBuilder.append(authorizationCodeRequest.getFhirEndpoints().getFhirServiceApi());
        stringBuilder.append("&state=");
        stringBuilder.append(authorizationCodeRequest.getOauthState());

        response.setHeader("Location", stringBuilder.toString());
    }

    /**
     * A response from the EHR after authorization request is sent.  If successful, proceed to request
     * an access token.
     *
     * @param request  Response from the EHR after authorization is requested
     * @param response A redirect requesting an access token
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void handleRedirectResponse(HttpServletRequest request, HttpServletResponse response) {
        // capture the state that was initially sent in the authorization request
        Map<String, String[]> params = request.getParameterMap();
        String oauthState = params.get("state")[0];
        String authorizationCode = params.get("code")[0];

        // create a session using the authorizationCode
        Session session = authorizationCodeSessionFactory.createSession(oauthState, authorizationCode);
        // stick the EHR session in the HTTP Session for usage by the application
        request.getSession().setAttribute(authorizationCodeSessionFactory.getSessionKey(), session);

        try {
            response.sendRedirect(appEntryPoint);
        } catch (IOException e) {
            throw new RuntimeException("Error sending handling redirect response", e);
        }
    }
}