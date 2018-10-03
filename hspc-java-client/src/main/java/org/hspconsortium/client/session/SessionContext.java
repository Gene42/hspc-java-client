package org.hspconsortium.client.session;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;

/**
 * DESCRIPTION.
 *
 * @version $Id$
 */
public interface SessionContext {

    /**
     * @return The patient id, or null if there is no patient id in context
     */
    String getPatientId();

    /**
     * @return The patient resource corresponding to the patient id in context.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    IBaseResource getPatientResource();

    /**
     * @return A patient context
     */
    PatientContext getPatientContext();

    /**
     * @return The encounter id, or null if there is no encounter id in context
     */
    String getEncounterId();

    /**
     * @return The encounter resource corresponding to the encounter id in context.  If caching is enabled, this
     *         resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    IBaseResource getEncounterResource();

    /**
     * @return The location id, or null if there is no location id in context
     */
    String getLocationId();

    /**
     * @return The location resource corresponding to the location id in context.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    IBaseResource getLocationResource();

    /**
     * @return The profile for the current user, or null if there is no current user (this shouldn't happen)
     */
    String getIdTokenProfileClaim();

    /**
     * @return The fhir resource corresponding to the current user.  If caching is enabled, this
     *         resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    IResource getCurrentUserResource();


    /**
     * Provides access to HAPI search() operations for rich search API.
     *
     * @return IUntypedQuery
     * @see <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client.html">HAPI RESTful Client</a>
     */
    IUntypedQuery search();

    /**
     * Caching allows the session getContextDSTU2 to remember values previously accessed in the session.
     * This helps with repeated calls to the getPatientContext, encounter, or location getContextDSTU2.
     * However, changes to the object, either inside or outside the session, are not visible to
     * the cached version.
     *
     * @param enableCaching Enable caching?
     */
    void setEnableCaching(boolean enableCaching);

    /**
     * @return Caching enabled?
     */
    boolean getEnableCaching();

    /**
     * Clears the patient, encounter, and location cached objects so they will be read from the resource server
     * on the next request (if enabled).
     */
    void resetCache();
}
