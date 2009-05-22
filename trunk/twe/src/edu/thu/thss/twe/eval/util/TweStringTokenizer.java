package edu.thu.thss.twe.eval.util;

public class TweStringTokenizer {
	private String string = null;
	private int index = -1;

	public TweStringTokenizer(String string) {
		this.string = string;
		index = -1;
	}

	public void reset() {
		index = -1;
	}

	// public static void main(String[] args) {
	// TweStringTokenizer n = new TweStringTokenizer("( 1 + 5 ) * 6fdafd");
	// String temp = null;
	// while ((temp = n.nextToken()) != null) {
	// System.out.println("["+temp+"]");
	// }
	// }

	public String nextToken() {
		skipSpace();
		if (index >= string.length())
			return null;
		char c = string.charAt(index);
		if (isOperatorChar(c)) {
			return resolveOperator();
		} else if (isBracketChar(c)) {
			return resolveBracket();
		} else {
			return resolveString();
		}
	}

	public String peekNextToken() {
		int temp = index;
		String token = nextToken();
		index = temp;
		return token;
	}

	private String resolveString() {
		int i = index;
		while (i < string.length()) {
			char c = string.charAt(i);
			if (isSpaceChar(c) || isOperatorChar(c) || isBracketChar(c)) {
				break;
			}
			i++;
		}
		String str = string.substring(index, i);
		index = i - 1;
		return str;
	}

	private String resolveBracket() {
		String str = string.substring(index, index + 1);
		return str;
	}

	private String resolveOperator() {
		int i = index;
		while (i < string.length()) {
			char c = string.charAt(i);
			if (!isOperatorChar(c)) {
				break;
			}
			i++;
		}
		String str = string.substring(index, i);
		index = i - 1;
		return str;
	}

	private void skipSpace() {
		index++;
		while (index < string.length() && isSpaceChar(string.charAt(index))) {
			index++;
		}
	}

	private boolean isSpaceChar(char c) {
		return (c == ' ' || c == '\r' || c == '\n' || c == '\t');
	}

	private boolean isOperatorChar(char c) {
		return (c == '+' || c == '-' || c == '*' || c == '/' || c == '='
				|| c == '>' || c == '<');
	}

	private boolean isBracketChar(char c) {
		return (c == '(' || c == ')');
	}
}
