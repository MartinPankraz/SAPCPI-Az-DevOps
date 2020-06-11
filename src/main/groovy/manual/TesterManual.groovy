package manual
import com.sap.gateway.ip.core.customdev.util.Message;
import com.sap.gateway.ip.core.customdev.processor.MessageImpl;
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

class TesterManual {
     
     public static void main(String[] args) {
//        System.out.println("Hello Java-World!");
          // Load Groovy Script
        GroovyShell shell = new GroovyShell();
        def script = shell.parse(new File("src/main/groovy/TriggerError/src/main/resources/script/script1.groovy"));
		Message msg = new MessageImpl();
		    
        String xmlFile = new File('traceMessages/script1.xml').getText('UTF-8');
		msg.setHeader("unitTestIndicator", true);
		msg.setBody(xmlFile);
			
		script.processData(msg);
		
		def result = true;
		try {
			Source schemaFile = new StreamSource(new File("traceMessages/script1.xsd"));
			SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI )
					   .newSchema( schemaFile )
					   .newValidator()
					   .validate( new StreamSource(new StringReader(msg.getBody() )))
		}catch (org.xml.sax.SAXParseException exception){
			System.out.println(exception.getMessage());
			result = false;
		} 
		println(result);
		// Display results of script in console
		println("Body:\r\n" + msg.getBody())
		  
		def displayMaps = { String mapName, Map map ->
			println mapName
			map.each { key, value -> println( key + " = " + value) }
		}
		displayMaps("Headers:", msg.getHeaders())
		displayMaps("Properties:", msg.getProperties())
     }

}