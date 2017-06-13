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

package org.hspconsortium.client.auth.credentialsflow;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.JsonAccessTokenProvider;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.hspconsortium.client.controller.FhirEndpointsProviderDSTU2;
import org.hspconsortium.client.session.ApacheHttpClientFactory;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.clientcredentials.ClientCredentialsSessionFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ClientCredentialsSessionFactoryTest {
    private FhirContext hapiFhirContext = FhirContext.forDstu2();

    @Test
    public void testClientCredentialsAccessTokenRequest() {
        Scopes requestedScopes = new Scopes();
        requestedScopes
                .add(new SimpleScope("system/*.read"));
        // note, system/*.read access is required to be added to the OpenId client

        String fhirServiceUrl = "https://api.hspconsortium.org/hspc/data";
//        String fhirServiceUrl = "https://api.hspconsortium.org/hspc/data";
        String clientId = "test_client";
        ClientSecretCredentials clientSecretCredentials = new ClientSecretCredentials("secret");
//        ApacheHttpClientFactory apacheHttpClientFactory = new ApacheHttpClientFactory(
//                null, null, null, null,
//                10000, 10000);

        AccessTokenProvider tokenProvider = new JsonAccessTokenProvider();
        FhirEndpointsProvider fhirEndpointsProvider = new FhirEndpointsProviderDSTU2(hapiFhirContext);
//        AccessTokenProvider tokenProvider = new JsonAccessTokenProvider(apacheHttpClientFactory);
//        FhirEndpointsProvider fhirEndpointsProvider = new FhirEndpointsProviderDSTU2(hapiFhirContext);

        ClientCredentialsSessionFactory<ClientSecretCredentials> sessionFactory
                = new ClientCredentialsSessionFactory<>(hapiFhirContext, tokenProvider, fhirEndpointsProvider, fhirServiceUrl, clientId, clientSecretCredentials, requestedScopes);
        Session session = sessionFactory.createSession();

        String patientId = "COREPATIENT1";
        Patient patient = session.read().resource(Patient.class).withId(patientId).execute();
        Assert.assertNotNull(patient);
        Assert.assertEquals("HumanNameDt[family=Cottrill,given=Buford]", patient.getNameFirstRep().toString());
    }
}
