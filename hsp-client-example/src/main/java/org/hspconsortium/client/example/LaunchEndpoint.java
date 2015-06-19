/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.example;

import org.hspconsortium.client.auth.codeflow.CodeFlowAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LaunchEndpoint {

    private CodeFlowAuthorizer codeFlowAuthorizer;
    private String clientId;
    private String scope;
    private String authRedirectUri;

    @Autowired
    public void setCodeFlowAuthorizer(CodeFlowAuthorizer codeFlowAuthorizer) {
        this.codeFlowAuthorizer = codeFlowAuthorizer;
    }

    @javax.annotation.Resource(name="clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @javax.annotation.Resource(name="scope")
    public void setScope(String scope) {
        this.scope = scope;
    }

    @javax.annotation.Resource(name="authRedirectUri")
    public void setAuthRedirectUri(String authRedirectUri) {
        this.authRedirectUri = authRedirectUri;
    }

    @RequestMapping(value = "/launch", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        Map paramMap = request.getParameterMap();

        codeFlowAuthorizer.authorize(request, response, clientId, scope, authRedirectUri, ((String[]) paramMap.get("launch"))[0], ((String[]) paramMap.get("iss"))[0]);
    }

}
