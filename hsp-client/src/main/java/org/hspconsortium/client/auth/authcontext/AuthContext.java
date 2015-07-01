/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.authcontext;

import org.hspconsortium.client.auth.AccessToken;

public final class AuthContext {

    private AccessToken bearerToken;

    public AuthContext(AccessToken bearerToken) {
        this.bearerToken = bearerToken;
    }

    public AccessToken getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(AccessToken bearerToken) {
        this.bearerToken = bearerToken;
    }

}
