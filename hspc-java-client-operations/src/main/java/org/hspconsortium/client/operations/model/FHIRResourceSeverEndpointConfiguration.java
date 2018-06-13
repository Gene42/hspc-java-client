package org.hspconsortium.client.operations.model;

public class FHIRResourceSeverEndpointConfiguration {

	private String fhirResourceServerURL;
	
	private String clientId;
	
	private String clientSecret;
	
	private String jsonWebKeySetLocation;
	
	private String scopes;
	
	private String audience;
	
	private Long jwtDuration;
	
	private String sessionKey;

	public String getFhirResourceServerURL() {
		return fhirResourceServerURL;
	}

	public void setFhirResourceServerURL(String fhirResourceServerURL) {
		this.fhirResourceServerURL = fhirResourceServerURL;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getJsonWebKeySetLocation() {
		return jsonWebKeySetLocation;
	}

	public void setJsonWebKeySetLocation(String jsonWebKeySetLocation) {
		this.jsonWebKeySetLocation = jsonWebKeySetLocation;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Long getJwtDuration() {
		return jwtDuration;
	}

	public void setJwtDuration(Long jwtDuration) {
		this.jwtDuration = jwtDuration;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
}
