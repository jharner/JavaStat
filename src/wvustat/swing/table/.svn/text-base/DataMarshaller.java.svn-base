package wvustat.swing.table;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 29, 2002
 * Time: 10:19:23 AM
 * To change this template use Options | File Templates.
 */
public class DataMarshaller extends DefaultHandler
{
    private DataSet dataSet;
    private CharArrayWriter contents = new CharArrayWriter();
    private String colName;
    private String colRole;
    private List colData=new ArrayList();

    public DataMarshaller(DataSet dataSet)
    {
        this.dataSet=dataSet;
    }

    public void collect(InputStream inputStream) throws Exception
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser saxParser = spf.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);
        xmlReader.parse(new InputSource(inputStream));
    }

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException
    {
        contents.reset();
        if(qName.equalsIgnoreCase("variable"))
        {
            colName=attributes.getValue("name");
            colRole=attributes.getValue("role");
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException
    {
        if(qName.equalsIgnoreCase("value"))
        {
            try
            {
                colData.add(new Double(contents.toString()));
            }
            catch(NumberFormatException e)
            {
                colData.add(new Double(0));
            }
        }
        else if(qName.equalsIgnoreCase("variable"))
        {
            double[] data=nativeArray(colData.toArray());
            dataSet.addColumn(colName, data);
            dataSet.setColumnRole(stringToRole(colRole),dataSet.getColumnCount()-1);
            colData.clear();
        }
    }

    public void characters(char ch[], int start, int length)
            throws SAXException
    {
        contents.write(ch, start, length);
    }

    private double[] nativeArray(Object[] array)
    {
        double[] data=new double[array.length];
        for(int i=0;i<array.length;i++)
        {
            data[i]=((Double)array[i]).doubleValue();
        }
        return data;
    }

    private int stringToRole(String role)
    {
        if(role.equalsIgnoreCase("x"))
            return DataSet.X_ROLE;
        else if(role.equalsIgnoreCase("y"))
            return DataSet.Y_ROLE;
        else if(role.equalsIgnoreCase("f"))
            return DataSet.FREQ_ROLE;
        else
            return DataSet.UNDEFINED_ROLE;
    }
}

