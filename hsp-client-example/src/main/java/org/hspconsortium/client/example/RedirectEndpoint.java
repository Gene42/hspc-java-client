/*
 * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.example;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hspconsortium.client.AbstractFhirClient;
import org.hspconsortium.client.auth.context.FhirClientContext;
import org.hspconsortium.client.impl.ClientFlowFhirClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RedirectEndpoint {

    private String clientSecret;

    @javax.annotation.Resource(name="clientSecret")
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public void handleRedirectRequest(HttpServletRequest request, HttpServletResponse response) {
        AbstractFhirClient defaultClient = new ClientFlowFhirClient(request, clientSecret);
        Bundle results = defaultClient.search().forResource(Patient.class).where(Patient.FAMILY.matches().value("Wilson")).execute();
        results.getEntries();

        FhirClientContext context = (FhirClientContext) request.getSession().getAttribute(FhirClientContext.FHIR_CLIENT_CONTEXT);
        Patient patient = defaultClient.read().resource(Patient.class).withId(context.getAccessToken().getPatientId()).execute();
        patient.getName();
    }

}
