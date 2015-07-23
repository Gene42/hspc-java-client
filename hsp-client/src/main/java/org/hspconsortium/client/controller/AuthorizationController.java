/*
 *
 *  * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 *
 */
package org.hspconsortium.client.controller;

import org.hspconsortium.client.session.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthorizationController {

    private SessionFactory sessionFactory;
    private String appEntryPoint;

    @javax.annotation.Resource(name="appEntryPoint")
    public void setAppEntryPoint(String appEntryPoint) {
        this.appEntryPoint = appEntryPoint;
    }

    @javax.annotation.Resource(name="codeFlowSessionFactory")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @RequestMapping(value = "/launch", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        sessionFactory.authorize(request, response);
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void handleRedirectRequest(HttpServletRequest request, HttpServletResponse response) {
        sessionFactory.requestAccessToken(request);
        try {
            response.sendRedirect(appEntryPoint);
        } catch (IOException e) {

        }

    }


}
