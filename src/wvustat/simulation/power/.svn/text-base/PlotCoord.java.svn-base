package wvustat.simulation.power;


public class PlotCoord {

  double	xstretch = 1.0;
  double	ystretch = 1.0;
  double	ywholeScreen;
  private double	xMinBackup;

  private double	hlMargin;
  private double	hrMargin;
  private double	vuMargin;
  private double	vlMargin;
  
  int inset;             /* How far from the edge the display is */

  PlotCoord(double xmax, double ymax, double xmin, double ymin, int width, 
           int height, int margin1, int margin2, int margin3, int margin4 ) {
           
  	hlMargin = margin1;
  	hrMargin = margin2;
  	vuMargin = margin3;
  	vlMargin = margin4;

    xstretch = (width - hlMargin - hrMargin) / (xmax - xmin);
    ystretch = (height - vuMargin - vlMargin) / (ymax - ymin);

    ywholeScreen = ymax - ymin;
    xMinBackup = xmin;

  }

  /* Return the X and Y coordinates on the canvas. */

  int ycoord(double y) {
    double ycoorddouble;

    ycoorddouble = (ywholeScreen - y ) * ystretch + vuMargin;
    return((int)(Math.round(ycoorddouble)));
  }

  int xcoord(double x) {

    double xcoorddouble;

    xcoorddouble = ( x - xMinBackup ) * xstretch + hlMargin;
    return((int)(Math.round(xcoorddouble)));
  }

}