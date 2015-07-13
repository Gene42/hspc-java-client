/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

public interface AccessTokenProvider {

    AccessToken getAccessToken(String tokenEndpointUrl, AccessTokenRequest request);

}
