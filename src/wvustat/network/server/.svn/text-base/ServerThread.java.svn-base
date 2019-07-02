package wvustat.network.server;

import java.rmi.RemoteException;
import java.util.*;

public class ServerThread implements Runnable{
	
	public void run() {

        for(;;) {
        	
        	Vector v = (Vector)AsyncJRIServerImpl.reqMsgQueue.getNextMsg();
        	String cmd = (String)v.elementAt(0);
        	
        	String callee = null;
        	Object obj = null;
    		String msg = null;
    		int taskId = -1;
        	
    		try {
    			if (cmd.equals("lm")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String formula = (String)v.elementAt(3);
	        		ArrayList columns = (ArrayList)v.elementAt(4);
        			obj = AsyncJRIServerImpl.getRInstance().lm(formula, columns);
    			}
	        	else if (cmd.equals("glm")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String formula = (String)v.elementAt(3);
	        		ArrayList columns = (ArrayList)v.elementAt(4);
	        		String family = (String)v.elementAt(5);
	        		String link = (String)v.elementAt(6);
	        		obj = AsyncJRIServerImpl.getRInstance().glm(formula, columns, family, link);
	        	}
	        	else if (cmd.equals("summary")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().summary(objName);
	        	}
	        	else if (cmd.equals("geneNames")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().geneNames(objName);
	        	}
	        	else if (cmd.equals("geneIntensity")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		String geneName = (String)v.elementAt(4);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().geneIntensity(objName, geneName);
	        	}
	        	else if (cmd.equals("geneIntensityAsColumnByNames")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		ArrayList geneNames = (ArrayList)v.elementAt(4);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().geneIntensityAsColumnByNames(objName, geneNames);
	        	}
	        	else if (cmd.equals("geneIntensityAsRowByNames")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		ArrayList geneNames = (ArrayList)v.elementAt(4);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().geneIntensityAsRowByNames(objName, geneNames);
	        	}
	        	else if (cmd.equals("geneAnnotationByNames")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String type = (String)v.elementAt(3);
	        		ArrayList geneNames = (ArrayList)v.elementAt(4);
	        		//AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().geneAnnotationByNames(type, geneNames);
	        	}
	        	else if (cmd.equals("preprocess")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		String bgcorrect = (String)v.elementAt(4);
	        		String normalize = (String)v.elementAt(5);
	        		String pmcorrect = (String)v.elementAt(6);
	        		String summary = (String)v.elementAt(7);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().preprocess(objName, bgcorrect, normalize, pmcorrect, summary);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else if (cmd.equals("gcrma")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().gcrma(objName);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else if (cmd.equals("exonRma")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().exonRma(callee, objName);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else if (cmd.equals("exonGcRma")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().exonGcRma(callee, objName);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else if (cmd.equals("mtp")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		String objName = (String)v.elementAt(3);
	        		String mtpName = (String)v.elementAt(4);
	        		String test = (String)v.elementAt(5);
	        		String typeone = (String)v.elementAt(6);
	        		int k = ((Integer)v.elementAt(7)).intValue();
	        		double q = ((Double)v.elementAt(8)).doubleValue();
	        		double alpha = ((Double)v.elementAt(9)).doubleValue();
	        		String method = (String)v.elementAt(10);
	        		String fdrMethod = (String)v.elementAt(11);
	        		boolean bootstrap = ((Boolean)v.elementAt(12)).booleanValue();
	        		List yInputs = (List)v.elementAt(13);
	        		List yValues = (List)v.elementAt(14);
	        		List yIncluded = (List)v.elementAt(15);
	        		List filterfuns = (List)v.elementAt(16);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().mtp(objName, mtpName, test, typeone, k, q, alpha, method, fdrMethod, bootstrap, yInputs, yValues, yIncluded, filterfuns);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else if (cmd.equals("rm")) {
	        		callee = (String)v.elementAt(1);
	        		taskId = ((Integer)v.elementAt(2)).intValue();
	        		List objNames = (List)v.elementAt(3);
	        		AsyncJRIServerImpl.getRInstance().loadWorkSpace(callee);
	        		obj = AsyncJRIServerImpl.getRInstance().deleteObjects(callee, objNames);
	        		AsyncJRIServerImpl.getRInstance().saveWorkSpace(callee);
	        	}
	        	else {
	        		msg = "Unsupported command";
	        	}
        	
    		}
    		catch (RemoteException rex) {
    			msg = rex.getMessage();
    			rex.printStackTrace();
    		}
    		catch (java.lang.Throwable t) {
    			msg = "Invalid data or operation causing " + t.getClass().getName();
    			t.printStackTrace();
    		}
        	
        	System.out.println(callee);
        	
    		if (msg == null) 
    			AsyncJRIServerImpl.taskQueue.addMsg(callee, taskId, obj);
    		else 
    			AsyncJRIServerImpl.taskQueue.addMsg(callee, taskId, msg);

        }
	}

}
