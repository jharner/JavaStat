package wvustat.util;

import java.io.*;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: May 9, 2004
 * Time: 1:52:23 PM
 * To change this template use Options | File Templates.
 */
public class TextFileParser
{
    private String delimiter;

    public TextFileParser()
    {
    }

    public String getDelimiter()
    {
        return delimiter;
    }

    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }

    public List parse(InputStream is) throws Exception
    {
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));
        String line=reader.readLine();
        if(line==null)
            throw new Exception("File is empty!");
        
        while (line.startsWith("#"))
        	line = reader.readLine();
        
        //StringTokenizer st=new StringTokenizer(line, delimiter);
        ArrayList st = myStringTokenizer(line, delimiter);
        List columns=new ArrayList();
        int i = 0;
        //while(st.hasMoreTokens())
        while(i < st.size())
        {
            List column=new ArrayList();
            //column.add(st.nextToken());
            column.add(st.get(i));
            columns.add(column);
            i++;
        }

        while((line=reader.readLine())!=null)
        {
            //StringTokenizer tokenizer=new StringTokenizer(line, delimiter);
        	st = myStringTokenizer(line, delimiter);
            int index=0;
            i = 0;
            //while(tokenizer.hasMoreTokens())
            while (i < st.size())
            {
                //String value=tokenizer.nextToken().trim();
            	String value = ((String)st.get(i)).trim();
                List column=(List)columns.get(index);
                column.add(value);
                index++;
                i++;
            }
            line = null;
        }

        return columns;
    }
    
    private ArrayList myStringTokenizer(String line, String delimiter) {
    	int i = 0;
    	int index = 0;
    	ArrayList tokens = new ArrayList();
    	
    	while (i < line.length()) {
    		char ch = line.charAt(i);
    		
    		if (ch == '\"') {
    			
    			i++;
    			while (i < line.length() && line.charAt(i) != '\"') {
    				i++;
    			}
    			
    			if (i == line.length() - 1) {
        			tokens.add(line.substring(index, line.length()));
        		}
    			
    		} 
    		else if (ch == delimiter.charAt(0)) {
    			tokens.add(line.substring(index, i));
    		    index = i+1;
    		
    		    if (i == line.length() - 1) 
        			tokens.add("");
    		} 
    		else if (i == line.length() - 1) {
    			tokens.add(line.substring(index, line.length()));
    		}
    		
    		i++;
    		
    	}
    	
    	for (i = 0; i < tokens.size(); i++) {
    		String s = ((String)tokens.get(i)).trim();
    		if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
    			s = s.substring(1, s.length() - 1);
    			tokens.set(i, s);
    		}
    	}
    	
    	
    	return tokens;
    }    
}
