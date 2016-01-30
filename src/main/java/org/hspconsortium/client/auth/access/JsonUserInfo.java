/*
 * #%L
 * hspc-client
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
package org.hspconsortium.client.auth.access;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class JsonUserInfo implements UserInfo {

    private JsonObject rootResponse;

    private final String sub;

    private final String name;

    private final String preferredUsername;

    public JsonUserInfo(JsonObject rootResponse, String sub, String name, String preferredUsername) {
        Validate.notNull(rootResponse, "The rootResponse must not be null");
        this.rootResponse = rootResponse;
        this.sub = sub;
        this.name = name;
        this.preferredUsername = preferredUsername;
    }

    @Override
    public JsonObject getRootResponse() {
        return rootResponse;
    }

    @Override
    public String getSub() {
        return sub;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPreferredUsername() {
        return preferredUsername;
    }

    @Override
    public List<NameValuePair> asNameValuePairList() {
        // create a list of all the non-null values to be transferred to the refresh token
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        if (StringUtils.isNoneEmpty(sub)) {
            nameValuePairs.add(new BasicNameValuePair(SUB, sub));
        }

        if (StringUtils.isNoneEmpty(name)) {
            nameValuePairs.add(new BasicNameValuePair(NAME, name));
        }

        if (StringUtils.isNoneEmpty(preferredUsername)) {
            nameValuePairs.add(new BasicNameValuePair(PREFERRED_USERNAME, preferredUsername));
        }

        return nameValuePairs;
    }
}
