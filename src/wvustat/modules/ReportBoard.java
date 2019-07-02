package wvustat.modules;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Vector;
/**
*	ReportBoard is a container that can be used to display a set of reports. If there are more
* than one report, it will construct a group of radio buttons labeled with the report names to 
* choose which report to display.
*
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 13, 2000
*/

public class ReportBoard extends JPanel{
	private CardLayout card;
	//private JPanel rptPanel;
	//private ButtonGroup group;
	//private JRadioButton[] jrbs;
	
	//This vector stores all the labels for the reports
	private Vector vec=new Vector();
	
	private int index=0;
	private Report[] rpts;
	
	//private EventListenerList eList=new EventListenerList();
	//private ChangeEvent event;

	/**
	*	Constructor
	*
	*	@param	labels	the names for the reports
	*
	*/
	public ReportBoard(String[] labels, Report[] reports){
		rpts=reports;
		
		
		setBackground(Color.white);
		card=new CardLayout();
		setLayout(card);
		
		for(int i=0;i<labels.length;i++){
			JScrollPane jsp=new JScrollPane(reports[i]);
			this.add(labels[i], jsp);
			vec.addElement(labels[i]);
		}
		
		card.show(this,labels[0]);		
	}
	
        /*
	private void fireStateChanged(){
		Object[] list=eList.getListenerList();
		for(int i=list.length-2;i>=0;i-=2){
			if(list[i]==ChangeListener.class){
				if(event==null)
					event=new ChangeEvent(this);
				((ChangeListener)list[i+1]).stateChanged(event);
			}
		}
	}
        
	private void fireActionEvent(ActionEvent evt){
		Object[] list=eList.getListenerList();
		for(int i=list.length-2;i>=0;i-=2){
			if(list[i]==ActionListener.class){			
				((ActionListener)list[i+1]).actionPerformed(evt);
			}
		}
	}        
	
	public void addChangeListener(ChangeListener l){
		eList.add(ChangeListener.class, l);
	}
	
	public void removeChangeListener(ChangeListener l){
		eList.remove(ChangeListener.class, l);
	}
        
        public void addActionListener(ActionListener al){
            eList.add(ActionListener.class, al);
        }
        
        public void removeActionListener(ActionListener al){
            eList.remove(ActionListener.class, al);
        }

	/**
	* Set the current group to be the indexed group
	*/
	public void setGroup(int k){
		for(int i=0;i<rpts.length;i++){
			rpts[i].setGroup(k);
		}
		
		card.show(this, (String)(vec.elementAt(index)));
	}
		
	/*
	public void actionPerformed(ActionEvent ae){
                
		String arg=ae.getActionCommand();
                ActionEvent evt=new ActionEvent(this, ActionEvent.ACTION_PERFORMED, arg);
                fireActionEvent(evt);
	}
	
	/**
	* Get the title of current report
	*/
	public String getCurrentReportTitle(){
		return (String)(vec.elementAt(index));
	}
	
	/**
	* Select a report to display
	*/
	public void selectReport(int index){
		card.show(this, (String)(vec.elementAt(index)));
		this.index=index;
	}
        
        /**
        * Select a report to display by name
        */
        public void showReport(String name){
            int n=vec.indexOf(name);
            selectReport(n);
        }
	
	/**
	* update all reports contained in the board
	*/
	public void updateBoard(){
		for(int i=0;i<rpts.length;i++)
			rpts[i].updateReport();
	}
		
}
		

		
	
