package caseStudies;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import com.variamos.hlcl.core.HlclProgram;
import com.variamos.hlcl.model.domains.IntervalDomain;
import com.variamos.hlcl.model.expressions.ComparisonExpression;
import com.variamos.hlcl.model.expressions.HlclFactory;
import com.variamos.hlcl.model.expressions.Identifier;
import com.variamos.hlcl.model.expressions.IntBooleanExpression;
import com.variamos.hlcl.model.expressions.NumericIdentifier;



public class LoganHLCL implements CaseStudy{
	static private HlclFactory f = new HlclFactory();
	static HlclProgram prog = new HlclProgram();
	static private Identifier  carBody, brakes, driveWheel, engine, tyreType;
	static private NumericIdentifier uno, dos, tres, cuatro, cinco;
	static private IntBooleanExpression c1, c2, c3, c4, c5 ;
	static private ComparisonExpression oc1, oc2, oc3;
	
	public LoganHLCL(){
		initVariables();
		initNumbers();
		initConstraints();
	}
	
	public static void initVariables(){
		Map<String, Identifier> idMap = new HashMap<>();
		carBody= f.newIdentifier("carBody");
		IntervalDomain domainCarBody= new IntervalDomain();
		domainCarBody.add(1);
		domainCarBody.add(2);
		domainCarBody.add(3);
		carBody.setDomain(domainCarBody);

		brakes= f.newIdentifier("brakes");
		IntervalDomain domainBrakes= new IntervalDomain();
		domainBrakes.add(1);
		domainBrakes.add(2);
		domainBrakes.add(3);
		brakes.setDomain(domainBrakes);
		
		
		driveWheel= f.newIdentifier("driveWheel");
		IntervalDomain domainDrive= new IntervalDomain();
		domainDrive.add(1);
		domainDrive.add(2);
		driveWheel.setDomain(domainDrive);
		
		engine = f.newIdentifier("engine");
		IntervalDomain domainEngine= new IntervalDomain();
		domainEngine.add(1);
		domainEngine.add(2);
		domainEngine.add(3);
		engine.setDomain(domainEngine);
		
		tyreType= f.newIdentifier("tyreType");
		IntervalDomain domainTyre= new IntervalDomain();
		domainTyre.add(1);
		domainTyre.add(2);
		tyreType.setDomain(domainTyre);
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
		// c1= new Constraint("C1","Carbody#= 1 #==> TyreType#\\=1");
		
		ComparisonExpression c1Left, c1Right;
		c1Left= f.equals(carBody, uno);
		c1Right= f.notEquals(tyreType, uno);
		c1= f.implies(c1Left, c1Right);	
		prog.add(c1);

		
		// C2= (Carbody#= 1 #==> Brakes#=1)  #/\\ (Carbody#= 2 #==> Brakes#=2)
		IntBooleanExpression c2Left, c2Right;
		ComparisonExpression c2Left_left, c2Left_right, c2Right_left, c2Right_right;
		c2Left_left= f.equals(carBody, uno);
		c2Left_right= f.equals(brakes, uno);
		c2Left= f.implies(c2Left_left, c2Left_right);
		c2Right_left= f.equals(carBody, dos);
		c2Right_right= f.equals(brakes, dos);
		c2Right= f.implies(c2Right_left, c2Right_right);
		c2= f.and(c2Left, c2Right);
		prog.add(c2);

		
		//c3= new Constraint("C3","Carbody#= 3 #==> (Brakes#=3 #/\\ DriveWheel#=2)");
		IntBooleanExpression c3Right;
		ComparisonExpression c3Left, c3Right_left, c3Right_right;
		c3Left= f.equals(carBody, tres);
		c3Right_left= f.equals(brakes, tres);
		c3Right_right= f.equals(driveWheel,dos);
		c3Right=f.and(c3Right_left, c3Right_right);
		c3=f.implies(c3Left, c3Right);
		prog.add(c3);

		
		//c4= new Constraint("C4","Brakes#= 1 #==> DriveWheel#\\=2");
		ComparisonExpression c4Left, c4Right;
		c4Left= f.equals(brakes, uno);
		c4Right= f.notEquals(driveWheel, dos);
		c4= f.implies(c4Left, c4Right);
		prog.add(c4);

		//		c5= new Constraint("C5","DriveWheel#= 1 #==> Engine#=1");
		
		ComparisonExpression c5Left, c5Right;
		c5Left= f.equals(driveWheel, uno);
		c5Right= f.equals(engine, uno);
		c5= f.implies(c5Left, c5Right);
		prog.add(c5);
		
		
		//oc1=new Constraint("OC1", "Carbody#=1");
		
		oc1= f.equals(carBody, uno);
		prog.add(oc1);
		
		// 		oc2=new Constraint("OC2", "Engine#\\= 1");
		
		oc2= f.notEquals(engine, uno);
		prog.add(oc2);
		
		// 		oc3=new Constraint("OC3", "TyreType#=2");
		
		oc3=f.equals(tyreType, dos);
		prog.add(oc3);
		
	}
	

	public HlclProgram getHLCL(){
		return prog;
	}
	
	public String getStart(){
		return carBody.getId();
	}
	


}
