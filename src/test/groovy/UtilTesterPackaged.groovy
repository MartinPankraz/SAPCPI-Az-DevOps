import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message
import java.text.SimpleDateFormat
import javax.xml.XMLConstants
import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory
import spock.lang.Shared
import spock.lang.Specification

class UtilTesterPackaged extends Specification {

	@Shared GroovyShell shell = new GroovyShell()
	@Shared Script script
	private Message msg
	
	def setupSpec() {
		// Load Groovy Script		
		script = shell.parse(new File("src/main/groovy/iFlowUtils/src/main/resources/script/myUtilsPackaged.groovy"))
	}
	
	def setup() {
	}
	
	def "Check altered text output"() {
		
		given: "arbitrary string"
		def text = "blabla"
		def compare = "!!!bla" 
		
		when: "we execute the Groovy script"
		def alteredString = script.doSomething(text);
		
		then: "text enhanced?"
		alteredString == compare
	}

}