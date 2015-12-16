/*
 * #%L
 * hsp-client
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
package org.hspconsortium.client.auth.credentials;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

public class JWTCredentials implements Credentials<JWT> {
    private RSAPrivateKey rsaPrivateKey;

    private String issuer;

    private String subject;

    private String audience;

    private Long durationSeconds;

    private String tokenReference;

    public JWTCredentials(RSAPrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * @return Duration of the token in seconds
     */
    public Long getDuration() {
        return durationSeconds;
    }

    /**
     * @param durationSeconds Duration of the token in seconds
     */
    public void setDuration(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getTokenReference() {
        return tokenReference;
    }

    public void setTokenReference(String tokenReference) {
        this.tokenReference = tokenReference;
    }

    @Override
    public JWT getCredentials() {
        return generateAuthenticationJwt();
    }

    private JWT generateAuthenticationJwt() {
        // Create RSA-signer with the private key
        JWSSigner signer = new RSASSASigner(this.rsaPrivateKey);
        // Prepare JWT with claims set
        JWTClaimsSet claimsSet = new JWTClaimsSet();
        claimsSet.setIssuer(issuer);
        claimsSet.setSubject(subject);
        claimsSet.setAudience(audience);
        claimsSet.setIssueTime(new Date());
        claimsSet.setExpirationTime(new Date(new Date().getTime() + durationSeconds * 1000));
        claimsSet.setJWTID(tokenReference);

        SignedJWT signedJWT = new SignedJWT(new com.nimbusds.jose.JWSHeader(JWSAlgorithm.RS256), claimsSet);

        try {
            signedJWT.sign(signer);
        } catch (JOSEException jose_ex) {
            throw new RuntimeException("Error signing JSON Web Token.", jose_ex);
        }

        return signedJWT;
    }
}
