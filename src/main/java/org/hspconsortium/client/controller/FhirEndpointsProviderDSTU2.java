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

package org.hspconsortium.client.controller;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.Constants;

import java.util.List;

public class FhirEndpointsProviderDSTU2 implements FhirEndpointsProvider {

    private static final String AUTH_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#authorize";
    private static final String TOKEN_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#token";
    private static final String URIS_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris";
    private final FhirContext fhirContext;

    public FhirEndpointsProviderDSTU2(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    public FhirEndpoints getEndpoints(String fhirServiceUrl) {
        Conformance conformance;
        try {
            IGenericClient client = fhirContext.newRestfulGenericClient(fhirServiceUrl);
            conformance = client.fetchConformance().ofType(Conformance.class).execute();
        } catch (FhirClientConnectionException e) {
            throw new FhirClientConnectionException(fhirContext.getLocalizer()
                    .getMessage(RestfulClientFactory.class, "failedToRetrieveConformance", fhirServiceUrl + "/"
                            + Constants.URL_TOKEN_METADATA), e);
        }

        Conformance.Rest rest = conformance.getRest().get(0);
        Conformance.RestSecurity restSecurity = rest.getSecurity();
        List<ExtensionDt> extensions = restSecurity.getUndeclaredExtensions();

        String authEndpoint = null;
        String tokenEndpoint = null;
        String userInfoEndpoint = null;
        for (ExtensionDt extension : extensions) {
            if (extension.getUrl().equalsIgnoreCase(URIS_ENDPOINT_EXTENSION)) {
                List<ExtensionDt> urisExtensions = extension.getExtension();
                for (ExtensionDt uriExtensionDt : urisExtensions) {
                    if (uriExtensionDt.getUrl().equalsIgnoreCase("authorize")) {
                        authEndpoint = uriExtensionDt.getValueAsPrimitive().getValueAsString();
                    } else if (uriExtensionDt.getUrl().equalsIgnoreCase("token")) {
                        tokenEndpoint = uriExtensionDt.getValueAsPrimitive().getValueAsString();
                        userInfoEndpoint = tokenEndpoint.replaceFirst("token", "userinfo");
                    }
                }
            }
        }
        return new FhirEndpoints(fhirServiceUrl, authEndpoint, tokenEndpoint, userInfoEndpoint);
    }

}
