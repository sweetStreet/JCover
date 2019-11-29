package main.java.agents;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class  MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	
    public MethodTransformVisitor(final MethodVisitor mv, String name) {
        super(ASM5, mv);
        this.mName=name;
    }
    
    //visits line of code along the path of the called method and parameters
    public void visitLineNumber(int line, Label start) {

        Constant.totalnumber++;

    	if (0 != line) {

			mv.visitLdcInsn(mName);
			mv.visitLdcInsn(line);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "main/java/agents/StatementCoverageCollection", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);

			if(start.getOffset()!=0){
                mv.visitLdcInsn(mName);
                mv.visitLdcInsn(line);
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
                mv.visitMethodInsn(INVOKESTATIC, "main/java/agents/BranchCoverageCollection", "addMethodBranch", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
            }

        }

    	super.visitLineNumber(line, start);
    }

    public void visitLabel(Label label){

    }

    
    //lets the visitor know they have reached the end of the method
    @Override
    public void visitEnd() 
    {
    	super.visitEnd();
    }
}	