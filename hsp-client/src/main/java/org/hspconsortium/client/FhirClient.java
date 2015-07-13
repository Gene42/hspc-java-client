/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client;

import ca.uhn.fhir.rest.client.IGenericClient;
import org.hspconsortium.client.auth.context.FhirClientContext;

/**
 */
public interface FhirClient extends IGenericClient {

    void setFhirClientContext(FhirClientContext fhirClientContext);

    FhirClientContext getFhirClientContext();

}
