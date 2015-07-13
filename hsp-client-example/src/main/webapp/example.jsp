<%--Copyright (c) 2015. Healthcare Services Platform Consortium. All Rights Reserved.--%>
<%@ page import="org.hspconsortium.client.impl.CodeFlowFhirClient" %>
<%@ page import="ca.uhn.fhir.model.api.Bundle" %>
<%@ page import="ca.uhn.fhir.model.dstu2.resource.Patient" %>
<%@ page import="ca.uhn.fhir.model.dstu2.resource.Observation" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.uhn.fhir.model.api.BundleEntry" %>
<%@ page import="ca.uhn.fhir.model.dstu2.composite.QuantityDt" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="ca.uhn.fhir.model.primitive.DateTimeDt" %>
<%@ page import="org.hspconsortium.client.FhirClient" %>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        th, td {
            padding: 5px;
            text-align: left;
        }
    </style>
    <title>Java Client Example</title>
</head>
<body>
<%
    FhirClient fhirClient = new CodeFlowFhirClient(request, "secret");
    String patientId = fhirClient.getFhirClientContext().getAccessToken().getPatientId();
    Patient patient = fhirClient.read().resource(Patient.class).withId(patientId).execute();

    Bundle results = fhirClient.search().forResource(Observation.class).where(
            Observation.SUBJECT.hasId(patientId)).
            and(Observation.CODE.exactly().identifier("8302-2")).execute();

%>
<h2><%= StringUtils.join(patient.getName().get(0).getGiven(), " ") + " " + patient.getName().get(0).getFamily().get(0) %></h2>

<div>
    <table id="observation_list">
        <tr><th>Date</th><th>Height</th> </tr>

    <%
    List<BundleEntry> entries = results.getEntries();
    for (BundleEntry entry : entries) {
%>
<tr>
    <td><%= ((DateTimeDt)((Observation)entry.getResource()).getApplies()).getValueAsString() %></td>
    <td><%= ((QuantityDt)((Observation)entry.getResource()).getValue()).getValue() %></td>
</tr>
<%
    }
%>
    </table>
</div>


</body>
</html>