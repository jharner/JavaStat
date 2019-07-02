package wvustat.simulation.continuousdist;

import java.awt.*;

public class PlotCoord {

  double	xstretch = 1.0;
  double	ystretch = 1.0;
  double	ywholeScreen;
  private double	xMinBackup;
  Insets inset;             /* How far from the edge the display is */

  PlotCoord(double xmax, double ymax, double xmin, double ymin,
            int width, int height, Insets insetin ) {

    inset = insetin;

    xstretch = ( width - insetin.left - insetin.right )/ ( (xmax - xmin) * 1.0 );
    ystretch = ( height - insetin.top - insetin.bottom ) / (ymax - ymin);

    ywholeScreen = ymax - ymin;
    xMinBackup = xmin;

  }

  /* Return the X and Y coordinates on the canvas. */

  int ycoord(double y) {
    double ycoorddouble;

    ycoorddouble = (ywholeScreen - y ) * ystretch + inset.top;
    return((int)(Math.round(ycoorddouble)));
  }

  int xcoord(double x) {

    double xcoorddouble;

    xcoorddouble = ( x - xMinBackup ) * xstretch + inset.left;
    return((int)(Math.round(xcoorddouble)));
  }

}
