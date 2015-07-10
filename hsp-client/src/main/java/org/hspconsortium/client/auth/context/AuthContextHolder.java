/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.context;

public class AuthContextHolder {

    private static ThreadLocal<AuthContext> authContextHolder = new ThreadLocal<AuthContext>();

    public static AuthContext getAuthContext(){
        return authContextHolder.get();
    }

    public static void setAuthContext(AuthContext authContext){
        authContextHolder.set(authContext);
    }

    public static void clearAuthContext(){
        authContextHolder.remove();
    }

}
