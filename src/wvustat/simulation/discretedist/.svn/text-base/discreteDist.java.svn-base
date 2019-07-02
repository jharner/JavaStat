package wvustat.simulation.discretedist;

import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class discreteDist extends JPanel
    implements Runnable, ActionListener{
	
	public JTextField textFieldPara2, textFieldPara1;
	public JPanel mainPanel,titlePanel,bottomPanel,resultPanel,qtPanel,approxPanel,totalPanel;
	public JMenu probMenu;
	public JMenuBar bar;
	public JMenuItem GEItem, LEItem, GELEItem, EItem;
	public JButton rescaleButton, resetButton;
	public JRadioButton pdfButton, cdfButton;
	public ButtonGroup bgr;
	public JLabel labelQuantile,labelApprox,labelProb,labelNorm;
	discreteInfo dInfo;
	adjustPanel aPanelPara1,aPanelPara2;
	quantilePanel qPanel;
	discretePanel distPanel;
	private int choiceIndex;

	public discreteDist(Map params){
		String distName, appDistName;
		
		Container content=this;
		content.setLayout(new BorderLayout());


		distName = (String)params.get("DistributionName");
		if(!"Binomial".equals(distName) && !"Poisson".equals(distName))
			distName = "Poisson";
		
		String display = (String)params.get("display");		
		if(display == null)
			display = "PDF";
		dInfo	= new discreteInfo(distName, display); 

		
		appDistName = (String)params.get("ApproxDistribution");
		if(appDistName == null)
			appDistName = ""; 
		dInfo.setApproxDistName(appDistName);
		
		if (dInfo.isBinomial){ 
			try {
				dInfo.setInitPara((String)params.get("n"), (int)0);
				dInfo.setInitPara((String)params.get("p"), (int)1);
			} catch (Exception e) {
				dInfo.setInitPara("10", (int)0);
				dInfo.setInitPara("0.5", (int)1);
			}
		}
		else {
			try {
				dInfo.setInitPara((String)params.get("lambda"), (int)0);
			} catch (Exception e) {
				dInfo.setInitPara("6", (int)0);
			}
		}  
		
		
		mainPanel=new JPanel();
		mainPanel.setBackground(Color.black);
		mainPanel.setLayout(new BorderLayout());
		
		JMenu probMenu=new JMenu("Probability");
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
		distPanel=new discretePanel(dInfo,this);
		
				
		JPanel bottomPanel=new JPanel(new GridBagLayout());
		JRadioButton pdfButton=new JRadioButton("f(x)");
		JRadioButton cdfButton=new JRadioButton("F(x)");
		pdfButton.setSelected(true); 
		if (dInfo.isPDF()){
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
		
		
		aPanelPara1 = new adjustPanel(dInfo, this, 1);
		bottomPanel.add(aPanelPara1,new GridBagConstraints(1,0,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		textFieldPara1=new JTextField(new String(dInfo.getParaString(0)),3);
		textFieldPara1.setFont(new Font("font1",0,12));
		textFieldPara1.setSize(10,8);
		textFieldPara1.setOpaque(false);
		bottomPanel.add(textFieldPara1,new GridBagConstraints(2,0,1,1,1.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		
		
		if ( dInfo.ifHavePara2() ){
		aPanelPara2 = new adjustPanel(dInfo, this, 2);
		bottomPanel.add(aPanelPara2,new GridBagConstraints(1,1,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		textFieldPara2 = new JTextField(new String(dInfo.getParaString(1)), 3);
		textFieldPara2.setFont(new Font("font2",0,12));
		textFieldPara2.setSize(10,8);
		textFieldPara2.setOpaque(false);
		bottomPanel.add(textFieldPara2,new GridBagConstraints(2,1,1,1,1.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		}
		else {
		JLabel tmpLabelPara1 = new JLabel(" ");
		bottomPanel.add(tmpLabelPara1,new GridBagConstraints(1,1,1,1,0.05,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		JLabel tmpLabelPara2 = new JLabel(" ");
		bottomPanel.add(tmpLabelPara2,new GridBagConstraints(2,1,1,1,1.5,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		} 
		
		resultPanel=new JPanel();
		resultPanel.setLayout(new CardLayout()); 
		
		try {
			qPanel = new quantilePanel(dInfo, (String)params.get("q"), this);
		} catch (Exception e) {
			qPanel = new quantilePanel(dInfo, "0.5", this);
		}
		qtPanel=new JPanel();
		qtPanel.setLayout(new FlowLayout());
		qtPanel.add(qPanel);
		labelQuantile=new JLabel();
		qtPanel.add(labelQuantile);
		labelProb =new JLabel();
		resultPanel.add("prob",labelProb);
		resultPanel.add("quantile",qtPanel);
		bottomPanel.add(resultPanel,new GridBagConstraints(3,0,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		approxPanel=new JPanel();
		
		approxPanel.setLayout(new CardLayout());
		labelNorm=new JLabel();
		approxPanel.add("norm",labelNorm);
		labelApprox=new JLabel();
		approxPanel.add("approx",labelApprox);
		totalPanel=new JPanel();
		totalPanel.setLayout(new FlowLayout());
		
		totalPanel.add(approxPanel);
		totalPanel.add(new JLabel(""));
		bottomPanel.add(totalPanel,new GridBagConstraints(3,1,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		
		
		rescaleButton=new JButton("Rescale");
		rescaleButton.addActionListener(this);
		resetButton=new JButton("Reset");
		resetButton.addActionListener(this);
		bottomPanel.add(rescaleButton,new GridBagConstraints(4,0,1,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		bottomPanel.add(resetButton,new GridBagConstraints(4,1,0,1,1,0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0));
		bottomPanel.setBorder(new javax.swing.border.EtchedBorder());
		
		mainPanel.add("Center",distPanel);
		mainPanel.add("North",new JLabel("          "));
		mainPanel.add("South",new JLabel("          "));
		content.add("Center",mainPanel);
		content.add("South",bottomPanel);
		
	
		
	}// end of inint
	
	public void actionPerformed(ActionEvent p1) {
        	Object src=p1.getSource();
        	String cmd=p1.getActionCommand();
        	
        	if ( src == rescaleButton) {
		
		dInfo.putParaValue(this);
		dInfo.doRescale = true;
		distPanel.repaint();
		}

		if ( src==resetButton ) {
		
		dInfo.reset();
		distPanel.repaint();
		
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
        	
        	if (( cmd=="PDF")&&(!dInfo.isPDF()) ){
        		distPanel.repaint();
			dInfo.doRescale = true;
			dInfo.fOrF = "f";
			((CardLayout)resultPanel.getLayout()).show(resultPanel, "prob");		
			((CardLayout)approxPanel.getLayout()).show(approxPanel,"norm");
		         GEItem.setEnabled(true);
		         LEItem.setEnabled(true);
		         GELEItem.setEnabled(true);
		         EItem.setEnabled(true); 
		
		}
		else if (( cmd=="CDF") && (dInfo.isPDF()) ){
			distPanel.repaint();
			dInfo.doRescale = true;
			dInfo.fOrF = "F";
			((CardLayout)resultPanel.getLayout()).show(resultPanel, "quantile");
			if (dInfo.needApprox()) {
			((CardLayout)approxPanel.getLayout()).show(approxPanel,"approx");}
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

