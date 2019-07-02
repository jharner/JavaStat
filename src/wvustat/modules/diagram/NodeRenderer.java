package wvustat.modules.diagram;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.Renderer;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

/**
 * @author jtan
 *
 * This class is used to get a component to display a node
 */
public class NodeRenderer implements Renderer{
	private JLabel normalComp;
	private JLabel highlightComp;
	private boolean isSelected;
	
	public NodeRenderer(){
		Font font = new Font("Monospaced", Font.PLAIN, 11);
		
		normalComp = new JLabel();
		normalComp.setFont(font);
		normalComp.setOpaque(false);
		normalComp.setForeground(Color.black);
		normalComp.setBorder(new CompoundBorder(new LineBorder(Color.black),
				new EmptyBorder(2, 5, 2, 5)));
		
		highlightComp = new JLabel();
		highlightComp.setFont(font);
		highlightComp.setOpaque(true);
		highlightComp.setBackground(Color.blue);
		highlightComp.setForeground(Color.white);
		highlightComp.setBorder(new CompoundBorder(new LineBorder(Color.blue),
				new EmptyBorder(2, 5, 2, 5)));
	}
	public void setValue(Object aValue, boolean isSelected) {
		this.isSelected = isSelected;
		JLabel comp = (isSelected)?(highlightComp):(normalComp);
		DiagramNode node = (DiagramNode)aValue;
		comp.setText(node.getName());
		comp.setSize(node.getWidth(), node.getHeight());
	}

	public Component getComponent() {
		return (isSelected)?(highlightComp):(normalComp);
	}

}
