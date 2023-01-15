package ExpressionTree;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	public static boolean isNumeric(String val) {
		try {
			float f = Float.parseFloat(val);
		} catch(NumberFormatException e) {
			return false;
		}

		return true;
	}
	
	public static String computerFormat(String expr) {
		expr = expr.replaceAll(" ", "");
		expr = expr.replaceAll("(\\d)([a-zA-Z])", "$1*$2");
		expr = expr.replaceAll("([a-zA-Z][a-zA-Z])\\*\\(", "$1\\(");
		expr = expr.replaceAll("^\\+*", "");
		expr = expr.replaceAll("^-", "~");
		expr = expr.replaceAll("/\\*\\(", "/\\(");
		expr = expr.replaceAll("\\*(e-?\\d)", "$1");
		expr = expr.replaceAll("--", "\\+");
		expr = expr.replaceAll("([\\*\\+/\\^-])\\-", "$1~");
		expr = expr.replaceAll("\\(\\-", "(~");
		expr = expr.replaceAll("[\\+]+", "+");
		expr = expr.replaceAll("-\\+", "-");
		expr = expr.replaceAll("\\+~", "-");
		expr = expr.replaceAll("(\\d)\\(", "$1\\*\\(");
		expr = expr.replaceAll("\\)(\\d)", "\\)\\*$1");
		expr = expr.replaceAll("\\)\\(", "\\)\\*\\(");
		expr = expr.replaceAll("\\)([aA-zZ])", "\\)\\*$1");
		expr = expr.replaceAll("ln\\(", "log");
		expr = expr.replaceAll("([aA-zZ]\\d)\\*\\(", "$1(");
		expr = expr.replaceAll("\\*\\^", "\\^");
		return expr;
	}

	public ArrayList<Token> getTokenList(String expr) {
		expr = computerFormat(expr);
		
		//System.out.println(expr);

		ArrayList<Token> tokenList = new ArrayList<>();

		Pattern p = Pattern.compile("[0-9]+(\\.[0-9]*)?([eE][\\+\\-]?[0-9]+)?|[A-Za-z_][A-Za-z_0-9]*|\\S", 0);
		Matcher m = p.matcher(expr);
		
		ArrayList<String> list = new ArrayList<>();

		while (m.find()) {
			list.add(m.group());
		}
		
		for (int i = 0; i < list.size(); i++) {
			String temp = list.get(i);
			if (temp.equals("(")) {
				tokenList.add(new Token(temp, TokenType.LPAR));
			} else if(temp.equals(")")) {
				tokenList.add(new Token(temp, TokenType.RPAR));
			} else if (isNumeric(temp)) {
				tokenList.add(new Token(temp, TokenType.NUMBER));
			}else if (temp.equals("+")) {
				tokenList.add(new Token(temp, TokenType.ADD));
			}else if (temp.equals("-")) {
				tokenList.add(new Token(temp, TokenType.SUB));
			}else if (temp.equals("/")) {
				tokenList.add(new Token(temp, TokenType.DIV));
			}else if (temp.equals("*")) {
				tokenList.add(new Token(temp, TokenType.MUL));
			}else if (temp.equals("^")) {
				tokenList.add(new Token(temp, TokenType.POW));
			}else if (temp.equals("~")) {
				tokenList.add(new Token(temp, TokenType.NEGATE));
			}else if (temp.length() == 1) {
				if (i+1 < list.size()) {
					if (list.get(i+1).equals("(")) {
						tokenList.add(new Token(temp, TokenType.FUNCTION));
					} else {
						tokenList.add(new Token(temp, TokenType.VARIABLE));
					}
				} else {
					tokenList.add(new Token(temp, TokenType.VARIABLE));
				}
			}else {
				tokenList.add(new Token(temp, TokenType.FUNCTION));
			}
		}

		return tokenList;
	}

	public ArrayList<Token> generatePostfix(String expr) {
		ArrayList<Token> tokens = getTokenList(expr);
		
		Stack<Token> operatorStack = new Stack<>();
		ArrayList<Token> output = new ArrayList<>();


		while (!tokens.isEmpty()) {
			Token t = tokens.get(0);

	
			if (t.getType() == TokenType.NUMBER || t.getType() == TokenType.VARIABLE) {
				output.add(t);
			} else if (t.getType() == TokenType.LPAR) {
				operatorStack.push(t);
			} else if (t.getType() == TokenType.RPAR) {
				while (!operatorStack.isEmpty() && operatorStack.lastElement().getType() != TokenType.LPAR) {
					output.add(operatorStack.lastElement());
					operatorStack.pop();
				}
				operatorStack.pop();
			} else {	
				while (!operatorStack.isEmpty() && operatorStack.lastElement().getType().getPrecendence() >= t.getType().getPrecendence()) {
					output.add(operatorStack.lastElement());
					operatorStack.pop();
				}
				operatorStack.add(t);
			}

			tokens.remove(0);
		}
		
		while (!operatorStack.isEmpty()) {
			output.add(operatorStack.lastElement());
			operatorStack.pop();
		}
		return output;
	}
}
