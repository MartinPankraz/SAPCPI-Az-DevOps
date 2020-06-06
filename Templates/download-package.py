from http.client import HTTPSConnection
import base64
import json
import os
import ssl
import sys
from io import BytesIO
from zipfile import ZipFile
from pathlib import Path
import shutil
#disable ssl check due to self signed certificat of SAP CPI
ssl._create_default_https_context = ssl._create_unverified_context

username = sys.argv[1]
password = sys.argv[2]
print("consider escaping characters on your password in case you have login problems...")
#This sets up the https connection
c = HTTPSConnection("658fa002trial.it-cpitrial.cfapps.us10.hana.ondemand.com")
#we need to base 64 encode it 
#and then decode it to acsii as python 3 stores it as a byte string
userAndPass = base64.b64encode(bytes(username + ':' + password, "utf-8")).decode("ascii")
myHeaders = { 'Authorization' : 'Basic %s' %  userAndPass, 'X-CSRF-Token':'Fetch' }

#Get all iflow ids of a package
path = "/api/v1/IntegrationPackages('AzureExamples')/IntegrationDesigntimeArtifacts?$format=json"
c.request('GET', path, headers=myHeaders)

res = c.getresponse()
out = json.loads(res.read())["d"]["results"]
for iflow in out:
    print("download " + iflow["Id"])
    
    individualPath = iflow["__metadata"]["media_src"].split(":443")[1]
    iFlowName = iflow["Id"]
    c.request('GET', individualPath, headers=myHeaders)
    res = c.getresponse()
    #unzip
    localPath = Path().resolve().parent / ("iFlows") / (iFlowName)
    #cleanup folder before write
    print("deleting: " + str(localPath))
    shutil.rmtree(localPath)
    #write zip content
    with ZipFile(BytesIO(res.read())) as zfile:
        zfile.extractall(localPath)
    print("write: " + str(localPath))
#close connection
res.close()
c.close()