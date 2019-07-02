package wvustat.modules;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.Vector;
import wvustat.interfaces.*;

public class ParallelCoordModule extends GUIModule implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	private Vector vy, vz;
	private ParallelCoordPlot plot;
	private OverlapSlicer chooser;
	private JMenuBar jmb;
	
	public ParallelCoordModule(DataSet data, Vector vy, Vector vz)
	{		
		this.data = data;
		this.vy = vy;
		this.vz = vz;
		
		setBackground(Color.white);
		
		if (this.vy.size() <= 1)
            throw new IllegalArgumentException("At least two y variables are needed to do parallel coordinates");
		
		init();
	}
	
	private void init()
	{
		data.addRemoteObserver(this);
		
		plot = new ParallelCoordPlot(data, vy, vz);
		
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
        
        jmb = new JMenuBar();
        JMenu plotMenu = new JMenu("Plot");
        plotMenu.add("Parallel Coordinates");
        jmb.add(plotMenu);
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
        if (vz.size() == 0)
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
	}
	
	public void update(String arg)
    {
        plot.updatePlot(arg);
    }
}
