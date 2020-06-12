package com.synectiks.cms.json.publish;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.synectiks.cms.JsontokafkaApp;
import com.synectiks.cms.config.ApplicationProperties;
import com.synectiks.cms.json.reader.JsonReader;
import com.synectiks.cms.util.CommonUtil;
import com.synectiks.commons.utils.IUtils;

@Service
public class PushJsonDataService {

	private static final Logger logger = LoggerFactory.getLogger(PushJsonDataService.class);
	
	private static final String PRM_MSG = "msg";
	private static final String PRM_TOPIC = "topic";
	
	public<T> void pushToKafka(String json, Class<T> cls) throws Exception {
		List<String> jsonList = CommonUtil.parseJson(json);
		Gson gson = new Gson();
		for(String src: jsonList) {
			T t = JsonReader.getObject(gson, src, cls);
			logger.debug(""+t);
			fireEvent(src);
		}
	}
	
	public void transfer(File file) throws Exception {
		List<String> jsonList = CommonUtil.parseJsonFile(file, "UTF-8");
		for(String jsonStr: jsonList) {
			fireEvent(jsonStr);
		}
	}
	
	private void fireEvent(String jsonStr) {
    	RestTemplate restTemplate = JsontokafkaApp.getBean(RestTemplate.class);
    	ApplicationProperties applicationProperties = JsontokafkaApp.getBean(ApplicationProperties.class);
		String res = null;
		try {
			res = IUtils.sendGetRestRequest(
					restTemplate, 
					applicationProperties.getKafkaUrl(),
					IUtils.getRestParamMap(PRM_TOPIC,"test", PRM_MSG, jsonStr), 
					String.class);
			logger.debug("Response : "+res);
		} catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			res = null;
		}
	}
	
	
    
}
