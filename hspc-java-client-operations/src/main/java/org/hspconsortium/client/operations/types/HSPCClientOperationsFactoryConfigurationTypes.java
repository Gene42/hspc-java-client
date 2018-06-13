package org.hspconsortium.client.operations.types;

public enum HSPCClientOperationsFactoryConfigurationTypes {
	HTTP_CONNECTION_TIMEOUT_IN_MILLIS("3000"),
	PROXY_HOST(null),
	PROXY_PORT(null),
	PROXY_USER(null),
	PROXY_PASSWORD(null),
	HTTP_READ_TIMEOUT_IN_MILLIS("15000"),
	JSON_WEB_KEYSET_SIZE_LIMIT("1000000");
	
	private String defaultValue;
	
	private HSPCClientOperationsFactoryConfigurationTypes(String defaultValue){
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}
