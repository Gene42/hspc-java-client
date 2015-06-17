package org.hspconsortium.client.auth;

import org.hspconsortium.client.auth.impl.DefaultAuthorizationEndpointsProvider;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class DefaultAuthorizationEndpointsProviderTest {

    @Test()
    public void testGetAuthorizationEndpoints(){
        String serviceUrl = "http://localhost:8080/hsp-api/data";
        DefaultAuthorizationEndpointsProvider authEndpointsProvider = new DefaultAuthorizationEndpointsProvider();
        AuthorizationEndpoints authEndpoints = authEndpointsProvider.getAuthorizationEndpoints(serviceUrl);

        Assert.assertNotNull(authEndpoints);

        String authEndpoint = authEndpoints.getAuthorizationEndpoint();
        String tokenEndpoint = authEndpoints.getTokenEndpoint();

        Assert.assertNotNull(authEndpoint);
        Assert.assertNotNull(tokenEndpoint);
    }
}
