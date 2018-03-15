package util;

import com.variamos.common.core.exceptions.FunctionalException;
import com.variamos.reasoning.medic.model.diagnoseAlgorithm.LogManager;

import caseStudies.CaseStudy;

public abstract class TestCase {

	/**
	 * problemPath is the pat where the files will be placed
	 */
	private String problemPath;
	
	/**
	 * problemPath is the pat where the files will be placed
	 */
	private String problemName;
	/**
	 * CSP representing the problem to be solved
	 */
	private static CaseStudy problem;
	
	/**
	 * fman is the object of class LogManager to create the log files
	 */

	private static  LogManager fman;
	
	
	public abstract void startTests(int iterations, int executions);
	
	public String getProblemPath() {
		return problemPath;
	}
	public void setProblemPath(String problemPath) {
		this.problemPath = problemPath;
	}
	public String getProblemName() {
		return problemName;
	}
	public void setProblemName(String problemName) {
		this.problemName = problemName;
	}
	public static CaseStudy getProblem() {
		return problem;
	}
	public static void setProblem(CaseStudy problem) {
		TestCase.problem = problem;
	}
	public static LogManager getFman() {
		return fman;
	}
	public static void setFman(LogManager fman) {
		TestCase.fman = fman;
	}
}
