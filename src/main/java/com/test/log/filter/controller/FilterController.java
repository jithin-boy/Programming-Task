package com.test.log.filter.controller;

import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.log.filter.model.Filter;
import com.test.log.filter.model.ResponseObject;
import com.test.log.filter.model.constants.ErrorMessages;
import com.test.log.filter.service.FilterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Jithin
 *
 */
@RestController
@RequestMapping("/api")
@Api(description = "To perform all filter operations.")
public class FilterController {

	private final static Logger LOGGER = Logger.getLogger("FilterController");

	@Autowired
	private FilterService filterService;

	@PostMapping("/logFilter")
	@ApiOperation(value = "Create Log files based on the given input json and given log file location.", response = ResponseObject.class)
	public ResponseEntity<ResponseObject> createLogFiles(@Valid @RequestBody Filter filter) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			LOGGER.info("Received POST Request form user with the filter" + mapper.writeValueAsString(filter));
		} catch (JsonProcessingException e) {
			 return new ResponseEntity<>(new ResponseObject("Unable for map the input Json " + ErrorMessages.INPUT_FORMAT_ERROR,"Error"),HttpStatus.BAD_REQUEST);

		}
		return filterService.generateLogs(filter);
	}

	@PostMapping("/gitLogFilter")
	@ApiOperation(value = "Create Log files based on the input json and log file present in the GIT location.", response = ResponseObject.class)
	public ResponseEntity<ResponseObject> generateLogsFromGitInput() {
		LOGGER.info("Received GET Request form user for fetching data from GIT");
		return filterService.generateLogsFromGitInput();
	}

}
