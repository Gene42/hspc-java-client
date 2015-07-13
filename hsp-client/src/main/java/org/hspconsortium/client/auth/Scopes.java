/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client.auth;

import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class Scopes {

    private List<String> scopes = new ArrayList<String>();

    public Scopes add(Scope scope){
        this.scopes.add(scope.asStringValue());
        return this;
    }

    public String asParamValue(){
        return StringUtils.join(scopes, " ");
    }
}
