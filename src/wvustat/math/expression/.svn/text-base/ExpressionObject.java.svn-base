/*
 * ExpressionObject.java
 *
 * Created on November 19, 2001, 10:35 AM
 */

package wvustat.math.expression;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.*;
import java.util.List;

//import wvustat.plotUtil.*;

/**
 *
 * @author  Hengyi Xue
 * @version
 */
public class ExpressionObject extends Object
{
    public static final int FUNCTION=0;
    public static final int MODEL=1;
    private Expression expression;
    private Map paramTable;
    private Color lineColor = Color.black;
    private Vector changeListeners;
    private String unparsedForm;
    private boolean visible=true;
    private int type;
    //private List parameters;


    public ExpressionObject()
    {
        type=FUNCTION;
    }

    public ExpressionObject(int type)
    {
        this.type=type;
    }

    /** Creates new ExpressionObject */
    public ExpressionObject(Expression expression, String unparsedForm, int type)
    {
        this.type=type;
        setExpression(expression);
        setUnparsedForm(unparsedForm);
    }

    public void setUnparsedForm(String str)
    {
        this.unparsedForm = str;
    }

    public String getUnparsedForm()
    {
        return unparsedForm;
    }

    public List getParameters()
    {
        List paramList=new ArrayList();
        Iterator iter=paramTable.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry entry=(Map.Entry)iter.next();
            String varName=entry.getKey().toString();
            if(varName.equalsIgnoreCase("x"))
                continue;
            paramList.add(entry.getValue());
        }

        Collections.sort(paramList, new Parameter.ParameterComparator());
        return paramList;
    }

    public Parameter getParameter(String varName)
    {
        return (Parameter)paramTable.get(varName);
    }

    public void setExpression(Expression expression)
    {
        this.expression = expression;
        Vector vars = expression.getVariableNames();
        //parameters = new ArrayList();

        paramTable = new Hashtable();

        for (int i = 0; i < vars.size(); i++)
        {
            Parameter param = new Parameter(vars.elementAt(i).toString(), 1.0);
            //parameters.add(param);
            //paramTable.put(vars.elementAt(i), new Double(1.0));
            paramTable.put(vars.elementAt(i), param);
        }
    }

    public void addChangeListener(ChangeListener listener)
    {
        if (changeListeners == null)
            changeListeners = new Vector();

        changeListeners.addElement(listener);
    }

    public void removeChangeListener(ChangeListener listener)
    {
        changeListeners.remove(listener);
    }

    protected void fireChangeEvent(ChangeEvent evt)
    {
        if (changeListeners == null)
            return;

        for (int i = 0; i < changeListeners.size(); i++)
            ((ChangeListener) changeListeners.elementAt(i)).stateChanged(evt);
    }

    public Expression getExpression()
    {
        return expression;
    }

    public Map getVariables()
    {
        return paramTable;
    }

    public double value() throws ExecError
    {
        Map map=new HashMap();
        Iterator iter=paramTable.entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry entry=(Map.Entry)iter.next();
            double value=((Parameter)entry.getValue()).getValue();
            map.put(entry.getKey(), new Double(value));
        }
        return expression.value(map);
    }

    public String toString()
    {
        return expression.unparse();
    }

    public void setLineColor(Color color)
    {
        this.lineColor = color;
        ChangeEvent evt = new ChangeEvent(this);
        this.fireChangeEvent(evt);
    }

    public Color getLineColor()
    {
        return lineColor;
    }

    public void setXValue(double value)
    {
        //setVariableValue("x", value);
        Parameter param=(Parameter)paramTable.get("x");
        if(param!=null)
        param.setValue(value);
    }

    public void setVariableValue(String varName, double value)
    {
        if (paramTable.containsKey(varName))
        {
            Parameter param=(Parameter)paramTable.get(varName);
            param.setValue(value);
            fireChangeEvent(new ChangeEvent(this));
        }
    }

    public double getSumOfSquares(double[] x, double[] y)
    {
        double ss = 0;

		//System.out.println(x.length);
        for (int i = 0; i < x.length; i++)
        {

			
            this.setXValue(x[i]);
            try
            {
                double yHat = this.value();
                ss += (y[i] - yHat) * (y[i] - yHat);
				//sum up all the I's and divide by num of I's = mean square error
				//sqrt that and you have standard deviation
            }
            catch (ExecError error)
            {
            }
        }

        return ss;
    }

    public void printVarValues()
    {
        Iterator iter = paramTable.keySet().iterator();
        while (iter.hasNext())
        {
            Object key = iter.next();
            System.out.println(key + "=" + paramTable.get(key).toString());
        }
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
        fireChangeEvent(new ChangeEvent(this));
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public Map getParamTable()
    {
        return paramTable;
    }

    public void setParamTable(Map paramTable)
    {
        this.paramTable = paramTable;
        this.fireChangeEvent(new ChangeEvent(this));
    }
}