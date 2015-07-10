<%@ page import="org.hspconsortium.client.AbstractFhirClient" %>
<%@ page import="org.hspconsortium.client.impl.ClientFlowFhirClient" %>
<%@ page import="ca.uhn.fhir.model.api.Bundle" %>
<%@ page import="ca.uhn.fhir.model.dstu2.resource.Patient" %>
<%@ page import="org.hspconsortium.client.auth.context.FhirClientContext" %>
<%@ page import="ca.uhn.fhir.model.dstu2.resource.Observation" %>
<%@ page import="java.util.List" %>
<%@ page import="ca.uhn.fhir.model.api.BundleEntry" %>
<%@ page import="ca.uhn.fhir.model.dstu2.composite.QuantityDt" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="ca.uhn.fhir.model.primitive.DateTimeDt" %>
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
    AbstractFhirClient defaultClient = new ClientFlowFhirClient(request, "secret");
    FhirClientContext context = (FhirClientContext) request.getSession().getAttribute(FhirClientContext.FHIR_CLIENT_CONTEXT);
    Patient patient = defaultClient.read().resource(Patient.class).withId(context.getAccessToken().getPatientId()).execute();

    Bundle results = defaultClient.search().forResource(Observation.class).where(
            Observation.SUBJECT.hasId(context.getAccessToken().getPatientId())).
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