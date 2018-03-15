package caseStudies;

import java.util.HashMap;
import java.util.Map;

import com.variamos.hlcl.core.HlclProgram;
import com.variamos.hlcl.model.domains.IntervalDomain;
import com.variamos.hlcl.model.expressions.ComparisonExpression;
import com.variamos.hlcl.model.expressions.HlclFactory;
import com.variamos.hlcl.model.expressions.Identifier;
import com.variamos.hlcl.model.expressions.IntBooleanExpression;
import com.variamos.hlcl.model.expressions.IntNumericExpression;
import com.variamos.hlcl.model.expressions.NumericIdentifier;

public class Example1 implements CaseStudy {
	static private HlclFactory f = new HlclFactory();
	static HlclProgram prog = new HlclProgram();
	static private Identifier  K, M, N, O;
	static private NumericIdentifier uno, dos, tres, cuatro, cinco;
	static private IntBooleanExpression c1, c2, c3, c4;
	static private ComparisonExpression oc1, oc2, oc3;
	
	public Example1(){
		initVariables();
		initNumbers();
		initConstraints();
	}
	
	public static void initVariables(){
		Map<String, Identifier> idMap = new HashMap<>();
		K= f.newIdentifier("K");
		IntervalDomain domainK= new IntervalDomain();
		domainK.add(10);
		domainK.add(11);
		domainK.add(12);
		domainK.add(13);
		domainK.add(14);
		K.setDomain(domainK);

		M= f.newIdentifier("M");
		IntervalDomain domainM= new IntervalDomain();
		domainM.add(3);
		domainM.add(5);
		domainM.add(7);
		domainM.add(9);
		M.setDomain(domainM);
		
		
		N= f.newIdentifier("N");
		IntervalDomain domainN= new IntervalDomain();
		domainN.add(2);
		domainN.add(4);
		domainN.add(6);
		domainN.add(8);
		N.setDomain(domainN);
		
		O = f.newIdentifier("O");
		IntervalDomain domainO= new IntervalDomain();
		domainO.add(1);
		domainO.add(2);
		domainO.add(3);
		domainO.add(5);
		domainO.add(8);
		O.setDomain(domainO);
		
	}
	
	static public void initNumbers(){
		uno= f.number(1);
		dos= f.number(2);
		tres= f.number(3);
		cuatro= f.number(4);
		cinco= f.number(5);
		
	}
	
	static public void initConstraints(){
		/*
		 * 1. componer la expresion de forma binaria para cada restriccion
		 * 2. escribir la restriccion
		 * 3. a�adir la restriccion
		 */
		//C1: K < M  
		c1= f.greaterThan(K, M);	
		prog.add(c1);

		
		//C2: O + M <=N   
		IntNumericExpression c2Left;
		c2Left= f.sum(O, M);
		c2= f.lessOrEqualsThan(c2Left, N);
		prog.add(c2);

		
		//C3: K - N > 5  
		IntNumericExpression c3Left;
		c3Left= f.diff(K, N);
		c3=f.greaterThan(c3Left, cinco);
		prog.add(c3);

		
		//N < 4
		c4= f.lessThan(N, cuatro);
		prog.add(c4);
		
	}
	



	public HlclProgram getHLCL(){
		return prog;
	}
	
	public String getStart(){
		return K.getId();
	}

}
