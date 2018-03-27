package com.example.demobatch;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CsvDownloader {
	private final DemoProperties props;
	private static final Logger log = LoggerFactory.getLogger(CsvDownloader.class);

	public CsvDownloader(DemoProperties props) {
		this.props = props;
	}

	public void download() {
		try {
			Resource csvSource = this.props.getCsvSource();
			Resource csvDownloadPath = this.props.getCsvDownloadPath();
			log.info("Start download from {} to {}.", csvSource, csvDownloadPath);
			Files.copy(csvSource.getInputStream(), csvDownloadPath.getFile().toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			log.info("Download finished.");
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
