package testRenault;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.variamos.common.core.utilities.FileUtils;
import com.variamos.common.model.enums.NotationType;
import com.variamos.hlcl.core.HlclProgram;
import com.variamos.hlcl.model.expressions.ComparisonExpression;
import com.variamos.hlcl.model.expressions.HlclFactory;
import com.variamos.hlcl.model.expressions.Identifier;
import com.variamos.hlcl.model.expressions.NumericIdentifier;
import com.variamos.reasoning.core.transformer.FeatureModelSPLOTransformer;
import com.variamos.reasoning.defectAnalyzer.model.dto.VMTransformerInDTO;
import com.variamos.reasoning.defectAnalyzer.model.transformation.VariabilityModel;
import com.variamos.solver.core.SWIPrologSolver;
import com.variamos.solver.core.compiler.Hlcl2SWIProlog;
import com.variamos.solver.core.compiler.PrologTransformParameters;

import HlclParser.XCSPHlclParser;
//import cspElements.Constraint;
//import cspElements.Constraint;
import testSplotModels.TestSPLOTModels;
import util.TestCaseFile;

public class TestRenaultCases {
	public final static String IN_TEST_PATH="/Users/Angela/Test/Tests_Medic_HLCL/Renault_Models/Models/";
	public final static String OUT_TEST_PATH="/Users/Angela/Test/Tests_Medic_HLCL/Renault_Models/Logs/";
	
	public static final String FILE_EXTENSION = ".xml";
	static private XCSPHlclParser Xparser;


	private int numberTests;
	static private HlclFactory f = new HlclFactory();
	//static private NumericIdentifier uno, dos, tres, cuatro, cinco;
	
	static private NumericIdentifier	cero= f.number(0);
	static private NumericIdentifier	uno= f.number(1);
	static private NumericIdentifier	dos= f.number(2);
	static private NumericIdentifier	tres= f.number(3);
	static private NumericIdentifier	cuatro= f.number(4);
	static private NumericIdentifier	cinco= f.number(5);
	static private String report ="";
	private BufferedWriter out;

	
	

	
	public TestRenaultCases(){
		numberTests=0;
		
	}
	
	
	
