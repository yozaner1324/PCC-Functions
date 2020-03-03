package com.contextholder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/********************************************************************************
 NOTE: This class is not necessary if using SpringContextBootstrappingInitializer
********************************************************************************/

@Configuration // Uncomment for Spring without Boot
@ComponentScan	// Uncomment for Spring without Boot
//@SpringBootApplication(scanBasePackages = "com.fun*") //Uncomment for Spring Boot
public class SpringContextHolder {

	private static ApplicationContext context;

	public static synchronized ApplicationContext getContext() {
		if(context == null) {
			// Uncomment for Spring without Boot
			context = new AnnotationConfigApplicationContext(SpringContextHolder.class) {
				@Override
				public ClassLoader getClassLoader() {
					return SpringContextHolder.class.getClassLoader();
				}
			};
			// UnComment for Spring Boot
//			context = new SpringApplicationBuilder(SpringContextHolder.class)
//					.web(false)
//					.build()
//					.run();
		}
		return context;
	}
}
