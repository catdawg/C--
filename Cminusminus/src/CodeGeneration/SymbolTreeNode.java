package Cminusminus;

import java.util.*;


public class SymbolTreeNode 
{
	private Boolean root;
	private ArrayList<SymbolTreeNode> sons;
	private SymbolTreeNode father;

	private Hashtable<String, Var> vars;
	private Hashtable<String, StructDefinition> structDefinitions;
	private Hashtable<String, Var> parameters;
	private ArrayList<Var> parameters_ordered;
	private String function_name = "";
	private String return_type = "";
		

	
	public SymbolTreeNode()
	{
		sons = new ArrayList<SymbolTreeNode>();
		vars = new Hashtable<String, Var>();
		parameters = new Hashtable<String, Var>();
		parameters_ordered = new ArrayList<Var>();
		structDefinitions = new Hashtable<String, StructDefinition>();
		root = true;
	}
	

	
	public SymbolTreeNode getSon(int i)
	{
		if(i < sons.size())
			return sons.get(i);
		return null;
	}
	
	public int getNumSons()
	{
		return sons.size();
	}
	
	public SymbolTreeNode getSonWithFunctionName(String name)
	{
		for(int i = 0; i < sons.size(); ++i)
		{
			if(sons.get(i).getFunctionName() == name)
			{
				return sons.get(i);
			}
		}
		return null;
	}
	
	public Boolean addFunctionParameter(String var, String type, int size,  Boolean struct_)
	{
		if(struct_ && !this.isDefinedType(type))
			return false;			
		if(this.isDefinedVar(var))
			return false;
		Var var_ = new Var();
		var_.setType(type);
		var_.setVarName(var);
		var_.setSize(size);
		parameters.put(new String(var),var_);
		parameters_ordered.add(var_);
		return true;
		
	}
	
	
	public Boolean isDefinedFunctionName(String name)
	{
		String a;
		for (int i = 0; i < sons.size(); i++) {
			a = sons.get(i).getFunctionName();
			if(a.equals(name))
				return true;
		}
		if(!root)
			return father.isDefinedFunctionName(name);
		return false;
	
	}
	
	public Hashtable<String, Var> getVars()
	{
		return vars;
	}
	
	public String getFunctionReturnType(String name)
	{
		String a;
		if(!root)
			return father.getFunctionReturnType(name);
		for (int i = 0; i < sons.size(); i++) 
		{
			a = sons.get(i).getFunctionName();
			if(a.equals(name))
				return sons.get(i).getReturnType();
		}
		return "void";
	
	}
	public void setReturnType(String ret)
	{
		return_type = ret;
	}
	public void setFunctionName(String name)
	{
		function_name = name;
	}
	public String getFunctionName()
	{
		return function_name;
	}
	public void setFather(SymbolTreeNode p_father)
	{
		father = p_father;
		root = false;
	}
	public SymbolTreeNode addSon()
	{
		SymbolTreeNode son = new SymbolTreeNode();
		sons.add(son);
		son.setFather(this);
		return son;
	}
	public void addSon(SymbolTreeNode son)
	{
		sons.add(son);
		son.setFather(this);
	}
	public SymbolTreeNode goUp()
	{
		if(!root)
		{
			return father;

		}
		return this;

	}
	
