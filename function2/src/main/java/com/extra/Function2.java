package com.extra;

import com.contextholder.SpringContextHolder;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.springframework.stereotype.Component;

@Component
public class Function2 implements Function {
	@Override
	public void execute(FunctionContext functionContext) {

		functionContext.getResultSender().lastResult(SpringContextHolder.getContext().getBean("Sub"));
	}

	@Override
	public String getId() {
		return "fun2";
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
}
