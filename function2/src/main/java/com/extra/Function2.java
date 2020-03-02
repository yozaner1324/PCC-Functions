package com.extra;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.support.LazyWiringDeclarableSupport;
import org.springframework.stereotype.Component;

@Component
public class Function2 extends LazyWiringDeclarableSupport implements Function {

	@Autowired
	public Long subtract;

	@Override
	public void execute(FunctionContext functionContext) {

		functionContext.getResultSender().lastResult(subtract);
	}

	@Override
	public String getId() {
		return "fun2";
	}

//	@Bean("Sub")
//	public Long getSubtract() {
//		return 28L;
//	}
}
