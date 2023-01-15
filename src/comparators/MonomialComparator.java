package comparators;

import java.util.Comparator;

import arithmetic.MultivariableMonomial;

public class MonomialComparator implements Comparator<MultivariableMonomial> {

	@Override
	public int compare(MultivariableMonomial m1, MultivariableMonomial m2) {
		
		if (m1.isSingleVaraiable() && m2.isSingleVaraiable()) {
			
		
			String var1 = m1.getVariablesList().get(0).getExpr();
			String var2 = m2.getVariablesList().get(0).getExpr();
			
			if (var1.equals(var2)) {
				return Double.compare(m1.getMaxDegree(), m2.getMaxDegree());
			}
		}
		
		return -1;
	}

}
