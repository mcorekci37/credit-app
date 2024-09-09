package com.emce.interestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.emce.commons.entity")
@EnableDiscoveryClient
@EnableScheduling
public class InterestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterestServiceApplication.class, args);
	}

}
