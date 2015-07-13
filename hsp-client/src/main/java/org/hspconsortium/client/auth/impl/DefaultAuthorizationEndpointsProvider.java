/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.RestfulClientFactory;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.server.Constants;
import org.hspconsortium.client.auth.AuthorizationEndpoints;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;

import java.util.List;

public class DefaultAuthorizationEndpointsProvider implements AuthorizationEndpointsProvider {

    private static final String AUTH_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#authorize";
    private static final String TOKEN_ENDPOINT_EXTENSION = "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris#token";
    private final String fhirServiceUrl;

    private AuthorizationEndpoints authorizationEndpoints;

    public DefaultAuthorizationEndpointsProvider(String fhirServiceUrl) {
        this.fhirServiceUrl = fhirServiceUrl;
    }

    @Override
    public AuthorizationEndpoints getAuthorizationEndpoints() {
        if (authorizationEndpoints != null) {
            return authorizationEndpoints;
        }

        FhirContext hapiFhirContext = FhirContext.forDstu2();
        Conformance conformance;
        try {
            IGenericClient client = hapiFhirContext.newRestfulGenericClient(fhirServiceUrl);
            conformance = client.fetchConformance().ofType(Conformance.class).execute();
        } catch (FhirClientConnectionException e) {
            throw new FhirClientConnectionException(hapiFhirContext.getLocalizer().getMessage(RestfulClientFactory.class, "failedToRetrieveConformance", fhirServiceUrl + Constants.URL_TOKEN_METADATA), e);
        }

        Conformance.Rest rest = conformance.getRest().get(0);
        Conformance.RestSecurity restSecurity = rest.getSecurity();
        List<ExtensionDt> extensions = restSecurity.getUndeclaredExtensions();

        String authEndpoint = null;
        String tokenEndpoint = null;
        for (ExtensionDt extensionDt : extensions) {
            if (extensionDt.getUrl().equalsIgnoreCase(AUTH_ENDPOINT_EXTENSION)){
                authEndpoint = extensionDt.getValueAsPrimitive().getValueAsString();
            }
            else if(extensionDt.getUrl().equalsIgnoreCase(TOKEN_ENDPOINT_EXTENSION)){
                tokenEndpoint = extensionDt.getValueAsPrimitive().getValueAsString();
            }
        }
        authorizationEndpoints = new AuthorizationEndpoints(authEndpoint, tokenEndpoint);
        return authorizationEndpoints;
    }
}
