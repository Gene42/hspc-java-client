/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.example;

import org.hspconsortium.client.auth.web.WebAuthorizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class RedirectEndpoint {

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void handleLaunchRequest(HttpServletRequest request, HttpServletResponse response) {
        Map paramMap = request.getParameterMap();
        paramMap.entrySet();
    }

}
