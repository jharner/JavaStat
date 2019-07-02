package wvustat.modules;

import javax.swing.*;
import java.awt.* ;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;


public class StemAndLeafPlotDropDownControl extends JPanel {
	private static final long serialVersionUID = 1L;
	private StemAndLeafPlotModel model ;
	
	public StemAndLeafPlotDropDownControl(StemAndLeafPlotModel theModel) {
		this.model = theModel ;
		final int POPUP_ARROW_WIDTH = 12 ;
		final int POPUP_ARROW_HEIGHT = 7 ;
		final int POPUP_ARROW_INSET = 5 ;
		final int POPUP_ARROW_IMAGE_WIDTH = POPUP_ARROW_WIDTH + 2 * POPUP_ARROW_INSET ;
		final int POPUP_ARROW_IMAGE_HEIGHT = POPUP_ARROW_HEIGHT + 2 * POPUP_ARROW_INSET ;
		final Image img = new BufferedImage(POPUP_ARROW_IMAGE_WIDTH, POPUP_ARROW_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		final GeneralPath popupArrow = new GeneralPath();
		popupArrow.moveTo(POPUP_ARROW_INSET, POPUP_ARROW_INSET);
		popupArrow.lineTo(POPUP_ARROW_WIDTH + POPUP_ARROW_INSET, POPUP_ARROW_INSET);
		popupArrow.lineTo(POPUP_ARROW_WIDTH/2 + POPUP_ARROW_INSET, POPUP_ARROW_HEIGHT + POPUP_ARROW_INSET);
		popupArrow.closePath();
		final Graphics2D g2d = (Graphics2D) img.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, POPUP_ARROW_IMAGE_WIDTH, POPUP_ARROW_IMAGE_HEIGHT);
		g2d.setColor(Color.BLACK);
		g2d.fill(popupArrow);
		final JLabel arrowLabel = new JLabel(new ImageIcon(img));
		
		final JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new JLabel("Approximate Length"));
		for (int i=5; i<=25; i=i+5) {
			popupMenu.add(new TargetLengthAction(i));
		}
		
		arrowLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				popupMenu.show(arrowLabel, evt.getX(), evt.getY());
			}
		});
		
		this.setBackground(Color.WHITE);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(arrowLabel);
	}
	
	public StemAndLeafPlotDropDownControl() {
		this(null);
	}
	
	private class TargetLengthAction extends AbstractAction {
		private int targetLength ;
		TargetLengthAction(int length) {
			super(Integer.toString(length));
			this.targetLength = length ;
		}
		public void actionPerformed(ActionEvent evt) {
			if (model!=null) {
				model.setTargetLength(targetLength);
			}
		}
	}
	
}
