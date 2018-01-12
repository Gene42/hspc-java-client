package org.hspconsortium.client.auth;

import org.hspconsortium.client.controller.FhirEndpoints;
import org.hspconsortium.client.controller.FhirEndpointsProvider;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Logout {

    @Inject
    private FhirEndpointsProvider fhirEndpointsProvider;

    public String userLogout(String redirectUrl, String fhirServiceURL, HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        httpSession.invalidate();
        FhirEndpoints authEndpoints = this.fhirEndpointsProvider.getEndpoints(fhirServiceURL);
        String retVal = "redirect:" + authEndpoints.getAuthorizationEndpoint() + "?hspcRedirectUrl=" +  redirectUrl;
        return retVal;
    }

}
