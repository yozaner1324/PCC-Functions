package com.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

	@Bean("Greeting")
	public String createGreeting() {
		return "Salutations";
	}

	@Bean("Addressee")
	public String createAddressee() {
		return "Earth";
	}
}
