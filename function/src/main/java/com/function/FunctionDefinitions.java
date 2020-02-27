package com.function;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FunctionDefinitions implements Function {

	//private ApplicationContext context;
	//@Autowired
	public Long subtract;

	@Override
	public void execute(FunctionContext functionContext) {
//		if (!(functionContext instanceof RegionFunctionContext)) {
//			throw new FunctionException(
//					"This is a data aware function, and has to be called on a region.");
//		}
		//functionContext.getResultSender().sendResult(SpringContextHolder.getContext());
		//functionContext.getResultSender().sendResult(SpringContextHolder.getContext().getClass().getClassLoader());

		//context = SpringContextHolder.getContext();

		ApplicationContext c = new SpringApplicationBuilder(FunctionDefinitions.class).web(false).run();

//				new AnnotationConfigApplicationContext(FunctionDefinitions.class) {
//			@Override
//			public ClassLoader getClassLoader() {
//				return FunctionDefinitions.class.getClassLoader();
//			}
//		};
		subtract = (Long) c.getBean("Sub");

//		Long sum = 0L;
//		RegionFunctionContext regionFunctionContext = (RegionFunctionContext) functionContext;
//		for(Object o : regionFunctionContext.getDataSet().values()) {
//			sum += (Long)o;
//		}

		//int mult = Integer.parseInt(SpringUtils.defaultIfEmpty("", "2"));
			//sum *= mult;
		//sum -= subtract;
		functionContext.getResultSender().lastResult(subtract);
	}

	public String getId() {
		return "funid";
	}

	@Override
	public boolean hasResult() {
		return true;
	}

	@Override
	public boolean isHA() {
		return true;
	}

	@Override
	public boolean optimizeForWrite() {
		return true;
	}

	@Bean("Sub")
	public Long getSubtract() {
		return 23L;
	}

//	@Bean("y")
//	public String gety() {
//		return "1L";
//	}
}
