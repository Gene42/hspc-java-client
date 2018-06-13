package org.hspconsortium.client.operations.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.clientcredentials.ClientCredentialsSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HSPCClientOperationsSessionFactory.class})
public class HSPCClientOperationsSessionFactoryTest {

	@InjectMocks
	private HSPCClientOperationsSessionFactory testSubject;
	
	@Mock
	private Session session;
	
	@Mock
	private ClientCredentialsSessionFactory<?> mockedSessionFactory;
	
	@Test
	public void testCreateFhirResourceSession() throws Exception{
		FHIRResourceSeverEndpointConfiguration endpointConf = new FHIRResourceSeverEndpointConfiguration();
		endpointConf.setClientSecret("secret");
		endpointConf.setAudience("audience");
		endpointConf.setClientId("client");
		endpointConf.setFhirResourceServerURL("https://localhost:8080");
		endpointConf.setJwtDuration(300000l);
		endpointConf.setScopes("read/*");
		endpointConf.setSessionKey("test");		
	
		PowerMockito.whenNew(ClientCredentialsSessionFactory.class).withAnyArguments().thenReturn(mockedSessionFactory);
		
		when(mockedSessionFactory.createSession()).thenReturn(session);
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		
		assertEquals(session,testSubject.createFHIRResourceSession(factoryConf, endpointConf));
	}
}
