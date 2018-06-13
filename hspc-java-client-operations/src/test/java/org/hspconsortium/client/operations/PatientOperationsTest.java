package org.hspconsortium.client.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.hspconsortium.client.operations.model.HSPCClientOperationsSession;
import org.hspconsortium.client.session.Session;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.gclient.IRead;
import ca.uhn.fhir.rest.gclient.IReadExecutable;
import ca.uhn.fhir.rest.gclient.IReadTyped;

@RunWith(MockitoJUnitRunner.class)
public class PatientOperationsTest {

	@InjectMocks
	private PatientOperations testSubject;
	
	@Mock
	private HSPCClientOperationsSession hspcclientOperationsSession;
	
	@Mock
	private Session session;
	
	@Mock
	private IRead read;
	
	@Mock
	private IReadTyped<Patient> patientIReadTyped;
	
	@Mock
	private IReadExecutable<Patient> patientIReadExecutable;
	
	private void assertNoMoreInteractions(){
		verifyNoMoreInteractions(hspcclientOperationsSession,session,read,patientIReadTyped,patientIReadExecutable);
	}
	
	@Test
	public void testGetPatientId(){
		
		Patient p = new Patient();
		
		List<Session> sessions = new ArrayList<>();
		sessions.add(session);
		
		when(hspcclientOperationsSession.getFilteredSessions("session#1")).thenReturn(sessions);
		when(session.read()).thenReturn(read);
		when(read.resource(Patient.class)).thenReturn(patientIReadTyped);
		when(patientIReadTyped.withId("12345")).thenReturn(patientIReadExecutable);
		when(patientIReadExecutable.execute()).thenReturn(p);
		
		List<Patient> result = testSubject.getPatientById("12345", "session#1");
		assertEquals(1, result.size());
		assertEquals(p,result.get(0));
		
		verify(hspcclientOperationsSession).getFilteredSessions("session#1");
		verify(session).read();
		verify(read).resource(Patient.class);
		verify(patientIReadTyped).withId("12345");
		verify(patientIReadExecutable).execute();
		
		assertNoMoreInteractions();
	}
}
