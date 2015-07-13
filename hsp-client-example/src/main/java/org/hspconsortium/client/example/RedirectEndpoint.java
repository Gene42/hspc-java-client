/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.example;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hspconsortium.client.FhirClient;
import org.hspconsortium.client.impl.CodeFlowFhirClient;
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
        FhirClient fhirClient = new CodeFlowFhirClient(request, clientSecret);
        Bundle results = fhirClient.search().forResource(Patient.class).where(Patient.FAMILY.matches().value("Wilson")).execute();
        results.getEntries();

        String patientId = fhirClient.getFhirClientContext().getAccessToken().getPatientId();
        Patient patient = fhirClient.read().resource(Patient.class).withId(patientId).execute();
        patient.getName();
    }

}
