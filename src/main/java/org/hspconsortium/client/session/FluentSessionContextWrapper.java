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

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Location;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;

public class FluentSessionContextWrapper {

    protected Session session;

    protected FluentPatientContextWrapper fluentPatientContextWrapper;

    protected boolean enableCaching = true;

    private Patient priorPatient;

    private Encounter priorEncounter;

    private Location priorLocation;

    private IResource priorUserResource;

    public FluentSessionContextWrapper(Session session) {
        this.session = session;
    }

    /**
     * @return The patient id, or null if there is no patient id in context
     */
    public String getPatientId() {
        // get this from the access token where it will be loaded if the user has requested a patient scope
        return session.getAccessToken().getPatientId();
    }

    /**
     * @return A fluent patient context
     */
    public FluentPatientContextWrapper getPatientContext() {
        if (fluentPatientContextWrapper == null && session.getContext().getPatientResource() != null) {
            fluentPatientContextWrapper = new FluentPatientContextWrapper(session);
        }
        return fluentPatientContextWrapper;
    }

    /**
     * @return The patient resource corresponding to the patient id in context.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    public Patient getPatientResource() {
        String patientId = getPatientId();

        if (patientId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded getPatientContext?
                if (priorPatient != null && priorPatient.getId() != null) {
                    if (patientId.equals(priorPatient.getId().getIdPart())) {
                        return priorPatient;
                    }
                }
            }
            // find the getPatientContext
            priorPatient = session.read().resource(Patient.class).withId(patientId).execute();
            return priorPatient;
        } else {
            priorPatient = null;
            return null;
        }
    }

    /**
     * @return The encounter id, or null if there is no encounter id in context
     */
    public String getEncounterId() {
        return session.getAccessToken().getEncounterId();
    }

    /**
     * @return The encounter resource corresponding to the encounter id in context.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    public Encounter getEncounterResource() {
        String encounterId = getEncounterId();

        if (encounterId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded encounter?
                if (priorEncounter != null && priorEncounter.getId() != null) {
                    if (encounterId.equals(priorEncounter.getId().getIdPart())) {
                        return priorEncounter;
                    }
                }
            }
            // find the encounter
            priorEncounter = session.read().resource(Encounter.class).withId(encounterId).execute();
            return priorEncounter;
        } else {
            priorEncounter = null;
            return null;
        }
    }

    /**
     * @return The location id, or null if there is no location id in context
     */
    public String getLocationId() {
        return session.getAccessToken().getLocationId();
    }

    /**
     * @return The location resource corresponding to the location id in context.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    public Location getLocationResource() {
        String locationId = getLocationId();

        if (locationId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded location?
                if (priorLocation != null && priorLocation.getId() != null) {
                    if (locationId.equals(priorLocation.getId().getIdPart())) {
                        return priorLocation;
                    }
                }
            }
            // find the location
            priorLocation = session.read().resource(Location.class).withId(locationId).execute();
            return priorLocation;
        } else {
            priorLocation = null;
            return null;
        }
    }

    /**
     * @return The profile for the current user, or null if there is no current user (this shouldn't happen)
     */
    public String getIdTokenProfileClaim() {
        return session.getIdTokenProfileClaim();
    }

    /**
     * @return The fhir resource corresponding to the current user.  If caching is enabled, this
     * resource will be remembered for subsequent calls, and not loaded fresh with each call.
     */
    public IResource getCurrentUserResource() {
        String profileClaim = this.getIdTokenProfileClaim();
        if (profileClaim != null) {

            String serverBase = session.getServerBase();
            String resourceType = "";
            String resourceId = "";
            if (profileClaim.startsWith(serverBase)) {
                String claim = profileClaim.substring(serverBase.length()+1);
                String[] claimParts = claim.split("/");
                resourceType = claimParts[0];
                resourceId = claimParts[1];
            }

            if (enableCaching) {

                // is the id matching the prior loaded location?
                if (priorUserResource != null && priorUserResource.getId() != null) {
                    if (resourceId.equals(priorUserResource.getId().getIdPart())) {
                        return priorUserResource;
                    }
                }
            }

            if (!resourceId.isEmpty() && !resourceType.isEmpty()) {
                priorUserResource = (IResource)session.read().resource(resourceType).withId(resourceId).execute();
                return priorUserResource;
            }
        } else {
            priorUserResource = null;
            return null;
        }
        return null;
    }

    /**
     * Provides access to HAPI search() operations for rich search API
     * @see <a href="http://jamesagnew.github.io/hapi-fhir/doc_rest_client.html">HAPI RESTful Client</a>
     * @return IUntypedQuery
     */
    public IUntypedQuery search() {
        return session.search();
    }

    /**
     * Caching allows the session getContext to remember values previously accessed in the session.
     * This helps with repeated calls to the getPatientContext, encounter, or location getContext.
     * However, changes to the object, either inside or outside the session, are not visible to
     * the cached version.
     *
     * @param enableCaching Enable caching?
     */
    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    /**
     * @return Caching enabled?
     */
    public boolean getEnableCaching() {
        return enableCaching;
    }

    /**
     * Clears the patient, encounter, and location cached objects so they will be read from the resource server
     * on the next request (if enabled)
     */
    public void resetCache() {
        this.priorPatient = null;
        this.priorEncounter = null;
        this.priorLocation = null;
    }

}
