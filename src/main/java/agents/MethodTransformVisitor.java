package main.java.agents;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class  MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	int lastVisitedLine;
	
    public MethodTransformVisitor(final MethodVisitor mv, String name) {
        super(ASM5, mv);
        this.mName=name;
    }

    //visits line of code along the path of the called method and parameters
    @Override
    public void visitLineNumber(int line, Label start) {

        List<Integer> list = Constant.totalCoverages.getOrDefault(mName, new ArrayList<Integer>());
        list.add(line);
        Constant.totalCoverages.put(mName, list);

    	if (0 != line) {
            lastVisitedLine = line;

			mv.visitLdcInsn(mName);
			mv.visitLdcInsn(line);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "main/java/agents/StatementCoverageCollection", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);

        }

    	super.visitLineNumber(line, start);

    }

    // label visiting after branching statement
    @Override
    public void visitLabel(Label label) {

        if (0 != lastVisitedLine) {

            mv.visitLdcInsn(mName);
            mv.visitLdcInsn(lastVisitedLine);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
            mv.visitMethodInsn(INVOKESTATIC, "main/java/agents/BranchCoverageCollection", "addMethodBranch", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);

        }

        super.visitLabel(label);
    }


    //lets the visitor know they have reached the end of the method
    @Override
    public void visitEnd() {
    	super.visitEnd();
    }
}	