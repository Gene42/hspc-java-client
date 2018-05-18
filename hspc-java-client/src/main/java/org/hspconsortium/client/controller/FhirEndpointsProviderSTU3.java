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
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.Constants;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.Extension;

import java.util.List;

public class FhirEndpointsProviderSTU3 implements FhirEndpointsProvider {

    private static final String URIS_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris";
    private final FhirContext fhirContext;

    public FhirEndpointsProviderSTU3(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    public FhirEndpoints getEndpoints(String fhirServiceUrl) {
        CapabilityStatement capabilityStatement;
        try {
            IGenericClient client = fhirContext.newRestfulGenericClient(fhirServiceUrl);
            capabilityStatement = client.fetchConformance().ofType(CapabilityStatement.class).execute();
        } catch (FhirClientConnectionException e) {
            throw new FhirClientConnectionException(fhirContext.getLocalizer()
                    .getMessage(RestfulClientFactory.class, "failedToRetrieveConformance", fhirServiceUrl + "/"
                            + Constants.URL_TOKEN_METADATA), e);
        }

        CapabilityStatement.CapabilityStatementRestComponent rest = capabilityStatement.getRest().get(0);
        CapabilityStatement.CapabilityStatementRestSecurityComponent restSecurity = rest.getSecurity();
        List<Extension> extensions = restSecurity.getExtension();

        String authEndpoint = null;
        String tokenEndpoint = null;
        String userInfoEndpoint = null;
        for (Extension extension : extensions) {
            if (extension.getUrl().equalsIgnoreCase(URIS_ENDPOINT_EXTENSION)) {
                List<Extension> urisExtensions = extension.getExtension();
                for (Extension uriExtensionDt : urisExtensions) {
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
