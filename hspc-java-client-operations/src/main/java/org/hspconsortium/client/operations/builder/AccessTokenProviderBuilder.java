package org.hspconsortium.client.operations.builder;

import java.util.Map;

import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.JsonAccessTokenProvider;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.session.ApacheHttpClientFactory;

public class AccessTokenProviderBuilder extends ClientOperationsBuilderBase{
	
	public ApacheHttpClientFactory getApacheHTTPClientFactory(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf) {
		return new ApacheHttpClientFactory(	factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_HOST), 
											getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.PROXY_PORT),
											factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_USER), 
											factoryConf.get(HSPCClientOperationsFactoryConfigurationTypes.PROXY_PASSWORD),
											getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS), 
											getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS));
	}

	public AccessTokenProvider<?> build(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf) {
		return new JsonAccessTokenProvider(getApacheHTTPClientFactory(factoryConf));
	}
}
