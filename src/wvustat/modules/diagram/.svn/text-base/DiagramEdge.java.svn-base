package wvustat.modules.diagram;

import java.io.Serializable;

public class DiagramEdge implements Serializable{
	public static final long serialVersionUID = 1L;
	
	int fromId;
	int toId;
	
	public DiagramEdge(int fromId, int toId){
		this.fromId = fromId;
		this.toId = toId;
	}

	public int getFromId() {
		return fromId;
	}

	public void setFromId(int id){
		fromId = id;
	}
	
	public int getToId() {
		return toId;
	}
	
	public void setToId(int id){
		toId = id;
	}
	
	public int hashCode(){
		return fromId * 100 + toId;
	}
	
	public boolean equals(Object obj){
		if(obj != null && obj instanceof DiagramEdge){
			DiagramEdge edge = (DiagramEdge)obj;
			return edge.getFromId() == fromId && edge.getToId() == toId;
		}
		
		return false;
	}
}
