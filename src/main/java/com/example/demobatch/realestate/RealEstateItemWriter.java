package com.example.demobatch.realestate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemWriter;

public class RealEstateItemWriter implements ItemWriter<RealEstate> {
	private static final Logger log = LoggerFactory.getLogger(RealEstateItemWriter.class);

	@Override
	public void write(List<? extends RealEstate> items) throws Exception {
		log.info("<=====");
		items.forEach(i -> log.info(">> {}", i));
		log.info("=====>");
	}
}
