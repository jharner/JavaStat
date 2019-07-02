package wvustat.launcher;

class ModuleInfo{
	private String name;
	private String type;
	private String implClass;
	
	public ModuleInfo(String name, String type, String implClass){
		if(name == null)
			throw new IllegalArgumentException("null module name");
		else if(implClass == null)
			throw new IllegalArgumentException("null module implementation " +
			" class");
		
		this.name = name;
		this.type = (type == null)?(""):(type);
		this.implClass = implClass;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public Class getImplClass() throws ClassNotFoundException{
		return Class.forName(implClass);
	}
}
