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

package org.hspconsortium.client.session.impl;


import org.hspconsortium.client.session.FhirSessionContext;
import org.hspconsortium.client.session.FhirSessionContextHolder;

import java.util.HashMap;
import java.util.Map;

public class SimpleFhirSessionContextHolder implements FhirSessionContextHolder {

    /* NOTE: This is a simple FhirSessionContextHolder which holds context in memory in the current JVM and is not
     * a robust, production level FhirSessionContextHolder.
     */
    private static final Map<String, FhirSessionContext> globalFhirSessionContextMap = new HashMap<>();

    @Override
    public FhirSessionContext create(String oauthState) {
        if (globalFhirSessionContextMap.containsKey(oauthState)) {
            throw new IllegalArgumentException("FhirSessionContext already exists for state: " + oauthState);
        }
        return put(oauthState, new FhirSessionContext(oauthState));
    }

    @Override
    public FhirSessionContext get(String oauthState) {
        return globalFhirSessionContextMap.get(oauthState);
    }

    @Override
    public FhirSessionContext put(String oauthState, FhirSessionContext fhirSessionContext) {
        globalFhirSessionContextMap.put(oauthState, fhirSessionContext);
        return fhirSessionContext;
    }
}
