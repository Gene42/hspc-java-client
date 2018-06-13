package org.hspconsortium.client.operations.builders;

import org.hspconsortium.client.controller.FhirEndpointsProviderDSTU2;
import org.hspconsortium.client.operations.builder.FhirEndpointsProviderBuilder;
import static org.junit.Assert.*;
import org.junit.Test;

public class FhirEndpointsProviderBuilderTest {

	private FhirEndpointsProviderBuilder builder = new FhirEndpointsProviderBuilder();
	
	@Test
	public void testBuild(){
		assertTrue(builder.build(null) instanceof FhirEndpointsProviderDSTU2);
	}
}
