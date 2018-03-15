package testCaseStudies;

import com.variamos.common.core.exceptions.FunctionalException;

import caseStudies.CaseStudy;
import caseStudies.Example1;
import caseStudies.LoganHLCL;
import util.TestCaseInstance;

public class TestCaseStudies {
	
	
	private TestCaseInstance felferning;
	private TestCaseInstance carElectronics;
	
	private CaseStudy logan;
	private CaseStudy example1;
	
	private TestCaseInstance test;
	
	
	public void runTests() throws  Exception{
		
		//testing logan
		logan= new LoganHLCL();
		test= new TestCaseInstance("/Users/Angela/Test/Tests_Medic_HLCL/case_studies/logan/", "logan",logan);
		// 10 iterations in the search path, 1 execution of the algorithm.
		test.startTests(10,1);
		
		//testing example 1
		example1= new Example1();
		test= new TestCaseInstance("/Users/Angela/Test/Tests_Medic_HLCL/case_studies/example1/", "example1",example1);
		test.startTests(10,1);
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TestCaseStudies testClass= new TestCaseStudies();
		testClass.runTests();
		System.out.println("end tests");

	}

}
