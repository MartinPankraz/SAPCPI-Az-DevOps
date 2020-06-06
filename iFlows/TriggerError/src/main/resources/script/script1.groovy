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
import groovy.xml.XmlUtil;

def Message processData(Message message) {
	//location of this script and load util groovy script "script2"
	def scriptDir = new File(getClass().protectionDomain.codeSource.location.path).parent
	File sourceFile = new File(scriptDir + "/script2.groovy");
	Class groovyClass = new GroovyClassLoader(getClass().getClassLoader()).parseClass(sourceFile);
	GroovyObject myObject = (GroovyObject) groovyClass.newInstance();
	//Body 
    def body = message.getBody(/*java.lang.String*/);
    body = new XmlParser().parseText(body);

    def me = myObject.getCurrentOdataTime(System.currentTimeMillis());
    println(me);
	body.RegisteredProduct.each{
		def value = it.Status[0].text();
		if(value == "2") {
			it.Status[0].value = "active";
		}else {
			it.Status[0].value = "disabled";
		}
	}
   
	def stringWriter = new StringWriter();
	XmlUtil.serialize(body, stringWriter)
	//put altered structure back into the message
    message.setBody(stringWriter.toString());
   
    return message;
}