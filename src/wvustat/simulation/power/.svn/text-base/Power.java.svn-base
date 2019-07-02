package wvustat.simulation.power;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Map;

public class Power extends JComponent implements  ActionListener{ 

	// Define standard components
	JLabel			labelTitle,eastLabel,powerLabel;
	JButton			buttonRescale, buttonReset;
	JPanel			panelTitle, panelBottom, panelWest, panelCenter,panelEast;
	
	// Definne my components
	adjustCanvas		aCanvasDelta, aCanvasN, aCanvasSigma, aCanvasAlpha;
	PowerCanvas		pCanvas;
	double			initDelta, initSigma, initN, initAlpha;
	JPopupMenu 		menuPowerType;
	JMenuItem		menuitemUpper, menuitemLower, menuitemTwo; 
	MenuTrigger 		menuPopup;
	publicData		pData;
	Class	c=getClass();

  public Power (Map initParams) {
  	
  	String deltas = (String)initParams.get("Delta");
  	String sigmas = (String)initParams.get("Sigma");
  	String ns = (String)initParams.get("N");
  	String alphas = (String)initParams.get("Alpha");
  	
  	initDelta = (deltas==null)?(1.0): (Double.parseDouble(deltas));
  	initN = (ns==null)?(5): (Double.parseDouble(ns));
  	initSigma = (sigmas==null)?(1.0): (Double.parseDouble(sigmas));
  	initAlpha = (alphas==null)?(0.05): (Double.parseDouble(alphas));
	

	
    	pData = new publicData(this);
    
    	GridBagLayout	gridbag = new GridBagLayout();
    	GridBagConstraints	constraints = new GridBagConstraints();
	
	setLayout(new BorderLayout(0,0));

	menuPowerType = new JPopupMenu(); 	
	menuPopup = new MenuTrigger();
	menuitemUpper = new JMenuItem("     Upper     "); menuitemUpper.addActionListener(this); menuPowerType.add(menuitemUpper);
	menuitemLower = new JMenuItem("     Lower     "); menuitemLower.addActionListener(this); menuPowerType.add(menuitemLower);
	menuitemTwo = new JMenuItem("      Two      "); menuitemTwo.addActionListener(this); menuPowerType.add(menuitemTwo);
	menuPopup.addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e){
			menuPopup.pressTrigger();
			menuPowerType.show(menuPopup, 2, menuPopup.getPreferredSize().height);
		}
				
		public void mouseReleased(MouseEvent e){
			menuPopup.releaseTrigger();
		}
	});
	// Title Panel
	
	panelWest = new JPanel(); 
	panelWest.setSize(110,30);
	panelWest.setLayout(new BorderLayout());
	panelWest.add("Center",menuPopup);
	
	panelTitle = new JPanel();
	panelTitle.setLayout(new BorderLayout());
	panelTitle.setBorder(new javax.swing.border.EtchedBorder());
	panelTitle.add("West", panelWest);
	add("North",panelTitle);
	
	panelCenter=new JPanel();
	panelCenter.setLayout(new BorderLayout());
	panelCenter.setBackground(Color.black);
	labelTitle = new JLabel("Power - Upper Tailed",SwingConstants.CENTER);
	labelTitle.setForeground(Color.white);
	labelTitle.setBackground(Color.black);
	panelCenter.add("North", labelTitle);
	
	panelBottom = new JPanel();
	panelBottom.setLayout(gridbag);
	
	// Main canvas
	pCanvas = new PowerCanvas(pData, this);
	panelEast=new JPanel();
	panelEast.setBackground(Color.black);
	panelEast.setLayout(new BorderLayout());
	Icon easticon=new ImageIcon(c.getResource("pdf.jpg"));
	eastLabel=new JLabel(easticon,SwingConstants.LEFT);
	panelEast.add("Center",eastLabel);
	panelCenter.add("West",panelEast);
	
	// Row 1.1 Button Recalculate
	constraints.fill   = GridBagConstraints.NONE;
	constraints.anchor = GridBagConstraints.WEST;
	constraints.gridwidth = 1;
	constraints.insets = new Insets(0, 0, 0, 10);
	
		
	// Row1.2  Adjustcanvas aCanvasDelta
	aCanvasDelta = new adjustCanvas(pData, this, "Delta");
	addComp(gridbag, aCanvasDelta, constraints);

	// Row1.3  Adjustcanvas aCanvasN
	aCanvasN = new adjustCanvas(pData, this, "N");
	addComp(gridbag, aCanvasN, constraints);

	// Row1.4  labelPower
	constraints.anchor = GridBagConstraints.CENTER;
	constraints.insets = new Insets(0, 0, 0, 0);
	powerLabel=new JLabel();
	powerLabel.setForeground(Color.red);
	addComp(gridbag, powerLabel, constraints);
	
	// Row 2.1 Button Recalculate
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	constraints.anchor = GridBagConstraints.EAST;
	buttonRescale = new JButton("Rescale");
	addComp(gridbag, buttonRescale, constraints);
			
	// Row2.2  Adjustcanvas aCanvasDelta
	constraints.gridwidth = 1;
	constraints.insets = new Insets(0, 0, 0, 10);
	constraints.anchor = GridBagConstraints.WEST;
	aCanvasSigma = new adjustCanvas(pData, this, "Sigma");
	addComp(gridbag, aCanvasSigma, constraints);

	// Row2.3  Adjustcanvas aCanvasN
	aCanvasAlpha = new adjustCanvas(pData, this, "Alpha");
	addComp(gridbag, aCanvasAlpha, constraints);
	
	constraints.anchor = GridBagConstraints.WEST;
	constraints.insets = new Insets(0, 0, 0, 20);
	addComp(gridbag, new JLabel(), constraints);
	constraints.insets = new Insets(0, 0, 0, 0);


	// Row 2.1 Button Recalculate
	constraints.gridwidth = GridBagConstraints.REMAINDER;
	constraints.anchor = GridBagConstraints.EAST;
	buttonReset = new JButton("  Reset  ");
	addComp(gridbag, buttonReset, constraints);
	panelBottom.setBorder(new javax.swing.border.EtchedBorder());
	add("South", panelBottom);
	buttonReset.addActionListener(this);
	buttonRescale.addActionListener(this);
	
	panelCenter.add("Center",pCanvas);
	add("Center", panelCenter);


  } // End of function init()
  
  //Method addComp
  private void addComp(GridBagLayout grid,
						Component comp,
						GridBagConstraints c){
	grid.setConstraints(comp,c);
	panelBottom.add(comp);
  
  }
  
  public void actionPerformed(ActionEvent e) {

	if ( e.getSource() == buttonRescale ) {
		
		pCanvas.rescaleNeeded = true;
		pCanvas.repaint();
	}

	if ( e.getSource() == buttonReset ) {
		
		pData.reset();
	}

	if ( e.getSource() == menuitemUpper ){labelTitle.setText("Power - Upper Tailed"); pData.setPowerType(0); pCanvas.repaint();}
	if ( e.getSource() == menuitemLower ){labelTitle.setText("Power - Lower Tailed"); pData.setPowerType(1); pCanvas.repaint();}
	if ( e.getSource() == menuitemTwo ){labelTitle.setText("Power - Two Tailed"); pData.setPowerType(2); pCanvas.repaint();} 

  } 
  
	
}
  
  