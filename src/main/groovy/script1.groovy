
import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;

def Message processData(Message message) {
	
	String httpQuery = message.getHeaders()["CamelHttpQuery"]
	// Split parameters by &
	String[] parameters = httpQuery.split("&")
	parameters.each {
		int index = it.indexOf("=")
		def decodeQuery = { input ->
			URI uri = new URI("http://localhost?" + input)
			return uri.getQuery()
		}
		message.setProperty(it.substring(0, index), decodeQuery(it.substring(index + 1)))
	}

	return message
}