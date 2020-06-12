package com.synectiks.cms.json.reader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class JsonReader {

	private static final Logger logger = LoggerFactory.getLogger(JsonReader.class);
	public static <T> T getObject(Gson gson, String src, Class<T> cls) {
		T instance = null;
		if (!StringUtils.isBlank(src)) {
			try {
				instance = gson.fromJson(src, cls); 
			} catch (Exception e) {
				logger.error("Failed to instantiate class: " + cls.getName(), e);
			} 
		}
		return instance;
	}
	
	public static <T> T getObject(Class<T> cls) {
		T instance = null;
		try {
			instance = cls.newInstance(); 
		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Failed to instantiate class: " + cls.getName(), e);
		} 
		return instance;
	}
}
