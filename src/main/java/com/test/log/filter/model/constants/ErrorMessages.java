package com.test.log.filter.model.constants;

public  class ErrorMessages {
	
	public static String FILE_FORMAT_ERROR = " Please use the following format <folderName>-<fileName>";
	public static String INPUT_FORMAT_ERROR = " expected format "+"{\r\n" + 
			"                \"logFile\": \"\",\r\n" + 
			"                \"data\": [{\r\n" + 
			"                                                \"ipAddress\": \"10.1.1.1\",\r\n" + 
			"                                                \"debugFlag\": [\"FAIL\", \"PASS\", \"LOGIN\"]\r\n" + 
			"                                }],\r\n" + 
			"";

}
