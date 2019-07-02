package wvustat.network;

import java.io.Serializable;
import java.util.Date;

public class JStatMessage implements Serializable{
    public final static int NORMAL = 0;
    public final static int ERROR = 1;

    private int status;
    private long id;
    private String operation;
    private Serializable data;

    public JStatMessage(){
	status = NORMAL;
	operation = "notDefined";
	data = null;
	id = getNextID();
    }

    public JStatMessage(int s, String op, Serializable d){
	status = s;
	operation = op;
	data = d;
    }

    /**
     * This is used for creating replying message for a request.
     */
    public JStatMessage(JStatMessage orig){
	status = orig.getStatus();
	operation = orig.getOperation();
	id = orig.getID();
	data = null;
    }

    public long getID(){
	return id;
    }

    public int getStatus(){
	return status;
    }

    public void setStatus(int newStatus){
	status = newStatus;
    }

    public String getOperation(){
	return operation;
    }

    public void setOperation(String op){
	operation = op;
    }

    public Serializable getData(){
	return data;
    }

    public void setData(Serializable d){
	data = d;
    }

    public synchronized static long getNextID(){
	Date date = new Date();
	return date.getTime();
    }
}
