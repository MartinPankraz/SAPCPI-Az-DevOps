import spock.lang.Shared
import spock.lang.Specification
import iFlowUtils.src.main.resources.script.MyUtilsPackaged

class UtilTesterPackaged extends Specification {

	@Shared MyUtilsPackaged script
	
	def setupSpec() {
		
	}
	
	def setup() {
		script = new MyUtilsPackaged();
	}
	
	def "Check altered text output"() {
		
		given: "arbitrary string"
		String text = "blabla"
		String compare = "!!!blabla" 
		
		when: "we execute the Groovy script"
		String alteredString = script.doSomething(text);
		
		then: "text enhanced?"
		alteredString == compare
	}

}