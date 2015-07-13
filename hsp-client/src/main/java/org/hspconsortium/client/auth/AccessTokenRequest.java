/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

import java.util.Map;

public interface AccessTokenRequest {
    public String getClientId();
    public String getClientSecret();
    public Map<String, String> getParameters();
}
