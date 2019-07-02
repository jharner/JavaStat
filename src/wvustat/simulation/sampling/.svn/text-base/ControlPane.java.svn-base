package wvustat.simulation.sampling;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.NumberFormat;

public class ControlPane extends JPanel{
	public final static String[] populationSizes = {
			"5", "10", "25", "50", "100", "250", "500"
	};
	
	public final static String[] percentages = {
			"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", 
			"0.9", "1.0"
	};
	
	JRadioButton noReplacementButton;
    JRadioButton withReplacementButton;
    JComboBox choiceN;
    JComboBox choiceP;
    JButton runButton;
    JTextField sampleSizeField;

    SamplePane samplePane;
    
    public ControlPane(SamplePane sp){
    		if(sp == null)
    			throw new IllegalArgumentException("Null SamplePane");
    		samplePane = sp;
    		
    		setBorder(new javax.swing.border.EmptyBorder(2, 2, 2, 2));
    		
    		GridBagLayout gridbag = new GridBagLayout();
    		GridBagConstraints constraints = new GridBagConstraints();
    		setLayout(gridbag);
    			
    		ButtonGroup bGroup = new ButtonGroup();
    		withReplacementButton = new JRadioButton("With Replacement");
    		withReplacementButton.setSelected(samplePane.isWithReplacement());
    		withReplacementButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    			    samplePane.setWithReplacement(withReplacementButton.isSelected());
    			}
    		    });
    		bGroup.add(withReplacementButton);
    		constraints.anchor = GridBagConstraints.WEST;
    		constraints.fill   = GridBagConstraints.NONE;
    		constraints.weightx = 1.0;
    		constraints.insets = new Insets(0, 0, 0, 10);
    		gridbag.setConstraints(withReplacementButton, constraints);
    		add(withReplacementButton);

    		JLabel label = new JLabel("N (Population) = ");
    		constraints.anchor = GridBagConstraints.EAST;
    		constraints.insets = new Insets(0, 0, 0, 0);
    		gridbag.setConstraints(label, constraints);
    		add(label);
    		
    		choiceN = new JComboBox();
    		boolean existingItem = false;
    		String pSizeStr = Integer.toString(samplePane.getPopulationSize());
    		for(int i = 0; i < populationSizes.length; i++){
    			choiceN.addItem(populationSizes[i]);
    			if(pSizeStr.equals(populationSizes[i])){
    				existingItem = true;
    				choiceN.setSelectedIndex(i);
    			}
    		}
    		if(!existingItem){
    			choiceN.insertItemAt(pSizeStr, 0);
    			choiceN.setSelectedIndex(0);
    		}
    		
    		choiceN.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    			    try{
    				String item = choiceN.getSelectedItem().toString();
    				samplePane.setPopulationSize(Integer.parseInt(item));
    			    }catch(Exception e){
    			    		JOptionPane.showMessageDialog(ControlPane.this, 
    			    				e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
    			    		int size = samplePane.getPopulationSize();
    			    		choiceN.setSelectedItem(Integer.toString(size));
    			    }
    			}
    		});
    		
    		constraints.anchor = GridBagConstraints.WEST;
    		constraints.insets = new Insets(0, 0, 0, 10);
    		gridbag.setConstraints(choiceN, constraints);
    		add(choiceN);
    		
    		label = new JLabel("n (Sample size) = ");
    		constraints.anchor = GridBagConstraints.EAST;
    		constraints.insets = new Insets(0, 0, 0, 0);
    		gridbag.setConstraints(label, constraints);
    		add(label);
    		    
    		sampleSizeField = 
    		    new JTextField(Integer.toString(samplePane.getSampleSize()), 5);
    		constraints.anchor = GridBagConstraints.WEST;
    		constraints.gridwidth = GridBagConstraints.REMAINDER;
    		constraints.insets = new Insets(0, 0, 0, 10);
    		gridbag.setConstraints(sampleSizeField, constraints);
    		add(sampleSizeField);
    		
    		noReplacementButton = new JRadioButton("Without Replacement");
    		bGroup.add(noReplacementButton);
    		noReplacementButton.setSelected(!samplePane.isWithReplacement());
    		noReplacementButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    			    samplePane.setWithReplacement(!noReplacementButton.isSelected());
    			}
    		    });
    		constraints.weightx = 1.0;
    		constraints.gridwidth = 1;
    		gridbag.setConstraints(noReplacementButton, constraints);
    		add(noReplacementButton);
    		
    		label = new JLabel("P (red) = ");
    		constraints.anchor = GridBagConstraints.EAST;
    		constraints.insets = new Insets(0, 0, 0, 0);
    		gridbag.setConstraints(label, constraints);
    		add(label);
    		
    		choiceP = new JComboBox();
    		existingItem = false;
    		String pStr = pToString(samplePane.getPRed());
    		for(int i = 0; i < percentages.length; i++){
    			choiceP.addItem(percentages[i]);
    			if(pStr.equals(percentages[i])){
    				choiceP.setSelectedIndex(i);
    				existingItem = true;
    			}
    		}
    		if(!existingItem){
    			choiceP.insertItemAt(pStr, 0);
    			choiceP.setSelectedIndex(0);
    		}
    		
    		choiceP.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    			    try{
    				String item = choiceP.getSelectedItem().toString();
    				samplePane.setPRed(Double.parseDouble(item));
    			    }catch(Exception e){
    			    		JOptionPane.showMessageDialog(ControlPane.this, 
    			    				e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
    			    		choiceP.setSelectedItem(pToString(samplePane.getPRed()));
    			    }
    			}
    		});
    		
    		constraints.anchor = GridBagConstraints.WEST;
    		constraints.insets = new Insets(0, 0, 0, 20);
    		gridbag.setConstraints(choiceP, constraints);
    		add(choiceP);
    		
    		runButton = new JButton("Run a Sample");
    		runButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent event){
    			    try{
    				String s = sampleSizeField.getText();
    				samplePane.runSample(Integer.parseInt(s));
    			    }catch(Exception e){
    				JOptionPane.showMessageDialog(ControlPane.this, 
    							      e.getMessage(),
    							      "Error", JOptionPane.ERROR_MESSAGE);
    				int size = samplePane.getSampleSize();
    				sampleSizeField.setText(Integer.toString(size));
    			    }
    			}
    		});
    		
    		constraints.anchor = GridBagConstraints.CENTER;
    		constraints.gridwidth = GridBagConstraints.REMAINDER;
    		gridbag.setConstraints(runButton, constraints);
    		add(runButton);
    }
    		
    private String pToString(double p){
    		NumberFormat numberFormat = NumberFormat.getInstance();
    		numberFormat.setMaximumFractionDigits(1);
    		return numberFormat.format(samplePane.getPRed());
    }
}
