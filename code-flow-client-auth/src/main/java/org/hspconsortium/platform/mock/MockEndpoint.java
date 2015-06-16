/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.platform.mock;

import org.hspconsortium.platform.util.WebAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MockEndpoint {

    private WebAuthorizer webAuthorizer;

    @Autowired
    public void setWebAuthorizer(WebAuthorizer webAuthorizer) {
        this.webAuthorizer = webAuthorizer;
    }

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        webAuthorizer.authorize(request,response, "", "http://localhost:8080/hsp-api/data");
    }

}
