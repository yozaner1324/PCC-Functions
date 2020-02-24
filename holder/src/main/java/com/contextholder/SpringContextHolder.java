package com.contextholder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.SplittableRandom;

@ComponentScan(basePackages = "com.fun*")
@Configuration
public class SpringContextHolder {

	private static AnnotationConfigApplicationContext context;

	public static AnnotationConfigApplicationContext getContext() {
		if(context == null) {
			context = new AnnotationConfigApplicationContext(SpringContextHolder.class) {
				@Override
				public ClassLoader getClassLoader() {
					return SpringContextHolder.class.getClassLoader();
				}
			};
		}

		return context;
	}

	public static void refreshContext() {
		context = new AnnotationConfigApplicationContext(SplittableRandom.class);
	}
}
