package org.hspconsortium.client.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.rest.server.EncodingEnum;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hspconsortium.client.AbstractFhirClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.AuthorizationEndpointsProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.credentialsflow.ClientCredentialsAccessTokenRequest;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
import org.hspconsortium.client.auth.impl.DefaultAuthorizationEndpointsProvider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CredentialsFlowFhirClient extends AbstractFhirClient  {

    public CredentialsFlowFhirClient(String fhirApi, String clientId, String clientSecret, Scopes scopes) {
        this.client = hapiFhirContext.newRestfulGenericClient(fhirApi);

        AuthorizationEndpointsProvider authorizationEndpointsProvider = new DefaultAuthorizationEndpointsProvider(fhirApi);
        ClientCredentialsAccessTokenRequest tokenRequest = new ClientCredentialsAccessTokenRequest(clientId, clientSecret, scopes);
        AccessTokenProvider tokenProvider = new DefaultAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken(authorizationEndpointsProvider.getAuthorizationEndpoints().getTokenEndpoint(), tokenRequest);

        this.client.registerInterceptor(new BearerTokenAuthorizationHeaderInterceptor(accessToken));
    }

}
