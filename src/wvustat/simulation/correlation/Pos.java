package wvustat.simulation.correlation;


import java.awt.*;
import java.util.*;


public class Pos{

	final int MAX_NUMBER = 100;
	final int DIMENSION_MAX = 85;
	Random rand = new Random();
	
	final int height=85;
	final int width=85;

        public int[] px = new int[MAX_NUMBER];
	public int[] py = new int[MAX_NUMBER];
	
	int sum_x1 = 0;
	int sum_y1 = 0;
	int ave_x = 0;
	int ave_y = 0;
	double sx = 0; 
	double sy = 0;
	double pr = 0;
	
	public int random_int (int center) {
	return Math.abs(rand.nextInt()) % center + 1;
}

public int[] getPlotx(){
	
                for (int index=0; index<MAX_NUMBER*0.1;index++){
		px[index] = random_int(DIMENSION_MAX);
		if ((px[index]<0) || (px[index]+3>width))
	        index=index-1;
	        }
	
                for (int index=(int)(MAX_NUMBER*0.1); index<MAX_NUMBER*0.4;index++){
        	px[index] = 250/4 +(int)(random_int((int)(DIMENSION_MAX*0.2)));
        	if ((px[index]<0) || (px[index]+3>width))
	        index=index-1;
        	}
        
                for (int index=(int)(MAX_NUMBER*0.4); index<MAX_NUMBER*0.7;index++){
        	px[index] = 205/4 +(int)(random_int((int)(DIMENSION_MAX*0.15)));
        	if ((px[index]<0) || (px[index]+3>width))
	        index=index-1;
        	}
	
	        for (int index=(int)(MAX_NUMBER*0.7); index<MAX_NUMBER;index++){
        	px[index] = 115/4 +(int)(random_int((int)(DIMENSION_MAX*0.2)));
        	if ((px[index]<0) || (px[index]+3>width))
	        index=index-1;
		}        	
       
	
	return px;
	
}

public int[] getPloty(){
	
	for (int index=0; index<MAX_NUMBER*0.1;index++){
	py[index] = random_int(DIMENSION_MAX);
	if ((py[index]<0)||(py[index]+3>height))
	index=index-1;
	}
	
        for (int index=(int)(MAX_NUMBER*0.1); index<MAX_NUMBER*0.4;index++){
        py[index] =30/4 +(int)(random_int((int)(DIMENSION_MAX*0.2)));
       	if ((py[index]<0)||(py[index]+3>height))
	index=index-1;
        }
        
        for (int index=(int)(MAX_NUMBER*0.4); index<MAX_NUMBER*0.7;index++){
        py[index] = 70/4 +(int)(random_int((int)(DIMENSION_MAX*0.3)));
        if ((py[index]<0)||(py[index]+3>height))
	index=index-1;
        }
	
	for (int index=(int)(MAX_NUMBER*0.7); index<MAX_NUMBER;index++){
        py[index] = 110/4 +(int)(random_int((int)(DIMENSION_MAX*0.3)));
        if ((py[index]<0)||(py[index]+3>height))
	index=index-1;
	}
	
	return py;
}

public void setMaximumFractionDigits(int n){
	n=3;
}

public double getPlotr(){
	
	double getMycorr;
	
	for(int index=0;index<MAX_NUMBER;index++){
		sum_x1 = sum_x1 + (px[index]-width/2);
		sum_y1 = sum_y1 + ((-1)*py[index]+height-height/2);
	}
	
	ave_x = (sum_x1) / MAX_NUMBER;
	ave_y = (sum_y1) / MAX_NUMBER;
	
	for (int index=0; index<MAX_NUMBER;index++){
		sx = sx + ((px[index]-width/2)-ave_x) * ((px[index]-width/2)-ave_x);
		sy = sy + (((-1)*py[index]+height-height/2)-ave_y) * (((-1)*py[index]+height-height/2)-ave_y);
		pr = pr + ((px[index]-width/2)-ave_x)*(((-1)*py[index]+height-height/2)-ave_y);
	}
	sx = Math.sqrt(sx / (MAX_NUMBER-1));
	sy = Math.sqrt(sy / (MAX_NUMBER-1));
	pr = pr / ((MAX_NUMBER-1)*sx*sy);
	
	getMycorr=pr;
	return getMycorr;
}


}