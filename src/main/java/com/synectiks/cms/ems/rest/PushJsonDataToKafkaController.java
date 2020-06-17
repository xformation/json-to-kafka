package com.synectiks.cms.ems.rest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.synectiks.cms.json.publish.PushJsonDataService;

@RestController
@RequestMapping("/api")
public class PushJsonDataToKafkaController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PushJsonDataService pushJsonDataService;
	
	@RequestMapping(method = RequestMethod.POST, value = "/send-data-to-kafka")
	public void uploadData(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> criteriaMap) throws Exception {
		logger.info("Starting data transfer to kafka");
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		logger.debug("File name : "+ file.getOriginalFilename());
		
		if(fileName.contains("..")) {
            throw new RuntimeException("Filename contains invalid path sequence: " + fileName);
        }

		if(!fileName.toLowerCase().contains("json")) {
			throw new RuntimeException("Unsupported file format. Only accept json file. " + fileName);
		}
		String kafkaTopic = criteriaMap.get("kafkaTopic");
		if(org.apache.commons.lang3.StringUtils.isBlank(kafkaTopic)) {
			throw new RuntimeException("Kafka topic not provided.");
		}
		
		File f = getTemproraryFile(file);
		pushJsonDataService.transfer(f, kafkaTopic);
		f.delete();
		logger.info("data transfer to kafka completed");
	}
	
	private File getTemproraryFile(MultipartFile mFile) throws IllegalStateException, IOException {
		File tempFile = new File(System.getProperty("java.io.tmpdir")+"/"+mFile.getOriginalFilename());
		tempFile.deleteOnExit();
		FileUtils.writeByteArrayToFile(tempFile, mFile.getBytes());
	    return tempFile;
	}
	
	
}
