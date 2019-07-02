package wvustat.util;

import wvustat.plotUtil.ComponentUtil;
import wvustat.swing.DataChooser;
import wvustat.table.DataSetTM;
import wvustat.table.XMLDataParser;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 14, 2003
 * Time: 10:12:29 AM
 * To change this template use Options | File Templates.
 */
public class DataFileUtils
{
    public static DataSet openFileAsDataSet(JComponent comp)
    {
        DataSetTM dataSet = null;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select a file");
        if (fc.showOpenDialog(comp) == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File openFile = fc.getSelectedFile();
                FileReader reader = new FileReader(openFile);
                XMLDataParser parser = new XMLDataParser();
                dataSet = parser.parse(reader);

            }
            catch (Exception ex)
            {
                dataSet = null;
                ex.printStackTrace();
                JOptionPane.showMessageDialog(comp, ex.getMessage());
            }
        }
        if(dataSet==null)
            return null;
        else
            return dataSet.getDataSet();
    }

    public static DataSet openRemoteFileAsDataSet(JComponent comp, String dataUrl)
    {
        DataSetTM dataSet = null;
        try
        {
            DataChooser dc = new DataChooser(ComponentUtil.getTopComponent(comp), dataUrl);
            dc.pack();
            dc.setLocationRelativeTo(comp);
            dc.show();

            String input = dc.getSelectedItem();
            if (input == null)
                return null;


            URL url = new URL(dataUrl + input.trim());

            InputStreamReader isr = new InputStreamReader(url.openStream());
            XMLDataParser parser = new XMLDataParser();
            dataSet = parser.parse(isr);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(comp, "Unable to connect to " + dataUrl);
        }
        return dataSet.getDataSet();
    }

    public static void exportCsv(DataSet dataset, File file, String delimiter) throws IOException {
        BufferedWriter writer=new BufferedWriter(new FileWriter(file));

        String newLine=System.getProperty("line.separator");

        for(int i=0;i<=dataset.getSize();i++){
            if(i>0)
                writer.write(newLine);
            for(int j=0;j<dataset.getVariableCount();j++){
                if(j>0)
                    writer.write(delimiter); // comma or tab
                Variable v=dataset.getVariable(j);
                if(i==0)
                    writer.write(v.getName());
                else {
                	String str = String.valueOf(v.getValue(i-1));
                	if (v.getValue(i-1) == null)
                		writer.write("");
                	else if (str.indexOf(',') == -1 && str.indexOf('\"') == -1 && str.indexOf('\t') == -1)
                		writer.write(str);
                	else
                		writer.write("\""+str+"\"");
                }
            }
        }
        writer.close();
    }


}
