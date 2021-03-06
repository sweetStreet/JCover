package main.java.agents;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;


public class JUnitListener extends RunListener {

	private static int i = 1;

    public void testRunStarted(Description description) throws Exception {

    	if (null == StatementCoverageCollection.testCase_StmtCoverages){
			StatementCoverageCollection.testCase_StmtCoverages = new HashMap<String, HashMap<String, HashSet<Integer>>>();
		}

    	if(null == BranchCoverageCollection.testCase_BranchCoverages){
			BranchCoverageCollection.testCase_BranchCoverages = new HashMap<String, HashMap<String, HashSet<Integer>>>();
		}
    	System.out.println("Starting tests...");
    }
    
    public void testStarted(Description description) {
    	StatementCoverageCollection.testCase = "[TEST] " + description.getClassName() + ":" + description.getMethodName() + ":" + i;
		StatementCoverageCollection.stmtcoverage = new HashMap<String, HashSet<Integer>>();

		BranchCoverageCollection.testCase = "[TEST] " + description.getClassName() + ":" + description.getMethodName() + ":" + i;
		BranchCoverageCollection.branchcoverage = new HashMap<String, HashSet<Integer>>();
    	i++;
    }

    public void testFinished(Description description) throws Exception {
    	StatementCoverageCollection.testCase_StmtCoverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.stmtcoverage);
		BranchCoverageCollection.testCase_BranchCoverages.put(BranchCoverageCollection.testCase, BranchCoverageCollection.branchcoverage);
    }

    public void testRunFinished(Result result) throws Exception {

    	System.out.println("Testing Finished.\n\n");

		GenerateHtml generateHtml = new GenerateHtml();
		DecimalFormat fmt = new DecimalFormat("##0.00");
        
        File fout = new File("stmt-cov.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        StringBuilder builder = new StringBuilder();
        builder.append("statement coverage"+"\n");
        for (String testCaseName : StatementCoverageCollection.testCase_StmtCoverages.keySet()) {
        	builder.append(testCaseName + "\n");

        	HashMap<String, HashSet<Integer>> caseStmtCoverage =
        			StatementCoverageCollection.testCase_StmtCoverages.get(testCaseName);
        	
            for (String className : caseStmtCoverage.keySet()) {

            	List<Integer> totalstatments = Constant.totalCoverages.getOrDefault(className, new ArrayList<>());
            	int totalstmtsize = totalstatments.size();
            	builder.append(className+" total statements: "+ totalstmtsize+"\n");

            	HashSet<Integer> lines = caseStmtCoverage.get(className);
				//print out the source file with highlights
				generateHtml.generateStmtHtml(className, totalstatments, lines);

				if(totalstmtsize!=0)
					builder.append("statement coverage rate: "+ fmt.format((double)lines.size()/totalstmtsize) +"\n");

            	Iterator<Integer> i = lines.iterator();
            	while(i.hasNext()){
                	builder.append(className + ":" + i.next() + "\n");
				}
            }
        }


		builder.append("branch coverage"+"\n");
		for (String testCaseName : BranchCoverageCollection.testCase_BranchCoverages.keySet()) {
			builder.append(testCaseName + "\n");

			HashMap<String, HashSet<Integer>> caseStmtCoverage =
					BranchCoverageCollection.testCase_BranchCoverages.get(testCaseName);


			for (String className : caseStmtCoverage.keySet()) {
				HashSet<Integer> lines = caseStmtCoverage.get(className);

				List<Integer> totalstatments = Constant.totalCoverages.getOrDefault(className, new ArrayList<>());
				generateHtml.generateBranchHtml(className, totalstatments, lines);

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
    	StatementCoverageCollection.testCase_StmtCoverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.stmtcoverage);
    }
    
    public void testAssumptionFailure(Failure failure) {
    	StatementCoverageCollection.testCase = "[TEST ASSUMPTION FAILED] " + failure.getDescription().getClassName() + ":" + failure.getDescription().getMethodName();
    	StatementCoverageCollection.testCase_StmtCoverages.put(StatementCoverageCollection.testCase, StatementCoverageCollection.stmtcoverage);
    }
}