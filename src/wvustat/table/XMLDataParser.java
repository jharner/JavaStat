/*
 * XMLDataParser.java
 *
 * Created on Sep 29, 2000, 11:02 AM
 * Modified on Dec 28, 2006 by djluo. Use Java XML SAXParser to replace apache xerces parser
 */

package wvustat.table;

import org.xml.sax.*;
//import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import wvustat.data.BoolVariable;
import wvustat.data.CatVariable;
import wvustat.data.DataSetImpl;
import wvustat.data.NumVariable;
import wvustat.interfaces.DataSet;
import wvustat.interfaces.Variable;

import java.awt.*;
import java.util.List;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * XMLDataParser parses a xml data file used in JavaStat into a vector of variables.
 *
 * @author  Hengyi Xue
 * @version 1.0, Sep. 29, 2000
 */
//public class XMLDataParser extends Object implements ContentHandler
public class XMLDataParser extends DefaultHandler
{
    public static final String Description="description";
    public static final String Subject="subject";
    public static final String Authorization="authorization";
    public static final String Reference="module";

    //private static String parserName = "org.apache.xerces.parsers.SAXParser";
    private Locator locator;
    private Vector vars;
    private String varName, varType;
    private Vector varValues;
    private boolean DEBUG = false;
    private char roleChar;
    private String varLevels;
    private String dataSetName;
    private boolean insideValue = false;
    private boolean insideDescription = false;
    private List boolList;
    private String description;
    private String reference;
    private String authorization;
    private String subject;

    /** Creates new XMLDataParser */
    public XMLDataParser()
    {
    }

    /** Parse the data file specified into a SweepData object
     *
     * @param r the character stream that contains the data to be parsed
     */
    public DataSetTM parse(Reader r) throws IOException, ParseException
    {
        InputSource src = new InputSource(r);
        /*XMLReader parser = null;

        try
        {
            parser = XMLReaderFactory.createXMLReader(parserName);
        }
        catch (SAXException e)
        {
            e.printStackTrace();
            throw new ParseException("Cannot create XML parser.");
        }

        try
        {
            parser.setContentHandler(this);
            parser.setFeature("http://xml.org/sax/features/validation", false);

            parser.parse(src);
        }*/
        try
	    {
        	SAXParserFactory spFactory = SAXParserFactory.newInstance();
        	spFactory.setValidating(true);
        	spFactory.setNamespaceAware(true);
        	SAXParser parser = spFactory.newSAXParser();
        	parser.parse(src, this);
		}    
        catch (SAXException se)
        {
            throw new ParseException(se.getMessage());
        }
        catch (Exception others)
        {
            others.printStackTrace();
            throw new ParseException(others.getMessage());
        }

        int size = ((Variable) vars.elementAt(0)).getSize();
        BoolVariable stateVar = new BoolVariable(size);
        BoolVariable maskVar = new BoolVariable(size);
        Vector colorVar = new Vector(size);
        for (int i = 0; i < size; i++)
            colorVar.addElement(Color.black);

        vars.insertElementAt(maskVar, 0);
        vars.insertElementAt(colorVar, 0);
        vars.insertElementAt(stateVar, 0);

        DataSet ds = new DataSetImpl(dataSetName, vars);
        ds.setDescription(description);
        ds.setSubject(subject);
        ds.setAuthorization(authorization);
        ds.setReference(reference);
        
        DataSetTM dataSetTM = new DataSetTM(ds);
        for(int i=2;i<dataSetTM.getColumnCount();i++)
        {
            Variable v=ds.getVariable(i-2);
            if(v instanceof NumVariable && boolList.get(i-2).equals(Boolean.TRUE))
            {
                dataSetTM.setColumnFormat(0,i);
            }
        }
        return dataSetTM;
    }

    public void endElement(java.lang.String p1, java.lang.String p2, java.lang.String p3) throws org.xml.sax.SAXException
    {
        
        if (p2.equals("variable"))
        {
            Variable v = null;
            if (varType.equals("num"))
            {
                v = new NumVariable(varName, varValues);
            }
            else
            {
                v = new CatVariable(varName, varValues);
                if (varType.equals("ord"))
                	v.setOrdinal(true);
            }
            switch (roleChar)
            {
                case 'x':
                    v.setRole(DataSet.X_ROLE);
                    break;
                case 'y':
                    v.setRole(DataSet.Y_ROLE);
                    break;
                case 'z':
                    v.setRole(DataSet.Z_ROLE);
                    break;
                case 'l':
                    v.setRole(DataSet.L_ROLE);
                    break;
                case 'w':
                    v.setRole(DataSet.W_ROLE);
                    break;
                case 'f':
                case 'F':
                    v.setRole(DataSet.F_ROLE);
                    break;
                default:
                    v.setRole(DataSet.U_ROLE);
                    break;
            }
            
            if (varLevels != null) {
            	StringTokenizer st = new StringTokenizer(varLevels, ",");
            	Vector vec = new Vector();
            	while (st.hasMoreTokens()) 
            		vec.add(st.nextToken());
            	v.setLevelCheck(true);
            	v.setLevels(vec);
            }

            vars.addElement(v);
        }
        else if(p2.equals("value"))
        {
            insideValue=false;
        }
        else if(p2.equals("description"))
        {
         	insideDescription=false;
        }
        
    }

