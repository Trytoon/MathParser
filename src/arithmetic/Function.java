package arithmetic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ExpressionTree.ExpressionTree;
import ExpressionTree.Token;
import ExpressionTree.TokenType;
import data.Register;

public class Function implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	String name;
	String infix; //To print the function after it is transformed
	ArrayList<Token> variables;
	ExpressionTree rep;
	
	public Function(String name, String infix, ArrayList<Token> variables, ExpressionTree rep) {
		this.name = name;
		this.infix = infix;
		this.variables = variables;
		this.rep = rep;
	}
	
	public Function() {
		this.name = "";
		this.variables = new ArrayList<Token>();
		this.rep = new ExpressionTree();
	}
	
	public String toString() {
		return ExpressionTree.retrieveExpression(rep);
	}
	
	public String getName() {
		return this.name;
	}
	
	public ExpressionTree getExpr() {
		return this.rep;
	}
	
	public void updateInfix() {
		this.infix = ExpressionTree.retrieveExpression(rep);
	}
	
	public void setInfix(String inf) {
		this.infix = inf;
	}
	
	public ArrayList<Token> getVariables() {
		return this.variables;
	}
	
	
	/*
	 * Parse a function written like f(x,...) = ...
	 */
	public static Function parseFunctionDefinition(String definition) {
		definition = definition.replaceAll(" ", "");
		String[] splitted = definition.split("=");
		
		if (splitted.length != 2) {
			return null;
		} else {
			Pattern p = Pattern.compile("^([aA-zZ][aA-zZ|\\d]*)\\(([aA-zZ])(,([aA-zZ]))*\\)$");
			Matcher m = p.matcher(splitted[0]);
			
			ArrayList<Token> variables = new ArrayList<>();
			String name = "";
			
			//True only if f has correct definition
			if (m.find()) {
				int i = 0;
				String declaration = m.group();
				while(declaration.charAt(i) != '(') {
					name += declaration.charAt(i);
					i++;
				}
				
				i++;
				
				while(i < declaration.length()) {
					variables.add(new Token(""+declaration.charAt(i), TokenType.VARIABLE));
					i+=2;
				}
				
				ExpressionTree representation = new ExpressionTree();
				try {
					representation = representation.generateTreeFromExpression(splitted[1]);
				} catch (Exception e) {
					return null;
				}
				
				if (variables.size() < representation.getvariablesAmountInTree()) {
					return null;
				}
				
				return new Function(name, splitted[1], variables, representation);	
			} else {
				return null;
			}
		}
	}
	
	
	public boolean instanciateFunction(String expr) {
		Function f = parseFunctionDefinition(expr);
		
		if (f != null) {
			if (!Register.functionExists(f)) {
				Register.addToRegister(f);
			}
			return true;
		}
		return false;
	}
}
