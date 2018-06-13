package org.hspconsortium.client.operations.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.auth.credentials.Credentials;
import org.hspconsortium.client.auth.credentials.JWTCredentials;
import org.hspconsortium.client.operations.builder.ClientCredentialsBuilder;
import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

@RunWith(PowerMockRunner.class)
@PrepareForTest({JWK.class,RSAKey.class, JWKSet.class})
public class ClienCredentialsBuilderTest {

	@InjectMocks
	private ClientCredentialsBuilder testSubject;
	
	@Mock
	private RSAPrivateKey privateKey;
	
	@Mock
	private JWKSet jwkSet;
	
	private void assertNoMoreInteractions(Object... o){
		
		if(o != null && o.length > 0)
			verifyNoMoreInteractions(o);
		
		verifyNoMoreInteractions(privateKey,jwkSet);
	}
	
	@Test
	public void testIsURLFalse(){
		assertFalse(testSubject.isURL("/usr/local/keystore.jwks"));
	}
	
	@Test
	public void testIsURLTrue(){
		assertTrue(testSubject.isURL("http://localhost:8080/keystore.jwks"));
		assertTrue(testSubject.isURL("https://localhost:8080/keystore.jwks"));
		assertTrue(testSubject.isURL("HTTP://localhost:8080/keystore.jwks"));
		assertTrue(testSubject.isURL("HTTPS://localhost:8080/keystore.jwks"));
	}
	
	@Test
	public void testGetJwtCredentials() throws NoSuchAlgorithmException, InvalidKeySpecException{
		
		RSAKey key = PowerMockito.mock(RSAKey.class);
		when(key.toRSAPrivateKey()).thenReturn(privateKey);
		
		List<JWK> keys = new ArrayList<>();
		keys.add(key);
		keys.add(key);
		when(jwkSet.getKeys()).thenReturn(keys);
		
		FHIRResourceSeverEndpointConfiguration endpointConf = new FHIRResourceSeverEndpointConfiguration();
		endpointConf.setAudience("audience");
		endpointConf.setClientId("client");
		endpointConf.setClientSecret("secret");
		endpointConf.setJwtDuration(1000l);
		
		JWTCredentials creds = testSubject.getJwtCredentials(jwkSet, endpointConf);
		
		assertEquals("audience", creds.getAudience());
		assertEquals("client", creds.getIssuer());
		assertEquals("client", creds.getSubject());
		assertEquals(1000L, creds.getDuration().longValue());
		
		
		verify(jwkSet).getKeys();
		verify(key).toRSAPrivateKey();
		
		assertNoMoreInteractions(key);
	}
	
	@Test
	public void testLoadJWKSetFromURL() throws IOException, ParseException{
		
		PowerMockito.mockStatic(JWKSet.class);
		when(JWKSet.load(any(URL.class),anyInt(),anyInt(),anyInt())).thenReturn(jwkSet);
		
		String jsonWebKeySetLocation = "http://localhost:8080/keyset";
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.JSON_WEB_KEYSET_SIZE_LIMIT, "10000");
		
		JWKSet response = testSubject.loadJWKSetFromURL(factoryConf, jsonWebKeySetLocation);
		
		assertEquals(jwkSet, response);
		assertNoMoreInteractions();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public void testLoadJWKSetFromURLWhenParseException() throws IOException, ParseException{
		
		PowerMockito.mockStatic(JWKSet.class);
		when(JWKSet.load(any(URL.class),anyInt(),anyInt(),anyInt())).thenThrow(ParseException.class);
		
		String jsonWebKeySetLocation = "http://localhost:8080/keyset";
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.JSON_WEB_KEYSET_SIZE_LIMIT, "10000");
		
		testSubject.loadJWKSetFromURL(factoryConf, jsonWebKeySetLocation);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public void testLoadJWKSetFromURLWhenIOException() throws IOException, ParseException{
		
		PowerMockito.mockStatic(JWKSet.class);
		when(JWKSet.load(any(URL.class),anyInt(),anyInt(),anyInt())).thenThrow(IOException.class);
		
		String jsonWebKeySetLocation = "http://localhost:8080/keyset";
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.JSON_WEB_KEYSET_SIZE_LIMIT, "10000");
		
		testSubject.loadJWKSetFromURL(factoryConf, jsonWebKeySetLocation);
	}
	
	
	@Test
	public void testLoadJWKSetFromFile(){
		
		String jsonWebKeySetLocation = "development.only.keystore.jwks";
		
		JWKSet jwkSet = testSubject.loadJWKSetFromFile(jsonWebKeySetLocation);
		
		assertNotNull(jwkSet);
		assertNotNull(jwkSet.getKeys());
		assertNotNull(jwkSet.getKeys().get(0));
		assertNotNull(jwkSet.getKeys().get(0).toPublicJWK());
		
		assertNoMoreInteractions();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLoadJWKSetFromFileWhenParseException(){
		
		String jsonWebKeySetLocation = "development.only.keystore.invalid.jwks";
		
		testSubject.loadJWKSetFromFile(jsonWebKeySetLocation);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalArgumentException.class)
	public void testLoadJWKSetFromFileWhenIOException() throws IOException, ParseException{
		
		PowerMockito.mockStatic(JWKSet.class);
		when(JWKSet.load(any(File.class))).thenThrow(IOException.class);
		
		String jsonWebKeySetLocation = "development.only.keystore.jwks";
		testSubject.loadJWKSetFromFile(jsonWebKeySetLocation);

		assertNoMoreInteractions();
	}
	
	public void testLoadJWKSetWhenLocationIsURL() throws IOException, ParseException{
		
		PowerMockito.mockStatic(JWKSet.class);
		when(JWKSet.load(any(URL.class),anyInt(),anyInt(),anyInt())).thenReturn(jwkSet);
		
		String jsonWebKeySetLocation = "http://localhost:8080/keyset";
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_READ_TIMEOUT_IN_MILLIS, "3000");
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.JSON_WEB_KEYSET_SIZE_LIMIT, "10000");
		
		JWKSet response = testSubject.loadJWKSet(factoryConf, jsonWebKeySetLocation);
		
		assertEquals(jwkSet, response);
		assertNoMoreInteractions();
	}
	
	public void testLoadJWKSetWhenLocationIsFile() throws IOException, ParseException{
		
		String jsonWebKeySetLocation = "development.only.keystore.jwks";
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		JWKSet jwkSet = testSubject.loadJWKSet(factoryConf,jsonWebKeySetLocation);
		
		assertNotNull(jwkSet);
		assertNotNull(jwkSet.getKeys());
		assertNotNull(jwkSet.getKeys().get(0));
		assertNotNull(jwkSet.getKeys().get(0).toPublicJWK());
		
		assertNoMoreInteractions();
	}
	
	@Test
	public void testBuildForClientSecrets(){
		FHIRResourceSeverEndpointConfiguration endpointConf = new FHIRResourceSeverEndpointConfiguration();
		endpointConf.setClientSecret("secret");
		
		// client secret creds
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		assertTrue(testSubject.build(factoryConf, endpointConf) instanceof ClientSecretCredentials);
		
		assertNoMoreInteractions();
	}
	
	@Test
	public void testBuildForJWTCredentials() throws IOException, ParseException{
		
		FHIRResourceSeverEndpointConfiguration endpointConf = new FHIRResourceSeverEndpointConfiguration();
		endpointConf.setJsonWebKeySetLocation("development.only.keystore.jwks");
		
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		Credentials<?> response = testSubject.build(factoryConf, endpointConf);
		
		assertTrue(response instanceof JWTCredentials);
		
		assertNoMoreInteractions();
	}
}
