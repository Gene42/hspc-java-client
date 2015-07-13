/*
 * Copyright (c) 2015. Health Services Platform Consortium. All Rights Reserved.
 */
package org.hspconsortium.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.resource.BaseConformance;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;
import ca.uhn.fhir.rest.gclient.*;
import ca.uhn.fhir.rest.server.EncodingEnum;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hspconsortium.client.auth.AccessToken;
import org.hspconsortium.client.auth.context.FhirClientContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractFhirClient implements FhirClient  {

    protected final FhirContext hapiFhirContext = FhirContext.forDstu2();
    protected IGenericClient client;
    protected FhirClientContext fhirClientContext;

    @Override
    public void setFhirClientContext(FhirClientContext fhirClientContext) {
        this.fhirClientContext = fhirClientContext;
    }

    @Override
    public FhirClientContext getFhirClientContext() {
        return this.fhirClientContext;
    }

    @Override
    public FhirContext getFhirContext() {
        return this.hapiFhirContext;
    }

    @Override
    public HttpClient getHttpClient() {
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
    public BaseConformance conformance() {
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
    public MethodOutcome create(IResource iResource) {
        return this.client.create(iResource);
    }

    @Override
    public IDelete delete() {
        return this.client.delete();
    }

    @Override
    public MethodOutcome delete(Class<? extends IResource> aClass, IdDt idDt) {
        return this.client.delete(aClass, idDt);
    }

    @Override
    public MethodOutcome delete(Class<? extends IResource> aClass, String s) {
        return this.client.delete(aClass, s);
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
    public <T extends IResource> Bundle history(Class<T> tClass, IdDt idDt, DateTimeDt dateTimeDt, Integer integer) {
        return this.client.history(tClass, idDt, dateTimeDt, integer);
    }

    @Override
    public <T extends IResource> Bundle history(Class<T> tClass, String s, DateTimeDt dateTimeDt, Integer integer) {
        return this.client.history(tClass, s, dateTimeDt, integer);
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
    public MethodOutcome update(IdDt idDt, IResource iResource) {
        return this.client.update(idDt, iResource);
    }

    @Override
    public MethodOutcome update(String s, IResource iResource) {
        return this.client.update(s, iResource);
    }

    @Override
    public MethodOutcome validate(IResource iResource) {
        return this.client.validate(iResource);
    }

    @Override
    public <T extends IBaseResource> T vread(Class<T> tClass, IdDt idDt) {
        return this.client.vread(tClass, idDt);
    }

    @Override
    public <T extends IResource> T vread(Class<T> tClass, IdDt idDt, IdDt idDt1) {
        return this.client.vread(tClass, idDt, idDt1);
    }

    @Override
    public <T extends IBaseResource> T vread(Class<T> tClass, String s, String s1) {
        return this.client.vread(tClass, s, s1);
    }

    protected static class BearerTokenAuthorizationHeaderInterceptor implements IClientInterceptor {

        private final AccessToken accessToken;

        public BearerTokenAuthorizationHeaderInterceptor(AccessToken accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void interceptRequest(HttpRequestBase httpRequestBase) {

            httpRequestBase.addHeader("Authorization", String.format("Bearer %s", accessToken.getValue()));
        }

        @Override
        public void interceptResponse(HttpResponse response) throws IOException {

        }
    }
}
