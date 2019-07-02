package wvustat.swing.table;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 29, 2002
 * Time: 10:03:23 AM
 * To change this template use Options | File Templates.
 */
public class XMLWriter
{
    public static void saveDataSetInXML(DataSet dataSet, File file) throws IOException
    {
        BufferedWriter writer=new BufferedWriter(new FileWriter(file));
	writer.write("<?xml version=\"1.0\" standalone=\"yes\" ?>\n");
	writer.write("<dataset>\n");

	//I set the initial value of i to 1 here to remove the Color column
	//By Jun Tan
        for(int i=1;i<dataSet.getColumnCount();i++)
        {
            writer.write("<variable name=\""+dataSet.getColumnName(i)+"\" ");
            writer.write("role=\"");
            int role=dataSet.getColumnRole(i);
            if(role==DataSet.X_ROLE)
                writer.write("x\"");
            else if(role==DataSet.Y_ROLE)
                writer.write("y\"");
            else if(role==DataSet.Z_ROLE)
                writer.write("z\"");
            else
                writer.write("u\"");

            writer.write(" type=\"num\">\n");

            double[] data=dataSet.getColumnData(i - 1);
            for(int j=0;j<data.length;j++)
            {
                writer.write("<value>"+data[j]+"</value>\n");
            }
            writer.write("</variable>\n");
        }

        writer.write("</dataset>\n");
        writer.close();
    }
}
