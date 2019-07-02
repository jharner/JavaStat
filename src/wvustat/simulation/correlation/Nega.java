package wvustat.simulation.correlation;

import java.awt.*;
import java.util.*;


public class Nega{

	final int MAX_NUMBER = 100;
	final int DIMENSION_MAX = 85;
	Random rand = new Random();
	
	final int height=85;
	final int width=85;

        public int[] nx = new int[MAX_NUMBER];
	public int[] ny = new int[MAX_NUMBER];
	
	int sum_x1 = 0;
	int sum_y1 = 0;
	int ave_x = 0;
	int ave_y = 0;
	double sx = 0; 
	double sy = 0;
	double nr = 0;
	
	public int random_int (int center) {
	return Math.abs(rand.nextInt()) % center + 1;
}

public int[] getPlotx(){
	
        	for (int index=0; index<MAX_NUMBER*0.1;index++){
		nx[index] = random_int(DIMENSION_MAX);
		if ((nx[index]<0)||(nx[index]+3>width))
	        index=index-1;
	        }
	
                for (int index=(int)(MAX_NUMBER*0.1); index<MAX_NUMBER*0.4;index++){
        	nx[index] = 100/4+(int)(random_int((int)(DIMENSION_MAX*0.2)));
                if ((nx[index]<0) || (nx[index]+3>width))
	        index=index-1;
        	}
        
                for (int index=(int)(MAX_NUMBER*0.4); index<MAX_NUMBER*0.7;index++){
        	nx[index] = 130/4+(int)(random_int((int)(DIMENSION_MAX*0.15)));
        	if ((nx[index]<0) || (nx[index]+3>width))
	        index=index-1;
        	}
	
	        for (int index=(int)(MAX_NUMBER*0.7); index<MAX_NUMBER;index++){
        	nx[index] = 190/4+(int)(random_int((int)(DIMENSION_MAX*0.15)));
                if ((nx[index]<0) || (nx[index]+3>width))
	        index=index-1;
		}
               
        	
        	return nx;
        }
        
public int[] getPloty(){
	
	for (int index=0; index<MAX_NUMBER*0.1;index++){
	ny[index] = random_int(DIMENSION_MAX);
	if ((ny[index]<0)||(ny[index]+3>height))
	index=index-1;
	}
	
        for (int index=(int)(MAX_NUMBER*0.1); index<MAX_NUMBER*0.4;index++){
        ny[index] =80/4+(int)(random_int((int)(DIMENSION_MAX*0.2)));
        if ((ny[index]<0)||(ny[index]+3>height))
	index=index-1;
        }
        
        for (int index=(int)(MAX_NUMBER*0.4); index<MAX_NUMBER*0.7;index++){
        ny[index] = 120/4+(int)(random_int((int)(DIMENSION_MAX*0.3)));
        if ((ny[index]<0)||(ny[index]+3>height))
	index=index-1;
        }
	
	for (int index=(int)(MAX_NUMBER*0.7); index<MAX_NUMBER;index++){
        ny[index] = 160/4+(int)(random_int((int)(DIMENSION_MAX*0.3)));
       	if ((ny[index]<0)||(ny[index]+3>height))
	index=index-1;
	}
       
	
	return ny;
}

public void setMaximumFractionDigits(int n){
	n=3;
}

public double getPlotr(){
	
	double getMycorr;
	
	for(int index=0;index<MAX_NUMBER;index++){
		sum_x1 = sum_x1 + (nx[index]-width/2);
		sum_y1 = sum_y1 + ((-1)*ny[index]+height-height/2);
	}
	
	ave_x = (sum_x1) / MAX_NUMBER;
	ave_y = (sum_y1) / MAX_NUMBER;
	
	for (int index=0; index<MAX_NUMBER;index++){
		sx = sx + ((nx[index]-width/2)-ave_x) * ((nx[index]-width/2)-ave_x);
		sy = sy + (((-1)*ny[index]+height-height/2)-ave_y) * (((-1)*ny[index]+height-height/2)-ave_y);
		nr = nr + ((nx[index]-width/2)-ave_x)*(((-1)*ny[index]+height-height/2)-ave_y);
	}
	sx = Math.sqrt(sx / (MAX_NUMBER-1));
	sy = Math.sqrt(sy / (MAX_NUMBER-1));
	nr = nr / ((MAX_NUMBER-1)*sx*sy);
	
	getMycorr=nr;
	return getMycorr;
}

}