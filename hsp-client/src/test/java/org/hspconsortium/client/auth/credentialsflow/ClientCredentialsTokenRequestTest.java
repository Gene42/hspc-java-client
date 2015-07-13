/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.credentialsflow;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.apache.commons.lang.StringUtils;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
import org.hspconsortium.client.impl.CredentialsFlowFhirClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore
public class ClientCredentialsTokenRequestTest {

    @Test
    public void testClientCredentialsAccessTokenRequest(){
        Scopes requestedScopes = new Scopes();
        requestedScopes
                .add(new SimpleScope("launch"))
                .add(new SimpleScope("patient/*.read"));

        ClientCredentialsAccessTokenRequest tokenRequest = new ClientCredentialsAccessTokenRequest("test_client", "secret", requestedScopes);
        AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken("http://localhost:8080/hsp-auth/token", tokenRequest);
        System.out.println("Access Token: " + accessToken.getValue());
        Assert.assertNotNull(accessToken);
        Assert.assertTrue(StringUtils.isNotBlank(accessToken.getValue()));

        String patientId = "ID9995679";
        IGenericClient fhirClient = new CredentialsFlowFhirClient("http://localhost:8080/hsp-api/data", "test_client", "secret", requestedScopes);
        Patient patient = fhirClient.read().resource(Patient.class).withId(patientId).execute();
        System.out.println(StringUtils.join(patient.getName().get(0).getGiven(), " ") + " " + patient.getName().get(0).getFamily().get(0));

        Bundle results = fhirClient.search().forResource(Observation.class).where(
                Observation.SUBJECT.hasId(patientId)).
                and(Observation.CODE.exactly().identifier("8302-2")).execute();

        List<BundleEntry> entries = results.getEntries();
        for (BundleEntry entry : entries) {
            System.out.println(((DateTimeDt)((Observation)entry.getResource()).getApplies()).getValueAsString() + " " +
            ((QuantityDt)((Observation)entry.getResource()).getValue()).getValue());
        }
    }
}
