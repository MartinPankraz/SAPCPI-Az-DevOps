import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message
import java.text.SimpleDateFormat
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import spock.lang.Shared
import spock.lang.Specification

class UtilTester extends Specification {

	@Shared GroovyShell shell = new GroovyShell()
	@Shared Script script
	private Message msg
	
	def setupSpec() {
		// Load Groovy Script		
		//script = shell.parse(new File("iFlows/TriggerError/src/main/resources/script/script2.groovy"))
		script = shell.parse(new File("src/main/groovy/iFlowUtils/src/main/resources/script/myUtils.groovy"))
	}
	
	def setup() {
	}
	
	def "Check timestamp"() {
		
		given: "arbitrary date"
		def mills = System.currentTimeMillis()
		def compare = "/Date(" + mills + ")/" 
		
		when: "we execute the Groovy script"
		def odataTimeString = script.getCurrentOdataTime(""+mills);
		
		then: "timestamp valid"
		odataTimeString == compare
	}
	
	def "Check prefix"() {
		
		given: "String to concatenate"
		def prefix = "pre";
		def delimiter = "-";
		def value = "covid19";
		
		when: "we execute the Groovy script"
		def text = script.addPrefix(prefix, delimiter, value);
		
		then: "text concatenanted?"
		text == "pre-covid19"
	}
}