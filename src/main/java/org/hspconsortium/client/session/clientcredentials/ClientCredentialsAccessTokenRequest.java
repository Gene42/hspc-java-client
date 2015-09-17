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

package org.hspconsortium.client.session.clientcredentials;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.access.AbstractAccessTokenRequest;
import org.hspconsortium.client.auth.access.AccessTokenGrantType;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ClientCredentialsAccessTokenRequest<T extends Credentials>
        extends AbstractAccessTokenRequest<T> implements Serializable {

    private Map<String, String> tokenRequestParams = new HashMap<>();

    public ClientCredentialsAccessTokenRequest(String clientId, T clientCredentials, Scopes scopes) {
        super(clientId, clientCredentials, AccessTokenGrantType.CLIENT_CREDENTIALS);
        Validate.notNull(scopes, "Scopes must not be null");
        this.tokenRequestParams.put("scope", scopes.asParamValue());

        if (clientCredentials instanceof JWTCredentials) {
            Validate.notNull(((JWTCredentials) clientCredentials).getIssuer(), "Issuer/Client ID must not ne null");
            Validate.notNull(((JWTCredentials) clientCredentials).getSubject(), "Subject/Client ID must not ne null");
            Validate.notNull(((JWTCredentials) clientCredentials).getAudience(), "Authorization server's token URL must not ne null");
            Validate.notNull(((JWTCredentials) clientCredentials).getDuration(), "Expiration time must not ne null");
            this.tokenRequestParams.put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
            this.tokenRequestParams.put("client_assertion", ((JWTCredentials) clientCredentials).getCredentials().serialize());
        }

    }

    @Override
    public Map<String, String> getAdditionalParameters() {
        return this.tokenRequestParams;
    }

}
