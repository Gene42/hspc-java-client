package org.hspconsortium.client.auth.credentialsflow;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.apache.commons.lang.StringUtils;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
import org.hspconsortium.client.impl.CredentialsFlowFhirClient;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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
        Assert.assertNotNull(accessToken);
        Assert.assertTrue(StringUtils.isNotBlank(accessToken.getValue()));

        IGenericClient defaultClient = new CredentialsFlowFhirClient("http://localhost:8080/hsp-api/data", "test_client", "secret", requestedScopes);
        Bundle results = defaultClient.search().forResource(Patient.class).where(Patient.FAMILY.matches().value("Wilson")).execute();
        results.getEntries();

    }
}
