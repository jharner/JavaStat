package wvustat.network.client;

import java.net.*;
import java.io.*;
import wvustat.network.JStatMessage;

public class ServerEntrance{
    URL serverURL;

    public ServerEntrance(URL url){
	serverURL = url;
    }

    /**
     * send the request to server and return response.
     */
    public JStatMessage sendRequest(JStatMessage request){
	if(serverURL == null || request == null)
	    return null;

	JStatMessage response = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	try{
	    URLConnection conn = serverURL.openConnection();
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    out = new ObjectOutputStream(conn.getOutputStream());
	    out.writeObject(request);

	    in = new ObjectInputStream(conn.getInputStream());
	    response = (JStatMessage)in.readObject();
	}catch(Exception exp){
	    response = new JStatMessage(request);
	    response.setStatus(JStatMessage.ERROR);
	    response.setData(exp);
	}finally{
	    try{
		out.close();
		in.close();
	    }catch(Exception e){
	    }
	}

	return response;
    }
}
