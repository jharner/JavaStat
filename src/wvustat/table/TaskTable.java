package wvustat.table;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.rmi.RemoteException;
import wvustat.network.*;
import wvustat.network.client.*;


public class TaskTable extends JInternalFrame implements Runnable{
	
	private JTable table;	
	//private ListSelectionListener rowSelectionListener;
	
	public TaskTable() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		
		table = new JTable(new TaskTableModel()) {
			public String getToolTipText(MouseEvent e) {
				java.awt.Point p = e.getPoint();
				int rowIndex = rowAtPoint(p);
				TaskTableModel model = (TaskTableModel)getModel();
				return model.getRowToolTip(rowIndex);
			}
			
		};
		table.setPreferredScrollableViewportSize(new Dimension(400, 100));
		
		//rowSelectionListener = new RowSelectionListener();
		//table.getSelectionModel().addListSelectionListener(rowSelectionListener);
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		
		
		TableColumn column = null;
		for (int i = 0; i < 3; i++){
			column = table.getColumnModel().getColumn(i);
			if ( i==0 )
				column.setPreferredWidth(200);
			else if ( i==1 )
				column.setPreferredWidth(100);
			else if ( i==2 )
				column.setPreferredWidth(100);			
		}
		
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane);
		
		this.setClosable(true);
		this.setResizable(true);
		this.setMaximizable(false);
        this.setIconifiable(false);
		
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setTitle("Remote Task");
		this.setContentPane(panel);
		this.pack();
		this.setVisible(false);		
		
		Runnable runnable = new Runnable()
		{
            public void run()
            {
            	for (;;) {
        			try
        	        {
        	            Thread.sleep(1000); //1 second
        	        }
        	        catch (InterruptedException e){}
        			
        	        ((TaskTableModel)table.getModel()).updateTask();
        		}
            }
		};
		
		(new Thread(runnable)).start();
		
		(new Thread(this)).start();
	}
	
	public void addTask(CallbackRemote callee, String clientid, int taskId, String name) {
		
		((TaskTableModel)table.getModel()).addTask(callee, clientid, taskId, name);
		
		Dimension d1 = MainPanel.getDesktopPane().getSize();
        Dimension d2 = this.getSize();
        
        this.setLocation(d1.width - d2.width, d1.height - d2.height);		
		this.setVisible(true);
		
		//Make the last row visible in the JTable component.
		if (!(table.getParent() instanceof JViewport)) {
			return;
		} else {
			JViewport viewport = (JViewport)table.getParent();
			Rectangle rect = table.getCellRect(table.getRowCount() - 1, 0, true);
			Point pt = viewport.getViewPosition();
			rect.setLocation(rect.x-pt.x, rect.y-pt.y);
			viewport.scrollRectToVisible(rect);
		}
		
	}
	
	public void finishTask(int taskId) {
		((TaskTableModel)table.getModel()).finishTask(taskId);
		
		Dimension d1 = MainPanel.getDesktopPane().getSize();
        Dimension d2 = this.getSize();
        
        this.setLocation(d1.width - d2.width, d1.height - d2.height);	
		this.setVisible(true);
	}
	
	public void deleteFinishedTask() {
		((TaskTableModel)table.getModel()).deleteFinishedTask();
	}
	
	public boolean containRunningTask() {
		return ((TaskTableModel)table.getModel()).containRunningTask();
	}
	
	public int getTaskCount() {
		return ((TaskTableModel)table.getModel()).getRowCount();
	}
	
	public void run(){
		for (;;) {
			try
	        {
	            Thread.sleep(1000); // 1 second
	        }
	        catch (InterruptedException e){}
			
	        ((TaskTableModel)table.getModel()).pollTask();
		}		
	}
	
	class TaskTableModel extends AbstractTableModel {
		private String[] columnNames = {"Name", "State", "Time"};
		private Hashtable taskIds;
		private Vector callees, clientids, taskNames, taskStates, taskTime, pollNum, lastPollTime, tips;
		
		public TaskTableModel(){
			taskIds = new Hashtable();
			callees = new Vector();
			clientids = new Vector();
			taskNames = new Vector();
			taskStates = new Vector();
			taskTime = new Vector();
			pollNum = new Vector();
			lastPollTime = new Vector();
			tips = new Vector();
		}
		
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public int getRowCount() {
			return taskNames.size();
		}
		
		public String getColumnName(int col) {
			return columnNames[col];
		}
		
		public Object getValueAt(int row, int col) {
			if (col == 0)
				return taskNames.elementAt(row);
			else if (col == 1)
				return taskStates.elementAt(row);
			else {
				boolean b = ((String)taskStates.elementAt(row)).equals("Running");
				Date t = (Date)taskTime.elementAt(row);
				if (b) {
					long s = ((new Date()).getTime() - t.getTime()) / 1000;
					long h = s / 3600;
					long m = s / 60 % 60;
					s = s % 60;
					
					NumberFormat nf = NumberFormat.getIntegerInstance();
					nf.setMinimumIntegerDigits(2);
										
					return  h+":"+ nf.format(m) +":"+ nf.format(s);
				} else {
					return DateFormat.getTimeInstance().format(t);
				}
			}
			
		}
		
		public Class getColumnClass(int col) {
				return String.class;
		}
		
		public boolean isCellEditable(int row, int col) {
			return false;
		}
		
		public void addTask(CallbackRemote callee, String clientid, int taskId, String name) {
			int row = taskNames.size();
			taskIds.put(new Integer(taskId), new Integer(row));
			callees.addElement(callee);
			clientids.addElement(clientid);
			taskNames.addElement(name);
			taskStates.addElement("Running");
			taskTime.addElement(new Date());
			pollNum.addElement(new Integer(0));
			lastPollTime.addElement(new Date());
			tips.addElement("");
			fireTableRowsInserted(row, row);
		}
		
		public void finishTask(int taskId) {
			int row = ((Integer)taskIds.get(new Integer(taskId))).intValue();
			taskStates.setElementAt("Finished", row);
			taskTime.setElementAt(new Date(), row);
			fireTableRowsUpdated(row, row);
		}
		
		public void deleteFinishedTask() {
			
			if (!taskStates.contains("Running") && !taskStates.contains("Error")) {
				taskIds.clear();
				callees.clear();
				clientids.clear();
				taskNames.clear();
				taskStates.clear();
				taskTime.clear();
				pollNum.clear();
				lastPollTime.clear();
				tips.clear();
				fireTableDataChanged();
			}
		}
		
		public void updateTask() {
			if (taskStates.contains("Running"))
				fireTableDataChanged(); //when called, the highlight of user selected row will be cleared.
		}
		
		public boolean containRunningTask() {
			return taskStates.contains("Running");
		}
		
		public void pollTask() {
			if (!taskStates.contains("Running")) return;
			
			int row = taskStates.indexOf("Running");
			String clientid = (String)clientids.elementAt(row);
			Date t = (Date)lastPollTime.elementAt(row);
			int n = ((Integer)pollNum.elementAt(row)).intValue() + 1;
			long s = ((new Date()).getTime() - t.getTime());
			long interval = Math.min(Math.round( Math.pow(n-1, 2.0d) * 2000 + 1000 ), 900000);
			
			int taskId = -1;
			for (Enumeration e = taskIds.keys(); e.hasMoreElements(); ) {
				taskId = ((Integer)e.nextElement()).intValue();
				if (((Integer)taskIds.get(new Integer(taskId))).equals(new Integer(row)))
					break;
		    }
			
			
			if (s >= interval) {
				
				String tip = "";
				
				try {
					
					Object result = JRIClient.pollTask(clientid, taskId);
					
					if (result != null) {
						taskStates.setElementAt("Finished", row);
						taskTime.setElementAt(new Date(), row);
						fireTableRowsUpdated(row, row);
						((CallbackRemote)callees.elementAt(row)).callback(result);
					}

				} catch (RemoteException rex) {
					rex.printStackTrace();
					
					taskStates.setElementAt("Error", row);
					taskTime.setElementAt(new Date(), row);
					fireTableRowsUpdated(row, row);
					
					String err = rex.getMessage().replaceAll("\t", "");
                    tip = err.substring(err.lastIndexOf(':') + 1) + ". ";
                    
                    ((CallbackRemote)callees.elementAt(row)).callback(tip);
				}
				
				pollNum.setElementAt(new Integer(n), row);
				lastPollTime.setElementAt(new Date(), row);
				tip += "Polled " + n + " times. Last polled at " + DateFormat.getTimeInstance().format(new Date());
				tips.setElementAt(tip, row);
				
			}
			
		}
		
		public String getRowToolTip(int row) {
			return (String)tips.elementAt(row);
		}
		
	} //end TaskTableModel
	
	/*class RowSelectionListener implements ListSelectionListener
    {
		public void valueChanged(ListSelectionEvent event)
        {
			if (event.getValueIsAdjusting()) return;
			ListSelectionModel lsm = (ListSelectionModel)event.getSource();
			
			if (!lsm.isSelectionEmpty()) {
				int row = lsm.getMinSelectionIndex();
				table.getSelectionModel().setSelectionInterval(row, row);
				table.getColumnModel().getSelectionModel().setSelectionInterval(0, table.getColumnCount()-1);
			}			
        }
    }*/

}
