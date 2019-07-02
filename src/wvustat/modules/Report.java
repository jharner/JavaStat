package wvustat.modules;

import java.awt.*;
import javax.swing.*;
/**
*	This is a definition of a 'Report' which is used in the bottom panel of an applet
*	 to report summary statistics.
*
*	@author: Hengyi Xue
*	@version: 1.0, Jan. 25, 1999
*/

public abstract class Report extends JPanel{
	
	public abstract void setGroup(int index);
	
	public abstract void updateReport();
	
} 
