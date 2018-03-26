package com.example.demobatch.item;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.item.ItemReader;

public class DemoItemReader implements ItemReader<String> {
	private AtomicInteger counter = new AtomicInteger(0);

	@Override
	public String read() {
		int c = counter.getAndIncrement();
		if (c == 100) {
			return null;
		}
		return String.format("Hello %02d", c);
	}
}
