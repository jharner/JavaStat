package wvustat.modules;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.Vector;
import wvustat.interfaces.*;
import wvustat.swing.TourBox;
import wvustat.swing.StopControl;
import wvustat.swing.SimpleSlider;
import wvustat.swing.ScaleConfigPanel;

public class GrandTourModule extends GUIModule implements ChangeListener, ActionListener, Runnable {
	private static final long serialVersionUID = 1L;
	
	private Vector vy, vz;
	private Variable z_var;
	private GrandTour plot;
	private OverlapSlicer chooser;
	private JMenuBar jmb;
	private boolean quit = false;
	private boolean pause = true;
	private int speedInterval = 100;
	private JCheckBoxMenuItem hideAxisMI;
	private JMenuItem scaleMI;
	private JRadioButtonMenuItem tourMI, pcaTourMI;
	
	public GrandTourModule(DataSet data, Vector vy, Vector vz)
	{
		this.data = data;
		this.vy = vy;
		this.vz = vz;
		this.z_var = null;
		
		setBackground(Color.white);
		
		if (this.vy.size() <= 2)
            throw new IllegalArgumentException("At least three x or y variables are needed to do grand tour");
		
		init();
		
		(new Thread(this)).start();
	}
	
	private void init()
	{
		data.addRemoteObserver(this);
		
		plot = new GrandTour(data, vy, vz);
		plot.setGUIModule(this);

		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JPanel jp = buildControlPanel();

        if (jp != null)
        {
            add(jp, gbc);
            gbc.gridy++;
        }

        gbc.weighty = 1.0;

        add(plot, gbc);
        
        //menu
        jmb = new JMenuBar();
        
        //plot menu
        ButtonGroup group = new ButtonGroup();
        JMenu plotMenu = new JMenu("Plot");
        
        JRadioButtonMenuItem jmi = new JRadioButtonMenuItem("Tour");
        tourMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        group.setSelected(jmi.getModel(), true);
        plotMenu.add(jmi);
        
        jmi = new JRadioButtonMenuItem("PCA Tour");
        pcaTourMI = jmi;
        jmi.addActionListener(this);
        group.add(jmi);
        plotMenu.add(jmi);
        plotMenu.addSeparator();
        
        JMenu optionMenu = new JMenu("Options");
        hideAxisMI = new JCheckBoxMenuItem("Hide Axis", false);
        hideAxisMI.addActionListener(this);
        optionMenu.add(hideAxisMI);
        
        scaleMI = new JMenuItem("Set Scale...");
        scaleMI.addActionListener(this);
        optionMenu.add(scaleMI);
        
        plotMenu.add(optionMenu);
        
        jmb.add(plotMenu);
	}
	
	public void actionPerformed(ActionEvent ae)
    {
        String arg = ae.getActionCommand();

        if (arg.equals("Tour")) 
        {
        	plot.enablePCAControlling(false);
        } 
        else if (arg.equals("PCA Tour"))
        {
        	plot.enablePCAControlling(true);
        }
        else if (arg.equals("Hide Axis"))
        {
            plot.enableAxisDrawing(!hideAxisMI.isSelected());
        
        } 
        else if (arg.equals("Set Scale..."))
        {
        	ScaleConfigPanel scalePanel = new ScaleConfigPanel(0, 1, plot.getScale());
        	int option=JOptionPane.showOptionDialog(this, scalePanel, "Choose scale", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        	if(option==JOptionPane.OK_OPTION)
        		plot.setScale(scalePanel.getValue());
        }
    }
	
	public void changeGroup()
    {
        int index = chooser.getCurrentIndex();
        plot.setGroup(index);
    }
	
	public JMenuBar getJMenuBar()
	{
		return jmb;
	}
	
	public JMenu getOptionMenu()
	{
		return null;
	}
	
	/**
     * build control panel for this component
     */
    private JPanel buildControlPanel()
    {
        if (z_var == null)
            return null;

        JPanel jp = new JPanel();
        jp.setBackground(Color.white);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        chooser = new OverlapSlicer(plot.getGroupMaker());
        chooser.addChangeListener(this);
        chooser.setBorder(BorderFactory.createEmptyBorder(0,2,0,0));
        jp.add(chooser);
        return jp;
    }
	
	public void stateChanged(ChangeEvent e)
	{
		Object obj = e.getSource();
		
		if (obj instanceof OverlapSlicer)
        {
            this.changeGroup();
        } 
		else if (obj instanceof StopControl)
		{
			StopControl ctrl = (StopControl)obj;
			if (pause && (checkTourBoxTypes() == false)) 
				return;
			
			pause = !pause;
			ctrl.setStopped(pause);
			tourMI.setEnabled(pause);
			pcaTourMI.setEnabled(pause);
			scaleMI.setEnabled(pause);
	    	plot.setTourBoxStatus(pause);
		}
		else if (obj instanceof SimpleSlider)
		{
			SimpleSlider slider = (SimpleSlider)obj;
			this.speedInterval = (int)(slider.getMaximum() + slider.getMinimum() - slider.getValue());
		}
	}
	
	public void update(String arg)
	{
		plot.updatePlot(arg);
	}

	public void run(){
		while (!quit) {
			try
	        {
	            Thread.sleep(speedInterval); // millisecond
	        }
	        catch (InterruptedException e){}
	        
	        if (pause) continue;
	        
	        plot.rotatePlot();
		}
	}
	
	public void destroy(){
		super.destroy();
		quit = true;
	}
	
	private boolean checkTourBoxTypes() {
		int[] types = plot.getTourBoxTypes();
		int na, nx, ny, no;
		na = nx = ny = no = 0;
		
		for (int i = 0; i < types.length; i++) {
			switch (types[i]) {
				case TourBox.A: na++; break;
				case TourBox.X: nx++; break;
				case TourBox.Y: ny++; break;
				case TourBox.O: no++; break;
			}
		}
		
		if ((nx > 0 && ny == 0) || (nx == 0 && ny > 0)) {
			JOptionPane.showMessageDialog(this, "Both X and Y variables are required!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if ((na + nx + ny) < 2) {
			JOptionPane.showMessageDialog(this, "Too few active variables!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}
