package edu.thu.thss.twe.eval.util;

import java.util.HashMap;
import java.util.Map;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.runtime.ProcessInstance;
import edu.thu.thss.twe.model.runtime.Variable;

public class VariableResolver {
	private ProcessInstance processInstance;
	private Map<String, Variable> variables;

	public VariableResolver(ProcessInstance instance) {
		if (instance == null) {
			throw new TweException(
					"cannot create variable resolver : process instance is not specified.");
		}
		this.processInstance = instance;
		this.variables = new HashMap<String, Variable>();
		if (processInstance.getVariables() != null) {
			for (Variable v : processInstance.getVariables()) {
				variables.put(v.getDataField().getName(), v);
			}
		}
	}

	public Variable resolve(String name) {
		return variables.get(name);
	}
}
