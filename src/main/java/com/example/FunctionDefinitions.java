package com.example;

import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.springframework.data.gemfire.util.SpringUtils;

public class FunctionDefinitions implements Function {


	@Override
	public void execute(FunctionContext functionContext) {
		if (!(functionContext instanceof RegionFunctionContext)) {
			throw new FunctionException(
					"This is a data aware function, and has to be called on a region.");
		}
		Long sum = 0L;
		RegionFunctionContext regionFunctionContext = (RegionFunctionContext) functionContext;
		for(Object o : regionFunctionContext.getDataSet().values()) {
			sum += (Long)o;
		}

		String multiplier = SpringUtils.defaultIfEmpty("", "2");
		int mult = Integer.parseInt(multiplier);
		regionFunctionContext.getResultSender().lastResult(sum*mult);
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
}
