package com.function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

	@Bean("Num")
	public Long getSubtract() {
		return 28L;
	}
}
