package org.hspconsortium.client.operations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hspconsortium.client.operations.model.FHIRResourceSeverEndpointConfiguration;
import org.hspconsortium.client.operations.model.HSPCClientOperationsSession;
import org.hspconsortium.client.operations.types.HSPCClientOperationsFactoryConfigurationTypes;
import org.hspconsortium.client.operations.util.HSPCClientOperationsSessionFactory;
import org.hspconsortium.client.operations.util.NullChecker;
import org.hspconsortium.client.operations.validator.HSPCClientOperationsFactoryValidator;
import org.hspconsortium.client.session.Session;

public class HSPCClientOperationsFactory {

	private HSPCClientOperationsSession sessions;

	private PatientOperations patientResourceOperations;

	public HSPCClientOperationsFactory(FHIRResourceSeverEndpointConfiguration... fhirEndpointConfigurations) {
		new HSPCClientOperationsFactory(new HashMap<>(), fhirEndpointConfigurations);
	}

	public HSPCClientOperationsFactory(Map<HSPCClientOperationsFactoryConfigurationTypes, String> props, FHIRResourceSeverEndpointConfiguration... fhirEndpointConfigurations) {

		// validate configuration object for required fields.
		HSPCClientOperationsFactoryValidator.validateFHIRResourceServerEndpointConfiguration(fhirEndpointConfigurations);

		// add default connection properties if not provided by client
		addDefaultConnectionProperties(props);

		Map<String, Session> sessionsMap = new HashMap<>();
		Arrays.asList(fhirEndpointConfigurations).forEach(c -> {

			// load create session map for each endpoint
			sessionsMap.put(getSessionKey(c), HSPCClientOperationsSessionFactory.createFHIRResourceSession(props, c));
		});

		this.sessions = new HSPCClientOperationsSession(sessionsMap);
	}

	private String getSessionKey(FHIRResourceSeverEndpointConfiguration c) {
		return NullChecker.isNotNullish(c.getSessionKey()) ? c.getSessionKey() : c.getFhirResourceServerURL();
	}

	private void addDefaultConnectionProperties(Map<HSPCClientOperationsFactoryConfigurationTypes, String> props) {
		// iterate through keys
		for (HSPCClientOperationsFactoryConfigurationTypes key : HSPCClientOperationsFactoryConfigurationTypes.values()) {
			if (NullChecker.isNullish(props.get(key)))
				props.put(key, key.getDefaultValue());
		}
	}

	public PatientOperations getPatientOperations() {

		if (NullChecker.isNullish(patientResourceOperations))
			this.patientResourceOperations = new PatientOperations(this.sessions);

		return patientResourceOperations;
	}

}
