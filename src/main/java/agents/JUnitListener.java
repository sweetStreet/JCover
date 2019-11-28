package main.java.agents;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class JUnitListener extends RunListener {

	private static int i = 1;

    public void testRunStarted(Description description) throws Exception {
    		

    	if (null == CoverageCollection.testCase_Coverages)
		{
			CoverageCollection.testCase_Coverages = new HashMap<String, HashMap<String, HashSet<Integer>>>();
		}
    	System.out.println("Starting tests...");
    }
    
    public void testStarted(Description description) {
    	
    	CoverageCollection.testCase = "[TEST] " + description.getClassName() + ":" + description.getMethodName() + ":" + i;
    	CoverageCollection.coverage = new HashMap<String, HashSet<Integer>>();
    	i++;
    }

    public void testFinished(Description description) throws Exception {
    	
    	CoverageCollection.testCase_Coverages.put(CoverageCollection.testCase, CoverageCollection.coverage);

    }

    public void testRunFinished(Result result) throws Exception {

    	System.out.println("Testing Finished.\n\n");
        
        
        File fout = new File("stmt-cov.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        StringBuilder builder = new StringBuilder();
        for (String testCaseName : CoverageCollection.testCase_Coverages.keySet()) {
        	builder.append(testCaseName + "\n");
        	
        	HashMap<String, HashSet<Integer>> caseCoverage = 
        			CoverageCollection.testCase_Coverages.get(testCaseName);
        	
            for (String className : caseCoverage.keySet()) {
            	HashSet<Integer> lines = caseCoverage.get(className);
            	
            	Iterator<Integer> i = lines.iterator();
            	while(i.hasNext()){
                	builder.append(className + ":" + i.next() + "\n");
				}
            }
        }
        bw.write(builder.toString());
        bw.close();
    }

    public void testFailure(Failure failure) {
    	CoverageCollection.testCase = "[TEST FAILED] " + failure.getDescription().getClassName() + ":" + failure.getDescription().getMethodName();
    	CoverageCollection.testCase_Coverages.put(CoverageCollection.testCase, CoverageCollection.coverage);
    }
    
    public void testAssumptionFailure(Failure failure) {
    	CoverageCollection.testCase = "[TEST ASSUMPTION FAILED] " + failure.getDescription().getClassName() + ":" + failure.getDescription().getMethodName();
    	CoverageCollection.testCase_Coverages.put(CoverageCollection.testCase, CoverageCollection.coverage);
    }
}