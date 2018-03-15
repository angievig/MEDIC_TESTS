package util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.variamos.hlcl.core.HlclProgram;
import com.variamos.hlcl.model.expressions.IntBooleanExpression;
import com.variamos.reasoning.defectAnalyzer.model.transformation.VariabilityModel;
import com.variamos.reasoning.medic.model.diagnoseAlgorithm.MinimalSetsDFSIterationsHLCL;
import com.variamos.reasoning.medic.model.graph.NodeConstraintHLCL;
import com.variamos.reasoning.medic.model.graph.NodeVariableHLCL;
import com.variamos.reasoning.medic.model.graph.VertexHLCL;
import com.variamos.reasoning.util.ConstraintRepresentationUtil;
import com.variamos.reasoning.util.LogParameters;
import com.variamos.solver.core.SWIPrologSolver;

import caseStudies.CaseStudy;
//import guiElements.Window;

public class TestCaseFile extends TestCase{
	
	VariabilityModel vmodel;
	String root;
	HlclProgram model;
	String report;

	/**
	 * Initializes the test case with the path and the name of the problem
	 * @param path
	 * @param name
	 */
	public TestCaseFile(String path, String name, VariabilityModel v, String root){
		setProblemPath( path);
		setProblemName(name);
		this.root=root;
		vmodel= v;
		
	}
	
	/**
	 * Initializes the test case with the path and the name of the problem
	 * @param path
	 * @param name
	 */
	public TestCaseFile(String path, String name,  HlclProgram model, String root ){
		setProblemPath( path);
		setProblemName(name);
		this.root=root;
		this.model= model;
		
	}
	public void startTests(int iterations, int executions){
		
		System.out.println("starting diagnosis");
		report =getProblemName() + ", ";


		SWIPrologSolver swiSolver= new SWIPrologSolver();
		swiSolver.setHLCLProgram(vmodel.getModel());
		//swiSolver.solve(new SolverSolution(), new ConfigurationOptionsDTO());
		swiSolver.solve();
		boolean consistent = swiSolver.hasSolution();
		if (!consistent){
			LinkedList<VertexHLCL> pathNodes;
		LogParameters log= new LogParameters(getProblemPath(), getProblemName());
		MinimalSetsDFSIterationsHLCL minimal=null;;
		for (int i = 0; i < executions; i++) {
		minimal = new MinimalSetsDFSIterationsHLCL(vmodel.getModel(), log);
		//Window ventana1= new Window(minimal.getGraph(), "initial graph");
		pathNodes =minimal.sourceOfInconsistentConstraints(root, iterations);
		//report+= minimal.graphInfo()+ ","+ minimal.getIter()+ ","+minimal.getPathLenght()+ ","+minimal.getTotal() + ",";
		//report+= minimal.getTime() + ", ";
		//report+= minimal.sizePath();
		//report+= minimal.graphInfo()+ ","+"  ,"+ minimal.getIter()+ ","+minimal.getPathLenght()+ ","+minimal.getTotal()+" ," + ","+ minimal.sizePath()+" ,";
		//report+=  minimal.graphInfo()+ ","+ minimal.getPathLenght()+","+ minimal.getIter()+ ","+minimal.getTotal();// ","+ minimal.sizePath()+" ,";
		report+=  minimal.getPathLenght()+","+ minimal.getNumVars();
		//report+= minimal.graphInfo()+ ",  ," ;
		//report+= minimal.getTotal();

//		
//		int totalVertices=0;
//		for (VertexHLCL vertex : pathNodes) {
//			totalVertices++;
//			if(vertex instanceof NodeVariableHLCL){
//				report+=(vertex.getId()+ " ");
//				report+=( "(");
//				for (NodeConstraintHLCL cons : ((NodeVariableHLCL) vertex).getUnary()) {
//					totalVertices++;
//					report+=(cons.getId()+ " -");
//				}
//				report+=( ") - ");
//			}else 
//			   report+=(vertex.getId()+ " -");
//		}
		
		//report+= minimal.getPathLenght();
		System.gc();
		}
		//report+= minimal.getIter();
		
		}
		else{
			report+= getProblemName() + " is consistent";
		}
	 report +="\n";

		
	}
	
	public String getReport(){
		return report;
	}
		
		public String startTestsRenault(int iterations, int executions){
			
			String report =getProblemName() + ",";
			String report2 =getProblemName() + ",";
			System.out.println("starting diagnosis");
			
			LogParameters log= new LogParameters(getProblemPath(), getProblemName());
			for (int i = 0; i < executions; i++) {
			MinimalSetsDFSIterationsHLCL minimal = new MinimalSetsDFSIterationsHLCL(model, log);
			//minimal.printNetwork(minimal.getGraph());
			//Window ventana1= new Window(minimal.getGraph(), "initial graph");
			LinkedList<VertexHLCL> pathNodes =minimal.sourceOfInconsistentConstraints(root, iterations);
			report+=  minimal.getPathLenght()+","+ minimal.getNumVars();// ","+ minimal.sizePath()+" ,";
			//report+= minimal.graphInfo()+ ","+" ,"+ minimal.getIter()+ ","+minimal.getPathLenght()+ ","+minimal.getTotal()+" ," + ","+ minimal.sizePath()+" ,";
//			report+= minimal.getTime() + ", ";
//			report2+= minimal.getPathLenght() + ", ";
			
//			int totalVertices=0;
//			for (VertexHLCL vertex : pathNodes) {
//				totalVertices++;
//				if(vertex instanceof NodeVariableHLCL){
//					report+=(vertex.getId()+ " ");
//					report+=( "(");
//					for (NodeConstraintHLCL cons : ((NodeVariableHLCL) vertex).getUnary()) {
//						totalVertices++;
//						report+=(cons.getId()+ " -");
//					}
//					report+=( ") - ");
//				}else 
//				   report+=(vertex.getId()+ " -");
//			}

			System.gc();
			}
			//System.out.println(report);
			
		return report +"\n" ;
		//return report +"\n" + report2;
		
		//TODO To print the path
//		System.out.println("Path " + pathNodes.size());
//		for (Vertex vertex : pathNodes) {
//			System.out.print(vertex.getId()+ " ");
//		}
//		System.out.println();
		
	

	}

}
