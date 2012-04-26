package Cminusminus;

public class Var
{

	private String type;
	private String varName;
	private int size;
	private int register;

	public void setRegister(int reg)
	{
		register = reg;
	}
	public int getRegister()
	{
		return register;
	}
	public void setType(String type_par)
	{
		type = type_par;
	}
	public void setVarName(String name)
	{
		varName = name;
	}
	public void setSize(int par_size)
	{
		size = par_size;
	}
	public int getSize()
	{
		return size;
	}
	public String getType()
	{
		return type;
	}
	public String getVarName()
	{
		return varName;
	}
	
	public String toString()
	{
		return type + " " + varName;
	}

}
