package Cminusminus;


import java.util.*;


public class StructDefinition
{
	private String type;
	private Hashtable<String, Var> vars;
	
	
	public StructDefinition()
	{
		vars = new Hashtable<String, Var>();
	}
	

	public Boolean setType(String par_type)
	{
		type = par_type;
		return true;
	}

	public Boolean addVar(Var var)
	{
		if(this.isDefinedVar(var.getVarName()))
			return false;
		vars.put(new String(var.getVarName()) , var );
		return true;
	}
	public Boolean isDefinedVar(String name)	
	{
		return vars.containsKey(name);
	}

	public String getType() {
		return type;
	}
	
	public String toString()
	{	
		return type + " " + vars.toString();
	}

}
