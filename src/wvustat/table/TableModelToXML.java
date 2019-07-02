/*
 * TableModelToXML.java
 *
 * Created on October 5, 2000, 2:06 PM
 */

package wvustat.table;

import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * TableModelToXML saves data in a javax.swing.table.TableModel and formats it as XML.
 *
 * @author  Hengyi Xue
 * @version 1.0, Oct. 5, 2000
 */
public class TableModelToXML extends Object
{

    /** Creates new TableModelToXML */
    public TableModelToXML()
    {
    }

    public String toXML(DataSet tm, String name)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        //String endl="\n"; //System.getProperty("line.separator");

        pw.println("<?xml version=\"1.0\" standalone=\"no\"?>");
        pw.println("<!DOCTYPE dataset SYSTEM \"http://ideal.stat.wvu.edu/ideal/dtd/dataset.dtd\">");
        StringBuffer buffer=new StringBuffer();
        buffer.append("<dataset name=\"").append(name).append("\"");
        if(tm.getSubject()!=null)
            buffer.append(" subject=\"").append(tm.getSubject()).append("\"");
        
        if(tm.getReference()!=null){
            buffer.append(" ").append(XMLDataParser.Reference).append("=\"").append(tm.getReference());
            buffer.append("\"");
        }
        
        if(tm.getAuthorization()!=null){
            buffer.append(" ").append(XMLDataParser.Authorization).append("=\"");
            buffer.append(tm.getAuthorization()).append("\"");
        }
        
        buffer.append(">");
        pw.println(buffer.toString());

        if(tm.getDescription()!=null){
            buffer=new StringBuffer();
            buffer.append("<");
            buffer.append(XMLDataParser.Description).append("><![CDATA[").
                    append(tm.getDescription()).append("]]></");
            
            buffer.append(XMLDataParser.Description).append(">");
            pw.println(buffer.toString());

        }

        //Variable freqVar = tm.getFreqVariable();

        for (int i = 0; i < tm.getVariableCount(); i++)
        {

            try
            {
                Variable v = tm.getVariable(i);
                pw.print("<variable name=\"" + v.getName() + "\" ");
                switch (v.getRole())
                {
                    case DataSet.X_ROLE:
                        pw.print("role=\"x\" ");
                        break;
                    case DataSet.Y_ROLE:
                        pw.print("role=\"y\" ");
                        break;
                    case DataSet.Z_ROLE:
                        pw.print("role=\"z\" ");
                        break;
                    case DataSet.L_ROLE:
                        pw.print("role=\"l\" ");
                        break;
                    case DataSet.F_ROLE:
                        pw.print("role=\"f\" ");
                        break;
                    default:
                        pw.print("role=\"u\" ");
                        break;
                }


                if (v.getType() == Variable.NUMERIC)
                {
                    pw.print("type=\"num\"");
                }
                else
                {
                	if (!v.isOrdinal())
                		pw.print("type=\"cat\"");
                	else
                		pw.print("type=\"ord\"");
                }

                if (v.getLevels() != null) {
                	pw.print(" levels=\"");
                	List levels = v.getLevels();
                	for (int k=0; k<levels.size(); k++){
                		pw.print(levels.get(k));
                		if(k<levels.size()-1) pw.print(",");
                	}                		
                	pw.print("\"");
                }
                                
                pw.println(">");

                for (int j = 0; j < tm.getSize(); j++)
                {
                    /*
                    if(freqVar!=null){
                        int cnt=(int)(((Double)freqVar.getValue(j)).doubleValue());
                        for(int k=0;k<cnt;k++){
                            if(v.getRole()==DataSet.F_ROLE)
                                pw.println("<value>1</value>");
                            else{
                                String val=v.getValue(j).toString();
                                if(val.equals(""))
                                    val=" ";
                                pw.println("<value>"+val+"</value>");
                            }
                        }
                    }
                    */
                    {
                        String val;
                        if (v.getValue(j) != null)
                        	val = v.getValue(j).toString();
                        else
                        	val = "";
                        
                        if (val.equals(""))
                            val = " ";
                        pw.println("<value>" + val + "</value>");
                    }
                }


                pw.println("</variable>");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        pw.println("</dataset>");
        pw.flush();
        pw.close();

        return sw.toString();
    }

}
