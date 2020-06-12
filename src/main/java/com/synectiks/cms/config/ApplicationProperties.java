package com.synectiks.cms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Jsontokafka.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private String kafkaUrl;

	public String getKafkaUrl() {
		return kafkaUrl;
	}

	public void setKafkaUrl(String kafkaUrl) {
		this.kafkaUrl = kafkaUrl;
	}
	
}
