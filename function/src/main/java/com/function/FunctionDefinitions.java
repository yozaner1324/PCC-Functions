package com.function;

import com.contextholder.SpringContextHolder;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.FunctionException;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FunctionDefinitions implements Function {

	//@Autowired
	public Long subtract;

	@Override
	public void execute(FunctionContext functionContext) {
		if (!(functionContext instanceof RegionFunctionContext)) {
			throw new FunctionException(
					"This is a data aware function, and has to be called on a region.");
		}
		functionContext.getResultSender().sendResult(SpringContextHolder.getContext());

		subtract = (Long)SpringContextHolder.getContext().getBean("Sub");
		Long sum = 0L;
		RegionFunctionContext regionFunctionContext = (RegionFunctionContext) functionContext;
		for(Object o : regionFunctionContext.getDataSet().values()) {
			sum += (Long)o;
		}

		//int mult = Integer.parseInt(SpringUtils.defaultIfEmpty("", "2"));
		//	sum *= mult;
		sum -= subtract;
		regionFunctionContext.getResultSender().lastResult(sum);
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
	public Long getSubtract(Long x) {
		return 28L - x;
	}

	@Bean("x")
	public Long getx() {
		return 8L;
	}
}
