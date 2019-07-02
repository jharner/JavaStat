package wvustat.simulation.correlation;

import java.awt.*;
import java.util.*;

public class Negaone{

	final int MAX_NUMBER = 100;
	final int DIMENSION_MAX = 85;
	Random rand = new Random();
	
	final int height=85;
	final int width=85;

        public int[] nonex = new int[MAX_NUMBER];
	public int[] noney = new int[MAX_NUMBER];
	
	int sum_x1 = 0;
	int sum_y1 = 0;
	int ave_x = 0;
	int ave_y = 0;
	double sx = 0; 
	double sy = 0;
	double noner = 0;
	
	public int random_int (int center) {
	return Math.abs(rand.nextInt()) % center + 1;
}

public int[] getPlotx(){
	
        	for (int index=0; index<MAX_NUMBER*0.8;index++){
		nonex[index] = 20/4+index/4+(int)(Math.pow(-1,(int)(Math.random()*4))*(int)(Math.random()*3)+10);
	      	}
	
	        for (int index=(int)(MAX_NUMBER*0.8); index<MAX_NUMBER;index++){
		nonex[index] = 60/4+(index%9)*5+(int)(Math.pow(-1,(int)(Math.random()*4))*(int)(Math.random()*3)+10);
		}
        	return nonex;
        }
        
public int[] getPloty(){
	
	for (int index=0; index<MAX_NUMBER*0.8;index++){
		noney[index] = (int)(nonex[index]+(int)( Math.pow(-1,(int)(Math.random() * 4)) * (int)( Math.random() * 10) )+10);
	}
	
	for (int index=(int)(MAX_NUMBER*0.8); index<MAX_NUMBER;index++){
		noney[index] = (int)(nonex[index]+(int)( Math.pow(-1,(int)(Math.random() * 4)) * (int)( Math.random() * 8) )+10 );
	}
	return noney;
}

public void setMaximumFractionDigits(int n){
	n=3;
}

public double getPlotr(){
	
	double getMycorr;
	
	for(int index=0;index<MAX_NUMBER;index++){
		sum_x1 = sum_x1 + (nonex[index]-width/2);
		sum_y1 = sum_y1 + ((-1)*noney[index]+height-height/2);
	}
	
	ave_x = (sum_x1) / MAX_NUMBER;
	ave_y = (sum_y1) / MAX_NUMBER;
	
	for (int index=0; index<MAX_NUMBER;index++){
		sx = sx + ((nonex[index]-width/2)-ave_x) * ((nonex[index]-width/2)-ave_x);
		sy = sy + (((-1)*noney[index]+height-height/2)-ave_y) * (((-1)*noney[index]+height-height/2)-ave_y);
		noner = noner + ((nonex[index]-width/2)-ave_x)*(((-1)*noney[index]+height-height/2)-ave_y);
	}
	sx = Math.sqrt(sx / (MAX_NUMBER-1));
	sy = Math.sqrt(sy / (MAX_NUMBER-1));
	noner = noner / ((MAX_NUMBER-1)*sx*sy);
	
	getMycorr=noner;
	return getMycorr;
}

}