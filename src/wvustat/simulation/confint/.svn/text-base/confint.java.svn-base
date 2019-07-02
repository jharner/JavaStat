package wvustat.simulation.confint;

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Map;
import javax.swing.border.*;
 
public class confint extends JComponent implements ActionListener{

	JLabel	labelTitle;		
	JButton	buttonRescale, buttonRecalculate; 
	JPanel	panelTitle, panelBottom,sigmaPanel; 
	
	// Definne my components
	adjustCanvas	aCanvasMu, aCanvasSigma, aCanvasN, aCanvasAlpha; 
	double 		initMu, initSigma, initN, initAlpha;
	confintCanvas	cCanvas; 
	publicData	pData; 
	probCanvas	pCanvas;
	private int sampleSize = 100;
	double[]	WholeSample;
	double[]	AllIntervalWidth;
	
	public confint(Map initParams) {
		
		String mus = (String)initParams.get("mu");
		String sigmas = (String)initParams.get("sigma");
		String alphas= (String)initParams.get("alpha");
		String ns= (String)initParams.get("n");
			
		initMu = (mus==null)?(0.0): (Double.parseDouble(mus));
		initSigma = (sigmas==null)?(2.0):(Double.parseDouble(sigmas));
		initAlpha = (alphas==null)?(0.10):(Double.parseDouble(alphas));
		initN= (ns==null)?(5):(Double.parseDouble(ns));
			
			
		GridBagLayout	gridbag = new GridBagLayout();
		GridBagConstraints	constraints = new GridBagConstraints();
	
		setLayout(new BorderLayout(0,0));

		pData = new publicData(this);
		
		// Title Panel
		panelTitle = new JPanel();
		panelTitle.setBackground(Color.black);
		labelTitle = new JLabel("Confidence Intervals");
		labelTitle.setForeground(Color.white);
		panelTitle.add(labelTitle);
		add("North", panelTitle);

		panelBottom = new JPanel();
		//panelBottom.setLayout(gridbag);
		panelBottom.setLayout(new GridLayout(2,4));

		// Row 1.1 Button Recalculate
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill   = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(5, 5, 0, 5);

		// Row1.2  Adjustcanvas aCanvasMu
		aCanvasMu = new adjustCanvas(pData, this, "mu");
		panelBottom.add(aCanvasMu);
		//addComp(gridbag, aCanvasMu, constraints);

		// Row1.3  Adjustcanvas aCanvasN
		aCanvasN = new adjustCanvas(pData, this, "n");
		panelBottom.add(aCanvasN);
		//addComp(gridbag, aCanvasN, constraints);

		// Row1.4  LabelProb
		constraints.anchor = GridBagConstraints.CENTER;
		
		pCanvas = new probCanvas(this);
		panelBottom.add(pCanvas);
		//addComp(gridbag, pCanvas, constraints);

		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(5, 5, 0, 10);
		buttonRecalculate = new JButton("Recalculate");
		buttonRecalculate.addActionListener(this);
		panelBottom.add(buttonRecalculate);
		//addComp(gridbag, buttonRecalculate, constraints);
		constraints.insets = new Insets(5, 5, 0, 5);
		
		// Row 2.1 Button Recalculate
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill   = GridBagConstraints.HORIZONTAL;

		// Row2.2  Adjustcanvas aCanvasMu
		aCanvasSigma = new adjustCanvas(pData, this, "sigma");
		panelBottom.add(aCanvasSigma);
		//addComp(gridbag, aCanvasSigma, constraints);

		// Row2.3  Adjustcanvas aCanvasN
		aCanvasAlpha = new adjustCanvas(pData, this, "alpha");
		panelBottom.add(aCanvasAlpha);
		//addComp(gridbag, aCanvasAlpha, constraints);
		add("South", panelBottom);

		// paint main canvas
		WholeSample=new double[sampleSize];
		AllIntervalWidth=new double[sampleSize];
		getSample();
		cCanvas = new confintCanvas(pData, this);
		add("Center", cCanvas);
		pData.setNormalInterval(initAlpha);
		
		panelBottom.add(new JLabel("      "));
		//addComp(gridbag, new Label("  "), constraints);
		constraints.anchor = GridBagConstraints.EAST;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.insets = new Insets(0, 5, 5, 10);
		buttonRescale = new JButton("Rescale");
		buttonRescale.addActionListener(this);
		panelBottom.add(buttonRescale);
		//addComp(gridbag, buttonRescale, constraints);
		constraints.insets = new Insets(0, 5, 5, 5); 
	} // End of confint()
  
	
	public double[] getSample(){
		for (int i=0;i<sampleSize; i++){
			WholeSample[i]=pData.getASample();
			AllIntervalWidth[i]=pData.intervalWidth;
		};
		return(WholeSample);
	};
 
	private void addComp(GridBagLayout grid,
						Component comp,
						GridBagConstraints c){
		grid.setConstraints(comp,c);
		panelBottom.add(comp);
	}
  

 
 	public void actionPerformed(ActionEvent e) { 

		if ( e.getSource() == buttonRescale ) {
		
			cCanvas.setXcoord();
			cCanvas.repaint();
		}

		if ( e.getSource() == buttonRecalculate ) {
			getSample();
			cCanvas.repaint();
		} 
	} 

	
	
	public void reset() {
		
		aCanvasMu.paraValue = initMu;
		aCanvasMu.paraInc = 1.0;
		aCanvasMu.repaint();
		
		aCanvasSigma.paraValue = initSigma;
		aCanvasSigma.paraInc = 1.0;
		aCanvasSigma.repaint();
		
		aCanvasN.paraValue = initN;
		aCanvasN.paraInc = 1.0;
		aCanvasN.repaint();
		
		aCanvasAlpha.paraValue = initAlpha;
		aCanvasAlpha.paraInc = 0.05;
		aCanvasAlpha.repaint();
	}

}
  
  