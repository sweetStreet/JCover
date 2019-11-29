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
    		

    	if (null == StatementCoverageCollection.testCase_Coverages)
		{
			StatementCoverageCollection.testCase_Coverages = new HashMap<String, HashMap<String, HashSet<Integer>>>();
		}
    	System.out.println("Starting tests...");
    }
    
    public void testStarted(Description description) {
    	StatementCoverageCollection.testCase = "[TEST] " + description.getClassName() + ":" + description.getMethodName() + ":" + i;
    	StatementCoverageCollection.coverage = new HashMap<String, HashSet<Integer>>();
    	i++;
    }

    public void testFinished(Description description) throws Exception {
    	
    	StatementCoverageCollection.testCase_Coverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.coverage);

    }

    public void testRunFinished(Result result) throws Exception {

    	System.out.println("Testing Finished.\n\n");

        
        File fout = new File("stmt-cov.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        StringBuilder builder = new StringBuilder();
        for (String testCaseName : StatementCoverageCollection.testCase_Coverages.keySet()) {
        	builder.append(testCaseName + "\n");
        	
        	HashMap<String, HashSet<Integer>> caseCoverage = 
        			StatementCoverageCollection.testCase_Coverages.get(testCaseName);
        	
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
    	StatementCoverageCollection.testCase = "[TEST FAILED] " + failure.getDescription().getClassName() + ":" + failure.getDescription().getMethodName();
    	StatementCoverageCollection.testCase_Coverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.coverage);
    }
    
    public void testAssumptionFailure(Failure failure) {
    	StatementCoverageCollection.testCase = "[TEST ASSUMPTION FAILED] " + failure.getDescription().getClassName() + ":" + failure.getDescription().getMethodName();
    	StatementCoverageCollection.testCase_Coverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.coverage);
    }
}