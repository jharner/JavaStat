package wvustat.network.server;

import java.util.Vector;

/**
 * MsgQueue is an asynchronous FIFO queue which blocks the writer or reader when queue is full or empty correspondingly.
 *  
 * @author dajieluo
 *
 */
public class MsgQueue {
	private int nMax;
	private Vector queue;
	private int nCurrentBlock=0;

	public MsgQueue() {
		this(1000);
	}
	
	public MsgQueue(int num){
		nMax = num;
		queue = new Vector(nMax);
	}
	
	public synchronized void addMsg(Object msg)  {

		while(nCurrentBlock==nMax){
			try{
				wait();
			}catch(Exception ex){
				ex.printStackTrace(System.out);
			}
		}
		
		nCurrentBlock++;
		queue.addElement(msg);
		notifyAll();
	}

	public synchronized Object getNextMsg() {

		while(nCurrentBlock==0){
			try{
				wait();
			}catch(Exception ex){
				ex.printStackTrace(System.out);
			}
		}

		nCurrentBlock--;
		Object e =queue.elementAt(0);
		queue.removeElementAt(0);
		notifyAll();
		return e;
	}

	public int getCurrentQueueSize(){
		return nCurrentBlock;
	}
	
	public boolean isEmpty(){
		return nCurrentBlock == 0;
	}
	
	public boolean isFull(){
		return nCurrentBlock == nMax;
	}
	
	public synchronized void clear(){
		if (nCurrentBlock > 0)
			System.out.println("Warning: " + nCurrentBlock + " requests are discarded.");
		
		queue.clear();
		nCurrentBlock = 0;
	}

	public int getCapacity(){
		return nMax;
	}

	public synchronized void appendCapacity(int nAppend){
		if(nAppend<1) return ;
		nMax+=nAppend;
	}

}
