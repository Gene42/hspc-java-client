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
		
		//create endpoint configuration1 [i.e. endpoint url, secrets or JWK configurations]
		FHIRResourceSeverEndpointConfiguration fhirEndpointConfiguration1 = getHSPCSandboxFHIRConfiguration("SESSION#1");
	
		//create endpoint configuration2 [i.e. endpoint url, secrets or JWK configurations]
		FHIRResourceSeverEndpointConfiguration fhirEndpointConfiguration2 = getHSPCSandboxFHIRConfiguration("SESSION#2");
		
		// get fhir operations factory configurations [i.e. proxy config, timeout configs etc]
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = getFactoryConfigurations();
		
		// initialize Operations factory [internally initializes threadsafe fhir sessions] -- application level factory instance, should be singleton bean.
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(props,fhirEndpointConfiguration1,fhirEndpointConfiguration2);
		
		// get handle on patient operations to perform patient context operations. Returns singleton instances of *Operations classes.
		PatientOperations patientResourceOperations = factory.getPatientOperations();
		
		// Sample #1: pull patients from all sessions
		List<Patient> patients = patientResourceOperations.getPatientById("SMART-621799");
		Assert.assertEquals(2, patients.size());
		
		//Sample #2: pull patient from selected session
		patients = patientResourceOperations.getPatientById("SMART-621799","SESSION#1");
		Assert.assertEquals(1, patients.size());
		
		//Sample #3: pull patient from more than one selected session
		patients = patientResourceOperations.getPatientById("SMART-621799","SESSION#1","SESSION#2");
		Assert.assertEquals(2, patients.size());
	}
	
	@Test
	public void testActiveEncounters(){
		FHIRResourceSeverEndpointConfiguration endpointConfigurations = getHSPCSandboxFHIRConfiguration("SESSION#1");
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = getFactoryConfigurations();
		
		HSPCClientOperationsFactory factory = new HSPCClientOperationsFactory(props,endpointConfigurations,endpointConfigurations);
		
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
		FHIRResourceSeverEndpointConfiguration endpointConfigurations = new FHIRResourceSeverEndpointConfiguration();
		endpointConfigurations.setSessionKey(sessionKey);
		endpointConfigurations.setClientId("test_client");
		endpointConfigurations.setClientSecret("secret");
		endpointConfigurations.setFhirResourceServerURL("https://api.hspconsortium.org/hspc/data");
		endpointConfigurations.setScopes("system/*.read");
		return endpointConfigurations;
	}
}
