package com.test.log.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.test.log.filter"})
public class FilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilterApplication.class, args);
	}
	
	   @Bean
	   public RestTemplate restTemplate(RestTemplateBuilder builder) {
		   RestTemplate restTemplate =builder.build(); 
	      return restTemplate ;
	   }

}
