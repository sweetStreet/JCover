package main.java.agents;

import java.lang.instrument.Instrumentation;


public class Agent{
	public static void premain(String agentArgs, Instrumentation inst){
		
		//creates a new TestingClassFileTransformer to edit the bytecode of the testing suite
		inst.addTransformer(new TestingClassFileTransformer());

	}
}