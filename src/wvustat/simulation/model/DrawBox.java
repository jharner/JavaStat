/*
 * DrawBox.java
 *
 * Created on March 11, 2002, 1:48 PM
 */

package wvustat.simulation.model;

import java.util.Vector;
/**
 * DrawBox implements an object that contains a set of number. Any number can be drawn from the box
 * at random and each number has the equal probability of being drawn. After an number is drawn, it can be
 * put back (with replacement) or removed (without replacement) from the box. 
 * @author  Hengyi Xue
 * @version 
 */
public class DrawBox extends Object {
    private Vector vec;

    /** Creates new DrawBox */
    public DrawBox(double[] array) {
        vec=new Vector(array.length);
        
        for(int i=0;i<array.length;i++){
            vec.addElement(new Double(array[i]));
        }
    }
    
    /**
    * @param putBack True if with replacement, false otherwise
    */
    public double draw(boolean putBack){
    	if (vec.size() == 0) throw new ArithmeticException("Number of trials exceeds sample size!"); //added by djluo
    	
        int index=randomSeed(0,vec.size()-1);
           
        Double elem=(Double)vec.elementAt(index);
        if(!putBack){
            vec.removeElementAt(index);
        }
        return elem.doubleValue();        
    }
    
    /**
    * This is drawing with replacement until all the numbers in the box have appeared at least once in the outcome.
    *
    * @return an integer array that contains the outcome of each drawing
    */
    public double[] cerealBoxDraw(){
        Vector v=new Vector();        
        
        do{
            int index=randomSeed(0,vec.size()-1);        
            Double elem=(Double)vec.elementAt(index);
            v.addElement(elem);            
        }
        while(!contains(v,vec));
        
        double[] ret=new double[v.size()];
        
        for(int i=0;i<ret.length;i++){
            ret[i]=((Double)v.elementAt(i)).doubleValue();
        }
        
        return ret;
    }
    
    /**
    * This sampling method draws numbers from the box until the fist time X is found.
    *
    * @param X int The number you want to draw from the box
    * @return an integer array containing all the numbers drawn from the box
    */
    public double[] drawUntil(double X){
        Vector v=new Vector(vec.size());
        
        double d;
        do{
            int index=randomSeed(0,vec.size()-1);
            v.addElement(vec.elementAt(index));
            d=((Double)vec.elementAt(index)).doubleValue();
        }
        while(d!=X);
        
        double[] ret=new double[v.size()];
        for(int i=0;i<v.size();i++){
            ret[i]=((Double)v.elementAt(i)).doubleValue();
        }
        
        return ret;
    }
    
    private boolean contains(Vector v1, Vector v2){
        boolean result=true;
        for(int i=0;i<v2.size();i++){
            if(!v1.contains(v2.elementAt(i))){
                result=false;
                break;
            }
        }
        
        return result;
    }
                
    
    private int randomSeed(int min, int max){
        double d=Math.random();
        
        int seed=(int)Math.floor(d*(max-min+1))+min;
        if(seed==max+1)
            seed=max;
        
        return seed;
    }    
    
    public int size(){
        return vec.size();
    }
    
    public double getAverage(){
        double sum=0;
        for(int i=0;i<vec.size();i++){
            sum+=((Double)vec.elementAt(i)).doubleValue();
        }
        
        return sum/vec.size();
    }
    
    public double getSD(){
        double avg=getAverage();
        
        double ss=0;
        for(int i=0;i<vec.size();i++){
            ss+=Math.pow(((Double)vec.elementAt(i)).doubleValue()-avg,2);
        }
        
        return Math.sqrt(ss/vec.size());
    }

    public double[] getRemaining()
    {
        double[] ret=new double[vec.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i]=((Double)vec.get(i)).doubleValue();
        }
        return ret;
    }

    public static void main(String[] args){
        DrawBox box=new DrawBox(new double[]{1,2,3,4,5,6});
        
        int[] count=new int[6];
        double ss=0;
        for(int i=0;i<120000;i++){
            int d=(int)box.draw(true);        
            count[d-1]++;
            ss+=Math.pow(d-3.5, 2);
        }
        

        for(int i=0;i<count.length;i++){
            System.out.println(count[i]/120000.0);
            System.out.println(Math.sqrt(ss/(120000-1)));
        }
    }
}