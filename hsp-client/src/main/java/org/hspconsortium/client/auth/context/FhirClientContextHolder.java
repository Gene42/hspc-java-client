/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FhirClientContextHolder {

    private static Map<String, FhirClientContext> globalFhirClientContextMap = new HashMap<String, FhirClientContext>();

    public static FhirClientContext getFhirClientContext(String state){
        return globalFhirClientContextMap.get(state);
    }

    public static void addFhirClientContext(FhirClientContext fhirClientContext){
        if(fhirClientContext != null){
            globalFhirClientContextMap.put(fhirClientContext.getState(), fhirClientContext);
        }
    }

    public static void clearGlobalFhirClientContextMap(FhirClientContext fhirClientContext){
        globalFhirClientContextMap.remove(fhirClientContext.getState());
    }
}
