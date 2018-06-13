package org.hspconsortium.client.operations.builders;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.builder.ClientOperationsBuilderBase;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClientOperationBuilderBaseTest {

	private ClientOperationsBuilderBase testSubject = new ClientOperationsBuilderBase();
	
	@Test
	public void testGetConfAsIntegerWhenNull(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		assertEquals(-1,testSubject.getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetConfAsIntegerWhenInvalid(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000ABCD");
		testSubject.getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS);
	}
	
	@Test
	public void testGetConfAsIntegerWhenValid(){
		Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf = new HashMap<>();
		factoryConf.put(HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS, "1000");
		testSubject.getConfAsInteger(factoryConf, HSPCClientOperationsFactoryConfigurationTypes.HTTP_CONNECTION_TIMEOUT_IN_MILLIS);
	}
}
