package org.hspconsortium.client.operations;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.hspconsortium.client.operations.common.Constants;
import org.hspconsortium.client.operations.model.HSPCClientOperationsSession;
import org.hspconsortium.client.session.Session;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;

public class PatientOperations extends OperationsBase{
	
	public PatientOperations(HSPCClientOperationsSession session){
		super(session);
	}
	
	public List<Patient> getPatientById(String patientId, String... targetSessionKeys){
		
		List<Session> sessions = getSession().getFilteredSessions(targetSessionKeys);
	
		ConcurrentLinkedQueue<Patient> patients = new ConcurrentLinkedQueue<Patient>();
	
	    sessions.parallelStream().forEach(s -> patients.add(s.read().resource(Patient.class).withId(patientId).execute()));
	
	    return Arrays.asList(patients.toArray(new Patient[patients.size()])); 
	}
	
	public List<Entry> getActiveEncountersForPatientId(String patientId, String... targetSessionKeys){
		List<Session> sessions = getSession().getFilteredSessions(targetSessionKeys);
		
		ConcurrentLinkedQueue<Entry> encounterBundles = new ConcurrentLinkedQueue<>();
		
	    sessions.parallelStream().forEach(s -> encounterBundles.addAll(s.search().forResource(Encounter.class).where(new ReferenceClientParam(Constants.SEARCH_PARAM_PATIENT).hasId(patientId)).returnBundle(Bundle.class).execute().getEntry()));
	
	   return Arrays.asList(encounterBundles.toArray(new Entry[encounterBundles.size()])).parallelStream().filter(e -> ((Encounter)e.getResource()).getStatus().equalsIgnoreCase(Constants.ACTIVE_ENCOUNTER_STATUS)).collect(Collectors.toList());
	}
	
}
