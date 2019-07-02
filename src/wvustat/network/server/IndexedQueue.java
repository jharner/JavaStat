package wvustat.network.server;

import java.util.Hashtable;

public class IndexedQueue {
	private Hashtable table;
	private static final String SEPARATOR = "|";
	
	public IndexedQueue() {
		table = new Hashtable();
	}
	
	public synchronized void addMsg(String userid, int taskid, Object msg)  {
		String key = getKey(userid, taskid);
		table.put(key, msg);
	}
	
	public synchronized Object getMsg(String userid, int taskid) {
		String key = getKey(userid, taskid);
		Object value = table.get(key);
		if (value == null) 
			return null;
		
		table.remove(key);
		return value;
		
	}
	
	public boolean contains(String userid, int taskid) {
		String key = getKey(userid, taskid);
		return table.containsKey(key);
	}
	
	public int getCurrentQueueSize(){
		return table.size();
	}
	
	public synchronized void clear(){
		if (table.size() > 0)
			System.out.println("Warning: " + table.size() + " tasks are discarded.");
		
		table.clear();
	}
	
	public static String getKey(String userid, int taskid) {
		String key = userid + SEPARATOR + Integer.toString(taskid);
		return key;
	}

}
