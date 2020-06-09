import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import spock.lang.Shared
import spock.lang.Specification

class ValidateXMLTransformation extends Specification {

	@Shared GroovyShell shell = new GroovyShell()
	@Shared Script script
	private Message msg
	
	def setupSpec() {
		// Load Groovy Script		
		script = shell.parse(new File("src/main/groovy/TriggerError/src/main/resources/script/script1.groovy"))
	}
	
	def setup() {
		this.msg = new MessageImpl()
	}
	
	def "XML matches expected output"() {
		
		String xmlFile = new File('traceMessages/script1.xml').getText('UTF-8');
		
		given: "source XML message"		
		this.msg.setBody(xmlFile);
			
		when: "we execute the Groovy script"
		script.processData(this.msg)
		
		then: "we get the message property populated with the parameter value"
		def result = true;
		try {
			Source schemaFile = new StreamSource(new File("traceMessages/script1.xsd"));
			SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI )
					   .newSchema( schemaFile )
					   .newValidator()
					   .validate( new StreamSource(new StringReader(this.msg.getBody() )))
		}catch (org.xml.sax.SAXParseException exception){
			System.out.println(exception.getMessage());
			result = false;
		} 
		result == true;
	}
}