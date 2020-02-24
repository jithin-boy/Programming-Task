/**
 * Implementation of .
 * 
 * @author I501157
 * 
 */
package com.test.log.filter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.test.log.filter.exception.BadRequestException;
import com.test.log.filter.exception.InternalServerException;
import com.test.log.filter.model.ResponseObject;
import com.test.log.filter.util.FilesUtil;

/**
 * @author Jithin P
 *
 */
@Service
public class FilesService {

	@Autowired
	private FilesUtil filesUtil;

	/**
	 * Gets the all file names.
	 *
	 * @return the all file names
	 */
	public ResponseEntity<ResponseObject> getAllFileNames() {
		
		try
		{
			 return new ResponseEntity<>(
					 new ResponseObject(filesUtil.getAllFoldersAndFile(),"Success"), 
				      HttpStatus.OK);
			
		}
		catch(InternalServerException ex)
		{
			return new ResponseEntity<>(
					new ResponseObject(ex.getMessage(),"Error"), 
			          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch(BadRequestException ex)
		{
			return new ResponseEntity<>(
					new ResponseObject(ex.getMessage(),"Error"), 
			          HttpStatus.BAD_REQUEST);
		}
		
		 
	}
	
	/**
	 * Gets the file content.
	 *
	 * @param codedFileName the coded file name
	 * @return the file content
	 */
	public ResponseEntity<ResponseObject> getFileContent(String codedFileName)
	{
		try
		{
			 return new ResponseEntity<>(
					 new ResponseObject(filesUtil.getContent(codedFileName),"Success"), 
				      HttpStatus.OK);
			
		}
		catch(InternalServerException ex)
		{
			return new ResponseEntity<>(
					new ResponseObject(ex.getMessage(),"Error"), 
			          HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		catch(BadRequestException ex)
		{
			return new ResponseEntity<>(
					new ResponseObject(ex.getMessage(),"Error"), 
			          HttpStatus.BAD_REQUEST);
		}
	
	}

}
