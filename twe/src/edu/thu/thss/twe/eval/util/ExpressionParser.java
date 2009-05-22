package edu.thu.thss.twe.eval.util;

import edu.thu.thss.twe.eval.expression.Expression;
import edu.thu.thss.twe.eval.expression.OperatorType;
import edu.thu.thss.twe.eval.expression.SingleValueExpression;
import edu.thu.thss.twe.eval.expression.TwoOperandExpression;
import edu.thu.thss.twe.eval.expression.VariableExpression;
import edu.thu.thss.twe.exception.EvaluationException;
import edu.thu.thss.twe.model.runtime.Variable;

/**
 * this utility class parses a string representation of a expression to a
 * Expression instance
 * 
 * @author Devin
 * 
 */
public class ExpressionParser {

	public static void main(String[] args) {

		String expression = "true";
		Expression exp = parse(expression, new VariableResolver(null));

	}

	public static Expression parse(String expression, VariableResolver resolver) {
		TweStringTokenizer tokenizer = new TweStringTokenizer(expression);
		Expression exp = parse(tokenizer, resolver);
		return exp;
	}

	private static Expression parse(TweStringTokenizer tokenizer,
			VariableResolver resolver) {
		Expression left = null;
		Expression right = null;
		OperatorType operator = null;
		// parse the left operand;
		String token = tokenizer.peekNextToken();
		if (token.equals("(")) {
			left = parseBracketedExpression(tokenizer, resolver);
		} else {
			left = parseSingleOrVariableExpression(tokenizer, resolver);
		}

		//
		token = tokenizer.nextToken();
		if (token == null) { // this expression contains just one token ,is a
			// single or variable token;
			return left;
		}
		// parse the operator
		operator = OperatorResolver.resolveOperator(token);
		if (operator == null) {
			throw new EvaluationException(
					"cannot parse the expression: unexpected token: " + token);
		}

		// parse the right operand;
		token = tokenizer.peekNextToken();
		if (token.equals("(")) {
			right = parseBracketedExpression(tokenizer, resolver);
		} else {
			right = parseSingleOrVariableExpression(tokenizer, resolver);
		}
		return new TwoOperandExpression(left, right, operator);
	}

	private static Expression parseBracketedExpression(
			TweStringTokenizer tokenizer, VariableResolver resolver) {
		// remove "(";
		tokenizer.nextToken();
		// parse expression
		Expression exp = parse(tokenizer, resolver);
		// remove ")";
		String token = tokenizer.nextToken();
		if (!token.equals(")")) {
			throw new EvaluationException(
					"cannot parse the expression: [)] expected but encounter "
							+ token);
		}
		return exp;

	}

	private static Expression parseSingleOrVariableExpression(
			TweStringTokenizer tokenizer, VariableResolver resolver) {
		Expression exp = null;
		String token = tokenizer.nextToken();
		if (token.startsWith("\"") && token.endsWith("\"")) {
			// string type;
			exp = new SingleValueExpression(token.substring(1,
					token.length() - 1));
		} else {
			Variable v = resolver.resolve(token);
			if (v != null) {
				// a variable
				exp = new VariableExpression(v);
			} else {
				Boolean value = getBooleanValue(token);
				if (value != null) {
					// a boolean
					exp = new SingleValueExpression(value);
				} else {
					// must be a number
					try {
						Double d = Double.parseDouble(token);
						exp = new SingleValueExpression(d);
					} catch (Exception e) {
						throw new EvaluationException(
								"cannot parse the expression: not a valid token: "
										+ token);
					}
				}
			}
		}
		return exp;
	}

	private static Boolean getBooleanValue(String token) {
		String temp = token.toLowerCase();
		if (temp.equals("true")) {
			return new Boolean(true);
		} else if (temp.equals("false")) {
			return new Boolean(false);
		} else {
			return null;
		}
	}
}
