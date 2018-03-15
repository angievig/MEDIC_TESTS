package util;

import java.util.LinkedList;
import com.variamos.common.core.exceptions.FunctionalException;

import com.variamos.reasoning.medic.model.diagnoseAlgorithm.LogManager;
import com.variamos.reasoning.medic.model.diagnoseAlgorithm.MinimalSetsDFSIterationsHLCL;
import com.variamos.reasoning.medic.model.graph.VertexHLCL;
import com.variamos.reasoning.util.LogParameters;

import caseStudies.CaseStudy;

//import graphicConstraintNetwork.Window;


public class TestCaseInstance extends TestCase{

	
	/**
	 * Initializes the test case with the path and the name of the problem
	 * @param path
	 * @param name
	 */
	public TestCaseInstance(String path, String name, CaseStudy problem){
		setProblemPath( path);
		setProblemName(name);
		setProblem(problem);
	
	}
	

	/**
	 * 
	 * @param net
	 * @param cant
	 * @param source
	 */
	public void startTests(int iterations, int executions){
		
		System.out.println("starting diagnosis");
		
		LogParameters log= new LogParameters(getProblemPath(), getProblemName());
		for (int i = 0; i < executions; i++) {
		MinimalSetsDFSIterationsHLCL minimal = new MinimalSetsDFSIterationsHLCL(getProblem().getHLCL(), log);
		LinkedList<VertexHLCL> pathNodes =minimal.sourceOfInconsistentConstraints(getProblem().getStart(), iterations);
		}
		
		//TODO To print the path
//		System.out.println("Path " + pathNodes.size());
//		for (Vertex vertex : pathNodes) {
//			System.out.print(vertex.getId()+ " ");
//		}
//		System.out.println();
		
	

	}


}



