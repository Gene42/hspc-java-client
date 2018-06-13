package org.hspconsortium.client.operations.builder;

import java.util.Map;

import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.NullChecker;

import ca.uhn.fhir.context.FhirContext;

public class FhirContextBuilder extends ClientOperationsBuilderBase {

	 public FhirContext build(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf) {
		   
		 // initialize for DSTU2
		 FhirContext hapiFhirContext = FhirContext.forDstu2();
		 
		 hapiFhirContext.getRestfulClientFactory().setConnectTimeout(getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS));
	     hapiFhirContext.getRestfulClientFactory().setSocketTimeout(getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS));

	     if(NullChecker.isNotNullish(factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_HOST))){
	    	 hapiFhirContext.getRestfulClientFactory().setProxy(factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_HOST), getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.PROXY_PORT));
	    	 hapiFhirContext.getRestfulClientFactory().setProxyCredentials(factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_USER), factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PASSWORD));
	     }
	     
	     return hapiFhirContext;
	 }
}
