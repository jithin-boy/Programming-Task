package com.test.log.filter.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.test.log.filter.exception.InternalServerException;

/**
 * @author Jithin P
 *
 */
@Component
public class CommunicationClient {
	
	private final static Logger LOGGER = Logger.getLogger("CommunicationClient");
	
	@Autowired RestTemplate restClient;
	
	/**
	 * Geta the data from the GIT Url Specified.
	 * @param remoteUrl - GIT Url value.
	 * @return String Response from the Git Url.
	 */
	public String getRemoteData(String remoteUrl)
	{
		LOGGER.info("Requesting data from the URL "+remoteUrl);
		ResponseEntity<String> response = restClient.getForEntity(remoteUrl, String.class);
		if(response.getStatusCodeValue() == 200)
		{
			return response.getBody();
		}
		else
		{
			return "DATA_UNAVAILABLE";
		}
	}
	/**
	 * Parse the data from the remote URL and structure the data to a map for further processing.
	 * @param remoteUrl - The URL From which the log has to be retrived.
	 * @return A map with the following structure
	 * { "<ipAddress>": {"<status>":{"<date>":[<actualLog>]}} }
	 */
	public Map<String,Map<String,Map<String,List<String>>>> getStructuredLogData(String remoteUrl)
	{
		

		Map<String,Map<String,Map<String,List<String>>>> structredLogData = new HashMap<String,Map<String,Map<String,List<String>>>>();
		String response = getRemoteData(remoteUrl);
		LOGGER.info("Processing the  data "+response);
		if(response.contentEquals("DATA_UNAVAILABLE"))
		{
			LOGGER.log(Level.SEVERE, "Unable to retrive the log from the URL "+ remoteUrl);
			throw new InternalServerException("Unable to retrive the log from the URL "+ remoteUrl);
			
		}
		
		String[] logs = response.split("\n");
		
		for(String log : logs)
		{
			String[] logComponents = log.split(",");
			try {
				
				String[] dateIpInfo = logComponents[0].split("ip-address:");
				String status = logComponents[1].split("status:")[1].replaceAll(" ", "");
				String[] dateInfo = dateIpInfo[0].split(" ");
				String date = dateInfo[1]+"-"+dateInfo[2]+"-"+dateInfo[0];
				String ipAddress = dateIpInfo[1].replaceAll(" ", "");
				
				
				if(structredLogData.containsKey(ipAddress))
				{
					Map<String,Map<String,List<String>>> dateLogData = structredLogData.get(ipAddress);
					if(dateLogData.containsKey(status))
					{
						Map<String,List<String>> statusLogData = dateLogData.get(status);
						if(statusLogData.containsKey(date))
						{
							List<String> exsitingLogList = statusLogData.get(date);
							exsitingLogList.add(log);
							statusLogData.put(date, exsitingLogList);
							
						}
						
						else
						{
							List<String> logList = new ArrayList<String>();
							logList.add(log);
							statusLogData.put(date, logList);
							
						}
					}
					else
					{
						Map<String,List<String>> statusLogData = new HashMap<String,List<String>>();
						List<String> logList = new ArrayList<String>();
						logList.add(log);
						statusLogData.put(date, logList);
						dateLogData.put(status, statusLogData);
					}
				}
				
				else
				{
					Map<String,Map<String,List<String>>> dateLogData = new HashMap<String,Map<String,List<String>>>();
					Map<String,List<String>> statusLogData = new HashMap<String,List<String>>();
					List<String> logList = new ArrayList<String>();
					logList.add(log);
					statusLogData.put(date, logList);
					dateLogData.put(status, statusLogData);
					structredLogData.put(ipAddress, dateLogData);
					
					
				}
				
				
			}
			catch(ArrayIndexOutOfBoundsException exception)
			{
				throw new InternalServerException("Unable to parse the data "+ log);
			}
		}
		
		return structredLogData;
		
		
	}
	


}
