package org.hspconsortium.client.operations.builder;

import java.util.Map;

import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.NullChecker;

public class ClientOperationsBuilderBase {

	public int getConfAsInteger(Map<HSPCClientOperationsFactoryConfigurationTypes, String> factoryConf, HSPCClientOperationsFactoryConfigurationTypes conf) {
		try {
			
			if(NullChecker.isNullish(factoryConf.get(conf)))
				return -1;
			
			return Integer.parseInt(factoryConf.get(conf));
		} catch (NumberFormatException ex) {
			throw new NumberFormatException("Invalid non numeric value:" + factoryConf.get(conf) + " for property: " + conf.name());
		}
	}
}
