package org.hspconsortium.client.operations.util;

import java.util.Map;

import org.hspconsortium.client.auth.Scopes;
import org.hspconsortium.client.auth.SimpleScope;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.hspconsortium.client.operations.builder.BuilderFactory;
import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.clientcredentials.ClientCredentialsSessionFactory;

import ca.uhn.fhir.context.FhirContext;

public class HSPCClientOperationsSessionFactory {

	public static Session createFHIRResourceSession(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf, FHIRResourceSeverEndpointConfiguration endpointConf) {

		// get client credentials
		Credentials<?> clientCredentials = BuilderFactory.INSTANCE.getClientcredentialsbuilder().build(factoryConf, endpointConf);
		
		// Access token provider
		AccessTokenProvider<?> accessTokenProvider =  BuilderFactory.INSTANCE.getAccesstokenproviderbuilder().build(factoryConf);
		
		//Fhir context
		FhirContext fhirContext = BuilderFactory.INSTANCE.getFhircontextbuilder().build(factoryConf);
		
		// Endpoint provider
		FhirEndpointsProvider fhirEndpointProvider = BuilderFactory.INSTANCE.getFhirendpointproviderbuilder().build(fhirContext);
		
		// get client credentials session factory
		Scopes scopes = new Scopes();
        scopes.add(new SimpleScope(endpointConf.getScopes()));
        
        // create session and return
        return new ClientCredentialsSessionFactory<>(fhirContext, accessTokenProvider, fhirEndpointProvider, endpointConf.getFhirResourceServerURL(), endpointConf.getClientId(), clientCredentials, scopes).createSession();
	}
}
