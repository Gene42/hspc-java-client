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

package org.hspconsortium.client.auth.access;

import org.apache.commons.lang3.Validate;
import org.hspconsortium.client.auth.credentials.Credentials;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RefreshAccessTokenRequest<T extends Credentials> extends AbstractAccessTokenRequest<T> implements Serializable {

    private Map<String, String> tokenRequestParams = new HashMap<String, String>();

    public RefreshAccessTokenRequest(String clientId, T clientSecret, String refreshToken) {
        super(clientId, clientSecret, AccessTokenGrantType.REFRESH_TOKEN);
        Validate.notNull(clientId, "the clientId must not be null");
        Validate.notNull(refreshToken, "the refreshToken must not be null");

        this.tokenRequestParams.put("client_id", clientId);
        this.tokenRequestParams.put("refresh_token", refreshToken);
    }

    @Override
    public Map<String, String> getAdditionalParameters() {
        return this.tokenRequestParams;
    }

}
