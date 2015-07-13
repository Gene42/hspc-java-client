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
    // NOTE: The following business logic code would normally be found in the controller,
    //       which would populate a model and only the model would be provided to the view.
    //       We have not separated the Model, View and Controller in order to show all the
    //       code in one location.

    // Create a CodeFlowFhirClient for querying the Fhir Service
    FhirClient fhirClient = new CodeFlowFhirClient(request, "secret");

    // Retrieve the patientId from the FhirClientContext. The patientId is returned to the
    // App along with the Access Token
    String patientId = fhirClient.getFhirClientContext().getAccessToken().getPatientId();

    // Use the CodeFlowFhirClient to read the Patient info
    Patient patient = fhirClient.read().resource(Patient.class).withId(patientId).execute();

    // Use the CodeFlowFhirClient to search for Observations for the Patient
    Bundle results = fhirClient.search().forResource(Observation.class).where(
            Observation.SUBJECT.hasId(patientId)).
            and(Observation.CODE.exactly().identifier("8302-2")).execute();
%>
<%--    Display the Patient name --%>
<h2><%= StringUtils.join(patient.getName().get(0).getGiven(), " ") + " " + patient.getName().get(0).getFamily().get(0) %></h2>

<div>
    <table id="observation_list">
        <tr><th>Date</th><th>Height</th> </tr>

    <%
    // Iterate over the Observations and display them in a table
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