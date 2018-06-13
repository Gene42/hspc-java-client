package org.hspconsortium.client.operations.builder;

import org.hspconsortium.client.operations.util.NullChecker;

public class BuilderFactory {

	private AccessTokenProviderBuilder accessTokenProviderBuilder;
	
	private ClientCredentialsBuilder clientCredentialsBuilder;
	
	private FhirContextBuilder fhirContextBuilder;
	
	private FhirEndpointsProviderBuilder fhirEndpointProviderBuilder;

	// Singleton instance of the factory;
	public static final BuilderFactory INSTANCE = new BuilderFactory();
	
	private BuilderFactory(){}
	
	public AccessTokenProviderBuilder getAccesstokenproviderbuilder() {
		if(NullChecker.isNullish(accessTokenProviderBuilder))
			accessTokenProviderBuilder = new AccessTokenProviderBuilder();
		
		return accessTokenProviderBuilder;
	}

	public ClientCredentialsBuilder getClientcredentialsbuilder() {
		
		if(NullChecker.isNullish(this.clientCredentialsBuilder))
			clientCredentialsBuilder = new ClientCredentialsBuilder();
		
		return clientCredentialsBuilder;
	}

	public FhirContextBuilder getFhircontextbuilder() {
		
		if(NullChecker.isNullish(this.fhirContextBuilder))
			fhirContextBuilder = new FhirContextBuilder();
		
		return fhirContextBuilder;
	}

	public FhirEndpointsProviderBuilder getFhirendpointproviderbuilder() {
		
		if(NullChecker.isNullish(this.fhirEndpointProviderBuilder))
			fhirEndpointProviderBuilder = new FhirEndpointsProviderBuilder();
		
		return fhirEndpointProviderBuilder;
	}
}
