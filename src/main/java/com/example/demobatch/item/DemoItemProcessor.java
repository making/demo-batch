package com.example.demobatch.item;

import org.springframework.batch.item.ItemProcessor;

public class DemoItemProcessor implements ItemProcessor<String, String> {
	@Override
	public String process(String item) throws Exception {
		Thread.sleep(300);
		return "Done " + item;
	}
}
