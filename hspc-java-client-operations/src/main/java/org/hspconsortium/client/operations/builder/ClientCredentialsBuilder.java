package org.hspconsortium.client.operations.builder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;
import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.HSPCClientOperationsSessionFactory;
import org.hspconsortium.client.operations.util.NullChecker;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

public class ClientCredentialsBuilder extends ClientOperationsBuilderBase{

	public Credentials<?> build(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf, FHIRResourceSeverEndpointConfiguration endpointConf) {
		
		// check if client credentials are provided, if provided return them as preferred option.
		 if(NullChecker.isNotNullish(endpointConf.getClientSecret()))
			 return new ClientSecretCredentials(endpointConf.getClientSecret());
		
		// processing will come here if client credentials are not provided. Private key should be loaded from JWS here and signed JWT identity token should be generated for exchanging for access token.
		return getJwtCredentials(loadJWKSet(factoryConf, endpointConf.getJsonWebKeySetLocation()), endpointConf);
	}

	public JWKSet loadJWKSet(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf, String jsonWebKeySetLocation) {

		// if it is URL, load as URL
		if (isURL(jsonWebKeySetLocation))
			return loadJWKSetFromURL(factoryConf, jsonWebKeySetLocation);

		// If it is not URL, it is going to be file based, so load from localfile.
		// At this point if it is bad location (ie. neither URL and nor file),
		// code will throw illegal argument exception from loadJWKSetFromFile method to let client app know
		return loadJWKSetFromFile(jsonWebKeySetLocation);
	}

	public JWKSet loadJWKSetFromFile(String jsonWebKeySetLocation) {

		URL url = HSPCClientOperationsSessionFactory.class.getClassLoader().getResource(jsonWebKeySetLocation);

		if (NullChecker.isNullish(url) || NullChecker.isNullish(url.getFile()))
			throw new IllegalArgumentException("Did not find keystore at the location: " + jsonWebKeySetLocation);

		String fileName = HSPCClientOperationsSessionFactory.class.getClassLoader().getResource(jsonWebKeySetLocation).getFile();

		try {
			return JWKSet.load(new File(fileName));
		} catch (IOException | ParseException e) {
			throw new IllegalArgumentException("Exception in loading JWK from File: " + jsonWebKeySetLocation, e);
		}
	}

	public JWKSet loadJWKSetFromURL(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf, String jsonWebKeySetLocation) {
		try {
			return JWKSet.load(new URL(jsonWebKeySetLocation), 
					getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS),
					getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS), 
					getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.JSON_WEB_KEYSET_SIZE_LIMIT));
		} catch (IOException | ParseException e) {
			throw new IllegalArgumentException("Exception in loading JWK from URL: " + jsonWebKeySetLocation, e);
		}
	}

	 public JWTCredentials getJwtCredentials(JWKSet jwkSet, FHIRResourceSeverEndpointConfiguration endpointConf) {
		try {
			RSAKey rsaKey = (RSAKey) jwkSet.getKeys().get(0);
			JWTCredentials credentials = new JWTCredentials(rsaKey.toRSAPrivateKey());
			credentials.setIssuer(endpointConf.getClientId());
			credentials.setSubject(endpointConf.getClientId());
			credentials.setAudience(endpointConf.getAudience());
			credentials.setTokenReference(UUID.randomUUID().toString());
			credentials.setDuration(endpointConf.getJwtDuration());
			return credentials;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean isURL(String jsonWebKeySetLocation) {

		if(NullChecker.isNullish(jsonWebKeySetLocation))
			return false;
		
		return jsonWebKeySetLocation.toLowerCase().matches("^https?://.*");
	}
}
