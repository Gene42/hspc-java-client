/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth.credentialsflow;

import org.apache.commons.lang.StringUtils;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.AccessTokenProvider;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.impl.DefaultAccessTokenProvider;
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
    }
}
