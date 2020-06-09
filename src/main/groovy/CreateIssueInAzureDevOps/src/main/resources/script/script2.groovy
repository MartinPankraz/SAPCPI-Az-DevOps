package CreateIssueInAzureDevOps.src.main.resources.script
/*
 The integration developer needs to create the method processData 
 This method takes Message object of package com.sap.gateway.ip.core.customdev.util 
which includes helper methods useful for the content developer:
The methods available are:
    public java.lang.Object getBody()
	public void setBody(java.lang.Object exchangeBody)
    public java.util.Map<java.lang.String,java.lang.Object> getHeaders()
    public void setHeaders(java.util.Map<java.lang.String,java.lang.Object> exchangeHeaders)
    public void setHeader(java.lang.String name, java.lang.Object value)
    public java.util.Map<java.lang.String,java.lang.Object> getProperties()
    public void setProperties(java.util.Map<java.lang.String,java.lang.Object> exchangeProperties) 
    public void setProperty(java.lang.String name, java.lang.Object value)
    public java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> getSoapHeaders()
    public void setSoapHeaders(java.util.List<com.sap.gateway.ip.core.customdev.util.SoapHeader> soapHeaders) 
       public void clearSoapHeaders()
 */
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
import groovy.json.JsonBuilder;

def Message processData(Message message) {
    //Body
    //def body = message.getBody();
    //Get Headers 
    def headersMap = message.getHeaders();
    message.setHeader("Content-Type","application/json");
    def statusCode = headersMap.get("MyResponseCode");
    if(!statusCode) statusCode = "missing";
	def mylogId = headersMap.get("MyMPLID");
	def mytime = headersMap.get("MyCamelCreatedTimestamp");
	def name = headersMap.get("MyIFlowName");
	def myErrorMsg = headersMap.get("MyCamelExceptionCaught");
	//Get Properties  
	def map = message.getProperties();
    def appUrl = map.get("tenantUrl");
	
	//def runId = idValue;
	def targetUrl = appUrl + "%7B%22messageGuid%22%3A%22" + mylogId + "%22%7D";
	//def targetUrlDetails = appUrl + 'MessageProcessingRun/%7B"parentContext":%7B"MessageMonitor":%7B"artifact":"CreateIssueInAzureDevOps","artifactKey":"CreateIssueInAzureDevOps","artifactName":"CreateIssueInAzureDevOps"%7D%7D,"messageProcessingLog":"' + mylogId + '","RunId":"' + runId + '"%7D';
	
	def builder = new JsonBuilder();
    builder.obj {
        integrationPackage 'AzureIntegrationExample'
        iflowName name
        httpCode statusCode
        time mytime
        url targetUrl
        logid mylogId 
    //    urlDetail targetUrlDetails
        msg myErrorMsg.getMessage()
    }
     
    message.setBody(builder.toPrettyString());
       
       return message;//thats it
}