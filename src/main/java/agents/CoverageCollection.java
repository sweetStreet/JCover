package main.java.agents;

import java.util.HashMap;
import java.util.HashSet;

public class CoverageCollection {
	
	public static HashMap<String, HashMap<String, HashSet<Integer>>> testCase_Coverages;
	public static HashMap<String, HashSet<Integer>> coverage;
	public static String testCase;
	
	
	public static void addMethodLine(String className, Integer line){
    	//return if no test has been started
		if (coverage == null) {
    		return;
    	}
    	
		//create a new integer hash set to store the visited line #s
    	HashSet<Integer> lines = new HashSet<Integer>();
    	
    	//get the hashset of ints stored in the coverage hashmap. 
    	//if the set is not empty just add the new line, else initialize a new set and add the lines. 
    	lines = coverage.get(className);
        if (lines != null) {
        	lines.add(line);
        }
        else {
        	lines = new HashSet<Integer>();
        	lines.add(line);
            coverage.put(className, lines);
        }
    }

}
