package com.extra;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.gemfire.support.LazyWiringDeclarableSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Function2 extends LazyWiringDeclarableSupport implements Function {

	@Autowired
	@Qualifier("Greeting")
	private String greeting;

	@Resource(name = "Addressee")
	private String addressee;

	@Override
	public void execute(FunctionContext functionContext) {

		functionContext.getResultSender().lastResult(greeting + ", " + addressee + "!");
	}

	@Override
	public String getId() {
		return "fun2";
	}
}
