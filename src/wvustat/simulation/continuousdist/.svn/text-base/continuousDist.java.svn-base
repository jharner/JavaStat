package wvustat.simulation.continuousdist;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class continuousDist extends JPanel
    implements Runnable, ActionListener{
	
	public JTextField textFieldPara2, textFieldPara1;
	public JPanel mainPanel,titlePanel,bottomPanel,resultPanel,qtPanel,approxPanel,totalPanel,infoPanel;
	public JMenu probMenu;
	public JMenuBar bar;
	public JMenuItem GEItem, LEItem, GELEItem, EItem;
	public JButton rescaleButton, resetButton;
	public JRadioButton pdfButton, cdfButton;
	public ButtonGroup bgr;
	public JLabel labelQuantile,labelApprox,rowLabel1,rowLabel2;
	continuousInfo cInfo;       // Problem doesn't happen here!
	adjustPanel aPanelPara1,aPanelPara2; //Here's a problem!
	quantilePanel qPanel;	//Here's another problem!!
	continuousPanel cPanel;         // Problem doesn't happen here

	
	private int choiceIndex;
	public boolean ifMenu=false;

	public continuousDist(Map params){
		String distName, appDistName;
		
		
		Container content=this;
		content.setLayout(new BorderLayout());


		distName = (String)params.get("DistributionName");
		if(distName == null)
			distName = "Normal";
		
		try{
			cInfo	= new continuousInfo(distName, (String)params.get("display")); 
		}catch (Exception e){ 
			cInfo = new continuousInfo(distName, "PDF");
		}	
	
		
		if (cInfo.isNormal){
			try {
				cInfo.setInitPara((String)params.get("mu"), (int)0);
				cInfo.setInitPara((String)params.get("sd"), (int)1);
			} catch (Exception e) {
				cInfo.setInitPara("0.0", (int)0);
				cInfo.setInitPara("1.0", (int)1);
			}	
		}
		else {
			try {
				cInfo.setInitPara((String)params.get("df"), (int)0);
			} catch (Exception e) {
				cInfo.setInitPara("3", (int)0);
			}
		}   
		
		
		mainPanel=new JPanel();
		mainPanel.setBackground(Color.black);
		mainPanel.setLayout(new BorderLayout());
		
		JMenu probMenu=new JMenu("Probability ");
		probMenu.setMnemonic('P');
		probMenu.addSeparator();
		JMenuBar bar=new JMenuBar();
		content.add(bar, BorderLayout.NORTH);
		//setJMenuBar(bar);
		GEItem=new JMenuItem("x>=a");
		GEItem.addActionListener(this);
		LEItem=new JMenuItem("x<=b");
		LEItem.addActionListener(this);
		GELEItem=new JMenuItem("a<=x<=b");
		GELEItem.addActionListener(this);
		EItem=new JMenuItem("x= ?");
		EItem.addActionListener(this);
		
		probMenu.add(GEItem);
		probMenu.add(LEItem);
		probMenu.add(GELEItem);
		probMenu.add(EItem); 
		bar.add(probMenu); 
		
		
		content.setBackground(Color.white);
		cPanel=new continuousPanel(cInfo,this);
		
				
		JPanel bottomPanel=new JPanel(new GridBagLayout());
		JRadioButton pdfButton=new JRadioButton("f(x)");
		JRadioButton cdfButton=new JRadioButton("F(x)");
		pdfButton.setSelected(true); 
		if (cInfo.isPDF()){
			pdfButton.setSelected(true);
		}
		else{
			cdfButton.setSelected(true);
		};  
		pdfButton.addActionListener(this);
		pdfButton.setActionCommand("PDF");
		cdfButton.addActionListener(this);
		cdfButton.setActionCommand("CDF");
		ButtonGroup bgr=new ButtonGroup();
		bgr.add(pdfButton);
		bgr.add(cdfButton); 
		
		bottomPanel.add(pdfButton,new GridBagConstraints(0,0,1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		bottomPanel.add(cdfButton,new GridBagConstraints(0,1,1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		
		aPanelPara1 = new adjustPanel(cInfo, this, 0,"mu");
		bottomPanel.add(aPanelPara1,new GridBagConstraints(1,0,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		textFieldPara1=new JTextField(new String(cInfo.getParaString(0)),3);
		textFieldPara1.setFont(new Font("font1",0,12));
		textFieldPara1.setSize(10,8);
		textFieldPara1.setOpaque(false);
		bottomPanel.add(textFieldPara1,new GridBagConstraints(2,0,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		
		
		if ( cInfo.ifHavePara2() ){
		aPanelPara2 = new adjustPanel(cInfo, this, 1,"sd");
		bottomPanel.add(aPanelPara2,new GridBagConstraints(1,1,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		textFieldPara2 = new JTextField(new String(cInfo.getParaString(1)), 3);
		textFieldPara2.setFont(new Font("font2",0,12));
		textFieldPara2.setSize(10,8);
		textFieldPara2.setOpaque(false);
		bottomPanel.add(textFieldPara2,new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		}
		else {
		JLabel tmpLabelPara1 = new JLabel("a");
		bottomPanel.add(tmpLabelPara1,new GridBagConstraints(1,1,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		JLabel tmpLabelPara2 = new JLabel("a");
		bottomPanel.add(tmpLabelPara2,new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		} 
		
	
		resultPanel=new JPanel();
		resultPanel.setLayout(new CardLayout()); 
		
		qPanel = new quantilePanel(cInfo, "0.5", this);
		
		try {
			qPanel = new quantilePanel(cInfo, (String)params.get("q"), this);
		} catch (Exception e) {
			qPanel = new quantilePanel(cInfo, "0.5", this);
		} 
		qtPanel=new JPanel();
		qtPanel.setLayout(new FlowLayout());
		qtPanel.add(qPanel);
		labelQuantile=new JLabel();
		qtPanel.add(labelQuantile);
		rowLabel1=new JLabel();
		resultPanel.add("prob",rowLabel1);
		resultPanel.add("quantile",qtPanel);
		bottomPanel.add(resultPanel,new GridBagConstraints(3,0,1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		approxPanel=new JPanel();
		approxPanel.setLayout(new CardLayout());
		rowLabel2=new JLabel();
		approxPanel.add("norm",rowLabel2);
		labelApprox=new JLabel(" ");
		totalPanel=new JPanel();
		totalPanel.setLayout(new FlowLayout());
		totalPanel.add(labelApprox);
		totalPanel.add(new JLabel("   "));
		approxPanel.add("approx",totalPanel);
		bottomPanel.add(approxPanel,new GridBagConstraints(3,1,1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
	
		
		rescaleButton=new JButton("Rescale");
		rescaleButton.addActionListener(this);
		resetButton=new JButton("Reset");
		resetButton.addActionListener(this);
		bottomPanel.add(rescaleButton,new GridBagConstraints(4,0,1,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		bottomPanel.add(resetButton,new GridBagConstraints(4,1,0,1,0.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		bottomPanel.setBorder(new javax.swing.border.EtchedBorder());
		
		mainPanel.add("Center",cPanel);
		mainPanel.add("North",new JLabel("          "));
		mainPanel.add("South",new JLabel("          "));
		content.add("Center",mainPanel);
		content.add("South",bottomPanel);
		
	
		
	}// end of inint
	
	public void actionPerformed(ActionEvent p1) {
        	Object src=p1.getSource();
        	String cmd=p1.getActionCommand();
        	
        	if ( src == rescaleButton) {
		
		cInfo.putParaValue(this);
		cInfo.doRescale = true;
		cPanel.repaint();
		}

		if ( src==resetButton ) {
		
		cInfo.reset();
		cPanel.repaint();
		
		}
		
		if ( src==GEItem  ){
		choiceIndex = 1;
		Thread newThread = new Thread(this);
		try {
			Thread.sleep(100);
			newThread.start();
		} catch (Exception ex) { }
		}
		
		if ( src==LEItem ){
		choiceIndex = 2;
		Thread newThread = new Thread(this);
		try {
			Thread.sleep(100);
			newThread.start();
		} catch (Exception ex) { }
		}
		
		if ( src==GELEItem ){
		choiceIndex = 3;
		Thread newThread = new Thread(this);
		try {
			Thread.sleep(100);
			newThread.start();
		} catch (Exception ex) { }
		}
		
		if ( src==EItem ){
		choiceIndex = 4;
		Thread newThread = new Thread(this);
		try {
			Thread.sleep(100);
			newThread.start();
		} catch (Exception ex) { }
		} 
        	
        	if (( cmd=="PDF")&&(!cInfo.isPDF()) ){
        		cPanel.repaint();
			cInfo.doRescale = true;
			cInfo.fOrF = "f";
			((CardLayout)resultPanel.getLayout()).show(resultPanel, "prob");		
			((CardLayout)approxPanel.getLayout()).show(approxPanel,"norm");
		         GEItem.setEnabled(true);
		         LEItem.setEnabled(true);
		         GELEItem.setEnabled(true);
		         EItem.setEnabled(true); 
		
		}
		else if (( cmd=="CDF") && (cInfo.isPDF()) ){
			cPanel.repaint();
			cInfo.doRescale = true;
			cInfo.fOrF = "F";
			((CardLayout)resultPanel.getLayout()).show(resultPanel, "quantile");
		 	GEItem.setEnabled(false);
		        LEItem.setEnabled(false);
		        GELEItem.setEnabled(false);
		        EItem.setEnabled(false); 
		} 
		
        }
  	
  
	public void run(){
		dialogFrame dFrame = new dialogFrame(new JFrame(), this, choiceIndex);
	} 
	
	

	
}//end of class

