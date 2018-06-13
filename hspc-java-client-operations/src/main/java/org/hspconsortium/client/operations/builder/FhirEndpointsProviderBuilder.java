package org.hspconsortium.client.operations.builder;

import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.hspconsortium.client.controller.FhirEndpointsProviderDSTU2;

import ca.uhn.fhir.context.FhirContext;

public class FhirEndpointsProviderBuilder {

	 public FhirEndpointsProvider build(FhirContext fhirContext){
	      return new FhirEndpointsProviderDSTU2(fhirContext);
	 }
}
