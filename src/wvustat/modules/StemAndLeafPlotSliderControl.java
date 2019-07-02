package wvustat.modules;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import wvustat.swing.* ;


public class StemAndLeafPlotSliderControl extends JPanel {
	private StemAndLeafPlotModel model ;
	private SimpleSlider slider ;
	private JLabel currentValueLabel ;
	private JLabel sliderLabel ;

	public StemAndLeafPlotSliderControl(StemAndLeafPlotModel theModel) {
		this.model = theModel;
		this.setBackground(Color.WHITE);
		this.slider = new SimpleSlider(5, 25, 15);
		this.setLayout(new GridBagLayout());
		this.slider.setLabel("Approx Length");
		sliderLabel = new JLabel(slider.getLabel());
		currentValueLabel = new JLabel(slider.getTextValue(), JLabel.RIGHT);
		sliderLabel.setFont(slider.getFont());
		currentValueLabel.setFont(slider.getFont());
				
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0 ;
		gbc.gridy = 0 ;
		gbc.gridwidth = 1 ;
		gbc.weightx = 0 ;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH ;
		this.add(currentValueLabel, gbc);
	
		gbc.gridx = 1 ;
		gbc.gridy = 0 ;
		gbc.gridwidth = 1 ;
		gbc.weightx = 0 ;
		gbc.weighty = 0 ;
		gbc.fill = GridBagConstraints.NONE ;
		gbc.anchor = GridBagConstraints.EAST ;
		gbc.insets = new Insets(4,4,0,4);
		this.add(slider, gbc);
		
		gbc.gridx = 2 ;
		gbc.gridwidth = 1 ;
		gbc.gridy = 0 ;
		gbc.fill = GridBagConstraints.NONE ;
		gbc.anchor = GridBagConstraints.SOUTH ;
		gbc.weightx = 0 ;
		gbc.weighty = 0 ;
		this.add(sliderLabel, gbc);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				update();
			}
		});
				
		update();
		
	}
	
	private void update() {
		if (model!=null) {
			int value = (int)slider.getValue();	
			model.setTargetLength(value);
		}
		currentValueLabel.setText(slider.getTextValue());
		sliderLabel.setText(slider.getLabel());
	}
	
}
