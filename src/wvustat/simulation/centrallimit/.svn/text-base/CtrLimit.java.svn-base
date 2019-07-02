package wvustat.simulation.centrallimit;

import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;

public class CtrLimit extends JPanel
	implements  ItemListener, ActionListener,MouseListener{ 
		
	JLabel	labelTitle,labelSample;
	JPanel	panelTitle, panelBottom, panelCenter,westPanel, eastPanel,southPanel; //panelGridCkBox, panelGridButton;
	JButton	buttonReset, buttonRescale; // buttonStep, buttonStop;
        JPanel	visualPanel;
	JLabel  muLabel,sigmaLabel,blankLabel,xLabel,muValue,sigmaValue;
	JPanel  muPanel,sigmaPanel;
	JLabel  percentLabel;
	JPopupMenu menuPara;
	JMenuItem menuitem1, menuitem2, menuitem3, menuitem4;
	JCheckBoxMenuItem checkBoxNormal, checkBoxHistogram;

	MenuTrigger 	menuPopup;
	CtrLimitCanvas	cCanvas;
	publicData		pData;
	adjustCanvas	nCanvas;
	DFCanvas	dCanvas;
	
	Class c=getClass();
	JLabel		distLabel;
	int	 	distIndex;
	Icon 		disticon[]={new ImageIcon(c.getResource("normal1.jpg")), new ImageIcon(c.getResource("chisq1.jpg")), new ImageIcon(c.getResource("uniform1.jpg")), new ImageIcon(c.getResource("bowtie1.jpg")), new ImageIcon(c.getResource("wedge_L1.jpg")), new ImageIcon(c.getResource("wedge_R1.jpg")), new ImageIcon(c.getResource("triangle1.jpg")) };;
	boolean		isBuildHist=false,isClear=true;
	
	String	distName[];
	final static String	paraNameString[] = {
		"Sample Size", "Total Samples", "# of Sample/click", "Bars" };
	final static String	paraName[] = {
		"SampleSize", "TotalSamples", "SamplePerSecond", "BarsNumber" };
	final static int defaultParamValue[] = {1, 2000, 500, 50	};
	
	int 	distNumber, choiceIndex;
	int	getHistSize=0;

	public CtrLimit(Map params, String[] dNames){		
		pData = new publicData(this);
		setLayout(new BorderLayout());

		distNumber = dNames.length;
		distName = new String[distNumber];
		
		// Load Graphics
		for ( int i = 0; i < distNumber; i++ ) 
			distName[i] = dNames[i];
		// Load Parameters
		for ( int i = 0; i < 4; i++ ) 
			pData.setInitParaValue(i, getParam(i, params));
		
    	    GridBagLayout	gridbag = new GridBagLayout();
    		GridBagConstraints	constraints = new GridBagConstraints();
		
		// Start to build pop-up menu
		menuPara = new JPopupMenu(); 	
		menuPopup = new MenuTrigger();
		menuitem4 = new JMenuItem("Parent Distribution"); 
		menuitem4.addActionListener(this); 
		menuPara.add(menuitem4);
		menuPara.addSeparator();
		checkBoxNormal = new JCheckBoxMenuItem("Normal Curve"); 
		checkBoxNormal.addItemListener(this); 
		menuPara.add(checkBoxNormal);
		checkBoxHistogram = new JCheckBoxMenuItem("Histogram"); 
		checkBoxHistogram.addItemListener(this); 
		menuPara.add(checkBoxHistogram);
		menuPara.addSeparator();
		menuitem1 = new JMenuItem(paraNameString[1]); 
		menuitem1.addActionListener(this); 
		menuPara.add(menuitem1);
		menuitem2 = new JMenuItem(paraNameString[2]); 
		menuitem2.addActionListener(this); 
		menuPara.add(menuitem2);
		menuitem3 = new JMenuItem(paraNameString[3]); 
		menuitem3.addActionListener(this); 
		menuPara.add(menuitem3);
		menuPopup.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				menuPopup.pressTrigger();
				menuPara.show(menuPopup, 2,menuPopup.getPreferredSize().height);
			}
				
			public void mouseReleased(MouseEvent e){
				menuPopup.releaseTrigger();
				
			}
		});
		

		westPanel=new JPanel();
		westPanel.setSize(100,30);
		westPanel.setLayout(new BorderLayout());
		westPanel.add("Center",menuPopup);	
		
		panelTitle=new JPanel();
		panelTitle.setBorder(new javax.swing.border.EtchedBorder());
		panelTitle.setLayout(new BorderLayout());
		panelTitle.add("West",westPanel);
		add("North", panelTitle);

		
		panelBottom = new JPanel();
		panelBottom.setLayout(gridbag);
		panelBottom.setForeground(Color.black);
		
		Icon muicon=new ImageIcon(c.getResource("mu.jpg"));
		muLabel=new JLabel(muicon,SwingConstants.LEFT);
		xLabel=new JLabel("=");
		JLabel x2Label=new JLabel("=");
		muValue=new JLabel();
		muPanel=new JPanel();
		muPanel.setLayout(new FlowLayout());
		muPanel.add(muLabel);
		muPanel.add(x2Label);
		muPanel.add(muValue);
		Icon sigmaicon=new ImageIcon(c.getResource("sd.jpg"));
		sigmaLabel=new JLabel(sigmaicon,SwingConstants.LEFT);
		sigmaValue=new JLabel();
		sigmaPanel=new JPanel();
		sigmaPanel.setLayout(new FlowLayout());
		sigmaPanel.add(sigmaLabel);
		sigmaPanel.add(xLabel);
		sigmaPanel.add(sigmaValue);
		checkBoxNormal.setState(false);
		checkBoxHistogram.setState(true);
		
		buttonReset=new JButton("  Reset  ");
		buttonRescale=new JButton("Rescale");
		constraints.fill=GridBagConstraints.NONE;
		constraints.anchor= GridBagConstraints.WEST;
		constraints.ipady   = 0;
		constraints.weightx=1.0;
		
		nCanvas = new adjustCanvas(this, 1, 50, 1, 1); 
  		addComp(gridbag, nCanvas, constraints);
		addComp(gridbag, muPanel, constraints);
		
		
		addComp(gridbag, new JLabel("                                    "), constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor   = GridBagConstraints.CENTER;
		addComp(gridbag, buttonRescale, constraints);
		constraints.gridwidth = 1;
		constraints.anchor   = GridBagConstraints.WEST;
		 
		dCanvas=new DFCanvas(this,1,10,4,1);
		blankLabel=new JLabel(" ");
		visualPanel=new JPanel();
		visualPanel.setLayout(new CardLayout());
		visualPanel.add("other",blankLabel);
		visualPanel.add("chi_sq",dCanvas);
		addComp(gridbag, visualPanel, constraints);

		constraints.anchor   = GridBagConstraints.WEST;
		addComp(gridbag, sigmaPanel, constraints);
		addComp(gridbag, new JLabel("                                    "), constraints);
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor   = GridBagConstraints.CENTER;
		addComp(gridbag, buttonReset, constraints);
		panelBottom.setBorder(new javax.swing.border.EtchedBorder());
		add("South", panelBottom);
		buttonRescale.addActionListener(this);
		buttonReset.addActionListener(this);
		
		panelCenter=new JPanel();
		panelCenter.setLayout(new BorderLayout());
		panelCenter.setBackground(Color.black);
		labelTitle=new JLabel("Central Limit Theorem",SwingConstants.CENTER);
		labelTitle.setBackground(Color.black);
		labelTitle.setForeground(Color.white);
		panelCenter.add("North",labelTitle);
		
		distIndex=pData.getDistIndex();
		distLabel=new JLabel();
		distLabel.setIcon(disticon[0]);
		distLabel.addMouseListener(this);
		
		cCanvas = new CtrLimitCanvas(this);
		cCanvas.addMouseListener(this);
		panelCenter.add("Center",cCanvas);
		eastPanel=new JPanel();
		eastPanel.setBackground(Color.black);
		eastPanel.setLayout(new BorderLayout());
		JLabel blankLabel2=new JLabel("                          ");
		eastPanel.add("East",blankLabel2);
		
		percentLabel=new JLabel("      0%");
		//percentLabel=new JLabel();
		//percentLabel.setText("      "+cCanvas.getPreferedString(getHistSize*100/pData.getAllParameter()[1],0)+"%");
		percentLabel.setForeground(Color.white);
		southPanel=new JPanel();
		southPanel.setBackground(Color.black);
		southPanel.setLayout(new GridLayout(2,1));
		southPanel.add(distLabel);
		southPanel.add(percentLabel);
		eastPanel.add("South",southPanel);
		panelCenter.add("East",eastPanel);
		add("Center", panelCenter);
		
		
	}

	public void setCardPanel(boolean chi_sq) {
		
		if ( chi_sq ) 
			((CardLayout)visualPanel.getLayout()).show(visualPanel, "chi_sq");
		else
			((CardLayout)visualPanel.getLayout()).show(visualPanel, "other");
			
		dCanvas.showDF = chi_sq;
		dCanvas.updateView();
	}
	
	private void addComp(GridBagLayout grid,
						Component comp,
						GridBagConstraints c){
		grid.setConstraints(comp,c);
		panelBottom.add(comp);
	}

	
	
	public void actionPerformed(ActionEvent e) {
	
		JFrame parFrame = new JFrame();
		if ( e.getSource() == buttonReset )	reset();

		if ( e.getSource() == buttonRescale ){
			if(cCanvas.h.getHistSize() == 0) //Try to avoid to clear existing graph
				clear(true);
			else
				cCanvas.rescaleYOnly();
		}

		if ( e.getSource() == menuitem1 ){
			
			choiceIndex = 1;
			changeParaDialog dialog = new changeParaDialog(parFrame, this, pData, choiceIndex);
		}
		
		if ( e.getSource() == menuitem2 ){
			
			choiceIndex = 2;
			changeParaDialog dialog = new changeParaDialog(parFrame, this, pData, choiceIndex);
		}
		
		if ( e.getSource() == menuitem3 ){
			
			choiceIndex = 3;
			changeParaDialog dialog = new changeParaDialog(parFrame, this, pData, choiceIndex);
		} 
		
		if ( e.getSource() == menuitem4 ) {
			selectImageDialog selector = new selectImageDialog(parFrame, this, pData);
		} 
	}	
	
	public void itemStateChanged(ItemEvent e){
		try{
		if ( ( e.getSource() == checkBoxNormal ) || ( e.getSource() == checkBoxHistogram ) ) {
			
			if ( !checkBoxNormal.getState() && !checkBoxHistogram.getState() ) 
				checkBoxHistogram.setState(true);
			cCanvas.setPaintState(checkBoxNormal.getState(), checkBoxHistogram.getState());
			cCanvas.repaint();
		}
		} catch (Exception el) {}
	}
	
	private final int getParam(int index, Map pMap) {
		try{
			String vs = (String)pMap.get(paraName[index]);
			int v = Integer.parseInt(vs);
			return(v);
		}catch (Exception e) { 
			return(defaultParamValue[index]); 
		}
	}

