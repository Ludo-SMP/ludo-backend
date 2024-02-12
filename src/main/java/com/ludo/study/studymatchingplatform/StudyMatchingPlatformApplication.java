package com.ludo.study.studymatchingplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class StudyMatchingPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudyMatchingPlatformApplication.class, args);
	}

}
