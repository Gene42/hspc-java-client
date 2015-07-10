/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.context;

import org.hspconsortium.client.auth.AccessToken;

public final class AuthContext {

    private final AccessToken bearerToken;

    public AuthContext(AccessToken bearerToken) {
        this.bearerToken = bearerToken;
    }

    public AccessToken getBearerToken() {
        return bearerToken;
    }

}
