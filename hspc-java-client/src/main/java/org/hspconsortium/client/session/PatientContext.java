package org.hspconsortium.client.session;

import java.util.Collection;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.gclient.ICriterion;

/**
 * DESCRIPTION.
 *
 * @version $Id$
 */
public interface PatientContext {

    /**
     * Gets the patient resource.
     * @return Patient resource
     */
    IBaseResource get();

    /**
     * General purpose find.
     *
     * @param clazz     Resource class to find
     * @param criterion Criterion to be applied to the query.  Must include a patient matching criterion
     * @param <T>       Resource
     * @return A list of resource matching the criterion
     */
    <T extends IBaseResource> Collection<T> find(Class<T> clazz, ICriterion<?>... criterion);
}
