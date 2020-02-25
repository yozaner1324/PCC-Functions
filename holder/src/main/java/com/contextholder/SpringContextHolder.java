package com.contextholder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.fun*")
@Configuration
public class SpringContextHolder {

	private static AnnotationConfigApplicationContext context;

	public static synchronized AnnotationConfigApplicationContext getContext() {
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

	public synchronized static void refreshContext() {
		context.refresh();
	}
}
