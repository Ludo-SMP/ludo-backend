package com.ludo.study.studymatchingplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyMatchingPlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudyMatchingPlatformApplication.class, args);
	}
}
