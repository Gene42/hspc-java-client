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

import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FluentPatientContextWrapperSTU3Test {

    private Session mockSession;
    private FluentSessionContextWrapperSTU3 mockFluentSessionContextWrapperSTU3;
    private Patient mockPatient;
    private String patientId = "patientId";
    private Observation mockObservation;
    private IUntypedQuery mockUntypedQuery;
    private IQuery mockQuery;
    private IClientExecutable mockClientExecutable;
    private Bundle mockBundle;
    private Bundle.BundleEntryComponent mockBundleEntryConponent;
    private IdType mockIdElement;

    @Before
    public void setUp() {
        mockSession = mock(Session.class);
        mockFluentSessionContextWrapperSTU3 = mock(FluentSessionContextWrapperSTU3.class);
        mockPatient = mock(Patient.class);
        mockObservation = mock(Observation.class);
        mockUntypedQuery = mock(IUntypedQuery.class);
        mockQuery = mock(IQuery.class);
        mockClientExecutable = mock(IClientExecutable.class);
        mockBundle = mock(Bundle.class);
        mockBundleEntryConponent = mock(Bundle.BundleEntryComponent.class);
        mockIdElement = new IdType(patientId);

        when(mockPatient.getIdElement()).thenReturn(mockIdElement);
        when(mockPatient.getId()).thenReturn(patientId);
        when(mockSession.getContextSTU3()).thenReturn(mockFluentSessionContextWrapperSTU3);
        when(mockFluentSessionContextWrapperSTU3.getPatientResource()).thenReturn(mockPatient);
        when(mockSession.search()).thenReturn(mockUntypedQuery);
        when(mockUntypedQuery.forResource((any(Class.class)))).thenReturn(mockQuery);
        when(mockQuery.and(any(ICriterion.class))).thenReturn(mockQuery);
        when(mockQuery.returnBundle(any(Class.class))).thenReturn(mockClientExecutable);
        when(mockClientExecutable.execute()).thenReturn(mockBundle);

        when(mockBundleEntryConponent.getResource()).thenReturn(mockObservation);
        List<Bundle.BundleEntryComponent> bundleEntries = new ArrayList<>();
        bundleEntries.add(mockBundleEntryConponent);
        when(mockBundle.getEntry()).thenReturn(bundleEntries);
    }

    @Test
    public void testGet() {
        FluentPatientContextWrapperSTU3 fluentPatientContextWrapperSTU3 = new FluentPatientContextWrapperSTU3(mockSession);

        assertEquals(mockPatient, fluentPatientContextWrapperSTU3.get());
    }

    @Test
    public void testFindNoParams() {
        FluentPatientContextWrapperSTU3 fluentPatientContextWrapperSTU3 = new FluentPatientContextWrapperSTU3(mockSession);

        assertEquals(mockObservation, fluentPatientContextWrapperSTU3.find(Observation.class).iterator().next());
    }

    @Test
    public void testFindWithParams() {
        FluentPatientContextWrapperSTU3 fluentPatientContextWrapperSTU3 = new FluentPatientContextWrapperSTU3(mockSession);

        assertEquals(mockObservation,
                fluentPatientContextWrapperSTU3
                        .find(Observation.class, Observation.CODE.exactly().identifier("8302-2"))
                        .iterator().next());
    }
}