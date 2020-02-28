package com.contextholder;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // Uncomment for Spring without Boot
@ComponentScan(basePackages = "com.fun*")	// Uncomment for Spring without Boot
//@SpringBootApplication(scanBasePackages = "com.fun*") //Uncomment for Spring Boot
public class SpringContextHolder {

	// UnComment for Spring Boot
//	private static ApplicationContext context = new SpringApplicationBuilder(SpringContextHolder.class)
//			.web(false)
//			.build()
//			.run();

	// Uncomment for Spring without Boot
	private static ApplicationContext context = new AnnotationConfigApplicationContext(SpringContextHolder.class) {
		@Override
		public ClassLoader getClassLoader() {
			return SpringContextHolder.class.getClassLoader();
		}
	};

	public static synchronized ApplicationContext getContext() {
		return context;
	}
}
