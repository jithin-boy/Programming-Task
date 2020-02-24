/**
 * Implementation of .
 * 
 * @author I501157
 * 
 */
package com.test.log.filter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import com.test.log.filter.exception.BadRequestException;
import com.test.log.filter.exception.InternalServerException;
import com.test.log.filter.model.constants.ErrorMessages;

/**
 * @author Jithin P
 *
 */

@Component
public class FilesUtil {

	private final static Logger LOGGER = Logger.getLogger("FilesUtil");

	/**
	 * @return returns the absoulte path of the project
	 */
	private String getProjectPath() {
		return System.getProperty("user.dir") + File.separator;
	}

	/**
	 * deletes the logs folder for a fresh request.
	 */
	public void deleteLogsFolder() {
		String pathToDelete = getProjectPath() + "logs";
		FileSystemUtils.deleteRecursively(new File(pathToDelete));
	}

	/**
	 * Creates the logs folder within the resources folder.
	 * 
	 * @param date String value for which folder has to e created.
	 * @return
	 */
	private String createFolderWithDate(String date) {

		String pathToAdd = getProjectPath() + "logs" + File.separator + date;
		Path path = Paths.get(pathToAdd);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
				LOGGER.info("Directory " + path + " is created.");
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Unable to create Directory with path " + path);
				throw new InternalServerException("Unable to create Directory with path " + path);
			}
		}
		return pathToAdd;
	}

	/**
	 * Creates files within the specified folder along with the log contents.
	 * 
	 * @param date      - date of the log
	 * @param ipAddress - IP address from the filter
	 * @param status    - status of the log
	 * @param contents  - Actual log content.
	 */
	public String createFileForLog(String date, String ipAddress, String status, List<String> contents) {

		String targetFolder = createFolderWithDate(date);
		String absoulteFilePath = targetFolder + File.separator + ipAddress + "_" + status + ".log";
		FileLock lock = null;
		PrintStream filePrinter = null;
		try {
			FileOutputStream fileToWriteStream = new FileOutputStream(absoulteFilePath, true);
			lock = fileToWriteStream.getChannel().tryLock();
			filePrinter = new PrintStream(fileToWriteStream);
			for (String content : contents) {
				LOGGER.info("Creating log file the IP Address" + ipAddress + "with status " + status + "for the date "
						+ date + " content " + content);
				filePrinter.append(content).append("\n");
			}
			lock.release();
			filePrinter.close();
		} catch (FileNotFoundException e) {
			LOGGER.log(Level.SEVERE, "Unable to write to file " + absoulteFilePath);
			throw new InternalServerException("Unable to write to file " + absoulteFilePath);

		} catch (IOException e) {

			LOGGER.log(Level.SEVERE, "Unable to write to file " + absoulteFilePath);
			throw new InternalServerException("Unable to write to file " + absoulteFilePath);
		}
		
		return date+'-'+ipAddress + "_" + status + ".log";
	}

	/**
	 * @return returns the list of files in the format [<folderName>-<fileName>]
	 */
	public List<String> getAllFoldersAndFile() {
		String pathToScan = getProjectPath() + "logs";
		List<String> result = null;
		LOGGER.info("Reading all files from the path "+pathToScan);
		try (Stream<Path> walk = Files.walk(Paths.get(pathToScan))) {

			result = walk.filter(Files::isRegularFile).map(x -> x.toString().split("logs\\\\")[1].replace("\\", "-"))
					.collect(Collectors.toList());

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to read file from  " + pathToScan);
			throw new InternalServerException("Unable to read the path " + pathToScan);
		}
		return result;

	}
	
	/**
	 * Gets the content.
	 *
	 * @param codedFileName the coded file name in the format <folderName>-<fileName>
	 * @return the log content in form of list
	 */
	public List<String> getContent(String codedFileName)
	{
		
		List<String> logs = new ArrayList<String>();
		try {
		String folderName =  codedFileName.substring(0,codedFileName.lastIndexOf("-"));
		String fileName = codedFileName.substring(codedFileName.lastIndexOf("-")+1,codedFileName.length());
		String pathToRetrive = getProjectPath() + "logs"+ File.separator+folderName+File.separator+fileName;
		LOGGER.info("Reading the file content from the path "+pathToRetrive);
		    
		    try (Stream<String> stream = Files.lines( Paths.get(pathToRetrive), StandardCharsets.UTF_8)) 
		    {
		        stream.forEach(s -> logs.add(s));
		    }
		    catch (IOException e) 
		    {
		    	LOGGER.log(Level.SEVERE, "Unable to read file from  " + pathToRetrive);
				throw new BadRequestException("Unable to read the path " + pathToRetrive);
		    }
		}
		
	    catch (ArrayIndexOutOfBoundsException  | StringIndexOutOfBoundsException e) 
	    {
	    	LOGGER.log(Level.SEVERE, "Unable to decode the coded file name   "+codedFileName +ErrorMessages.FILE_FORMAT_ERROR) ;
			throw new BadRequestException("Unable to decode the coded file name "+codedFileName +ErrorMessages.FILE_FORMAT_ERROR);
	    }
		
		    return logs;
		
	}

	public static void main(String args[]) {
		FilesUtil util = new FilesUtil();
		System.out.println(util.getContent("Dec-01-2019-10.1.2.40_PASS.log"));
	}

}
