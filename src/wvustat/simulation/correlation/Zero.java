package wvustat.simulation.correlation;


import java.awt.*;
import java.util.*;

public class Zero{

	final int MAX_NUMBER = 100;
	final int DIMENSION_MAX = 85;
	Random rand = new Random();
	
	final int height=85;
	final int width=85;

        public int[] zx = new int[MAX_NUMBER];
	public int[] zy = new int[MAX_NUMBER];
	
	int sum_x1 = 0;
	int sum_y1 = 0;
	int ave_x = 0;
	int ave_y = 0;
	double sx = 0; 
	double sy = 0;
	double zr = 0;
	
	public int random_int (int center) {
	return Math.abs(rand.nextInt()) % center + 1;
}

public int[] getPlotx(){
	
        	for (int index=0; index<MAX_NUMBER*0.2;index++){
		zx[index] = random_int(DIMENSION_MAX);
		if ((zx[index]<0) || (zx[index]+3>width))
		index = index - 1;
	        }
	        
	        for (int index=(int)(MAX_NUMBER*0.2); index<MAX_NUMBER;index++){
        	zx[index] = (int)(random_int((int)(DIMENSION_MAX*0.3)))+110/4;
        	if ((zx[index]<0) || (zx[index]+3>width))
	        index=index-1;
        	} 
        	return zx;
        }
        
public int[] getPloty(){
	
	for (int index=0; index<MAX_NUMBER*0.3;index++){
	zy[index] = random_int(DIMENSION_MAX);
	if ((zy[index]<0)||(zy[index]+3>height))
	index = index - 1;
	}
	        
	for (int index=(int)(MAX_NUMBER*0.3); index<MAX_NUMBER;index++){
        zy[index] =120/4+(int)(random_int((int)(DIMENSION_MAX*0.3)));
        if ((zy[index]<0)||(zy[index]+3>height))
	index=index-1;
        } 
	
	return zy;
}

public void setMaximumFractionDigits(int n){
	n=3;
}

public double getPlotr(){
	
	double getMycorr;
	
	for(int index=0;index<MAX_NUMBER;index++){
		sum_x1 = sum_x1 + (zx[index]-width/2);
		sum_y1 = sum_y1 + ((-1)*zy[index]+height-height/2);
	}
	
	ave_x = (sum_x1) / MAX_NUMBER;
	ave_y = (sum_y1) / MAX_NUMBER;
	
	for (int index=0; index<MAX_NUMBER;index++){
		sx = sx + ((zx[index]-width/2)-ave_x) * ((zx[index]-width/2)-ave_x);
		sy = sy + (((-1)*zy[index]+height-height/2)-ave_y) * (((-1)*zy[index]+height-height/2)-ave_y);
		zr = zr + ((zx[index]-width/2)-ave_x)*(((-1)*zy[index]+height-height/2)-ave_y);
	}
	sx = Math.sqrt(sx / (MAX_NUMBER-1));
	sy = Math.sqrt(sy / (MAX_NUMBER-1));
	zr = zr / ((MAX_NUMBER-1)*sx*sy);
	
	getMycorr=zr;
	return getMycorr;
}

}