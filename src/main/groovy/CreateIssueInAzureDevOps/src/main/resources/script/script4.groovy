package CreateIssueInAzureDevOps.src.main.resources.script

import com.sap.gateway.ip.core.customdev.util.Message;
import java.util.HashMap;
def Message processData(Message message) {
    //Body 
       def body = message.getBody(java.lang.String) as String;
       
       def messageLog = messageLogFactory.getMessageLog(message);
       if(messageLog != null){
        messageLog.setStringProperty("Logging#1", "Printing Payload As Attachment")
        messageLog.addAttachmentAsString("Link to Issue", body, "text/html");
       }
       
       return message;
}