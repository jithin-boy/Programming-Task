# Log Filter

URL : http://localhost:8080/api/logFilter

Method : POST,GET

Description : 

POST -> Takes the Payload as the input for filtering.
GET -> Reads the input json and the log file from the specified GIT Url and performs the action 

Payload Explanation : 

logFile -> The Git File location
data -> Filters

Sample Payload for POST :

{"logFile": "https://raw.githubusercontent.com/dhruvbehl/jsonTest/master/login.log",
"data": [{
			"ipAddress": "10.1.2.40",
			"debugFlag": ["FAIL", "PASS", "LOGIN"]
		},
		{
			"ipAddress": "10.1.20.10",
			"debugFlag": ["FAIL", "PASS", "LOGIN"]
		},
		{
			"ipAddress": "10.4.3.102",
			"debugFlag": ["PASS"]
		},
		{
			"ipAddress": "10.5.3.140",
			"debugFlag": ["FAIL", "PASS", "LOGIN"]
		},
		{
			"ipAddress": "10.1.2.45",
			"debugFlag": ["FAIL", "PASS", "LOGIN"]
		}
	]
}
    
Sample Response:

{
    "status": "Success",
    "message": "Added logs to the folder"
}



Deployment Using Eclipse:

1) Download the project.
2) Import the project in eclipse as maven project.
3) Right click on the project -> Build path -> configure build path -> select your JDK lib -> Apply and close.
4) Edit the /src/application.properties file for changing the server.address and server.port.
5) Right click the project and run as maven install.
6) A jar will be generated in the /target folder with the name filter-0.0.1-SNAPSHOT.jar.
7) Now run the jar using java -jar filter-0.0.1-SNAPSHOT.jar.
8) Use the above API's to generate the logs
9) The logs will be inside the log folder where the jar is being executed in our case it will be in the /target folder .