	public static void main(String[] args){
		
		TestRenaultCases tester= new  TestRenaultCases();
		
		List<File> filesList = FileUtils.readFileFromDirectory(IN_TEST_PATH);
		

		
		
		for (File file : filesList) {
			try {
				String fileName= file.getName();
				//System.out.println(fileName);

				if (fileName.endsWith(FILE_EXTENSION)) {
					String shortName, path;
					shortName= fileName.replaceFirst(FILE_EXTENSION, "");
					path=IN_TEST_PATH+ fileName;
					
					//se inicializa elparser
					Xparser = new XCSPHlclParser(path);
					System.gc();
					//OBTENER EL PROGRAMA llamando al parser

					HlclProgram csp= Xparser.getCSP();
					
					Map<String, Identifier> idMap = Xparser.getIdMap();
				
					
					//System.out.println(shortName+" "+params.getPathToTransform());
					tester.runTest(shortName, csp, idMap); 
				}
			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("Excepcion transformando modelo"
						+ file.getName());
			}
		}
		


		System.out.println(report);	
		System.out.println("end tests");

	}
	
	
public void runTest(String name, HlclProgram csp, Map<String, Identifier> idMap ){
		
		// con la ruta del archivo es necesario
		// transformar el archivo en un variamibility model
		// obtener el hlcl
		// organizar los parámetros para las pruebas : nombre del problema
		// ruta en donde se alojan los resultados de las pruebas
	

//	System.out.println(prologProgram);
	
	
	
	    String root="";
	
	    
	    if (name.equals("Renault-souffleuse-pos")){
	    	root="X0_0";
	    	nuevasSouffleusePos(csp, idMap);
	    	
	    }else if(name.equals("Renault-small")){
	    	root="X0_0";
	    	nuevasSmall(csp, idMap);
	    	
	    }else if (name.equals("Renault-small-pos")){
	    	root="X0_0";
	    	nuevasSmallPos(csp, idMap);
	    	
	    }else if (name.equals("Renault-megane-pos")){
	    	root="X0";
	    	nuevasMegane(csp, idMap);
	    	
	    }else if (name.equals("Renault-medium")){
	    	root="X0_0";
	    	nuevasMedium(csp, idMap);
	    	
	    }else if (name.equals("Renault-medium-pos")){
	    	root="X0_0";
	    	nuevasMediumPos(csp, idMap);
	    	
	    }else if (name.equals("Renault-master-pos")){
	    	root="X0";
	    	nuevasMaster(csp, idMap);
	    	
	    }else if (name.equals("Renault-big")){
	    	root="X0_0";
	    	nuevasBig(csp, idMap);
	    }else if (name.equals("Renault-big-pos")){
	    	root="X0_0";
	    	nuevasBigPos(csp, idMap);
	    	
	    }
		
//		PrologTransformParameters params = new PrologTransformParameters();
//		Hlcl2SWIProlog swiPrologTransformer = new Hlcl2SWIProlog();
//		String prologProgram = swiPrologTransformer.transform(csp, params);
//		System.out.println(prologProgram);
	    
		String path=OUT_TEST_PATH+name+"/";
		
//		PrologTransformParameters params = new PrologTransformParameters();
//		Hlcl2SWIProlog swiPrologTransformer = new Hlcl2SWIProlog();
//		String prologProgram = swiPrologTransformer.transform(csp, params);		
//		String prologFile= path+name+".pl";
//		try {
//			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(prologFile)));
//			//poner labeling
//			//sb.append(LABELING);
//			out.write(prologProgram);
//			//System.out.println(sb.toString()+".");
//			out.flush();
//			out.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			
//			e.printStackTrace();
//		}
		
		
		SWIPrologSolver swiSolver= new SWIPrologSolver();
		swiSolver.setHLCLProgram(csp);
		//swiSolver.solve(new SolverSolution(), new ConfigurationOptionsDTO());
		swiSolver.solve();
		boolean consistent = swiSolver.hasSolution();
		System.gc();
		if(!consistent){
		TestCaseFile test = new TestCaseFile(path, name, csp,"RAND"); // max, min, rand
		report +=test.startTestsRenault(10, 1);
		numberTests++;
		} else
			report += "\n" + name + "  is consistent";
		System.out.println();
		System.out.println();
		
		
	}

public void nuevasSouffleusePos( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	cn1= f.equals(idMap.get("X2_0"), dos);
	csp.add(cn1);
	
	cn2= f.equals(idMap.get("X0_4"), dos);
	csp.add(cn2);
	
	cn3= f.equals(idMap.get("X5_3"), cero);
	csp.add(cn3);

	cn4= f.equals(idMap.get("X3"), cuatro);
	csp.add(cn4);

	cn5= f.equals(idMap.get("X0_1"), cero);
	csp.add(cn5);

	cn6= f.notEquals(idMap.get("X8_1"), cero);
	csp.add(cn6);
	
}

public void nuevasSmall( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	
//	
////	cn1= new Constraint("CN1","X6_0 #=0");
////	csp.addConstaint(cn1);
////	csp.addVarToConstraint(cn1, "X6_0");
	
	cn1= f.equals(idMap.get("X6_0"), cero);
	csp.add(cn1);

//	
//	cn2= new Constraint("CN2","X7_77#\\=1");
//	csp.addConstaint(cn2);
//	csp.addVarToConstraint(cn2, "X7_77");
	
	cn2= f.notEquals(idMap.get("X7_77"), uno);
	csp.add(cn2);
//	

	
}

public void nuevasSmallPos( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;

//X3_4 #=0,
//X5_0 #=0,
//X1_5 #=1,
//X7_72#=1,
//X7_75#=1,
//X7_80#=1,
//cn1= new Constraint("CN1","X3_4 #=0");
//csp.addConstaint(cn1);
//csp.addVarToConstraint(cn1, "X3_4");
	cn1= f.equals(idMap.get("X3_4"), cero);
	csp.add(cn1);	
	
//
//cn2= new Constraint("CN2","X5_0 #=0");
//csp.addConstaint(cn2);
//csp.addVarToConstraint(cn2, "X5_0");
	
	cn2= f.equals(idMap.get("X5_0"), cero);
	csp.add(cn2);
	
	
//cn3= new Constraint("CN3","X1_5 #=1");
//csp.addConstaint(cn3);
//csp.addVarToConstraint(cn3, "X1_5");
	cn3= f.equals(idMap.get("X1_5"), uno);
	csp.add(cn3);	
	
//
//cn4= new Constraint("CN4","X7_72 #=1");
//csp.addConstaint(cn4);
//csp.addVarToConstraint(cn4, );
	cn4= f.equals(idMap.get("X7_72"), uno);
	csp.add(cn4);
	
//
//cn5= new Constraint("CN5","X7_75#=1");
//csp.addConstaint(cn5);
//csp.addVarToConstraint(cn5, "X7_75");
	cn5= f.equals(idMap.get("X7_75"), uno);
	csp.add(cn5);
	

//
//cn6= new Constraint("CN6","X7_80#=1");
//csp.addConstaint(cn6);
//csp.addVarToConstraint(cn6, "X7_80");

	cn6= f.equals(idMap.get("X7_80"), uno);
	csp.add(cn6);
}


public void nuevasMegane( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	
	cn1= f.equals(idMap.get("X3_51"), uno);
	csp.add(cn1);
	
	cn2= f.notEquals(idMap.get("X7_14"), uno);
	csp.add(cn2);
}
public void nuevasMediumPos( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	
	
	cn1= f.equals(idMap.get("X3_3"), cero);
	csp.add(cn1);
//
	
	cn2= f.equals(idMap.get("X5_5"), cero);
	csp.add(cn2);


	cn3= f.notEquals(idMap.get("X7_0"), uno);
	csp.add(cn3);


}
public void nuevasMedium( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	
	cn1= f.equals(idMap.get("X3_3"), cero);
	csp.add(cn1);

	
	cn2= f.equals(idMap.get("X3_2"), dos);
	csp.add(cn2);


	cn3= f.equals(idMap.get("X1_1"), cero);
	csp.add(cn3);
	
	cn4= f.equals(idMap.get("X4_5"), cero);
	csp.add(cn4);

}
public void nuevasMaster( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;


	cn1= f.equals(idMap.get("X2_1"), uno);
	csp.add(cn1);


	cn2= f.equals(idMap.get("X4_23"), cero);
	csp.add(cn2);
}
public void nuevasBig( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;


	cn1= f.equals(idMap.get("X4_49"), cero);
	csp.add(cn1);


	cn2= f.equals(idMap.get("X5"), cinco);
	csp.add(cn2);

}

public void nuevasBigPos( HlclProgram csp, Map<String, Identifier> idMap){
	
	ComparisonExpression  cn1, cn2, cn3, cn4, cn5, cn6;
	

	
	cn1= f.equals(idMap.get("X9_2"), cero);
	csp.add(cn1);
		
	cn2= f.equals(idMap.get("X9_3"), cero);
	csp.add(cn2);
}
}