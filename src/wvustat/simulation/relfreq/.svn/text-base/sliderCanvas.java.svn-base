package wvustat.simulation.relfreq;

import java.awt.*;  
import javax.swing.*;

public class sliderCanvas extends JPanel {

    RelFreq tmpApplet;
	PlotCanvas pCanvas;
  	final  int sliderX = 0;
  	final  int sliderY = 10;
  	final  int sliderW = 70;
  	final  int sliderH = 5;
  	public int cursorX = 30;
  	final  int cursorW = 10;
  	
        	
    public sliderCanvas ( PlotCanvas tmpCanvas, RelFreq simpleApplet) {
	
		pCanvas = tmpCanvas;
		tmpApplet = simpleApplet;
    }

    public void paintComponent (Graphics g) {

		g.setColor(Color.white);
		g.drawRect(sliderX, sliderY, sliderW, sliderH);
		g.fillRect(sliderX + cursorX, sliderY, cursorW, sliderH);
		g.drawString("Slow", 1, 7);
		g.drawString("Fast", sliderW + sliderX - 20, 8);
    }

    public boolean handleEvent(Event e) {

      try {
        if  ( ( e.y > sliderY ) && ( e.y < sliderH + sliderY )&& ( e.id == e.MOUSE_DOWN) ) {

			if  ( ( e.x > sliderX + cursorW + cursorX ) && ( e.x < sliderX + sliderW ) ) {	
			   cursorX += cursorW;	
			   this.repaint();	
               		   pCanvas.TimeInterval /= 2;
                           if (pCanvas.TimeInterval < 1) pCanvas.TimeInterval = 1;
          	        }

			if  ( ( e.x > sliderX ) && ( e.x < sliderX + cursorX ) ) {
			   cursorX -= cursorW;
			   this.repaint();	
                           pCanvas.TimeInterval *= 2;
                           if (pCanvas.TimeInterval > 2000) pCanvas.TimeInterval = 2000;
          	        }
        }
      } catch (NumberFormatException e1) {}
      
      return false;
	
      }

}
