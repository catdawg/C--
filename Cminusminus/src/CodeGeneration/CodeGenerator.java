package Cminusminus;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Stack;


public class CodeGenerator {
	
	private static Boolean lvalue_assignment = false;
	private static String type = "";
	private static Stack<Integer> scopes = new Stack<Integer>();
	private static int labelCounter = 0;
	private static Stack<String> rvalue_type = new Stack<String>();
	private static Boolean isfunction = false;
	private static Iterator<Var> functionParameters;
	private static int stack_vars = 0;
	private static String assigntype;
	
    public static String generateCode(String filename, SimpleNode treeRoot, SymbolTreeNode currentSymbolTreeNode)
    {
    	
    	String ret = "";
    	
 		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
 		{
 			// :( 
 			switch(((SimpleNode)treeRoot.jjtGetChild(i)).id)
 			{
 				case(C2jvmTreeConstants.JJTTRANSLATIONUNIT):
 				{
			    	ret += ".class " + filename + "\n";
			    	ret += ".super java/lang/Object" + "\n";
			    	
			    	ret += CodeGenerator.generateGlobalVarCode(filename, ((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
			    	ret += "; standard initializer" +  "\n";
					ret += ".method public <init>()V" + "\n";
					ret += " aload_0" + "\n";
					ret += " invokenonvirtual java/lang/Object/<init>()V" + "\n";
					ret += " return" + "\n";
					ret += ".end method" + "\n";
					
					scopes.push(5);
					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					
					break;
 				}
 	
 				
 				case(C2jvmTreeConstants.JJTDECLARATION):
 				{
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTVARTYPE):
 				{
 					type = ((SimpleNode)treeRoot.jjtGetChild(i)).val;
 					break;
 				}
 				case(C2jvmTreeConstants.JJTFUNCTIONDEFINITION):
 				{
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTRETURNTYPE):
 				{
 					//TODO
 					
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTFUNCTIONIDENTIFIER):
 				{
 					
 					
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTPARAMETERDEFINITIONLIST):
 				{
 					
 					
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTFUNCTIONBODY):
 				{
 					int aux = scopes.pop();
 					currentSymbolTreeNode = currentSymbolTreeNode.getSon(aux);
 					aux++;
 					scopes.push(aux);
 					scopes.push(0);
 					
 					if(currentSymbolTreeNode.getFunctionName() == "")
 					{
 						ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					}
 					else
 					{
 						
 						ret += ".method public static ";
 						ret += currentSymbolTreeNode.getFunctionName();
 						if(currentSymbolTreeNode.getFunctionName().equals("main"))
 						{
 							ret += "([Ljava/lang/String;)";
 						}
 						else
 						{
 							ret += CodeGenerator.generateFunctionParameters(currentSymbolTreeNode);
 						}
 						if(currentSymbolTreeNode.getFunctionName().equals("main"))
 							ret += "V";
 						else							
 							ret += CodeGenerator.generateReturnType(currentSymbolTreeNode);
 						stack_vars = 0;
 						ret += "\n";
 						
 						ret += " .limit stack " + String.valueOf(CodeGenerator.stackSize(currentSymbolTreeNode)) + "\n";
 						ret += " .limit locals " + String.valueOf(CodeGenerator.localSize(currentSymbolTreeNode)) + "\n";
 						
 						ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 						
 						if(currentSymbolTreeNode.getFunctionName().equals("main") || currentSymbolTreeNode.getReturnType().equals("void") || currentSymbolTreeNode.getReturnType().equals(""))
 						{
 							ret += "return" + "\n";
 						}
 						else if (currentSymbolTreeNode.getReturnType().equals("int"))
 						{

 							ret += "ldc " + "0"+ "\n";
 							ret += "ireturn " + "\n";
 						}
 						else if (currentSymbolTreeNode.getReturnType().equals("float"))
 						{
 							ret += "ldc " + "0.0"+ "\n";
 							ret += "freturn " + "\n";
 						}
 						
 						ret += ".end method" + "\n";
 					}
 						
 					scopes.pop();
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTINTERNALDECLARATION):
 				{
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTSUBINTERNALDECLARATION):
 				{
 					
 					int register = CodeGenerator.getVarRegister((SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					int a = ret.length();
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					if(a < ret.length())
	 				{
 						
 						String assignment_type = currentSymbolTreeNode.getVarType(getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
 						String operation_type = rvalue_type.pop();
 						if(operation_type.equals(assignment_type))
 						{
 							
 							ret += CodeGenerator.StoreVar(false, assignment_type, register, filename, getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
 						}
 						else
 						{
 							if(operation_type.equals("float") && assignment_type.equals("int"))
 							{
 								ret += "f2i" + "\n";
 								ret += CodeGenerator.StoreVar(false, assignment_type, register, filename, getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
 							}
 							else
 							{
 								ret += "i2f" + "\n";
 								ret += CodeGenerator.StoreVar(false, assignment_type, register, filename, getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
 							}
 						}
 					}
 					else
 					{
 						
 						String assignment_type = currentSymbolTreeNode.getVarType(getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));


						if(assignment_type.equals("float"))
						{
							ret += "ldc 0.0" + "\n";
							stack_vars++;
							ret += CodeGenerator.StoreVar(false, assignment_type, register, filename, getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
						}
						else if(assignment_type.equals("[float"))
						{
							ret += "bipush" + currentSymbolTreeNode.getVar(getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i)))).getSize() + "\n";
							ret += "newarray float" + "\n";
							stack_vars++;
							ret += "astore " + String.valueOf(register) + "\n";
							
						}else if(assignment_type.equals("int"))
						{
							ret += "ldc 0" + "\n";
							stack_vars++;
							ret += CodeGenerator.StoreVar(false, assignment_type, register, filename, getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i))));
						}else if(assignment_type.equals("[int"))
						{
							ret += "bipush " + currentSymbolTreeNode.getVar(getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i)))).getSize() + "\n";
							ret += "newarray int" + "\n";
							ret += "astore " + String.valueOf(register) + "\n";
							
							
						}
 						
 					}
 					break;
 					
 				}
 				
 				case(C2jvmTreeConstants.JJTVARIDENTIFIER):
 				{
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 					
 				}
 				
 				
 				case(C2jvmTreeConstants.JJTSTATEMENT):
 				{
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					if(stack_vars > 0)
 					{
 						while(stack_vars > 0)
 						{
 							stack_vars--;
 							ret += "pop \n";
 						}
 					}
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTEXPRESSIONSTATEMENT):
 				{
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTCOMPOUNDSTATEMENT):
 				{
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTEXPRESSION):
 				{
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTSUBEXPRESSION):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					String var_name = CodeGenerator.getVarIdentifier((SimpleNode)treeRoot.jjtGetChild(i));
 					boolean assignment_of_global_var = false;
 					int register = CodeGenerator.getVarRegister((SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					if(register == -1)
 					{
 						assignment_of_global_var = true;
 					}
 					lvalue_assignment = true;
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					String optype = rvalue_type.peek();
 					if(!optype.equals(assigntype))
 					{
 						if(!(optype.equals("void") || optype.equals("")))
 						{
	 						if(optype.equals("int") || optype.equals("[int"))
	 						{
	 							ret += "i2f" + "\n";
	 						}
	 						else
	 						{
	 							ret += "f2i" + "\n";
	 						}
 						}
 					}
 					if(op.equals("="))
 					{
 	 						ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 					}
 					else
 					{
 						if(op.equals("*="))
 						{
							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 							
 							if(currentSymbolTreeNode.getVarType(var_name).equals("int") || currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 								ret += "imul " + "\n";
 							else
 								ret += "fmul " + "\n";
 							stack_vars -= 1;
 							ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					
 						}
 						if(op.equals("-="))
 						{
 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					ret += "swap" + "\n";
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "isub " + "\n";
 		 					else
 		 						ret += "fsub " + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("+="))
 						{
 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "iadd " + "\n";
 		 					else
 		 						ret += "fadd " + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("/="))
 						{

 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 							ret += "swap" + "\n";
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "idiv " + "\n";
 		 					else
 		 						ret += "fdiv" + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("%="))
 						{

 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 							ret += "swap" + "\n";
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "irem " + "\n";
 		 					else
 		 						ret += "frem" + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("&="))
 						{

 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "iand " + "\n";
 		 					else
 		 						ret += "fand" + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("|="))
 						{

 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "ior " + "\n";
 		 					else
 		 						ret += "for" + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 						if(op.equals("^="))
 						{

 							ret += CodeGenerator.LoadVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 		 					if(currentSymbolTreeNode.getVarType(var_name).equals("int")|| currentSymbolTreeNode.getVarType(var_name).equals("[int"))
 		 						ret += "ixor " + "\n";
 		 					else
 		 						ret += "fxor" + "\n";
 							stack_vars -= 1;
 		 					ret += CodeGenerator.StoreVar(assignment_of_global_var, currentSymbolTreeNode.getVarType(var_name), register, filename, var_name);
 						}
 					}
 					lvalue_assignment = false;
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTOPERATION):
 				{
 					
 					boolean isfunction_aux = isfunction;
 					isfunction = false;
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					isfunction = isfunction_aux;
 					if(isfunction && functionParameters.hasNext())
 					{

 						String optype = rvalue_type.pop();
 						String partype = functionParameters.next().getType();
 						if(!optype.equals(partype))
 						{
 							if(partype.equals("int"))
 								ret += "f2i" + "\n";
 							else
 								ret += "i2f" + "\n";
 						}		
 					}
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTADDITIVE):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					String type1 = rvalue_type.pop();
 					String type2 = rvalue_type.pop();
 					if(type1.equals("float") && type2.equals("int"))
 					{
 						ret += "swap" + "\n";
 						ret += "i2f" + "\n";
 						rvalue_type.push("float");
 					}
 					else 
	 					if(type1.equals("int") && type2.equals("float"))
	 					{
	 						ret += "i2f" + "\n";
	 						rvalue_type.push("float");
	 						
	 					}
	 					else 
		 					if(type1.equals("int") && type2.equals("int"))
		 					{
		 						rvalue_type.push("int");
		 					}
		 					else
		 					{
		 						rvalue_type.push("float");
		 					}
 					
 					if( op.equals("+") )
 						if(rvalue_type.peek().equals("int"))
 						{
 							ret += "iadd" + "\n";
 							stack_vars -= 1;
 						}
 						else
 						{
 							ret += "fadd" + "\n";
 							stack_vars -= 1;
 						}
 								
 					else
 					{
 						if(rvalue_type.peek().equals("int"))
 						{
 							ret += "isub" + "\n";
 							stack_vars -= 1;
 						}
 							
 						else
 						{
 							ret += "fsub" + "\n";
 							stack_vars -= 1;
 						}
 					}
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTMULTIPLICATIVE):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					String type1 = rvalue_type.pop();
 					String type2 = rvalue_type.pop();
 					if(type1.equals("float") && type2.equals("int"))
 					{
 						ret += "swap" + "\n";
 						ret += "i2f" + "\n";
 						ret += "swap" + "\n"; 
 						rvalue_type.push("float");
 					}
 					else 
	 					if(type1.equals("int") && type2.equals("float"))
	 					{
	 						ret += "i2f" + "\n";
	 						rvalue_type.push("float");
	 						
	 					}
	 					else 
		 					if(type1.equals("int") && type2.equals("int"))
		 					{
		 						rvalue_type.push("int");
		 					}
		 					else
		 					{
		 						rvalue_type.push("float");
		 					}
 					if( op.equals("*") )
 						if(rvalue_type.peek().equals("int"))
 						{
 							ret += "imul" + "\n";
 							stack_vars -= 1;
 						}
 						else
 						{
 							ret += "fmul" + "\n";
 							stack_vars -= 1;
 						}
 								
 					else
 					{
 						if(op.equals("/"))
 						{
	 						if(rvalue_type.peek().equals("int"))
	 						{
	 							ret += "idiv" + "\n";
	 							stack_vars -= 1;
	 						}
	 						else
	 						{
	 							ret += "fdiv" + "\n";
	 							stack_vars -= 1;
	 						}
 						}
 						else
 						{
 							if(rvalue_type.peek().equals("int"))
 							{
	 							ret += "irem" + "\n";
	 							stack_vars -= 1;
 							}
	 						else
	 						{
	 							ret += "frem" + "\n";
	 							stack_vars -= 1;
	 						}
 						}
 					}
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTSHIFT):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					String type1 = rvalue_type.pop();
 					String type2 = rvalue_type.pop();
 					if(type1.equals("float") && type2.equals("int"))
 					{
 						ret += "swap" + "\n";
 						ret += "i2f" + "\n";
 						ret += "swap" + "\n";
 						rvalue_type.push("float");
 					}
 					else 
	 					if(type1.equals("int") && type2.equals("float"))
	 					{
	 						ret += "i2f" + "\n";
	 						rvalue_type.push("float");
	 						
	 					}
	 					else 
		 					if(type1.equals("int") && type2.equals("int"))
		 					{
		 						rvalue_type.push("int");
		 					}
		 					else
		 					{
		 						rvalue_type.push("float");
		 					}
 					if( op.equals("<<") )
 						if(rvalue_type.peek().equals("int"))
 						{
 							ret += "ishl" + "\n";
 							stack_vars -= 1;
 						}
 						else
 						{
 							ret += "fshl" + "\n";
 							stack_vars -= 1;
 						}
 								
 					else
 					{
 						if(rvalue_type.peek().equals("int"))
 						{
 							stack_vars -= 1;
 							ret += "ishr" + "\n";
 						}
 						else
 						{
 							stack_vars -= 1;
 							ret += "fshr" + "\n";
 						}
 					}
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTCOMPARATIVE):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					String type1 = rvalue_type.pop();
 					String type2 = rvalue_type.pop();
 					if(type1.equals("float") && type2.equals("int"))
 					{
 						ret += "swap" + "\n";
 						ret += "i2f" + "\n";
 						rvalue_type.push("float");
 					}
 					else 
	 					if(type1.equals("int") && type2.equals("float"))
	 					{
	 						ret += "i2f" + "\n";
	 						rvalue_type.push("float");
	 						
	 					}
	 					else 
		 					if(type1.equals("int") && type2.equals("int"))
		 					{
		 						rvalue_type.push("int");
		 					}
		 					else
		 					{
		 						rvalue_type.push("float");
		 					}
 					ret += "isub" + "\n";
 					
 					if( op.equals("==") )
 						ret += "ifeq ";
 					else if ( op.equals("!=") )
 						ret += "ifne ";
 					else if ( op.equals("<") )
 						ret += "iflt ";
 					else if ( op.equals("<=") )
 						ret += "ifle ";
 					else if ( op.equals(">") )
 						ret += "ifgt ";
 					else if ( op.equals(">=") )
 						ret += "ifge ";
						stack_vars -= 1;
						ret +=  "Label" + labelCounter + "\n" +
 							"ldc 0" + "\n" +
 							"goto Label" + (labelCounter+1) + "\n" +
 							"Label" + labelCounter + ": \n" +
 							"ldc 1" + "\n" +
 							"Label" + (labelCounter+1) + ": \n";
 					
 					labelCounter+=2;
 					
 					break;
 				}
 				case(C2jvmTreeConstants.JJTLOGIC):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					String type1 = rvalue_type.pop();
 					String type2 = rvalue_type.pop();
 					if(type1.equals("float") && type2.equals("int"))
 					{
 						ret += "swap" + "\n";
 						ret += "i2f" + "\n";
 						rvalue_type.push("float");
 					}
 					else 
	 					if(type1.equals("int") && type2.equals("float"))
	 					{
	 						ret += "i2f" + "\n";
	 						rvalue_type.push("float");
	 						
	 					}
	 					else 
		 					if(type1.equals("int") && type2.equals("int"))
		 					{
		 						rvalue_type.push("int");
		 					}
		 					else
		 					{
		 						rvalue_type.push("float");
		 					}
 					if( op.equals("&&") )
 					{
 						if(rvalue_type.peek().equals("int"))
 							ret += "iand" + "\n";
 						else
 							ret += "fand" + "\n";
						stack_vars -= 1;
 					}	
 					else 
 					{
	 					if( op.equals("||") )
	 					{
	 						if(rvalue_type.peek().equals("int"))
	 							ret += "ior" + "\n";
	 						else
	 							ret += "for" + "\n";
 							stack_vars -= 1;
	 					}
	 					else 
	 					{
	 						if( op.equals("&") )
		 					{
		 						if(rvalue_type.peek().equals("int"))
		 							ret += "iand" + "\n";
		 						else
		 							ret += "fand" + "\n";
	 							stack_vars -= 1;
		 					}
		 					else 
		 					{
		 						if( op.equals("|") )
			 					{
			 						if(rvalue_type.peek().equals("int"))
			 							ret += "ior" + "\n";
			 						else
			 							ret += "for" + "\n";
		 							stack_vars -= 1;
			 					}
			 					else 
			 					{
			 						if( op.equals("^") )
			 						{
				 						if(rvalue_type.peek().equals("int"))
				 							ret += "ixor" + "\n";
				 						else
				 							ret += "fxor" + "\n";
			 							stack_vars -= 1;
			 						}
			 					}
		 					}
	 					}
 					}
 							
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTUNARYOPERATION):
 				{
 					String op = CodeGenerator.getOperator((SimpleNode)treeRoot.jjtGetChild(i));
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					if(op.equals("!"))
					{
						ret += "ifeq Label" + labelCounter + "\n";
						ret += "ldc 0" + "\n";
						ret += "goto Label" + String.valueOf(labelCounter + 1) +"\n";
						ret += "Label" + labelCounter + ":\n";
						ret += "ldc 1" + "\n";
						ret += "Label" + String.valueOf(labelCounter + 1) +":\n";
						labelCounter += 2;
						
					}
					else if(op.equals("~"))
					{
						if(rvalue_type.peek().equals("int"))
						{
							ret += "ldc -1" + "\n";
							ret += "ixor" + "\n";
						}else
						{
							ret += "ldc -1.0" + "\n";
							ret += "fxor" + "\n";
						}
					}
					else if(op.equals("int"))
					{
						if(!rvalue_type.peek().equals("int"))
						{
							ret += "f2i" + "\n";
							rvalue_type.pop();
							rvalue_type.push("int");
						}
						
					}
					else if(op.equals("float"))
					{
						if(rvalue_type.peek().equals("int"))
						{
							ret += "i2f" + "\n";
							rvalue_type.pop();
							rvalue_type.push("float");
						}
					}else
					{
						if(rvalue_type.peek().equals("int"))
						{
							ret += "ineg" + "\n";
						}else
						{
							ret += "fneg" + "\n";
						}
					}
					break;
 					
 					
 				}
 				case(C2jvmTreeConstants.JJTRVALUE):
 				{
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTFUNCTIONCALL):
 				{	
 					String functionName = getFunctionName((SimpleNode)treeRoot.jjtGetChild(i));
 					boolean isfunction_aux = isfunction;
 					isfunction = true;
					SymbolTreeNode root = currentSymbolTreeNode;
					SymbolTreeNode functionNode = new SymbolTreeNode();
					while(!root.isRoot())
					{
						root = root.goUp();
					}
					for(int j = 0; j < root.getNumSons(); ++j)
					{
						if(root.getSon(j).getFunctionName().equals(functionName))
						{
							functionNode = root.getSon(j);
							break;
						}
					}
					functionParameters = functionNode.getParametersList().iterator();

					stack_vars -= functionNode.getParametersList().size();
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					isfunction = isfunction_aux;
 					ret += "invokestatic ";
 					if(functionName.equals("print"))
 					{
 						ret += "io" + "/" + functionName;
 						ret += "(Ljava/lang/String;I)V";
 						rvalue_type.push("");
 					}
 					else
 					{
 						if(functionName.equals("println"))
 						{
 							ret += "io" + "/" + functionName;
 							ret += "()V";
 	 						rvalue_type.push("");
 						}
 						else
 						{
 							if(functionName.equals("read"))
 							{
 								ret += "io" + "/" + functionName;
 								ret += "()I";
 	 							stack_vars += 1;
 		 						rvalue_type.push("int");
 							}
 							else
 							{
	 							if(functionName.equals("printStr"))
	 							{
	 								ret += "io" + "/" + functionName;
	 								ret += "(Ljava/lang/String;)V";
	 		 						rvalue_type.push("");
	 								
	 							}
	 							else
	 							{
	 								if(functionName.equals("printFloat"))
		 							{
	 									ret += "io" + "/" + functionName;
		 								ret += "(Ljava/lang/String;F)V";
		 		 						rvalue_type.push("");
		 							}
	 								else
	 								{
	 									
	 									ret += filename + "/" + functionName;
	 									ret += generateFunctionParameters(functionNode);
	 			 						rvalue_type.push(functionNode.getReturnType());
	 									ret += generateReturnType(functionNode);
	 									
	 								}
	 							}
 							}
 						}
 					}
 					
 					ret += "\n";
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTPARAMETERLIST):
 				{
 					
 					Boolean aux = lvalue_assignment;
 					lvalue_assignment = false;
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					lvalue_assignment = aux;
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTCONSTANT):
 				{
 					ret += "ldc " + ((SimpleNode)treeRoot.jjtGetChild(i)).val + "\n";

					stack_vars += 1;
 					if(((SimpleNode)treeRoot.jjtGetChild(i)).val.lastIndexOf(".") == -1)
 					{
 						rvalue_type.push("int");
 					}
 					else
 					{
 						rvalue_type.push("float");
 					}
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTPOSTINCREMENT):
 				{
 					Boolean aux = lvalue_assignment;
 					lvalue_assignment = true;
 					String var = getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i)));
 					int register = getVarRegister(((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
 					type = currentSymbolTreeNode.getVarType(var);
 					if(type.equals("int"))
 					{
 						ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("++"))
 						{
 							ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 							ret += "ldc " + "1" + "\n";
 							ret += "iadd" + "\n";
 						}
 						else
 						{
 							ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 							ret += "ldc " + "1" + "\n";
 							ret += "isub" + "\n";
 						}

 						ret += "istore " + String.valueOf(register) + "\n";
 						--stack_vars;
 						
 					}
 					else
 					{
 						ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("++"))
 						{
 							ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 							ret += "ldc " + "1.0" + "\n";
 							ret += "fadd" + "\n";
 						}
 						else
 						{
 							ret += CodeGenerator.LoadVar(register == -1, type, register, filename, var);
 							ret += "ldc " + "1.0" + "\n";
 							ret += "fsub" + "\n";
 						}
 						ret += "fstore " + String.valueOf(register) + "\n";
 						--stack_vars;
 					}
 					rvalue_type.push(type);

 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					lvalue_assignment = aux;
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTPREINCREMENT):
 				{
 					Boolean aux = lvalue_assignment;
 					lvalue_assignment = true;
 					String var = getVarIdentifier(((SimpleNode)treeRoot.jjtGetChild(i)));
 					int register = getVarRegister(((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
 					type = currentSymbolTreeNode.getVarType(var);
 					if(type.equals("int"))
 					{
 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("++"))
 						{
 	 						ret += "iinc " + String.valueOf(register) + " 1" + "\n";
 	 						ret += "iload " + String.valueOf(register) + "\n";
 						}
 	 					else
 						{
 	 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("--"))
 	 						{
 	 							ret += "iinc " + String.valueOf(register) + " -1" + "\n";
 	 							ret += "iload " + String.valueOf(register) + "\n";
 	 						}
 	 						else
 	 						{
 	 							ret += "iload " + String.valueOf(register) + "\n";
 	 							ret += "ineg " + "\n";
 	 						}
 						}
 						
 						
 					}
 					else
 					{
 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("++"))
 						{
 							ret += "fload " + String.valueOf(register) + "\n";
 							ret += "ldc " + "1.0" + "\n";
 							ret += "fadd" + "\n";
 						}
 						else
 						{
	 						if(((SimpleNode)treeRoot.jjtGetChild(i)).val.equals("--"))
 	 						{
	 							ret += "fload " + String.valueOf(register) + "\n";
	 							ret += "ldc " + "1.0" + "\n";
	 							ret += "fsub" + "\n";
 	 						}
	 						else
 	 						{
	 							ret += "fload " + String.valueOf(register) + "\n";
 	 							ret += "fneg " + "\n";
 	 						}
 						}
 						ret += "fstore " + String.valueOf(register) + "\n";
 						ret += "fload " + String.valueOf(register) + "\n";
 					}

					stack_vars += 1;
 					rvalue_type.push(type);
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					lvalue_assignment = aux;
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTSTRINGCONSTANT):
 				{
 					ret += "ldc " + ((SimpleNode)treeRoot.jjtGetChild(i)).val + "\n";
 					rvalue_type.push("String");
					stack_vars += 1;
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTLVALUE):
 				{
 					if(!lvalue_assignment)
 					{
	 					int register = currentSymbolTreeNode.getVarRegister(((SimpleNode)treeRoot.jjtGetChild(i)).val);
	 					type = currentSymbolTreeNode.getVarType(((SimpleNode)treeRoot.jjtGetChild(i)).val);
	 					String var = ((SimpleNode)treeRoot.jjtGetChild(i)).val;
	 					String aux = type;
	 					if(type.charAt(0) == '[')
 						{
	 						type = type.substring(1);
	 						assigntype = type;
	 						ret += "aload " + register + "\n";
	 						int ret_size = ret.length();
		 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild( i), currentSymbolTreeNode);
		 					if(ret.length() > ret_size)
		 						ret += CodeGenerator.LoadVar(register == -1, aux, register, filename, var);
 						}
 						else
 						{
 							ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild( i), currentSymbolTreeNode);
 							ret += CodeGenerator.LoadVar(register == -1, aux, register, filename, var);
 						}
 					}
 					else
 					{
 						lvalue_assignment = false;
 						type = currentSymbolTreeNode.getVarType(((SimpleNode)treeRoot.jjtGetChild(i)).val);
 						assigntype = type;
 						if(type.charAt(0) == '[')
 						{
 							
	 						int register = currentSymbolTreeNode.getVarRegister(((SimpleNode)treeRoot.jjtGetChild(i)).val);
	 						type = type.substring(1);
	 						assigntype = type;
	 						ret += "aload " + register + "\n";
	 						ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild( i), currentSymbolTreeNode);
	 						--stack_vars;
	 						
 						}
 					}
 					rvalue_type.push(type);
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild( i), currentSymbolTreeNode);
 					
 					break;
 				}
 				
 				case(C2jvmTreeConstants.JJTARRAYACCESS):
 				{
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					break;
 				}
 				case(C2jvmTreeConstants.JJTJUMPSTATEMENT):
 				{
 				
 					
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					SymbolTreeNode root = currentSymbolTreeNode;
 					while(!root.goUp().isRoot())
 					{
 						root = root.goUp();
 					}
 					String ret_type = root.getReturnType();
 					if(!ret_type.equals(rvalue_type.peek()))
 					{
 						if(ret_type.equals("int"))
 						{
 							ret += "f2i" + "\n";
 						}
 						else
 						{
 							ret += "i2f" + "\n";
 						}
 					}
 					if(ret_type.equals("") || ret_type.equals("void"))
 					{
 						ret += "return" + "\n";
 						break;
 					}
 					if(ret_type.equals("int"))
 					{
 						ret += "ireturn" + "\n";
 						stack_vars -= 1;
 					}
 					else
 					{
 						ret += "freturn" + "\n";
 						stack_vars -= 1;
 					}

					
 					break;
 				}
 				
 				case (C2jvmTreeConstants.JJTSELECTIONSTATEMENT):
 				{
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					break;
 				}
 				
 				case (C2jvmTreeConstants.JJTIFSTATEMENT):
 				{
 					int label_end = labelCounter;
 					int label_else = labelCounter + 1;
 					labelCounter += 2;
 					ret += CodeGenerator.generateIfComparison(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					ret += "ifeq " + "Label" + String.valueOf(label_else) + "\n";
 					--stack_vars;
 					ret += CodeGenerator.generateIfStatements(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					ret += "goto Label" + String.valueOf(label_end) + "\n";
 					ret += "Label" + String.valueOf(label_else) + ":\n";
					ret += CodeGenerator.GenerateElseStatement(filename,treeRoot, currentSymbolTreeNode);
 					ret += "Label" + String.valueOf(label_end) + ":\n";
 					break;
 				}
 				
 				
 				case(C2jvmTreeConstants.JJTITERATIONSTATEMENT):
 				{
 					int whStart = labelCounter++;
 					int whFinish = labelCounter++;
 					
 					ret += "Label" + whStart + ":\n";

 					ret += CodeGenerator.generateIfComparison(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);

 					ret += "ifeq Label" + whFinish + "\n";
					stack_vars -= 1;
 					
 					ret += CodeGenerator.generateIfStatements(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);

 					ret += "goto Label" + whStart + "\n";
 					ret += "Label" + whFinish + ":\n";

 					break;
 				}
 				case(C2jvmTreeConstants.JJTARRAYSIZEDECLARATION):
 				{
 					type = "[" + type;
 					break;
 				}
 				default :
 				{
 					//TODO
 					ret += "; :(" + "\n";
 					
 					//ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode, scopes);
 					
 					break;
 					
 				}
 					
 			}

 		}

    	return  ret;
    }

	private static String GenerateElseStatement(String filename,
			SimpleNode treeRoot, SymbolTreeNode currentSymbolTreeNode) 		
	{
		String ret = "";
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
		{
				// :( 
				switch(((SimpleNode)treeRoot.jjtGetChild(i)).id)
				{
					case(C2jvmTreeConstants.JJTELSESTATEMENT):
					{
						ret += CodeGenerator.generateCode(filename, ((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
						break;
					}
					default: 
					{
						break;
					}
				
				}
		}
		return ret;
	}


	private static String generateIfStatements(String filename,
			SimpleNode treeRoot, SymbolTreeNode currentSymbolTreeNode) {
		String ret = "";
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
 		{
 			// :( 
 			switch(((SimpleNode)treeRoot.jjtGetChild(i)).id)
 			{
 				case(C2jvmTreeConstants.JJTOPERATION):
 				{
 					break;
 				}
 				case(C2jvmTreeConstants.JJTFUNCTIONBODY):
 				{
 					int aux = scopes.pop();
 					currentSymbolTreeNode = currentSymbolTreeNode.getSon(aux);
 					aux++;
 					scopes.push(aux);
 					scopes.push(0);
 					ret += CodeGenerator.generateCode(filename, ((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
 					scopes.pop();
 					break;
 				}
 				default: 
 				{
 					ret += CodeGenerator.generateCode(filename, ((SimpleNode)treeRoot.jjtGetChild(i)), currentSymbolTreeNode);
 					break;
 				}
 			
 			}
 		}
		return ret;
	}

	private static String generateIfComparison(String filename,
			SimpleNode treeRoot, SymbolTreeNode currentSymbolTreeNode) {
		String ret = "";
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
 		{
 			// :( 
 			switch(((SimpleNode)treeRoot.jjtGetChild(i)).id)
 			{
 				case(C2jvmTreeConstants.JJTOPERATION):
 				{
 					boolean isfunction_aux = isfunction;
 					isfunction = false;
 					ret += CodeGenerator.generateCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
 					isfunction = isfunction_aux;
 					if(isfunction && functionParameters.hasNext())
 					{

 						String optype = rvalue_type.pop();
 						String partype = functionParameters.next().getType();
 						if(!optype.equals(partype))
 						{
 							if(partype == "int")
 								ret += "i2f" + "\n";
 							else
 								ret += "f2i" + "\n";
 						}		
 					}
 					
 					break;
 	 			
 				}
 				default: 
 					break;
 			
 			}
 		}
		return ret;
	}

	private static String StoreVar(boolean assignment_of_global_var,
			String type2, int register, String filename, String var_name) {
		String ret = "";
		if(type2.equals("int"))
		{
			if(assignment_of_global_var)
			{
				ret += "putstatic " + filename + "/" + var_name + " I" + "\n";
				stack_vars -= 1;
			}
			else
			{
				ret += "istore " + String.valueOf(register)+ "\n";
				stack_vars -= 1;
			}
		}
		else if(type2.equals("float"))
		{
			if(assignment_of_global_var)
			{
				ret += "putstatic " + filename + "/" + var_name + " F" + "\n";
				stack_vars -= 1;
			}
			else
			{
				ret += "fstore "  + String.valueOf(register) + "\n";
				stack_vars -= 1;
			}
		}
		else if(type2.equals("[int"))
		{
			ret += "iastore " + "\n";
			stack_vars -= 1;
		} 
		else if(type.equals("[float"))
		{
			ret += "fastore " +  "\n";
			stack_vars -= 1;
		}
		return ret;
	}
	private static String LoadVar(boolean assignment_of_global_var,
			String type2, int register, String filename, String var_name) {
		String ret = "";
		if(type2.equals("int"))
		{
			if(assignment_of_global_var)
			{
				ret += "getstatic " + filename + "/" + var_name + " I" + "\n";
				stack_vars += 1;
			}
			else
			{
				ret += "iload " + String.valueOf(register) + "\n";
				stack_vars += 1;
			}
		}
		else
		if(type2.equals("float"))
		{
			if(assignment_of_global_var)
			{
				ret += "getstatic " + filename + "/" + var_name + " F" + "\n";
				stack_vars += 1;
			}
			else
			{
				ret += "fload " + String.valueOf(register) + "\n";
				stack_vars += 1;
			}
		}
		else if(type2.equals("[float"))
		{
			if(assignment_of_global_var)
			{
				
				ret += "getstatic " + filename + "/" + var_name + " [F" + "\n";
				stack_vars -= 1;
			}
			else
			{
				ret += "faload" +  "\n";
				stack_vars -= 1;
			}
		} else if(type2.equals("[int"))
		{
			if(assignment_of_global_var)
			{
				ret += "getstatic " + filename + "/" + var_name + " [I" + "\n";
				stack_vars -= 1;
			}
			else
			{
				
				ret += "iaload" +  "\n";
				stack_vars -= 1;
			}
		}
		return ret;
	}

	private static String generateGlobalVarCode(String filename,
			SimpleNode treeRoot, SymbolTreeNode currentSymbolTreeNode) {
		String ret = "";
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
 		{
 			// :( 
 			switch(((SimpleNode)treeRoot.jjtGetChild(i)).id)
 			{
 				case(C2jvmTreeConstants.JJTDECLARATION):
				{
					ret += CodeGenerator.generateGlobalVarCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					break;
				}
				
 				case(C2jvmTreeConstants.JJTEXTERNALDECLARATION):
				{
					
					
					ret += CodeGenerator.generateGlobalVarCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					
					break;
				}
				
				case(C2jvmTreeConstants.JJTSUBEXTERNALDECLARATION):
				{
					
					ret += ".field public static ";
					ret += CodeGenerator.getVarIdentifier((SimpleNode)treeRoot.jjtGetChild(i));
					ret += " ";
					if(type.equals("int"))
					{
						ret += "I";
					}
					else
						if(type.equals("float"))
						{
							ret += "F";
						}

					ret += CodeGenerator.generateGlobalVarCode(filename, (SimpleNode)treeRoot.jjtGetChild(i), currentSymbolTreeNode);
					ret += "\n";
					break;
				}
				case(C2jvmTreeConstants.JJTVARTYPE):
 				{
 					type = ((SimpleNode)treeRoot.jjtGetChild(i)).val;
 					break;
 				}
				case(C2jvmTreeConstants.JJTSUBEXTERNALDECLARATIONASSIGNMENT):
 				{
 					ret += " = " + ((SimpleNode)treeRoot.jjtGetChild(i)).val + "\n";
 					
 					break;
 				}
				default:
					break;
 			}
 		}
		return ret;
		
	}

	private static String getOperator(SimpleNode treeRoot) {
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
		{ 
			if(		((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTLOGICOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTCOMPARATIVEOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTSHIFTOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTADDITIVEOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTMULTIPLICATIVEOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTASSIGNMENTOPERATOR
					| ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTUNARYOPERATOR
			)
			{
				return ((SimpleNode)treeRoot.jjtGetChild(i)).val;
			}
		}
		return "NOOOOOO";
	}

	public static int getVarRegister(SimpleNode treeRoot,
			SymbolTreeNode symbolNode) 
	{
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
		{
			if(((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTVARIDENTIFIER || ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTLVALUE )
			{
				return symbolNode.getVarRegister(((SimpleNode)treeRoot.jjtGetChild(i)).val);
			}
		}
		return 0;
	}
	public static String getVarIdentifier(SimpleNode treeRoot)
	{
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
		{
			if(((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTVARIDENTIFIER || ((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTLVALUE )
			{
				return ((SimpleNode)treeRoot.jjtGetChild(i)).val;
			}
		}
		return "";
	}

	public static int assignRegisters(SymbolTreeNode root, int reg) 
	{

		if(!root.getFunctionName().equals(""))
		{
			reg = 0;
			Iterator<Var> iter = root.getParametersList().iterator();
			while(iter.hasNext()) 
			{
				iter.next().setRegister(reg++);
			}
		}
		
		Enumeration<Var> vars = root.getVars().elements();
		while(vars.hasMoreElements())
		{
			if(root.isRoot())
			{
				vars.nextElement().setRegister(-1);
				continue;
			}
			vars.nextElement().setRegister(reg++);
		}
		
		for(int i = 0; i < root.getNumSons(); ++i)
		{
			reg = assignRegisters(root.getSon(i), reg);
		}
		return reg;
	}

	private static String getFunctionName(SimpleNode treeRoot) 
	{
		
		for(int i = 0; i < treeRoot.jjtGetNumChildren(); ++i)
		{
			if(((SimpleNode)treeRoot.jjtGetChild(i)).id == C2jvm.JJTFUNCTIONIDENTIFIER)
			{
				return ((SimpleNode)treeRoot.jjtGetChild(i)).val;
			}
		}
		return "";
	}

	private static int localSize(SymbolTreeNode currentSymbolTreeNode) {
		// TODO Auto-generated method stub
		return 100;
	}

	private static int stackSize(SymbolTreeNode currentSymbolTreeNode) {
		// TODO Auto-generated method stub
		return 100; 
	}

	private static String generateReturnType(SymbolTreeNode currentSymbolTreeNode) 
	{

		stack_vars += 1;
		if(currentSymbolTreeNode.getReturnType().equals("int"))
			return "I";
		if(currentSymbolTreeNode.getReturnType().equals("float"))
			return "F";
		stack_vars -= 1;
		return "V";
	}

	private static String generateFunctionParameters(SymbolTreeNode currentSymbolTreeNode)
	{
		//TODO only works with void, int and float
		String ret = "";
		Iterator<Var> iter = currentSymbolTreeNode.getParametersList().iterator();
		Var var;
		
		ret += "(";
		while(iter.hasNext())
		{
			var = iter.next();
			if(var.getSize() > 0)
				ret += "[";
			if(var.getType().equals("int"))
				ret += "I";
			else
			{
				if(var.getType().equals("float"))
					ret += "F";
				else
					ret += "V";
			}
		}
		ret += ")";
		
		return ret;

		
		
	}
	
	
	

}
