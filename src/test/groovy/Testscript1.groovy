import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import com.sap.gateway.ip.core.customdev.processor.MessageImpl
import com.sap.gateway.ip.core.customdev.util.Message

class Testscript1 {
	
	private GroovyShell shell = new GroovyShell();
	private Script script = shell.parse(new File("iFlows/CreateIssueInAzureDevOps/src/main/resources/script/script1.groovy"));
	//private Script script = shell.parse(new File("src/main/groovy/script1.groovy"));
	private Message msg;

	@Before
	public void setUp() throws Exception {
		msg = new MessageImpl();
	}

	@Test
	public void testReturnCode() {
		msg.setHeader("CamelHttpResponseCode", "200");
		script.processData(msg);
		assertTrue("Header matches return code", msg.getProperties()["returnCode"] == "200");
	}
	
	@Test
	public void testParam() {
		msg.setHeader("CamelHttpQuery", "mode=test");
		script.processData(msg);
		assertTrue("Property does not match test string", msg.getProperties()["mode"] == "test");
	}

	@Test
	public void testMultiParam() {
		msg.setHeader("CamelHttpQuery", "firstname=martin&surname=pankraz&salutation=mr");
		script.processData(msg);
		assertTrue("Property does not match firstname", msg.getProperties()["firstname"] == "martin");
		assertTrue("Property does not match lastname", msg.getProperties()["surname"] == "pankraz");
		assertTrue("Property does not match salutation", msg.getProperties()["salutation"] == "mr");
	}
	
	@Test
	public void testSpecialCharacters() {
		msg.setHeader("CamelHttpQuery", "input=this%26that");
		script.processData(msg);
		assertTrue("Property not decoded", msg.getProperties()["input"] == "this&that");
	}
	
	@Test
	public void testODataVerbs() {
		msg.setHeader("CamelHttpQuery", '$filter=firstname%20eq%20martin');
		script.processData(msg);
		assertTrue("OData verb not interpreted", msg.getProperties()['$filter'] == "firstname eq martin");
	}
}