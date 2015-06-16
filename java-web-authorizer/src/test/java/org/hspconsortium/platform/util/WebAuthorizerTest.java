package org.hspconsortium.platform.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class WebAuthorizerTest {

    @Test
    public void testAuthorize() throws Exception {
        WebAuthorizer webAuthorizer = new WebAuthorizer();
        HttpClient httpClient = new DefaultHttpClient();

        webAuthorizer.authorize(null, null, null, "http://localhost:8080/hsp-api/data");

    }
}
