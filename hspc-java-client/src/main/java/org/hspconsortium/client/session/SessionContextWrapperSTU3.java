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

import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Location;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;

public class SessionContextWrapperSTU3 implements SessionContext {

    protected Session session;

    protected PatientContextWrapperSTU3 patientContextWrapperSTU3;

    protected boolean enableCaching = true;

    private Patient priorPatient;

    private Encounter priorEncounter;

    private Location priorLocation;

    private IResource priorUserResource;

    private boolean localizeClaimUrl = true;

    public SessionContextWrapperSTU3(Session session) {
        this(session, true);
    }

    public SessionContextWrapperSTU3(Session session, boolean localizeClaimUrl) {
        this.session = session;
        this.localizeClaimUrl = localizeClaimUrl;
    }

    public boolean getLocalizeClaimUrl() {
        return localizeClaimUrl;
    }

    public void setLocalizeClaimUrl(boolean doLocalizeClaimUrl) {
        this.localizeClaimUrl = doLocalizeClaimUrl;
    }

    /**
     * @return The patient id, or null if there is no patient id in context
     */
    public String getPatientId() {
        // get this from the access token where it will be loaded if the user has requested a patient scope
        return session.getAccessToken().getPatientId();
    }

    /**
     * @return A patient context
     */
    public PatientContextWrapperSTU3 getPatientContext() {
        if (patientContextWrapperSTU3 == null && session.getContextSTU3().getPatientResource() != null) {
            patientContextWrapperSTU3 = new PatientContextWrapperSTU3(session);
        }
        return patientContextWrapperSTU3;
    }

    @Override
    public Patient getPatientResource() {
        String patientId = getPatientId();

        if (patientId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded getPatientContext?
                if (priorPatient != null && priorPatient.getId() != null) {
                    if (patientId.equals(priorPatient.getId())) {
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

    @Override
    public String getEncounterId() {
        return session.getAccessToken().getEncounterId();
    }

    @Override
    public Encounter getEncounterResource() {
        String encounterId = getEncounterId();

        if (encounterId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded encounter?
                if (priorEncounter != null && priorEncounter.getId() != null) {
                    if (encounterId.equals(priorEncounter.getId())) {
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

    @Override
    public String getLocationId() {
        return session.getAccessToken().getLocationId();
    }

    @Override
    public Location getLocationResource() {
        String locationId = getLocationId();

        if (locationId != null) {
            if (enableCaching) {
                // is the id matching the prior loaded location?
                if (priorLocation != null && priorLocation.getId() != null) {
                    if (locationId.equals(priorLocation.getId())) {
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

    @Override
    public String getIdTokenProfileClaim() {
        return session.getIdTokenProfileClaim();
    }

    @Override
    public IResource getCurrentUserResource() {
        String profileClaim = this.getIdTokenProfileClaim();
        if (profileClaim != null) {
            boolean doIt = localizeClaimUrl;
            if (!doIt) {
                String serverBase = extractClaimServerBase(profileClaim);
                doIt = (serverBase != null && serverBase.equalsIgnoreCase(session.getServerBase()));
            }
            if (doIt) {
                String[] claimParts = profileClaim.split("/");
                String resourceType = extractClaimResource(claimParts);
                String resourceId = extractClaimResourceId(claimParts);

                if (enableCaching) {
                    // is the id matching the prior loaded location?
                    if (priorUserResource != null && priorUserResource.getId() != null) {
                        if (resourceId.equals(priorUserResource.getId())) {
                            return priorUserResource;
                        }
                    }
                }

                if (!resourceId.isEmpty() && !resourceType.isEmpty()) {
                    priorUserResource = (IResource) session.read().resource(resourceType).withId(resourceId).execute();
                    return priorUserResource;
                }
            }
        } else {
            priorUserResource = null;
            return null;
        }
        return null;
    }

    private String extractClaimServerBase(String profileClaim) {
        try {
            if (profileClaim.contains("Practitioner")) {
                return profileClaim.substring(0, (profileClaim.indexOf("Practitioner") - 1));
            } else if (profileClaim.contains("Patient")) {
                return profileClaim.substring(0, (profileClaim.indexOf("Patient") - 1));
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return null;
    }

    private String extractClaimResource(String[] claimParts) {
        for (String token : claimParts) {
            if (token.equals("Practitioner")) {
                return token;
            } else if (token.equals("Patient")) {
                return token;
            }
        }
        throw new RuntimeException("ClaimResource not supported");
    }

    private String extractClaimResourceId(String[] claimParts) {
        boolean returnNext = false;
        for (String token : claimParts) {
            if (returnNext) {
                return token;
            }
            if (token.equals("Practitioner")) {
                // return the next token
                returnNext = true;
            } else if (token.equals("Patient")) {
                // return the next token
                returnNext = true;
            }
        }
        throw new RuntimeException("ClaimResource not supported");
    }

    @Override
    public IUntypedQuery search() {
        return session.search();
    }

    @Override
    public void setEnableCaching(boolean enableCaching) {
        this.enableCaching = enableCaching;
    }

    @Override
    public boolean getEnableCaching() {
        return enableCaching;
    }

    @Override
    public void resetCache() {
        this.priorPatient = null;
        this.priorEncounter = null;
        this.priorLocation = null;
    }

}
