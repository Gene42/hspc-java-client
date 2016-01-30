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
package org.hspconsortium.client.auth;

import ca.uhn.fhir.context.FhirContext;
import org.hspconsortium.client.controller.FhirEndpoints;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DefaultFhirEndpointsProviderTest {
    private FhirContext hapiFhirContext = FhirContext.forDstu2();

    @Test()
    public void testGetAuthorizationEndpoints(){
        String serviceUrl = "http://localhost:8080/hspc-reference-api/data";
        FhirEndpointsProvider.Impl authEndpointsProvider = new FhirEndpointsProvider.Impl(hapiFhirContext);
        FhirEndpoints authEndpoints = authEndpointsProvider.getEndpoints(serviceUrl);

        Assert.assertNotNull(authEndpoints);

        String authEndpoint = authEndpoints.getAuthorizationEndpoint();
        String tokenEndpoint = authEndpoints.getTokenEndpoint();

        Assert.assertNotNull(authEndpoint);
        Assert.assertNotNull(tokenEndpoint);
    }
}
