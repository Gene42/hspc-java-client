package org.hspconsortium.client.operations;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.util.HSPCClientOperationsSessionFactory;
import org.hspconsortium.client.session.Session;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HSPCClientOperationsSessionFactory.class})
public class HSPCClientOperationsFactoryTest {
	
	@Mock
	private Session session;

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateHSPCClientOperationsFactory(){
		
		// set configuration class
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();	
		config.setClientId("ClientId");
		config.setClientSecret("xyabcdaaa");
		config.setFhirResourceServerURL("https://localhost:8080");
		config.setScopes("patient/*");
		config.setSessionKey("key1");
	
		PowerMockito.mockStatic(HSPCClientOperationsSessionFactory.class);
		PowerMockito.when(HSPCClientOperationsSessionFactory.createFHIRResourceSession(Mockito.any(Map.class), Mockito.eq(config))).thenReturn(session);
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(config);
		
		assertNotNull(factory);
		Mockito.verifyNoMoreInteractions(session);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testCreateHSPCClientOperationsFactoryOverloadedConstructor(){
		
		// set configuration class
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();	
		config.setClientId("ClientId");
		config.setClientSecret("xyabcdaaa");
		config.setFhirResourceServerURL("https://localhost:8080");
		config.setScopes("patient/*");
		config.setSessionKey("key1");
	
		PowerMockito.mockStatic(HSPCClientOperationsSessionFactory.class);
		PowerMockito.when(HSPCClientOperationsSessionFactory.createFHIRResourceSession(Mockito.any(Map.class), Mockito.eq(config))).thenReturn(session);
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(new HashMap<>(),config);
		
		assertNotNull(factory);
		Mockito.verifyNoMoreInteractions(session);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testGetPatientOperations(){
		
		// set configuration class
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();	
		config.setClientId("ClientId");
		config.setClientSecret("xyabcdaaa");
		config.setFhirResourceServerURL("https://localhost:8080");
		config.setScopes("patient/*");
		config.setSessionKey("key1");
	
		PowerMockito.mockStatic(HSPCClientOperationsSessionFactory.class);
		PowerMockito.when(HSPCClientOperationsSessionFactory.createFHIRResourceSession(Mockito.any(Map.class), Mockito.eq(config))).thenReturn(session);
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(config);
		
		assertNotNull(factory.getPatientOperations());
		assertTrue(factory.getPatientOperations() instanceof PatientOperations);
		Mockito.verifyNoMoreInteractions(session);
	}
	
}
