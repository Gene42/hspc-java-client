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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.lang.StringUtils;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.access.AccessToken;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.JsonAccessTokenProvider;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;
import org.hspconsortium.client.session.clientcredentials.ClientCredentialsAccessTokenRequest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Ignore
public class ClientCredentialsTokenRequestTest {

    @Test
    public void testClientCredentialsAccessTokenRequest() {
        Scopes requestedScopes = new Scopes();
        requestedScopes
                .add(new SimpleScope("launch"))
                .add(new SimpleScope("patient/*.read"));

        ClientSecretCredentials clientSecretCredentials = new ClientSecretCredentials("secret");
        ClientCredentialsAccessTokenRequest tokenRequest = new ClientCredentialsAccessTokenRequest("test_client", clientSecretCredentials, requestedScopes);
        AccessTokenProvider tokenProvider = new JsonAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken("http://localhost:8080/hspc-reference-authorization/token", tokenRequest);
        Assert.assertNotNull(accessToken);
        Assert.assertTrue(StringUtils.isNotBlank(accessToken.getValue()));
    }

    @Test
    public void testClientJWTCredentialsAccessTokenRequest() throws Exception {
        Scopes requestedScopes = new Scopes();
        requestedScopes
                .add(new SimpleScope("launch"))
                .add(new SimpleScope("patient/*.read"));

        // RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures
        Class currentClass = new Object() {
        }.getClass().getEnclosingClass();
        String fileName = currentClass.getClassLoader().getResource("openid-connect-jwks/development.only.keystore.jwks").getFile();

        JWKSet jwks = JWKSet.load(new File(fileName));
        RSAKey rsaKey = (RSAKey) jwks.getKeys().get(0);
        JWTCredentials jwtCredentials = new JWTCredentials(rsaKey.toRSAPrivateKey());

        jwtCredentials.setIssuer("test_client_jwt");
        jwtCredentials.setSubject("test_client_jwt");
//        final String tokenProviderUrl = "http://localhost:8080/hspc-reference-authorization/token";
        final String tokenProviderUrl = "http://lpv-hdsvnev02.co.ihc.com:8080/hspc-reference-authorization/token";

        jwtCredentials.setAudience("http://localhost:8080/hspc-reference-authorization/token");
        jwtCredentials.setTokenReference(UUID.randomUUID().toString());
        jwtCredentials.setDuration(300L);

        ClientCredentialsAccessTokenRequest<JWTCredentials> tokenRequest = new ClientCredentialsAccessTokenRequest("test_client_jwt", jwtCredentials, requestedScopes);

        AccessTokenProvider tokenProvider = new JsonAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken(tokenProviderUrl, tokenRequest);
        Assert.assertNotNull(accessToken);
        Assert.assertTrue(StringUtils.isNotBlank(accessToken.getValue()));

        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken.getValue());
            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            Assert.assertTrue(signedJWT.verify(verifier));
            // Retrieve / verify the JWT claims according to the app requirements
            Assert.assertEquals(jwtCredentials.getIssuer(), signedJWT.getJWTClaimsSet().getAudience().get(0));
            Assert.assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
        } catch (ParseException | InvalidKeySpecException | NoSuchAlgorithmException | JOSEException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClientJWTCredentialsAccessTokenRequestBehindProxy() throws Exception {
        Scopes requestedScopes = new Scopes();
        requestedScopes
                .add(new SimpleScope("launch"))
                .add(new SimpleScope("offline_access"))
                .add(new SimpleScope("system/*.read"));

        // RSA signatures require a public and private RSA key pair, the public key
        // must be made known to the JWS recipient in order to verify the signatures
        Class currentClass = new Object() {
        }.getClass().getEnclosingClass();
        String fileName = currentClass.getClassLoader().getResource("openid-connect-jwks/development.only.keystore.jwks").getFile();

        JWKSet jwks = JWKSet.load(new File(fileName));
        RSAKey rsaKey = (RSAKey) jwks.getKeys().get(0);
        JWTCredentials jwtCredentials = new JWTCredentials(rsaKey.toRSAPrivateKey());

        jwtCredentials.setIssuer("test_client_jwt");
        jwtCredentials.setSubject("test_client_jwt");
        final String tokenProviderUrl = "https://sandbox.hspconsortium.org/dstu2/hspc-reference-authorization/token";
//        final String tokenProviderUrl = "http://lpv-hdsvnev02.co.ihc.com:8080/hspc-reference-authorization/token";
//        jwtCredentials.setAudience(tokenProviderUrl);
//        jwtCredentials.setAudience("http://localhost:8080/hspc-reference-authorization/");
        jwtCredentials.setAudience("https://sandbox.hspconsortium.org/dstu2/hspc-reference-authorization/token");
        jwtCredentials.setTokenReference(UUID.randomUUID().toString());
        jwtCredentials.setDuration(300L);

        ClientCredentialsAccessTokenRequest<JWTCredentials> tokenRequest = new ClientCredentialsAccessTokenRequest("test_client_jwt", jwtCredentials, requestedScopes);

        AccessTokenProvider tokenProvider = new JsonAccessTokenProvider();
        AccessToken accessToken = tokenProvider.getAccessToken(tokenProviderUrl, tokenRequest);
        Assert.assertNotNull(accessToken);
        Assert.assertTrue(StringUtils.isNotBlank(accessToken.getValue()));

        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken.getValue());
            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            Assert.assertTrue(signedJWT.verify(verifier));
            // Retrieve / verify the JWT claims according to the app requirements
            Assert.assertEquals(jwtCredentials.getIssuer(), signedJWT.getJWTClaimsSet().getAudience().get(0));
            Assert.assertTrue(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
        } catch (ParseException | InvalidKeySpecException | NoSuchAlgorithmException | JOSEException e) {
            e.printStackTrace();
        }
    }
}
