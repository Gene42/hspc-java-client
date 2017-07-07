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

package org.hspconsortium.client.session;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.SummaryEnum;
import org.hspconsortium.client.auth.access.AccessToken;
import org.hspconsortium.client.auth.access.AccessTokenRequest;
import org.hspconsortium.client.auth.access.UserInfo;

public class Session extends AbstractFhirSession {

    private final SessionContextWrapperSTU3 sessionContextWrapperSTU3;

    private final SessionContextWrapperDSTU2 sessionContextWrapperDSTU2;

    public Session(FhirContext hapiFhirContext, String fhirServiceApi, AccessToken accessToken, UserInfo userInfo) {
        this(hapiFhirContext, fhirServiceApi, accessToken, userInfo, null, null);
    }

    public Session(FhirContext hapiFhirContext, String fhirServiceApi, AccessToken accessToken, UserInfo userInfo,
                   AccessTokenRequest refreshTokenRequest, String tokenEndpoint) {
        super(hapiFhirContext, fhirServiceApi, accessToken, userInfo, refreshTokenRequest, tokenEndpoint);
        sessionContextWrapperSTU3 = new SessionContextWrapperSTU3(this);
        sessionContextWrapperDSTU2 = new SessionContextWrapperDSTU2(this);
    }

    public SessionContextWrapperSTU3 getContextSTU3() {
        return sessionContextWrapperSTU3;
    }

    public SessionContextWrapperDSTU2 getContextDSTU2() {
        return sessionContextWrapperDSTU2;
    }

    @Override
    public void setSummary(SummaryEnum theSummary) {

    }



}
