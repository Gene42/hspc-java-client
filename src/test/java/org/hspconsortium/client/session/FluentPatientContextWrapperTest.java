/*
 * #%L
 * Health Services Platform Consortium - HSP Client
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

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
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

public class FluentPatientContextWrapperTest {

    private Session mockSession;
    private FluentSessionContextWrapper mockFluentSessionContextWrapper;
    private Patient mockPatient;
    private IdDt mockPatientId;
    private String patientId = "patientId";
    private Observation mockObservation;
    private IUntypedQuery mockUntypedQuery;
    private IQuery mockQuery;
    private Bundle mockBundle;
    private BundleEntry mockBundleEntry;

    @Before
    public void setUp() {
        mockSession = mock(Session.class);
        mockFluentSessionContextWrapper = mock(FluentSessionContextWrapper.class);
        mockPatient = mock(Patient.class);
        mockPatientId = mock(IdDt.class);
        mockObservation = mock(Observation.class);
        mockUntypedQuery = mock(IUntypedQuery.class);
        mockQuery = mock(IQuery.class);
        mockBundle = mock(Bundle.class);
        mockBundleEntry = mock(BundleEntry.class);

        when(mockPatient.getId()).thenReturn(mockPatientId);
        when(mockPatientId.getIdPart()).thenReturn(patientId);
        when(mockSession.getContext()).thenReturn(mockFluentSessionContextWrapper);
        when(mockFluentSessionContextWrapper.getPatientResource()).thenReturn(mockPatient);
        when(mockSession.search()).thenReturn(mockUntypedQuery);
        when(mockUntypedQuery.forResource((any(Class.class)))).thenReturn(mockQuery);
        when(mockQuery.and(any(ICriterion.class))).thenReturn(mockQuery);
        when(mockQuery.execute()).thenReturn(mockBundle);
        when(mockBundleEntry.getResource()).thenReturn(mockObservation);
        List<BundleEntry> bundleEntries = new ArrayList<>();
        bundleEntries.add(mockBundleEntry);
        when(mockBundle.getEntries()).thenReturn(bundleEntries);
    }

    @Test
    public void testGet() {
        FluentPatientContextWrapper fluentPatientContextWrapper = new FluentPatientContextWrapper(mockSession);

        assertEquals(mockPatient, fluentPatientContextWrapper.get());
    }

    @Test
    public void testFindNoParams() {
        FluentPatientContextWrapper fluentPatientContextWrapper = new FluentPatientContextWrapper(mockSession);

        when(mockQuery.where(any(ICriterion.class))).thenReturn(mockPatient);

        assertEquals(mockObservation, fluentPatientContextWrapper.find(Observation.class).iterator().next());
    }

    @Test
    public void testFindWithParams() {
        FluentPatientContextWrapper fluentPatientContextWrapper = new FluentPatientContextWrapper(mockSession);

        when(mockQuery.where(any(ICriterion.class))).thenReturn(mockPatient);
        //

        assertEquals(mockObservation,
                fluentPatientContextWrapper
                        .find(Observation.class, Observation.CODE.exactly().identifier("8302-2"))
                        .iterator().next());
    }

}