from http.client import HTTPSConnection
import base64
import json
import os
#This sets up the https connection
c = HTTPSConnection("mycpitrial.it-cpitrial01.cfapps.eu10.hana.ondemand.com")
#we need to base 64 encode it 
#and then decode it to acsii as python 3 stores it as a byte string
userAndPass = base64.b64encode(b"$(username):$(password)").decode("ascii")
headers = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token':'Fetch' }
#then connect
c.request('GET', '/api/v1/MessageProcessingLogs', headers=headers)
#get the response back
res = c.getresponse()
res.read()
xsrftoken = res.getheader("X-CSRF-Token")
myCookie = res.getheader("Set-Cookie").split(";")[0]

#prepare to update groovy scripts in remote CPI tenant
directory = "../src/main/resources/script/"
for filename in os.listdir(directory):
    if filename.endswith(".groovy"): 
        print(os.path.join(directory, filename))    
        path = os.path.join(directory, filename)#"C:/Users/mapankra/projects/SAPCPI-Az-DevOps/src/main/resources/script/script1.groovy"
        #encode file content as base64
        content = open(path, "rb").read()
        b64 = base64.b64encode(content).decode("ascii")

        data = {
        "ResourceContent": b64
        }
        #encode json for http body
        data = json.dumps(data).encode('ascii')

        headers = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token': xsrftoken, "Content-Type": "application/json", "Cookie": myCookie }
        path = "/api/v1/IntegrationDesigntimeArtifacts(Id='CreateIssueInAzureDevOps',Version='active')/$links/Resources(Name='" + filename + "',ResourceType='groovy')"

        c.request('PUT', path, headers=headers, body=data)

        res = c.getresponse()
        out = res.read()
        code = res.getcode()
        print(code)
        print(out)
        continue
    else:
        continue
#close connection
res.close()
c.close()