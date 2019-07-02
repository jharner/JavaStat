/*
 * TransformAction.java
 *
 * Created on January 30, 2002, 11:35 AM
 */

package wvustat.modules;

import java.awt.Component;
import javax.swing.*;

import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Hashtable;

import wvustat.interfaces.*;
import wvustat.math.expression.*;
/**
 *
 * @author  James Harner
 * @version
 */
public class TransformAction extends javax.swing.AbstractAction implements java.awt.event.ActionListener {
	private static final long serialVersionUID = 1L;
	
	private Component rootComp;
    private DataSet dataSet;
    
    /** Creates new TransformAction */
    public TransformAction(DataSet dataSet, Component comp) {
        super("Transform...");
        this.dataSet=dataSet;
        this.rootComp=comp;
    }
    
    public void setDataSet(DataSet dataSet){
        this.dataSet=dataSet;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent p1) {    	
        Component comp=rootComp.getParent();
        
        while(!(comp instanceof JFrame))
            comp=comp.getParent();
        
        //Get the names of all variables
        Vector v1 = dataSet.getVariables();
        Vector v2 = new Vector();
        for (int i=0; i<v1.size(); i++){
        	Variable var = (Variable)v1.elementAt(i);
        	v2.addElement(var.getName());
        }
            
        String formula = "";
        for(;;) {
        	FormulaDialog fd=new FormulaDialog((JFrame)comp, v2, formula);
        	fd.setLocationRelativeTo(rootComp);
        	fd.show();
        
        	formula=fd.getFormula();
        	String varName=fd.getName();
        
        	if(formula==null||formula.equals(""))
        		return;
        
        	try{
        		generateNewVariable(formula,varName);
        		break;
        	}
        	catch(ParseException se){
        		JOptionPane.showMessageDialog(comp,"Invalid expression, please make sure the formula you input is corrent.","Error",JOptionPane.WARNING_MESSAGE);
        	}
        	catch(TokenMgrError tme){
        		JOptionPane.showMessageDialog(comp,"Invalid expression, please make sure the formula you input is corrent.","Error",JOptionPane.WARNING_MESSAGE);
        	}
        	catch(ExecError ee){
        		/*JOptionPane.showMessageDialog(comp,"Error, formula contains undefined variable.","Error",JOptionPane.WARNING_MESSAGE);*/
        		JOptionPane.showMessageDialog(comp,ee.getMessage(),"Error",JOptionPane.WARNING_MESSAGE);
        		ee.printStackTrace();
        	}
        	catch(RemoteException re){
        		JOptionPane.showMessageDialog(comp,"Unexpected error while performing transformation!","Error",JOptionPane.WARNING_MESSAGE);
        		re.printStackTrace();
        	}
        }
    }
    
    private void generateNewVariable(String formula, String newVarName) throws RemoteException, ParseException, ExecError{
        
    	ExpressionParser ep = new ExpressionParser();
        Expression exp=ep.parse(formula);
        
        Vector v1=exp.getVariableNames();
        
        String[] varNames=dataSet.getVariableNames();
        
        boolean matched=false;        
        for(int i=0;i<v1.size();i++){
            String elem=v1.elementAt(i).toString();
            matched=false;
            
            for(int j=0;j<varNames.length;j++){
                
                if(elem.equalsIgnoreCase(varNames[j])){
                    matched=true;
                    break;
                }
            }                
            
            if(!matched)
                throw new ExecError("Formula contains undefined variable or variables.");
        }
        
        
        Hashtable ht=new Hashtable(5);
        
        //double[] vals=null;
        double[] vals2 = new double[dataSet.getSize()];
        String[] vals3 = new String[dataSet.getSize()];
        
        for(int j=0; j < vals2.length; j++){
        	for(int i=0; i<v1.size(); i++){
                String varName=v1.elementAt(i).toString();
                Variable var=dataSet.getVariable(varName);
                //vals=var.getNumValues();
                //ht.put(varName, new Double(vals[j]));
                ht.put(varName, var.getValue(j));
            }
        	
        	//vals2[j] = exp.value(ht);
        	exp.setVariables(ht);
        	if (exp.getReturnType() == Expression.NUMERICAL)
        		vals2[j] = exp.getDoubleValue();
        	else
        		vals3[j] = exp.getValue().toString();
        }
        
        //double[] vals2=exp.value(ht,vals.length);
        
        if(newVarName==null || newVarName.trim().equals(""))
         	newVarName=exp.unparse();
        newVarName = newVarName.trim();
        //if(newVarName.startsWith("(") && newVarName.endsWith(")"))
        //    newVarName=newVarName.substring(1,newVarName.length()-1); //Get rid of the "(" and ")"
        if (exp.getReturnType() == Expression.NUMERICAL)
        	dataSet.addVariable(newVarName,vals2);
        else
        	dataSet.addVariable(newVarName,vals3);
        
    }
    
}
