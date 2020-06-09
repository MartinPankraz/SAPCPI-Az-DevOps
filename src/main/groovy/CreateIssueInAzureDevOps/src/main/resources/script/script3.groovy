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
import java.text.SimpleDateFormat;
import java.io.*
import java.lang.*;
import java.util.*;

def Message processData(Message message) {

       def map = message.getProperties();
       def alreadyExists = map.get("alreadyExists");
       //default already exists to past by default
       if(alreadyExists == ""){
           alreadyExists = new Date(System.currentTimeMillis()-24*60*60*1000).format( "yyyy-MM-dd hh:mm" );
       }
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
       Date currentDate = sdf.parse(new Date().format( "yyyy-MM-dd hh:mm" ));
       Date storedDate = sdf.parse(alreadyExists); 
       
       message.setProperty("printDebugCurrent", currentDate);
       message.setProperty("printDebug", storedDate);
       def dateDiff = currentDate - storedDate;
       message.setProperty("printDebugDiff", dateDiff);
       
       if(dateDiff == 1){
           message.setProperty("skipAzure", "false");    
       }else{
           message.setProperty("skipAzure", "true");
       }
       
       return message;
}