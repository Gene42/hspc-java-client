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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FluentPatientContextWrapperDSTU2 {

    private Session session;

    public FluentPatientContextWrapperDSTU2(Session session) {
        this.session = session;
        if (session.getContextDSTU2().getPatientResource() == null) {
            throw new RuntimeException("Cannot have PatientContextWrapper without Patient");
        }
    }

    /**
     * Gets the patient resource
     * @return Patient resource
     */
    public Patient get() {
        return session.getContextDSTU2().getPatientResource();
    }

    /**
     * General purpose fluent find
     *
     * @param clazz     Resource class to find
     * @param criterion Criterion to be applied to the query.  Must include a patient matching criterion
     * @param <T>       Resource
     * @return A list of resource matching the criterion
     */
    public <T extends IResource> Collection<T> find(Class<T> clazz, ICriterion<?>... criterion) {
        String patientId = session.getContextDSTU2().getPatientResource().getIdElement().getIdPart();
        IQuery<ca.uhn.fhir.model.api.Bundle> queryBuilder = session.search().forResource(clazz);

        // use reflection to match the getPatientContext criterion
        queryBuilder.where(findPatientReferenceOnResource(clazz).hasId(patientId));

        for (ICriterion<?> theCriterion : criterion) {
            queryBuilder = queryBuilder.and(theCriterion);
        }

        IClientExecutable<IQuery<Bundle>, Bundle> results = queryBuilder.returnBundle(Bundle.class);

        return asCollection(results.execute());
    }

    private <T extends IResource> Collection<T> asCollection(Bundle results) {
        List<T> list = new ArrayList<>();
        for (Bundle.Entry entry : results.getEntry()) {
            list.add((T) entry.getResource());
        }
        return list;
    }

    private ReferenceClientParam findPatientReferenceOnResource(Class<? extends IResource> clazz) {
        try {
            Field field = clazz.getDeclaredField("PATIENT");
            return (ReferenceClientParam) field.get(ReferenceClientParam.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Resource: [" + clazz + "] does not have a PATIENT field", e);
        }
    }
}
