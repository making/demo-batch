package com.example.demobatch;

import javax.sql.DataSource;

import com.example.demobatch.crime.Crime;
import com.example.demobatch.crime.CrimeFieldSetMapper;
import com.example.demobatch.crime.CrimeItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DemoProperties props;
	private final CrimeItemWriter crimeItemWriter;

	public BatchJobConfig(JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory, DemoProperties props,
			CrimeItemWriter crimeItemWriter) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.props = props;
		this.crimeItemWriter = crimeItemWriter;
	}

	@Bean
	public JobParametersConverter jobParametersConverter(DataSource dataSource) {
		return new JsrJobParametersConverter(dataSource);
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step") //
				.<Crime, Crime>chunk(10) //
				.reader(crimeItemReader()) //
				.processor(new PassThroughItemProcessor<>()) //
				.writer(this.crimeItemWriter) //
				.build();
	}

	@Bean
	public FlatFileItemReader<Crime> crimeItemReader() {
		return new FlatFileItemReaderBuilder<Crime>() //
				.name("crimeItemReader") //
				.delimited()
				.names(new String[] { "cdatetime", "address", "district", "beat", "grid",
						"crimedescr", "ucr_ncic_code", "latitude", "longitude" }) //
				.linesToSkip(1) //
				.fieldSetMapper(new CrimeFieldSetMapper()) //
				.resource(this.props.getCsvSource()) //
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job") //
				.start(step()) //
				.build();
	}
}
