package arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import ExpressionTree.ExpressionTree;
import ExpressionTree.Token;
import ExpressionTree.TokenType;
import ExpressionTree.Tokenizer;

public class MultivariableMonomial {

	
	double coeff;
	
	//Token = variables: 'x', 'y' etc... and Double = degree 2, 3 or 4 ...
	HashMap<Token, Double> variables = new HashMap<>();


	public MultivariableMonomial(double coeff, HashMap<Token, Double> variables) {
		this.coeff = coeff;
		this.variables = variables;
	}
	
	
	public  MultivariableMonomial(double coeff, Token t, Double degree) {
		this.coeff = coeff;
		variables.put(t, degree);
	}
	
	public MultivariableMonomial() {
		coeff = 0.0d;
		variables = null;
	}
	
	public double getCoeff() {
		return this.coeff;
	}
	
	public double getDegreeOfVariable(Token t) {
		return variables.get(t);
	}
	
	public int getVariablesAmount() {
		return variables.entrySet().size();
	}
	
	public boolean isConstant() {
		return variables.isEmpty();
	}
	
	public boolean isSingleVaraiable() {
		return this.variables.size() == 1;
	}
	
	public HashMap<Token, Double> getVariables() {
		return variables;
	}

	public void setVariables(HashMap<Token, Double> variables) {
		this.variables = variables;
	}
	

	public void setCoeff(double coeff) {
		this.coeff = coeff;
	}
	
	public ArrayList<Token> getVariablesList() {
		return new ArrayList<>(variables.keySet());
	}
	
	public ArrayList<Double> getDegreesList() {
		return new ArrayList<>(variables.values());
	}
	
	
	public static boolean canBeConvertedToTerm(ExpressionTree tree) {
		
		if (tree == null) {
			return true;
		}
		
		if (tree.getRight() == null && tree.getLeft() == null) {
			return true;
		} else if (tree.getValue().getType().equals(TokenType.FUNCTION)) {
			return false;
		}
		
		if (tree.getValue().getType().ordinal() <= 1) {
			return tree.getLeft().getvariablesAmountInTree() == 0 && tree.getRight().getvariablesAmountInTree() == 0;
		}
		
		if (tree.getValue().getType().equals(TokenType.DIV)) {
			
			if (tree.getRight().getvariablesAmountInTree() != 0) {
				return false;
			}
			
			if (tree.getLeft().getValue().getType().equals(TokenType.NUMBER) && tree.getLeft().getValue().getType().equals(TokenType.NUMBER)) {
				return true;
			}
			
			if (tree.getLeft().getValue().getType() == TokenType.VARIABLE && tree.getLeft().getValue().getType() == TokenType.NUMBER) {
				return true;
			}
			
			
			
			return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		}
		
		if (tree.getValue().getType().equals(TokenType.POW)) {
			
			if (tree.getRight().getvariablesAmountInTree() != 0) {
				return false;
			}
			
			return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		}
		
		return canBeConvertedToTerm(tree.getRight()) && canBeConvertedToTerm(tree.getLeft());
		
	}
	
	public MultivariableMonomial getMonomialRepresentation(String expr) {
		
		
		ExpressionTree tree = new ExpressionTree();
		tree = tree.generateTreeFromExpression(expr);
		
		if (!canBeConvertedToTerm(tree)) {
			System.out.println(false);
			return null;
		}
		
		
		// Extract each single variable monomial and coefficients
		expr = Tokenizer.computerFormat(expr);
		String[] valeurs = expr.split("\\*(?![^()]*\\))");
		Arrays.sort(valeurs);
		
		for (int i = 0; i < valeurs.length; i++) {
			valeurs[i] = valeurs[i].replaceAll("([aA-zZ]\\)?)$", "$1\\^1");
			//System.out.println(valeurs[i]);
		}
		
		//Get the representation
		ArrayList<ExpressionTree> trees = new ArrayList<>();
		
		for (String s: valeurs) {
			
			ExpressionTree temp = new ExpressionTree();
			temp = temp.generateTreeFromExpression(s);
			trees.add(temp);
		}
		
		HashMap<Token, Double> variables = new HashMap<>();
		
		double coeff = 1;
		for (int i = 0; i < trees.size(); i++) {
			if (ExpressionTree.isNumeric(trees.get(i))) {
				coeff *= ExpressionTree.evalTree(trees.get(i));
			} else {
				Token tokenTemp = trees.get(i).getLeft().getValue();
				double degreeTemp = 0;
				if (tokenTemp != null) {
					

					
					while(trees.get(i).getLeft().getValue().equals(tokenTemp)) {
						//Apply negation if needed
						if (trees.get(i).getRight().getValue().getExpr().equals("~")) {
							String negative = "";
							negative += trees.get(i).getRight().getLeft().getValue().getExpr();
							Token t = new Token("-" +negative, TokenType.NUMBER);			
							trees.get(i).setRight(new ExpressionTree(t, null, null));
						}
						degreeTemp += ExpressionTree.evalTree(trees.get(i).getRight());
						i++;
						
						if (i >= trees.size()) {
							break;
						}
					}
					variables.put(tokenTemp, degreeTemp);
				}
				
				i--;
			}
		}
		return new MultivariableMonomial(coeff, variables);
	}
	
	public double getMaxDegree() {
		
		double max = 0;
		
		for (Token t: variables.keySet()) {
		    max = Math.max(max, variables.get(t));
		}
		
		return max;
	}
	
	@Override
	public String toString() {
		String toPrint = "" + this.coeff;
		for(Token t: this.variables.keySet()) {
			toPrint += t.getExpr();
			if(this.variables.get(t) != 1d) {
				toPrint += "^" +this.variables.get(t);
			}
		}
		return toPrint;
		
	}
	
	public static MultivariableMonomial multiplyMonomials(MultivariableMonomial monomial, MultivariableMonomial monomial2) {
		double coeff = monomial.getCoeff() * monomial2.getCoeff();
		HashMap<Token, Double> variables = new HashMap<>();
		
		
		if (!monomial.isConstant()) {
			for (Token t : monomial.getVariables().keySet()) {
				if (monomial2.getVariables().containsKey(t)) {
					variables.put(t, monomial.getVariables().get(t) + monomial2.getVariables().get(t));
				} else {
					variables.put(t, monomial.getVariables().get(t));
				}
			}
			
			// Add variables of p2 that were not multiplied
			for (Token t : monomial2.getVariables().keySet()) {
				if (!variables.containsKey(t)) {
					variables.put(t, monomial2.getVariables().get(t));
				}
			}
		} else {
			variables = monomial2.getVariables();
		}
		
	
		return new MultivariableMonomial(coeff, variables);
	}


	public static void main(String[] args) {
		
		MultivariableMonomial monomial = new MultivariableMonomial();
		monomial = monomial.getMonomialRepresentation("2");
		if (monomial != null) {
			System.out.println(monomial);	
		}
		
		System.out.println(monomial.variables.entrySet());
		System.out.println(monomial.getMaxDegree());

	}

}
