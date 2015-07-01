/*
 * Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.
 */

package org.hspconsortium.client.auth.authcontext;

public class AuthContextHolder {

    private static ThreadLocal<AuthContext> authContextHolder = new ThreadLocal<AuthContext>();

    public static AuthContext getAuthContext(){
        AuthContext authContext = authContextHolder.get();
        if(authContext == null){
            authContextHolder.set(authContext);
        }
        return authContext;
    }

    public static void setAuthContext(AuthContext authContext){
        authContextHolder.set(authContext);
    }

    public static void clearAuthContext(){
        authContextHolder.remove();
    }

}
