package org.hspconsortium.client.operations.validator;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HSPCClientOperationsFactoryValidatorTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateWhenNullConfigs(){
		FHIRResourceSeverEndpointConfiguration[] configs = null;
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateWhenNullConfig(){
		FHIRResourceSeverEndpointConfiguration[] configs = new FHIRResourceSeverEndpointConfiguration[1];
		configs[0]= null;
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateWhenMissingBothClientSecretsAndJWKLocation(){
		FHIRResourceSeverEndpointConfiguration[] configs = new FHIRResourceSeverEndpointConfiguration[1];
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();
		configs[0] = config;
		
		config.setClientId("abc");
		config.setFhirResourceServerURL("https://localhost:8080");
		config.setScopes("patient/*");
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateWhenResourceURLMissing(){
		FHIRResourceSeverEndpointConfiguration[] configs = new FHIRResourceSeverEndpointConfiguration[1];
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();
		configs[0] = config;
		
		config.setClientId("abc");
		config.setClientSecret("xabbccda");
		config.setScopes("patient/*");
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateWhenScopeIsMissing(){
		FHIRResourceSeverEndpointConfiguration[] configs = new FHIRResourceSeverEndpointConfiguration[1];
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();
		configs[0] = config;
		
		config.setClientId("abc");
		config.setClientSecret("xabbccda");
		config.setFhirResourceServerURL("https://localhost:8080");
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test
	public void testValidateWhenValid(){
		FHIRResourceSeverEndpointConfiguration[] configs = new FHIRResourceSeverEndpointConfiguration[1];
		FHIRResourceSeverEndpointConfiguration config = new FHIRResourceSeverEndpointConfiguration();
		configs[0] = config;
		
		config.setClientId("abc");
		config.setClientSecret("xabbccda");
		config.setFhirResourceServerURL("https://localhost:8080");
		config.setScopes("patient/*");
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(configs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateClientOperationFactoryConfigurationNull(){
		HSPCClientOperationsFactoryValidator.validateClientOperationsFactoryConfigurationTypes(null);
	}
	
	@Test
	public void testValidateClientOperationFactoryConfigurationValid(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> props = new HashMap<>();
		HSPCClientOperationsFactoryValidator.validateClientOperationsFactoryConfigurationTypes(props);
	}
}