    public void setDocumentLocator(final org.xml.sax.Locator p1)
    {
        locator = p1;
    }

    public void startPrefixMapping(java.lang.String p1, java.lang.String p2) throws org.xml.sax.SAXException
    {
    }

    public void endDocument() throws org.xml.sax.SAXException
    {
        if (DEBUG)
        {
            System.err.println("End parsing document.");
        }
    }

    public void startDocument() throws org.xml.sax.SAXException
    {
        if (DEBUG)
        {
            System.err.println("Starting document...");
        }
    }

    public void endPrefixMapping(java.lang.String p1) throws org.xml.sax.SAXException
    {
    }

    public void startElement(java.lang.String p1, java.lang.String p2, java.lang.String p3, final org.xml.sax.Attributes p4) throws org.xml.sax.SAXException
    {
        if (DEBUG)
        {
            System.err.println("Starting element " + p2 + " ...");
        }

        if (p2.equals("dataset"))
        {
            vars = new Vector();
            dataSetName = p4.getValue("", "name");
            boolList=new ArrayList();
            subject=p4.getValue(Subject);
            description = null;
            authorization=p4.getValue(Authorization);
            reference=p4.getValue(Reference);
        }
        else if (p2.equals("variable"))
        {
            //insideValue = true;
            varValues = new Vector();
            varType = p4.getValue("", "type");
            varName = p4.getValue("", "name");
            roleChar = p4.getValue("", "role").charAt(0);
            varLevels = p4.getValue("", "levels");
            boolList.add(new Boolean(true));
        }
        else if(p2.equals("value"))
        {
            insideValue=true;
        }
        else if(p2.equals(Description)){
            //description=p4.getValue("text");
        	insideDescription = true;
        }

    }

    public void ignorableWhitespace(char[] p1, int p2, int p3) throws org.xml.sax.SAXException
    {
    }

    public void skippedEntity(java.lang.String p1) throws org.xml.sax.SAXException
    {
    }

    public void processingInstruction(java.lang.String p1, java.lang.String p2) throws org.xml.sax.SAXException
    {
    }

    public void characters(char[] p1, int p2, int p3) throws org.xml.sax.SAXException
    {
        if (insideValue == false && insideDescription == false)
            return;
        char[] tmp = new char[p3];

        for (int i = 0; i < tmp.length; i++)
            tmp[i] = p1[p2 + i];

        String str = new String(tmp);

        if (insideDescription == true) {
        	description = str;
        	return;
        }
        
        if (varType.equals("num"))
        {
            if (str.equals("NA"))
            {
                varValues.addElement(Variable.NUM_MISSING_VAL);
                return;
            }
            try
            {
                Double dVal = new Double(str);
                if(isWholeNumber(dVal.doubleValue())==false)
                {
                    boolList.set(boolList.size()-1, Boolean.FALSE);
                }
                varValues.addElement(dVal);
            }
            catch (NumberFormatException nfe)
            {
                StringBuffer buf = new StringBuffer(150);
                buf.append("Character data found when numeric data is expected at line ");
                buf.append(locator.getLineNumber());
                buf.append(" and column " + locator.getColumnNumber() + " within variable " + varName);
                throw new SAXException(buf.toString());
            }
        }
        else
        {
            varValues.addElement(str);
        }

    }

    public void printCurrentLocation()
    {
        System.out.println("At line " + locator.getLineNumber() + " column " + locator.getColumnNumber());
        System.out.println("Current variable " + varName);
    }

    private boolean isWholeNumber(double value)
    {
        return value==(int)value;
    }
    
    public void fatalError(SAXParseException exp) throws SAXException{
    	exp.printStackTrace();
    }
    
    public void warning(SAXParseException exp) throws SAXException{
    	exp.printStackTrace();
    }
}
