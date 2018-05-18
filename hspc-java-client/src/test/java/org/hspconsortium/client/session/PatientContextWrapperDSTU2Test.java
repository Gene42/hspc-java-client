/*
 * #%L
 * Health Services Platform Consortium - HSPC Client
 * %%
 * Copyright (C) 2014 - 2015 Healthcare Services Platform Consortium
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.hspconsortium.client.session;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PatientContextWrapperDSTU2Test {

    private Session mockSession;
    private SessionContextWrapperDSTU2 mockSessionContextWrapperDSTU2;
    private Patient mockPatient;
    private String patientId = "patientId";
    private Observation mockObservation;
    private IUntypedQuery mockUntypedQuery;
    private IQuery mockQuery;
    private IClientExecutable mockClientExecutable;
    private Bundle mockBundle;
    private Bundle.Entry mockBundleEntryConponent;
    private IdDt mockIdElement;

    @Before
    public void setUp() {
        mockSession = mock(Session.class);
        mockSessionContextWrapperDSTU2 = mock(SessionContextWrapperDSTU2.class);
        mockPatient = mock(Patient.class);
        mockObservation = mock(Observation.class);
        mockUntypedQuery = mock(IUntypedQuery.class);
        mockQuery = mock(IQuery.class);
        mockClientExecutable = mock(IClientExecutable.class);
        mockBundle = mock(Bundle.class);
        mockBundleEntryConponent = mock(Bundle.Entry.class);
        mockIdElement = new IdDt(patientId);

        when(mockPatient.getIdElement()).thenReturn(mockIdElement);
        when(mockPatient.getId()).thenReturn(new IdDt(patientId));
        when(mockSession.getContextDSTU2()).thenReturn(mockSessionContextWrapperDSTU2);
        when(mockSessionContextWrapperDSTU2.getPatientResource()).thenReturn(mockPatient);
        when(mockSession.search()).thenReturn(mockUntypedQuery);
        when(mockUntypedQuery.forResource((any(Class.class)))).thenReturn(mockQuery);
        when(mockQuery.and(any(ICriterion.class))).thenReturn(mockQuery);
        when(mockQuery.returnBundle(any(Class.class))).thenReturn(mockClientExecutable);
        when(mockClientExecutable.execute()).thenReturn(mockBundle);

        when(mockBundleEntryConponent.getResource()).thenReturn(mockObservation);
        List<Bundle.Entry> bundleEntries = new ArrayList<>();
        bundleEntries.add(mockBundleEntryConponent);
        when(mockBundle.getEntry()).thenReturn(bundleEntries);
    }

    @Test
    public void testGet() {
        PatientContextWrapperDSTU2 patientContextWrapperDSTU2 = new PatientContextWrapperDSTU2(mockSession);

        assertEquals(mockPatient, patientContextWrapperDSTU2.get());
    }

    @Test
    public void testFindNoParams() {
        PatientContextWrapperDSTU2 patientContextWrapperDSTU2 = new PatientContextWrapperDSTU2(mockSession);

        assertEquals(mockObservation, patientContextWrapperDSTU2.find(Observation.class).iterator().next());
    }

    @Test
    public void testFindWithParams() {
        PatientContextWrapperDSTU2 patientContextWrapperDSTU2 = new PatientContextWrapperDSTU2(mockSession);

        assertEquals(mockObservation,
                patientContextWrapperDSTU2
                        .find(Observation.class, Observation.CODE.exactly().identifier("8302-2"))
                        .iterator().next());
    }
}