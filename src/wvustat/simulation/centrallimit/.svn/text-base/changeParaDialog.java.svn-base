package wvustat.simulation.centrallimit;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Hashtable;
import javax.swing.text.NumberFormatter;
import java.beans.*;

public class changeParaDialog extends JDialog implements ActionListener,ChangeListener,PropertyChangeListener{

    JPanel	panelUpper, panelLower,namePanel;
    JSlider	mySlider;
    publicData	pData;
    CtrLimit	hostApplet;
    JButton	buttonOK, buttonCancel;
    JLabel      slidername,inforLabel,infor2;
    JFormattedTextField mytextField;
    int index;

    public changeParaDialog( JFrame parent, CtrLimit anApplet, publicData tmpData, int index ){

        super( parent, "Set Parameter Value", false );
      	this.getContentPane().setLayout(new FlowLayout());
      	
        pData = tmpData;
        hostApplet = anApplet;

        panelUpper = new JPanel();
        panelUpper.setLayout(new GridLayout(4,1));
        this.index=index;
        
        if (index==1){
        	slidername=new JLabel("Number of Samples",SwingConstants.CENTER);
        	mySlider = new JSlider(pData.paraLower[1], pData.paraUpper[1], pData.getTotalSample());
        	mySlider.addChangeListener(this);
        	mySlider.setPaintTicks(true);
        	mySlider.setMajorTickSpacing(950);
        	Hashtable labelTable1 = new Hashtable();
		labelTable1.put( new Integer(50), new JLabel("50") );
		labelTable1.put( new Integer(1000), new JLabel("1000") );
		labelTable1.put(new Integer(2000),new JLabel("2000"));
		labelTable1.put(new Integer(3000),new JLabel("3000"));
		labelTable1.put(new Integer(4000),new JLabel("4000"));
		labelTable1.put( new Integer(5000), new JLabel("5000") );
		mySlider.setLabelTable( labelTable1);
		mySlider.setPaintLabels(true);
		mySlider.setPaintTrack(true);
		
		java.text.NumberFormat numberFormat = java.text.NumberFormat.getIntegerInstance();
       	 	NumberFormatter formatter = new NumberFormatter(numberFormat);
       	 	formatter.setMinimum(new Integer(pData.paraLower[1]));
        	formatter.setMaximum(new Integer(pData.paraUpper[1]));
        	mytextField = new JFormattedTextField(formatter);
        	mytextField.setValue(new Integer(pData.getTotalSample()));
        	mytextField.setColumns(5);
        	mytextField.addPropertyChangeListener(this);
        	
        	mytextField.getInputMap().put(KeyStroke.getKeyStroke(
                                        KeyEvent.VK_ENTER, 0),
                                        "check");
        	mytextField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!mytextField.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    mytextField.selectAll();
                } else try {                    //The text is valid,
                    mytextField.commitEdit();     //so use it.
                } catch (java.text.ParseException exc) { }
            }
        });
        	
	}
	else if (index==2){
		slidername=new JLabel("Number of Samples/Second",SwingConstants.CENTER);
		mySlider = new JSlider(pData.paraLower[2], pData.paraUpper[2], pData.getSamplePerSecond());
        	mySlider.addChangeListener(this);
        	mySlider.setPaintTicks(true);
        	mySlider.setMajorTickSpacing(1000);
        	Hashtable labelTable2 = new Hashtable();
		labelTable2.put( new Integer(1), new JLabel("1") );
		labelTable2.put( new Integer(1000), new JLabel("1000") );
		labelTable2.put( new Integer(2000), new JLabel("2000") );
		labelTable2.put( new Integer(3000), new JLabel("3000") );
		labelTable2.put( new Integer(4000), new JLabel("4000") );
		labelTable2.put( new Integer(5000), new JLabel("5000") );
		mySlider.setLabelTable( labelTable2 );
		mySlider.setPaintLabels(true);
		
		java.text.NumberFormat numberFormat = java.text.NumberFormat.getIntegerInstance();
       	 	NumberFormatter formatter = new NumberFormatter(numberFormat);
       	 	formatter.setMinimum(new Integer(pData.paraLower[2]));
        	formatter.setMaximum(new Integer(pData.paraUpper[2]));
        	mytextField = new JFormattedTextField(formatter);
        	mytextField.setValue(new Integer(pData.getSamplePerSecond()));
        	mytextField.setColumns(5);
        	mytextField.addPropertyChangeListener(this);
        	
        	mytextField.getInputMap().put(KeyStroke.getKeyStroke(
                                        KeyEvent.VK_ENTER, 0),
                                        "check");
        	mytextField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!mytextField.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    mytextField.selectAll();
                } else try {                    //The text is valid,
                    mytextField.commitEdit();     //so use it.
                } catch (java.text.ParseException exc) { }
            }
        });
        }
        else if (index==3) {
        	slidername=new JLabel("Number of Bars",SwingConstants.CENTER);
		mySlider = new JSlider(pData.paraLower[3], pData.paraUpper[3], pData.getBarNumber());
        	mySlider.addChangeListener(this);
        	mySlider.setPaintTicks(true);
        	mySlider.setMajorTickSpacing(10);
        	Hashtable labelTable3 = new Hashtable();
		labelTable3.put( new Integer(2), new JLabel("2") );
		labelTable3.put( new Integer(12), new JLabel("12") );
		labelTable3.put( new Integer(22), new JLabel("22") );
		labelTable3.put( new Integer(32), new JLabel("32") );
		labelTable3.put( new Integer(42), new JLabel("42") );
		labelTable3.put( new Integer(50), new JLabel("50") );
		mySlider.setLabelTable( labelTable3 );
		mySlider.setPaintLabels(true);
		
		java.text.NumberFormat numberFormat = java.text.NumberFormat.getIntegerInstance();
       	 	NumberFormatter formatter = new NumberFormatter(numberFormat);
       	 	formatter.setMinimum(new Integer(pData.paraLower[3]));
        	formatter.setMaximum(new Integer(pData.paraUpper[3]));
        	mytextField = new JFormattedTextField(formatter);
        	mytextField.setValue(new Integer(pData.getBarNumber()));
        	mytextField.setColumns(5);
        	mytextField.addPropertyChangeListener(this);
        	
        	mytextField.getInputMap().put(KeyStroke.getKeyStroke(
                                        KeyEvent.VK_ENTER, 0),
                                        "check");
        	mytextField.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!mytextField.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    mytextField.selectAll();
                } else try {                    //The text is valid,
                    mytextField.commitEdit();     //so use it.
                } catch (java.text.ParseException exc) { }
            }
        });
   	};
        
        namePanel=new JPanel();
	namePanel.add(slidername);
	namePanel.add(mytextField);
       	panelUpper.add(namePanel);
       	panelUpper.add(mySlider);
       	inforLabel=new JLabel("",SwingConstants.CENTER);
       	infor2=new JLabel("",SwingConstants.CENTER);
       	inforLabel.setForeground(Color.blue);
       	infor2.setForeground(Color.blue);
       	panelUpper.add(inforLabel);
       	panelUpper.add(infor2);
		
       	buttonOK = new JButton("   OK  ");
        buttonCancel = new JButton("Cancel");
        panelLower=new JPanel();
        panelLower.add(buttonOK);
        panelLower.add(buttonCancel);

	buttonOK.addActionListener(this);
	buttonCancel.addActionListener(this);
	enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		
        this.getContentPane().add(panelUpper);
        this.getContentPane().add(panelLower);
   		
   	this.setSize(300, 280); 
   	this.show(); 
   	this.setLocationRelativeTo(null);
    }
    
    public void stateChanged(ChangeEvent e) {
    	if (e.getSource()==mySlider){ 
    	JSlider source = (JSlider)e.getSource();
        int fps = (int)source.getValue();
        if (!source.getValueIsAdjusting()) { //done adjusting
            mytextField.setValue(new Integer(fps)); //update ftf value
            
        } else { //value is adjusting; just set the text
            mytextField.setText(String.valueOf(fps));
        }
	}
}

   public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
            Number value = (Number)e.getNewValue();
            if (mySlider != null && value != null) {
                mySlider.setValue(value.intValue());
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
    	
	if(index==1){
	  try {
         	if  ( e.getSource() == buttonOK ){
          		if ( mySlider.getValue() >= pData.getSamplePerSecond() ) {
          			pData.setTotalSample(mySlider.getValue());
          			hostApplet.clear(); dispose();
				} else {inforLabel.setText("# of total sample should be greater"); infor2.setText("than or equal to sample/second!");}
          		
			} 
         	if ( e.getSource() == buttonCancel ) dispose();

       	} catch (Exception ex) {dispose();} 
   	}//end of if
	else if(index==2){
	  try {
         	if  ( e.getSource() == buttonOK ){
          		if ( pData.getTotalSample() >= mySlider.getValue() ) {
          			pData.setSamplePerSecond(mySlider.getValue());
          			hostApplet.clear(); dispose();
				} else {inforLabel.setText("# of total sample should be greater"); infor2.setText("than or equal to sample/second!");}
          		
			} 
         	if ( e.getSource() == buttonCancel ) dispose();

       	} catch (Exception ex) {dispose();} 
   	}//end of else if
	else if(index==3){
	  try {
         	if  ( e.getSource() == buttonOK ){
          		if ( pData.getTotalSample()>= pData.getSamplePerSecond() ) {
          			pData.setBarNumber(mySlider.getValue());
          			hostApplet.clear(); dispose();
				} else {inforLabel.setText("# of total sample should be greater"); infor2.setText("than or equal to sample/second!");}
          		
			} 
         	if ( e.getSource() == buttonCancel ) dispose();

       	} catch (Exception ex) {dispose();} 
   	}//end of else if
}

}		
