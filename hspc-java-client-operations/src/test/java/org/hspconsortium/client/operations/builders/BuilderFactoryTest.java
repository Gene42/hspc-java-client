package org.hspconsortium.client.operations.builders;

import static org.junit.Assert.assertEquals;

import org.hspconsortium.client.operations.builder.AccessTokenProviderBuilder;
import org.hspconsortium.client.operations.builder.BuilderFactory;
import org.hspconsortium.client.operations.builder.ClientCredentialsBuilder;
import org.hspconsortium.client.operations.builder.FhirContextBuilder;
import org.hspconsortium.client.operations.builder.FhirEndpointsProviderBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BuilderFactoryTest {
	
	@Test
	public void testGetAccessTokenProviderBuilderAssertSingleton(){
		AccessTokenProviderBuilder builder = BuilderFactory.INSTANCE.getAccesstokenproviderbuilder();
		assertEquals(builder, BuilderFactory.INSTANCE.getAccesstokenproviderbuilder());
	}
	
	@Test
	public void testClientCredentialsBuilder(){
		ClientCredentialsBuilder builder = BuilderFactory.INSTANCE.getClientcredentialsbuilder();
		assertEquals(builder, BuilderFactory.INSTANCE.getClientcredentialsbuilder());
	}
	
	@Test
	public void testFhirContextBuilder(){
		FhirContextBuilder builder = BuilderFactory.INSTANCE.getFhircontextbuilder();
		assertEquals(builder, BuilderFactory.INSTANCE.getFhircontextbuilder());
	}
	
	@Test
	public void testFhirEndpointProviderBuilder(){
		FhirEndpointsProviderBuilder builder = BuilderFactory.INSTANCE. getFhirendpointproviderbuilder();
		assertEquals(builder, BuilderFactory.INSTANCE.getFhirendpointproviderbuilder());
	}
}
