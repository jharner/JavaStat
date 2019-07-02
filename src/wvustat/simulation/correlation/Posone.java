package wvustat.simulation.correlation;

import java.awt.*;
import java.util.*;


public class Posone{

	final int MAX_NUMBER = 100;
	final int DIMENSION_MAX = 85;
	Random rand = new Random();
	
	final int height=85;
	final int width=85;

        public int[] ponex = new int[MAX_NUMBER];
	public int[] poney = new int[MAX_NUMBER];
	

	
	
	public int random_int (int center) {
	return Math.abs(rand.nextInt()) % center + 1;
}

public int[] getPlotx(){
	
        	for (int index=0; index<MAX_NUMBER*0.8;index++){
		ponex[index] = 220/4+index/4+(int)(Math.pow(-1,(int)(Math.random()*4))*(int)(Math.random()*3)-10);
	       	}
	
	        for (int index=(int)(MAX_NUMBER*0.8); index<MAX_NUMBER;index++){
		ponex[index] = 160/4+(index%9)*5+(int)(Math.pow(-1,(int)(Math.random()*4))*(int)(Math.random()*3)-10);
		}
        	return ponex;
        }
        
public int[] getPloty(){
	
	for (int index=0; index<MAX_NUMBER*0.8;index++){
		poney[index] = (int)((-1)*ponex[index]+(int)( Math.pow(-1,(int)(Math.random() * 4)) * (int)( Math.random() * 10) ) +90);
	        }
	
	for (int index=(int)(MAX_NUMBER*0.8); index<MAX_NUMBER;index++){
		poney[index] = (int)((-1)*ponex[index]+(int)( Math.pow(-1,(int)(Math.random() * 4)) * (int)( Math.random() * 8) ) +90);
		}

	return poney;
}

public void setMaximumFractionDigits(int n){
	n=3;
}

public double getPlotr(){
	
	int sum_x1 = 0;
	int sum_y1 = 0;
	int ave_x = 0;
	int ave_y = 0;
	double sx = 0; 
	double sy = 0;
	double poner = 0;
	for(int index=0;index<MAX_NUMBER;index++){
		sum_x1 = sum_x1 + (ponex[index]-width/2);
		sum_y1 = sum_y1 + ((-1)*poney[index]+height-height/2);
	}
	
	ave_x = (sum_x1) / MAX_NUMBER;
	ave_y = (sum_y1) / MAX_NUMBER;
	
	for (int index=0; index<MAX_NUMBER;index++){
		sx = sx + ((ponex[index]-width/2)-ave_x) * ((ponex[index]-width/2)-ave_x);
		sy = sy + (((-1)*poney[index]+height-height/2)-ave_y) * (((-1)*poney[index]+height-height/2)-ave_y);
		poner = poner + ((ponex[index]-width/2)-ave_x)*(((-1)*poney[index]+height-height/2)-ave_y);
	}
	sx = Math.sqrt(sx / (MAX_NUMBER-1));
	sy = Math.sqrt(sy / (MAX_NUMBER-1));
	poner = poner / ((MAX_NUMBER-1)*sx*sy);
	
       
	return poner;
}

}