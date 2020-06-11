from http.client import HTTPSConnection
import base64
import json
import os
import ssl
#import sys
#disable ssl check due to self signed certificat of SAP CPI
ssl._create_default_https_context = ssl._create_unverified_context
baseDirectory = './$(Release.PrimaryArtifactSourceAlias)/drop/src/main/groovy/'
#baseDirectory = 'C:/Users/mapankra/projects/SAPCPI-Az-DevOps/src/main/groovy/'

def performUpdateWithGivenPath(iFlowName, iFlowPath, myType):
    #prepare to update groovy scripts in remote CPI tenant
    directory = baseDirectory + iFlowPath
    for filename in os.listdir(directory):
        if filename.endswith("." + myType): 
            print(os.path.join(directory, filename))    
            path = os.path.join(directory, filename)
            #encode file content as base64
            content = open(path, "rb").read()
            b64 = base64.b64encode(content).decode("ascii")

            resourceExists = doesIFlowResourceExist(iFlowName, filename, myType)
            if resourceExists == True:
                data = {
                    "ResourceContent": b64
                }
                #encode json for http body
                data = json.dumps(data).encode('ascii')

                path = "/api/v1/IntegrationDesigntimeArtifacts(Id='" + iFlowName + "',Version='active')/$links/Resources(Name='" + filename + "',ResourceType='" + myType +"')"

                c.request('PUT', path, headers=myHeaders, body=data)
                print("updating...")

            else:
                data = {
                    "Name": filename,
                    "ResourceType": myType,
                    "ResourceContent": b64
                }
                #encode json for http body
                data = json.dumps(data).encode('ascii')

                path = "/api/v1/IntegrationDesigntimeArtifacts(Id='" + iFlowName + "',Version='active')/Resources"

                c.request('POST', path, headers=myHeaders, body=data)
                print("creating...")

            res = c.getresponse()
            out = res.read()
            code = res.getcode()
            print(code)
            print(out)
            continue
        else:
            continue

def deployIflowByName(iFlowName):
    path = "/api/v1/DeployIntegrationDesigntimeArtifact?Id='" + iFlowName +"'&Version='active'"
    c.request('POST', path, headers=myHeaders)
    res = c.getresponse()
    out = res.read()
    print("deploying iflow " + iFlowName)
    code = res.getcode()
    print(code)
    print(out)

def doesIFlowResourceExist(iFlowName, filename, myType):
    path = "/api/v1/IntegrationDesigntimeArtifacts(Id='" + iFlowName +"',Version='active')/Resources(Name='" + filename + "',ResourceType='" + myType +"')"
    c.request('GET', path, headers=myHeaders)
    res = c.getresponse()
    out = res.read()
    print("does resource on" + iFlowName + " exist? " + filename)
    code = res.getcode()
    print("exists:" + str(code))
    if code == 200:
        return True
    else:
        return False
    
    print(out)

#This sets up the https connection
c = HTTPSConnection("658fa002trial.it-cpitrial.cfapps.us10.hana.ondemand.com")
#we need to base 64 encode it 
#and then decode it to acsii as python 3 stores it as a byte string
#username = sys.argv[1]
#password = sys.argv[2]
userAndPass = base64.b64encode(b"$(username):$(password)").decode("ascii")
#userAndPass = base64.b64encode(bytes(username + ':' + password, "utf-8")).decode("ascii")
headers = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token':'Fetch' }
#then connect
c.request('GET', '/api/v1/MessageProcessingLogs', headers=headers)
#get the response back
res = c.getresponse()
res.read()
xsrftoken = res.getheader("X-CSRF-Token")
print("token fetch status:" + str(res.getcode()))
myHeaders = {}
myCookie = res.getheader("Set-Cookie")
#print(res.getheaders())
if myCookie is not None:
    myCookie = myCookie.split(";")[0]
    myHeaders = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token': xsrftoken, "Content-Type": "application/json", "Cookie": myCookie }
else:
    myHeaders = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token': xsrftoken, "Content-Type": "application/json"}
    print("no cookie header!")

#do iFlow update or create
myDirectory = 'TriggerError/src/main/resources/script/'
performUpdateWithGivenPath('TriggerError', myDirectory, 'groovy')

myDirectory = 'TriggerError/src/main/resources/lib/'
performUpdateWithGivenPath('TriggerError', myDirectory, 'jar')

#deploy iflow
deployIflowByName('TriggerError')

myDirectory = 'iFlowUtils/src/main/resources/script/'
performUpdateWithGivenPath('iFlowUtils', myDirectory, 'groovy')

#deploy iflow
deployIflowByName('iFlowUtils')

#close connection
res.close()
c.close()