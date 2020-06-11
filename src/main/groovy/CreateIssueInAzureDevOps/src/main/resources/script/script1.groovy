package CreateIssueInAzureDevOps.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
	
	def map = message.getHeaders();
	String httpQuery = map.get("CamelHttpQuery");
	def value = map.get("CamelHttpResponseCode");
	//Properties
	message.setProperty("returnCode", value);
	// Split parameters by &
	if(httpQuery) {
		String[] parameters = httpQuery.split("&")
		parameters.each {
			int index = it.indexOf("=")
			def decodeQuery = { input ->
				URI uri = new URI("http://localhost?" + input)
				return uri.getQuery()
			}
			message.setProperty(it.substring(0, index), decodeQuery(it.substring(index + 1)))
		} 
	}
       return message;
}