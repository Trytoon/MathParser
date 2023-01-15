package arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import ExpressionTree.Token;
import comparators.MonomialComparator;

public class MultivariablePolynomial {
	
	ArrayList<MultivariableMonomial> poly;
	
	
	public MultivariablePolynomial() {
		poly = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		String res = "";
		for (MultivariableMonomial m : this.poly) {
			res += m.toString() + " + ";
		}
		
		if (res.isEmpty()) {
			return "";
		}
		
		return res.substring(0, res.length() - 2).trim();		
	}
	
	public ArrayList<MultivariableMonomial> getMonomials() {
		return this.poly;
	}
	
	public MultivariablePolynomial sumWith(MultivariablePolynomial p) {
		MultivariablePolynomial result = new MultivariablePolynomial();
	    result.getMonomials().addAll(this.getMonomials());
	    result.getMonomials().addAll(p.getMonomials());
	    
	    return result.simplifyPolynomial();
	}
	
	public MultivariablePolynomial simplifyPolynomial() {
		Collections.sort(this.poly, new MonomialComparator());
		
		MultivariablePolynomial result = new MultivariablePolynomial();
		
		for (int i = 0; i < this.poly.size(); i++) {
			
			double coef = poly.get(i).coeff;
			HashMap<Token, Double> variables = poly.get(i).getVariables();
			
			
			while (i < poly.size() - 1 && poly.get(i).getDegreesList().equals(poly.get(i+1).getDegreesList())
					&& poly.get(i).getVariablesList().equals(poly.get(i+1).getVariablesList())) {
				
				coef += poly.get(i+1).coeff;
				i++;
			}
			
			MultivariableMonomial m = new MultivariableMonomial(coef, variables);
			result.poly.add(m);
			
		}
		
		return result;
		
	}
	
	public MultivariablePolynomial multiplyWith(MultivariablePolynomial p2) {
		MultivariablePolynomial result = new MultivariablePolynomial();
		
		for (int i = 0; i < this.poly.size(); i++) {
			for (int j = 0; j < p2.poly.size(); j++) {
				result.poly.add(MultivariableMonomial.multiplyMonomials(this.poly.get(i), p2.poly.get(j)));		
			}
		}
		
		return result.simplifyPolynomial();
	}
}