	public Boolean isDefinedVar(String var)
	{
		if(vars.containsKey(var))
			return true;
		if(parameters.containsKey(var))
			return true;
		if(!root)
			return father.isDefinedVar(var);
		return false;
	}
	public Boolean isDefinedVarWithinScope(String var)
	{
		if(vars.containsKey(var))
			return true;
		if(parameters.containsKey(var))
			return true;
		return false;
	}
	public Boolean isParameter(String var)
	{
		if(function_name != "")
			return parameters.containsKey(var);
		if(root)
			return false;
		return father.isParameter(var);
	}
	public Boolean isDefinedStructVar(String struct_type, String var)
	{
		if(structDefinitions.containsKey(struct_type) && structDefinitions.get(struct_type).isDefinedVar(var))
			return true;
		if(root)
			return false;
		
		return father.isDefinedStructVar(struct_type, var);
	}
	public Boolean isDefinedType(String type)
	{
		if(type == "int" || type == "float" ||type ==  "void" || type == "String")
			return true;   
		if(structDefinitions.containsKey(type))
			return true;
		if(!root)
			return father.isDefinedType(type);
		return false;
	}
	public Boolean isDefinedStructType(String type)
	{
		if(structDefinitions.containsKey(type))
			return true;
		if(!root)
			return father.isDefinedStructType(type);
		return false;
	}
	public Boolean isDefinedArrayVar(String var)
	{
		if(vars.containsKey(var))
		{
			if((vars.get(var)).getSize() > 0)
				return true;
			return false;
		}
		if(parameters.containsKey(var))
		{
			if(parameters.get(var).getSize() > 0)
				return true;
			return false;
		}
		if(!root)
			return father.isDefinedArrayVar(var);
		return false;
	}
	public String getVarType(String var)
	{
		if(vars.containsKey(var))
		{
			return (vars.get(var).getSize() > 0?"[":"" )+ vars.get(var).getType();
		}
		if(parameters.containsKey(var))
		{
			return (parameters.get(var).getSize() > 0?"[":"") +parameters.get(var).getType();
		}
		if(!root)
			return father.getVarType(var);
		return "";
	}
	public Boolean addType(String type)
	{
		if(this.isDefinedType(type))
			return false;
		StructDefinition newType = new StructDefinition();
		newType.setType(type);
		structDefinitions.put(new String(type),newType);
		return true;
	}
	public Boolean addVar(String var, String type, int size,  Boolean struct_)
	{
		if(struct_ && !this.isDefinedType(type))
			return false;			
		if(this.isDefinedVarWithinScope(var))
			return false;
		
		Var var_ = new Var();
		var_.setType(type);
		var_.setVarName(var);
		var_.setSize(size);
		vars.put(new String(var),var_);
		return true;
	}
	public Boolean addStructVar(String struct_type, String type, String var, int size, Boolean struct_)
	{
		if(!this.isDefinedType(struct_type) || !this.isDefinedType(type))
			return false;
		if(isDefinedStructVar(struct_type, var))
			return false;
		Var var_ = new Var();
		var_.setSize(size);
		var_.setVarName(var);
		var_.setType(type);
		structDefinitions.get(struct_type).addVar(var_);
			
		return true;
	}
	
	public String getReturnType() 
	{
		if(root)
		{
			return "";
		}
		if(function_name == "")
		{
			return father.getReturnType();
		}
		return return_type;
	}
	
	public String toString(int tab)
	{		
		String table = new String();
		int i2 = tab;
		while(i2-->0)
			table += "\t";
		table += "=======SCOPE=======\n";
		if(function_name != "")
		{
			i2 = tab;
			while(i2-->0)
				table += "\t";
			table += return_type + " " + function_name + "\n"; 
			i2 = tab;
			while(i2-->0)
				table += "\t";
			table += "Parameter Definitions" + parameters.toString() + "\n";
			i2 = tab;
		}
		i2 = tab;
		while(i2-- > 0)
			table += "\t";
		table += "Variable Definitions" + vars.toString() + "\n";
		i2 = tab;
		while(i2-- > 0)
			table += "\t";
		table += "Struct Definitions" + structDefinitions.toString() + "\n";
		
		for(int i = 0; i != sons.size(); i++)
			table += sons.get(i).toString(tab + 1) + "\n";
		
		return table;
	}

	public ArrayList<Var> getParametersList() 
	{
		return parameters_ordered;
	}
	public Hashtable<String, Var> getParameters() 
	{
		return parameters;
	}
	public ArrayList<Var> getParametersList(String name) 
	{
		String a;
		if(!root)
			return father.getParametersList(name);
		for (int i = 0; i < sons.size(); i++) 
		{
			a = sons.get(i).getFunctionName();
			if(a.equals(name))
				return sons.get(i).getParametersList();
		}
		return new ArrayList<Var>();
	
	}
	
	public Hashtable<String, Var> getParameters(String name) 
	{
		String a;
		if(!root)
			return father.getParameters(name);
		for (int i = 0; i < sons.size(); i++) 
		{
			a = sons.get(i).getFunctionName();
			if(a.equals(name))
				return sons.get(i).getParameters();
		}
		return new Hashtable<String, Var>();
	
	}



	public int getVarRegister(String var) 
	{
		if(vars.containsKey(var))
		{
			return vars.get(var).getRegister();
		}
		if(parameters.containsKey(var))
		{
			return parameters.get(var).getRegister();
		}
		if(!root)
			return father.getVarRegister(var);
		return 0;
	}
	
	public Var getVar(String var)
	{
		if(vars.containsKey(var))
		{
			return vars.get(var);
		}
		if(parameters.containsKey(var))
		{
			return parameters.get(var);
		}
		if(!root)
			return father.getVar(var);
		return null;
	}



	public boolean isRoot() {
		return root;
	}
	
}

