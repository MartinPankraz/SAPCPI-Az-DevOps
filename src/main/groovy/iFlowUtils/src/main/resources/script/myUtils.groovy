package iFlowUtils.src.main.resources.script

static String addPrefix(String prefix, String delimiter, String value){
    return "$prefix$delimiter$value"
}

static String getCurrentOdataTime(String timemills){
	return "/Date($timemills)/";//https://blogs.sap.com/2017/01/05/date-and-time-in-sap-gateway-foundation/
}