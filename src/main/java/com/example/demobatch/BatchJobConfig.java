package com.example.demobatch;

import javax.sql.DataSource;

import com.example.demobatch.realestate.RealEstate;
import com.example.demobatch.realestate.RealEstateFieldSetMapper;
import com.example.demobatch.realestate.RealEstateItemWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

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
	public Step step() throws Exception {
		return stepBuilderFactory.get("step") //
				.<RealEstate, RealEstate>chunk(3) //
				.reader(realEstateItemReader()) //
				.processor(new PassThroughItemProcessor<>()) //
				.writer(realEstateItemWriter()) //
				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<RealEstate> realEstateItemReader() throws Exception {
		return new FlatFileItemReaderBuilder<RealEstate>() //
				.name("realEstateItemReader") //
				.delimited()
				.names(new String[] { "street", "city", "zip", "state", "beds", "baths",
						"sq__ft", "type", "sale_date", "price", "latitude", "longitude" }) //
				.linesToSkip(1) //
				.fieldSetMapper(new RealEstateFieldSetMapper()) //
				.resource(new UrlResource(
						"http://samplecsvs.s3.amazonaws.com/Sacramentorealestatetransactions.csv")) //
				.build();
	}

	@Bean
	@StepScope
	public RealEstateItemWriter realEstateItemWriter() {
		return new RealEstateItemWriter();
	}

	@Bean
	public Job job() throws Exception {
		return this.jobBuilderFactory.get("job") //
				.start(step()) //
				.build();
	}
}
