package com.priya.sbAwsSQS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;

@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class})
public class SbAwsSqsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbAwsSqsApplication.class, args);
	}

}
