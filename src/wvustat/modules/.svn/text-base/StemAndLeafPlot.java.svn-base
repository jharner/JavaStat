package wvustat.modules;

import javax.swing.*;
import java.awt.* ;
import javax.swing.event.*;


public class StemAndLeafPlot extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 1L;
	private StemAndLeafPlotModel model ;
	private JTextArea textArea ;
	
	public StemAndLeafPlot(StemAndLeafPlotModel model) {
		this.model = model;
		this.setLayout(new BorderLayout());
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
		textArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setBorder(BorderFactory.createEmptyBorder());
		this.add(scroller);
		if (model!=null) {
			model.addChangeListener(this);
			textArea.setText(model.toString());
		}
	}
	
	public StemAndLeafPlot() {
		this(null);
	}

	public StemAndLeafPlotModel getModel() {
		return model;
	}

	public void setModel(StemAndLeafPlotModel model) {
		int caret = textArea.getCaretPosition() ;
		if (model!=null) {
			model.removeChangeListener(this);
		}
		this.model = model;
		if (model!=null) {
			model.addChangeListener(this);
			textArea.setText(model.toString());
		} else {
			textArea.setText("");
		}
		textArea.moveCaretPosition(Math.min(caret, textArea.getText().length()));
	}

	public void stateChanged(ChangeEvent e) {
		int caret = textArea.getCaretPosition() ;
		textArea.setText(model.toString());
		textArea.moveCaretPosition(Math.min(caret, textArea.getText().length()));
	}
	
	public int getFontSize() {
		return textArea.getFont().getSize() ;
	}
	
	public void setFontSize(int fontSize) {
		if(fontSize<=0) {
			throw new IllegalArgumentException("Font size must be positive");
		}
		Font f = textArea.getFont();
		String fontName = f.getName();
		int fontStyle = f.getStyle();
		Font newFont = new Font(fontName, fontStyle, fontSize);
		textArea.setFont(newFont);
	}

}
