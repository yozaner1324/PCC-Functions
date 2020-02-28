package com.function;

import com.contextholder.SpringContextHolder;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Function1 implements Function {

	public Long subtract;

	@Override
	public void execute(FunctionContext functionContext) {

		ApplicationContext c = SpringContextHolder.getContext();

		subtract = (Long) c.getBean("Sub");

		functionContext.getResultSender().lastResult(subtract);
	}

	public String getId() {
		return "fun1";
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
}
