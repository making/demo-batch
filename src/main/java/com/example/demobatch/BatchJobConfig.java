package com.example.demobatch;

import javax.sql.DataSource;

import com.example.demobatch.item.DemoItemProcessor;
import com.example.demobatch.item.DemoItemReader;
import com.example.demobatch.item.DemoItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public BatchJobConfig(JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public JobParametersConverter jobParametersConverter(DataSource dataSource) {
		return new JsrJobParametersConverter(dataSource);
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step") //
				.<String, String>chunk(3) //
				.reader(demoItemReader()) //
				.processor(demoItemProcessor()) //
				.writer(demoItemWriter()) //
				.build();
	}

	@Bean
	@StepScope
	public DemoItemReader demoItemReader() {
		return new DemoItemReader();
	}

	@Bean
	@StepScope
	public DemoItemProcessor demoItemProcessor() {
		return new DemoItemProcessor();
	}

	@Bean
	@StepScope
	public DemoItemWriter demoItemWriter() {
		return new DemoItemWriter();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job") //
				.start(step()) //
				.build();
	}
}
