/**
 * Implementation of FilesController.
 * 
 * @author Jithin P
 * 
 */
package com.test.log.filter.controller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.log.filter.model.ResponseObject;
import com.test.log.filter.service.FilesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/files")
@Api(description = "To perform all file related operations.")
public class FilesController {
	
	private final static Logger LOGGER = Logger.getLogger("FilesController");
	
	@Autowired
	private FilesService filesService;
	
	/**
	 * Gets the all files.
	 *
	 * @return the all  files names present in the logs folder.
	 */
	@ApiOperation(value = "List all log files in the system", response = ResponseObject.class)
	@GetMapping
	public ResponseEntity<ResponseObject> getAllFiles()
	{
		
		LOGGER.info("GET Request for all files ");
		return filesService.getAllFileNames();
	}
	
	/**
	 * Gets the file content.
	 *
	 * @param codedFileName the coded file name in the format <folderName>-<fileName>
	 * @return the file content
	 */
	@ApiOperation(value = "Retrive the file content log for a given file.", response = ResponseObject.class)
	@GetMapping("/{codedFileName}")
	public ResponseEntity<ResponseObject> getFileContent(@PathVariable String codedFileName)
	{
		
		LOGGER.info("GET Request for the file "+codedFileName);
		return filesService.getFileContent(codedFileName);
		
	}

}
