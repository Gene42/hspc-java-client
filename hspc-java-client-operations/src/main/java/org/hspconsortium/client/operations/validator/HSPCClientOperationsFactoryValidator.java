package org.hspconsortium.client.operations.validator;

import java.util.Arrays;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.NullChecker;

public class HSPCClientOperationsFactoryValidator {

	public static void validateFHIRResourceServerEndpointConfiguration(FHIRResourceSeverEndpointConfiguration... configs){
		
		if(NullChecker.isNullish(configs))
			throw new IllegalArgumentException("Invalid configs supplied...");
		
		Arrays.asList(configs).forEach(config -> {
			
			if(NullChecker.isNullish(config))
				throw new IllegalArgumentException("FHIR endpoint configuration object can not be null");
			
			if(NullChecker.isNullish(config.getClientSecret()) && NullChecker.isNullish(config.getJsonWebKeySetLocation()))
				throw new IllegalArgumentException("At least one of Client secret or Json Web Key Location must be provided");
			
			if(NullChecker.isNullish(config.getFhirResourceServerURL()))
				throw new IllegalArgumentException("Fhir server resource URL can not be null");
			
			if(NullChecker.isNullish(config.getClientId()))
				throw new IllegalArgumentException("ClientId can not be null");
			
			if(NullChecker.isNullish(config.getScopes()))
				throw new IllegalArgumentException("Fhir session scopes can not be null");
		});
	}
	
	
	public static void validateClientOperationsFactoryConfigurationTypes(Map<HSPCClientOperationsFactoryConfigurationTypes, String> props){
		if(props == null)
			throw new IllegalArgumentException("Client Operations Factory Configuration can not be null");
	}
}
