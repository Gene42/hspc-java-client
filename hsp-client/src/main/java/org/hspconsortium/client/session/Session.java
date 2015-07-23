/*
 *
 *  * Copyright (c) 2014. Health Services Platform Consortium. All Rights Reserved.
 *
 */

package org.hspconsortium.client.session;

import org.hspconsortium.client.auth.AccessToken;

public class Session extends AbstractFhirSession {

    Session (String fhirServiceApi, AccessToken accessToken) {
        super(fhirServiceApi, accessToken);
    }

}
