package org.hspconsortium.client.controller;

public interface FhirEndpointsProvider {
    FhirEndpoints getEndpoints(String serviceUrl);
}
