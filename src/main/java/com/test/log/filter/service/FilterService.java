package com.test.log.filter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.log.filter.exception.InternalServerException;
import com.test.log.filter.model.Filter;
import com.test.log.filter.model.FilterData;
import com.test.log.filter.model.ResponseObject;
import com.test.log.filter.model.constants.CommonConstants;
import com.test.log.filter.model.constants.ErrorMessages;
import com.test.log.filter.util.CommunicationClient;
import com.test.log.filter.util.FilesUtil;

/**
 * @author Jithin P
 *
 */
@Service
public class FilterService {

	private final static Logger LOGGER = Logger.getLogger("FilterService");

	@Autowired
	private CommunicationClient remoteConnector;

	@Autowired
	private FilesUtil fileUtil;

	/**
	 * Generates Logs based on the given filter
	 * 
	 * @param filter the input filter.
	 */
	public ResponseEntity<ResponseObject> generateLogs(Filter filter) {
		List<String> addedFiles = new ArrayList<String>();
		fileUtil.deleteLogsFolder();
		Map<String, Map<String, Map<String, List<String>>>> structuredLogData = null;
		try
		{
		 structuredLogData = remoteConnector
				.getStructuredLogData(filter.getLogFile());
		}
		catch(InternalServerException ex)
		{
			 return new ResponseEntity<>(new ResponseObject(ex.getMessage(),"Error"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<FilterData> filtersToBeApplied = filter.getData();

		for (FilterData filterToBeApplied : filtersToBeApplied) {
			String ipAddress = filterToBeApplied.getIpAddress();				
			if (structuredLogData.containsKey(ipAddress)) {

				Map<String, Map<String, List<String>>> statusEntry = structuredLogData.get(ipAddress);
				for (String status : filterToBeApplied.getDebugFlag()) {
					if (statusEntry.containsKey(status)) {
						Map<String, List<String>> dateEntry = statusEntry.get(status);
						for (Entry<String, List<String>> dateLogEntry : dateEntry.entrySet()) {
							String date = dateLogEntry.getKey();
							List<String> contents = dateLogEntry.getValue();
							LOGGER.info("Creating log file the IP Address" + ipAddress + "with status " + status
									+ "for the date " + date);
							try {
								
								addedFiles.add(fileUtil.createFileForLog(date, ipAddress, status, contents));
							}
							catch(InternalServerException ex)
							{
								return new ResponseEntity<>(new ResponseObject(ex.getMessage(),"Error"),HttpStatus.INTERNAL_SERVER_ERROR);
							}
							

						}

					}

				}

			}
		}

		 return new ResponseEntity<>(new ResponseObject(addedFiles,"Success"),HttpStatus.CREATED);

	}

	/**
	 * Generates logs from the GIT content.
	 */
	public ResponseEntity<ResponseObject> generateLogsFromGitInput() {

		fileUtil.deleteLogsFolder();
		String inputJson = remoteConnector
				.getRemoteData(CommonConstants.INPUT_JSON_URL);
		ObjectMapper mapper = new ObjectMapper();
		Filter filter = null;
		try {
			filter = mapper.readValue(inputJson, Filter.class);
		} catch (JsonMappingException e) {
			return new ResponseEntity<>(new ResponseObject("Unable for map the inputJson "+ inputJson + ErrorMessages.INPUT_FORMAT_ERROR,"Error"),HttpStatus.BAD_REQUEST);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (filter != null) {
			filter.setLogFile(CommonConstants.LOG_URL);
		}

		return generateLogs(filter);
	}

}
