package com.synectiks.cms.entityresolver;

import org.apache.commons.lang3.StringUtils;

public class EntityClassResolver {

	public static Class getEntityClass(String entityName) throws ClassNotFoundException {
		if(StringUtils.isBlank(entityName)) {
			return null;
		}
//		Class.forName(entityName, true, this.getClass().getClassLoader());
		Class name = Class.forName("com.synectiks.cms.domain."+entityName);
		return name;
	}
	
}

