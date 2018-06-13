package org.hspconsortium.client.operations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.NullChecker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ca.uhn.fhir.model.dstu2.resource.Patient;

@RunWith(JUnit4.class)
public class PatientOperationsIntegTest {

	@Test
	public void testGetPatientById(){
		
		//cerner session1
		FHIRResourceSeverEndpointConfiguration cernerEndpointConfigurations = getHSPCSandboxFHIRConfiguration("SESSION#1");
	
		// cerner session2
		FHIRResourceSeverEndpointConfiguration cernerEndpointConfigurations2 = getHSPCSandboxFHIRConfiguration("SESSION#2");
		
		// get factory configuration
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = getFactoryConfigurations();
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(props,cernerEndpointConfigurations,cernerEndpointConfigurations2);
		
		PatientOperations patientResourceOperations = factory.getPatientOperations();
		
		List<Patient> patients = patientResourceOperations.getPatientById("SMART-621799");
		Assert.assertEquals(2, patients.size());
		
		patients = patientResourceOperations.getPatientById("SMART-621799","SESSION#1");
		Assert.assertEquals(1, patients.size());
		
		patients = patientResourceOperations.getPatientById("SMART-621799","SESSION#2");
		Assert.assertEquals(1, patients.size());
	}
	
	@Test
	public void testActiveEncounters(){
		//cerner session1
		FHIRResourceSeverEndpointConfiguration cernerEndpointConfigurations = getHSPCSandboxFHIRConfiguration("SESSION#1");
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = getFactoryConfigurations();
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(props,cernerEndpointConfigurations,cernerEndpointConfigurations);
		
		PatientOperations patientResourceOperations = factory.getPatientOperations();
		
		Assert.assertEquals(0,patientResourceOperations.getActiveEncountersForPatientId("SMART-621799").size());
	}

	private Map<HSPCClientOperationsFactoryConfigurationTypes, String> getFactoryConfigurations() {
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = new HashMap<>();
		
		if(NullChecker.isNotNullish(System.getenv("proxy.host")))
			props.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_HOST, System.getenv("proxy.host"));
		
		if(NullChecker.isNotNullish(System.getenv("proxy.port")))
			props.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PORT, System.getenv("proxy.port"));
		
		if(NullChecker.isNotNullish(System.getenv("proxy.user")))
			props.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_USER, System.getenv("proxy.user"));
		
		if(NullChecker.isNotNullish(System.getenv("proxy.password")))
			props.put(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PASSWORD, System.getenv("proxy.password"));
		
		return props;
	}

	private FHIRResourceSeverEndpointConfiguration getHSPCSandboxFHIRConfiguration(String sessionKey) {
		FHIRResourceSeverEndpointConfiguration cernerEndpointConfigurations = new FHIRResourceSeverEndpointConfiguration();
		cernerEndpointConfigurations.setSessionKey(sessionKey);
		cernerEndpointConfigurations.setClientId("test_client");
		cernerEndpointConfigurations.setClientSecret("secret");
		cernerEndpointConfigurations.setFhirResourceServerURL("https://api.hspconsortium.org/hspc/data");
		cernerEndpointConfigurations.setScopes("system/*.read");
		return cernerEndpointConfigurations;
	}
}
