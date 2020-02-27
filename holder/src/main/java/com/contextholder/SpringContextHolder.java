package com.contextholder;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.fun*")
@Configuration
public class SpringContextHolder {

	private static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringContextHolder.class) {
		@Override
		public ClassLoader getClassLoader() {
			return SpringContextHolder.class.getClassLoader();
		}
	};

	public static synchronized AnnotationConfigApplicationContext getContext() {
		return context;
	}

	public synchronized static void refreshContext() {
		context = new AnnotationConfigApplicationContext(SpringContextHolder.class) {
			@Override
			public ClassLoader getClassLoader() {
				return SpringContextHolder.class.getClassLoader();
			}
		};
	}
}
