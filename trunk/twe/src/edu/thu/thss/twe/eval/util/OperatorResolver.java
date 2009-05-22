package edu.thu.thss.twe.eval.util;

import java.util.HashMap;
import java.util.Map;

import edu.thu.thss.twe.eval.expression.OperatorType;

public class OperatorResolver {
	public static Map<String, OperatorType> operatorMap;

	static {
		operatorMap = new HashMap<String, OperatorType>();
		operatorMap.put("+", OperatorType.ADD);
		operatorMap.put("-", OperatorType.MINUS);
		operatorMap.put("*", OperatorType.TIMES);
		operatorMap.put("/", OperatorType.DEVIDE);
		operatorMap.put("=", OperatorType.ASSIGN);
		operatorMap.put("==", OperatorType.EQUALS);
		operatorMap.put(">", OperatorType.BIGGER_THAN);
		operatorMap.put("<", OperatorType.SMALLER_THAN);
		operatorMap.put(">=", OperatorType.NO_SMALLER_THAN);
		operatorMap.put("<=", OperatorType.NO_BIGGER_THAN);
	}

	public static OperatorType resolveOperator(String symbol) {
		OperatorType type = operatorMap.get(symbol.trim());
		return type;
	}

	public static String symbolOf(OperatorType op) {
		for (Map.Entry<String, OperatorType> ent : operatorMap.entrySet()) {
			if (op == ent.getValue()) {
				return ent.getKey();
			}
		}
		return "";
	}

	public static boolean isBasicOperation(OperatorType type) {
		return (type == OperatorType.ADD || type == OperatorType.MINUS
				|| type == OperatorType.TIMES || type == OperatorType.DEVIDE);
	}

	public static boolean isComparisonOperation(OperatorType type) {
		return (type == OperatorType.EQUALS || type == OperatorType.BIGGER_THAN
				|| type == OperatorType.SMALLER_THAN
				|| type == OperatorType.NO_SMALLER_THAN || type == OperatorType.NO_BIGGER_THAN);
	}
}
