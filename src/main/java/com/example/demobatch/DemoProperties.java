package com.example.demobatch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
	private Resource csvSource;

	public Resource getCsvSource() {
		return csvSource;
	}

	public void setCsvSource(Resource csvSource) {
		this.csvSource = csvSource;
	}
}