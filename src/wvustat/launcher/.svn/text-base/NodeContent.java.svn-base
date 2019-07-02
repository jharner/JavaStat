package wvustat.launcher;

import java.util.Map;
import java.util.HashMap;

class NodeContent{
    private String id;
    private String title;
    private String toolName;
    private Map params = new HashMap();

    public String toString(){
	if(title == null)
	    return "Untitled";
	else
            return title;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }
    
    public void setTitle(String title){
	this.title = title;
    }

    public String getTitle(){
	return title;
    }

    public void setToolName(String name){
	toolName = name;
    }

    public String getToolName(){
	return toolName;
    }

    public Map getParameters(){
	return params;
    }

    public void setParameter(Object key, Object value){
	params.put(key, value);
    }

    public Object getParameter(Object key){
	return params.get(key);
    }

    public Object removeParameter(Object key){
	return params.remove(key);
    }
}
