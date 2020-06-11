package TriggerError.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.xml.XmlUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import iFlowUtils.src.main.resources.script.MyUtilsPackaged

def Message processData(Message message) {
	def externalValueFromJar = new MyUtilsPackaged().doSomething("test me!!!");
	println(externalValueFromJar);
	String customStringUtilsScriptContent;
	// Reusable script coordinates
	String flowName = 'iFlowUtils'
	String scriptName = 'myUtils.groovy';
	//unit testing sequence without OSGi
	def isUnitTest = message.getHeaders()["unitTestIndicator"];
	if(isUnitTest) {
		def scriptDir = "./src/main/groovy/$flowName/src/main/resources/script/"
		customStringUtilsScriptContent = new File(scriptDir + "$scriptName").getText();
	}//CPI runtime sequence with OSGi
	else {
		// Get bundle context and from it, access reusable iFlow bundle
	    BundleContext context = FrameworkUtil.getBundle(Message).bundleContext
	    Bundle utilsBundle = context.bundles.find { it.symbolicName == flowName }
	    
	    // Within the bundle, access reusable script and read its content
	    customStringUtilsScriptContent = utilsBundle.getEntry("script/$scriptName").text
	}
    
    // Parse script content and execute its function
    Script customStringUtilsScript = new GroovyShell().parse(customStringUtilsScriptContent)
    String result = customStringUtilsScript.addPrefix(prefix, delimiter, value)
	println(result);
	//Body 
    def body = message.getBody(/*java.lang.String*/);
    body = new XmlParser().parseText(body);

    //def me = myObject.getCurrentOdataTime(System.currentTimeMillis());
    //println(me);
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