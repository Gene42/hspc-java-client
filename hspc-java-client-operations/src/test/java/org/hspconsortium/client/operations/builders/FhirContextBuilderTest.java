package org.hspconsortium.client.operations.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.builder.FhirContextBuilder;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.apache.ApacheRestfulClientFactory;

@RunWith(JUnit4.class)
public class FhirContextBuilderTest {

	private FhirContextBuilder testSubject = new FhirContextBuilder();
	
	@Test
	public void testBuildWithProxyServer(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_HOST, "test.proxy.com");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PORT, "8080");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_USER, "TEST");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PASSWORD, "PASSWORD");
		
		FhirContext fhirContext = testSubject.build(factoryConf);
		
		assertTrue(fhirContext.getRestfulClientFactory() instanceof ApacheRestfulClientFactory);
		ApacheRestfulClientFactory clientFactory = (ApacheRestfulClientFactory) fhirContext.getRestfulClientFactory();
		
		assertEquals(1000,clientFactory.getConnectTimeout());
		assertEquals(3000,clientFactory.getSocketTimeout());
	}
	
	@Test
	public void testBuildWithoutProxyServer(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		
		FhirContext fhirContext = testSubject.build(factoryConf);
		
		assertTrue(fhirContext.getRestfulClientFactory() instanceof ApacheRestfulClientFactory);
		ApacheRestfulClientFactory clientFactory = (ApacheRestfulClientFactory) fhirContext.getRestfulClientFactory();
		
		assertEquals(1000,clientFactory.getConnectTimeout());
		assertEquals(3000,clientFactory.getSocketTimeout());
	}
}
