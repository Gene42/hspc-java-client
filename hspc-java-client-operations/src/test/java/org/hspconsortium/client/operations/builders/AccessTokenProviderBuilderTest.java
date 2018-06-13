package org.hspconsortium.client.operations.builders;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.auth.access.JsonAccessTokenProvider;
import org.hspconsortium.client.operations.builder.AccessTokenProviderBuilder;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.session.ApacheHttpClientFactory;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AccessTokenProviderBuilderTest {
	
	private AccessTokenProviderBuilder testSubject = new AccessTokenProviderBuilder();
	
	private Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf;
	
	@Before
	public void setup(){
		factoryConf = new HashMap<>();
	}
	
	@Test
	public void testGetApacheHTTPClientFactory(){
		assertTrue(testSubject.getApacheHTTPClientFactory(factoryConf) instanceof ApacheHttpClientFactory);
	}
	
	@Test
	public void testBuild(){
		assertTrue(testSubject.build(factoryConf) instanceof JsonAccessTokenProvider);
	}
}
