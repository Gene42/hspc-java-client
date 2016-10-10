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

package org.hspconsortium.client.session;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.rest.server.EncodingEnum;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseConformance;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hspconsortium.client.auth.access.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractFhirSession implements FhirSession {

    protected final FhirContext hapiFhirContext;
    protected final IGenericClient client;
    protected AccessToken accessToken;
    protected final AccessTokenRequest refreshTokenRequest;
    protected final String tokenEndpoint;
    protected final UserInfo userInfo;
    protected IClientInterceptor clientInterceptor;

    protected AccessTokenProvider accessTokenProvider;

    public AbstractFhirSession(FhirContext hapiFhirContext, String fhirServiceApi, AccessToken accessToken,
                               UserInfo userInfo, AccessTokenRequest refreshTokenRequest, String tokenEndpoint) {
        this.hapiFhirContext = hapiFhirContext;
        Validate.notNull(fhirServiceApi, "the fhirServiceApi must not be null");
        Validate.notNull(accessToken, "the accessToken must not be null");

        this.accessToken = accessToken;
        this.refreshTokenRequest = refreshTokenRequest;
        this.tokenEndpoint = tokenEndpoint;
        this.client = this.hapiFhirContext.newRestfulGenericClient(fhirServiceApi);
        if (refreshTokenRequest == null) {
            this.client.registerInterceptor(new BearerTokenAuthInterceptor(accessToken.getValue()));
        } else {
            this.client.registerInterceptor(new AutoRefreshingBearerTokenAuthorizationHeaderInterceptor(30));
        }
        this.userInfo = userInfo;
    }

    public void setAccessTokenProvider(JsonAccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public String getIdTokenProfileClaim() {
        AccessToken accessToken = this.getAccessToken();
        if (accessToken instanceof JsonAccessToken) {
            Map<String, Object> claims = ((JsonAccessToken)accessToken).getIdToken().getClaimsMap();
            return (String)claims.get("profile");
        }
        return null;
    }

    @Override
    public FhirContext getFhirContext() {
        return this.hapiFhirContext;
    }

    @Override
    public IHttpClient getHttpClient() {
        return this.client.getHttpClient();
    }

    @Override
    public void setEncoding(EncodingEnum encodingEnum) {
        this.client.setEncoding(encodingEnum);
    }

    @Override
    public void setPrettyPrint(Boolean aBoolean) {
        this.client.setPrettyPrint(aBoolean);
    }

    @Override
    public String getServerBase() {
        return this.client.getServerBase();
    }

    @Override
    public IBaseConformance conformance() {
        return this.client.conformance();
    }

    @Override
    public IFetchConformanceUntyped fetchConformance() {
        return this.client.fetchConformance();
    }

    @Override
    public ICreate create() {
        return this.client.create();
    }

    @Override
    @Deprecated
    public MethodOutcome create(IBaseResource theResource) {
        return client.create(theResource);
    }

    @Override
    public IDelete delete() {
        return this.client.delete();
    }

    @Override
    @Deprecated
    public MethodOutcome delete(Class<? extends IBaseResource> theType, IdDt theId) {
        return client.delete(theType, theId);
    }

    @Override
    @Deprecated
    public MethodOutcome delete(Class<? extends IBaseResource> theType, String theId) {
        return client.delete(theType, theId);
    }

    @Override
    public void forceConformanceCheck() throws FhirClientConnectionException {
        this.client.forceConformanceCheck();
    }

    @Override
    public IGetTags getTags() {
        return this.client.getTags();
    }

    @Override
    public IHistory history() {
        return this.client.history();
    }

    @Override
    @Deprecated
    public <T extends IBaseResource> Bundle history(Class<T> theType, IdDt theId, DateTimeDt theSince, Integer theLimit) {
        return client.history(theType, theId, theSince, theLimit);
    }

    @Override
    @Deprecated
    public <T extends IBaseResource> Bundle history(Class<T> theType, String theId, DateTimeDt theSince, Integer theLimit) {
        return client.history(theType, theId, theSince, theLimit);
    }

    @Override
    public IGetPage loadPage() {
        return this.client.loadPage();
    }

    @Override
    public IOperation operation() {
        return this.client.operation();
    }

    @Override
    public IRead read() {
        return this.client.read();
    }

    @Override
    public <T extends IBaseResource> T read(Class<T> tClass, String s) {
        return this.client.read(tClass, s);
    }

    @Override
    public <T extends IBaseResource> T read(Class<T> tClass, UriDt uriDt) {
        return this.client.read(tClass, uriDt);
    }

    @Override
    public IBaseResource read(UriDt uriDt) {
        return this.client.read(uriDt);
    }

    @Override
    public void registerInterceptor(IClientInterceptor iClientInterceptor) {
        this.client.registerInterceptor(iClientInterceptor);
    }

    @Override
    public IUntypedQuery search() {
        return this.client.search();
    }

    @Override
    public <T extends IBaseResource> Bundle search(Class<T> tClass, Map<String, List<IQueryParameterType>> stringListMap) {
        return this.client.search(tClass, stringListMap);
    }

    @Override
    public <T extends IBaseResource> Bundle search(Class<T> tClass, UriDt uriDt) {
        return this.client.search(tClass, uriDt);
    }

    @Override
    public Bundle search(UriDt uriDt) {
        return this.client.search(uriDt);
    }

    @Override
    public void setLogRequestAndResponse(boolean b) {
        this.client.setLogRequestAndResponse(b);
    }

    @Override
    public ITransaction transaction() {
        return this.client.transaction();
    }

    @Override
    public IValidate validate() {
        return this.client.validate();
    }

    @Override
    public List<IBaseResource> transaction(List<IBaseResource> iBaseResources) {
        return this.client.transaction(iBaseResources);
    }

    @Override
    public void unregisterInterceptor(IClientInterceptor iClientInterceptor) {
        this.client.unregisterInterceptor(iClientInterceptor);
    }

    @Override
    public IUpdate update() {
        return this.client.update();
    }

    @Override
    public MethodOutcome update(IdDt theId, IBaseResource theResource) {
        return client.update(theId, theResource);
    }

    @Override
    public MethodOutcome update(String theId, IBaseResource theResource) {
        return client.update(theId, theResource);
    }

    @Override
    public MethodOutcome validate(IBaseResource theResource) {
        return client.validate(theResource);
    }

    @Override
    public <T extends IBaseResource> T vread(Class<T> tClass, IdDt idDt) {
        return this.client.vread(tClass, idDt);
    }

    @Override
    @Deprecated
    public <T extends IBaseResource> T vread(Class<T> theType, IdDt theId, IdDt theVersionId) {
        return client.vread(theType, theId, theVersionId);
    }

    @Override
    public <T extends IBaseResource> T vread(Class<T> tClass, String s, String s1) {
        return this.client.vread(tClass, s, s1);
    }

    @Override
    public <T extends IBaseResource> T fetchResourceFromUrl(Class<T> aClass, String s) {
        return this.client.fetchResourceFromUrl(aClass, s);
    }

    @Override
    public IMeta meta() {
        return this.client.meta();
    }



    private class AutoRefreshingBearerTokenAuthorizationHeaderInterceptor implements IClientInterceptor {

        private final int refreshThreshold;

        public AutoRefreshingBearerTokenAuthorizationHeaderInterceptor(int refreshThreshold) {
            this.refreshThreshold = refreshThreshold;
        }

        @Override
        public void interceptRequest(IHttpRequest theRequest) {
            if ((accessToken.getExpiresIn() <= refreshThreshold)) {
                accessToken = accessTokenProvider.refreshAccessToken(tokenEndpoint, refreshTokenRequest, accessToken);
            }
            theRequest.addHeader("Authorization", String.format("Bearer %s", accessToken.getValue()));
        }

        @Override
        public void interceptResponse(IHttpResponse theResponse) throws IOException {

        }

    }
}
