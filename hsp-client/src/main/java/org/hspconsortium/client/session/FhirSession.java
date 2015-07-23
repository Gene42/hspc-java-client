/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.session;

import ca.uhn.fhir.rest.client.IGenericClient;
import org.hspconsortium.client.auth.AccessToken;

/**
 */
public interface FhirSession extends IGenericClient {

    public AccessToken getAccessToken();

}
