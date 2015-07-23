/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

public interface AuthorizationEndpointsProvider {
    AuthorizationEndpoints getAuthorizationEndpoints(String serviceUrl);
}
