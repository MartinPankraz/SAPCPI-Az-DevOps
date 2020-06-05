import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message
import spock.lang.Shared
import spock.lang.Specification

class ExtractQueryParametersSpockTester extends Specification {

	@Shared GroovyShell shell = new GroovyShell()
	@Shared Script script
	private Message msg
	
	def setupSpec() {
		// Load Groovy Script		
		script = shell.parse(new File("src/main/groovy/script1.groovy"))
	}
	
	def setup() {
		this.msg = new MessageImpl()
	}
	
	def "message property is populated with single parameter from query string"() {
		
		given: "a single parameter is in the query string"		
		this.msg.setHeader("CamelHttpQuery", "mode=test")
			
		when: "we execute the Groovy script"
		script.processData(this.msg)
		
		then: "we get the message property populated with the parameter value"
		this.msg.getProperties()["mode"] == "test"
	}
	def "message properties are populated with multiple parameters from query string"() {
		
		given: "multiple parameters are in the query string"	
		this.msg.setHeader("CamelHttpQuery", "firstname=engswee&surname=yeoh&salutation=mr")
		
		when: "we execute the Groovy script"
		script.processData(this.msg)
		
		then: "we get the message properties populated with the parameters' value"
		this.msg.getProperties()["firstname"] == "engswee"
		this.msg.getProperties()["surname"] == "yeoh"
		this.msg.getProperties()["salutation"] == "mr"
	}
	def "message property is populated when special characters are included in query string"() {
		
		given: "special characters are included in query string"
		this.msg.setHeader("CamelHttpQuery", "input=this%26that")
		
		when: "we execute the Groovy script"
		script.processData(this.msg)
		
		then: "we get the message properties populated with the decoded special characters"
		this.msg.getProperties()["input"] == "this&that"
	}
	def "message property is populated when query string contains OData query string options"() {
		
		given: "OData query string options are included in query string"
		this.msg.setHeader("CamelHttpQuery", '$filter=firstname%20eq%20engswee')
		
		when: "we execute the Groovy script"
		script.processData(this.msg)
		
		then: "we get the message properties populated with the decoded OData query string content"
		this.msg.getProperties()['$filter'] == "firstname eq engswee"
	}
}