public void mouseClicked(MouseEvent e){
}

public void mouseReleased(MouseEvent e){
}

public void mouseEntered(MouseEvent e){
}

public void mouseExited(MouseEvent e){
}

public void mousePressed(MouseEvent e){
	if(e.getSource()==distLabel){
		if ( (cCanvas.h.getHistSize()+1) >= pData.getAllParameter()[cCanvas.TOTAL_SAMPLE] ) {
			JOptionPane.showMessageDialog(eastPanel,"Please click in the histogram to clean it.","Message",JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			cCanvas.addSamples();
			getHistSize=getHistSize+pData.getSamplePerSecond();
			percentLabel.setText("      "+cCanvas.getPreferedString(getHistSize*100/pData.getAllParameter()[1],0)+"%");	
			isBuildHist=true;
			isClear=false;
			
			
		}
	}
	else if(e.getSource()==cCanvas){
		isClear=true;
		isBuildHist=false;
		cCanvas.repaint();
		percentLabel.setText("      0%");
		getHistSize=0;
	}
}
	 

	public final void reset(int index) {
		
		setCardPanel(index==1);
		pData.reset(index);
		distLabel.setIcon(disticon[index]);
		percentLabel.setText("      0%");
		cCanvas.reset(true);
		nCanvas.reset();
		dCanvas.reset();
	}

	public final void reset() {
		try{
		setCardPanel(false);
		pData.reset();
		distLabel.setIcon(disticon[0]);
		percentLabel.setText("      0%");
		setCardPanel(false);
                pData.reset(0);
         	distIndex=0;
		cCanvas.reset(true);
		nCanvas.reset();
		dCanvas.reset();
	}catch (Exception el){}
	}

	public final void clear() { clear(false); }
	
	public final void clear(boolean needRescale) {
		
		
		cCanvas.reset(needRescale);
		isClear=true;
		isBuildHist=false;
		getHistSize=0;
		percentLabel.setText("      0%");
		cCanvas.repaint();
		//hBar.reset(0, pData.getTotalSample(), 0);
	}
	
	public final void setMuAndSigma (String mu, String sigma) {
	
		muValue.setText(mu);
		sigmaValue.setText(sigma);
	}	
}


