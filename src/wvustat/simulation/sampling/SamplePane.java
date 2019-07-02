package wvustat.simulation.sampling;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.text.NumberFormat;

public class SamplePane extends JPanel{
    public static final Font font = new Font("Dialog", Font.PLAIN, 10);
    public static final int rowGap = 2;
    public static final int ballSize = 10;

    /*
     * Since there are only two colors, I use the lowest bit to mark the 
     * color of a sample. The rest part of the integer is used to count
     * how many time this particular sample was selected.
     */
	public static final int REDBIT = 0x1;
    public static final int BLUEBIT = 0x0;
    
    private double pRed;
    private double realP;
    
    int[] population;
    int sampleSize;
    int nRedSamples;
	boolean replacement;
    
	public SamplePane(){
		this(50, 10, 0.4, true);
	}
	
	/**
	 * 
	 * @param pSize population size
	 * @param sSize sample size
	 * @param p probability of red balls
	 * @param r with replacement or not
	 */
	public SamplePane(int pSize, int sSize, double p, boolean r){
		setPreferredSize(new Dimension(400, 300));
		
		if(sSize <= 0)
		    throw new IllegalArgumentException("Negative sample size.");
		if(sSize > pSize)
		    throw new IllegalArgumentException("Sample size is bigger " +
						       "than population size.");
		if(p < 0 || p > 1.0)
		    throw new IllegalArgumentException("Invalid Probability.");
			
		population = new int[pSize];
		sampleSize = sSize;
		pRed = p;
		replacement = r;
		generatePopulation();
		runSample();
	}
	
	private void generatePopulation(){
		int nr = (int)(population.length * pRed);
		int n = population.length;
		
		realP = (double)nr / (double)population.length;
		for(int i = 0; i < population.length; i++){
		    if(Math.random() * n > nr)
		    		population[i] = BLUEBIT;  //Blue
		    else{
		    		population[i] = REDBIT; //Red
		    		nr--;
		    }
		    n--;
		}
	}
	
	public void runSample(int n){ //Run sample with a new sampel size
		if(n <= 0 || n > population.length)
		    throw new IllegalArgumentException("Sample size must between 1.." +
		    		population.length);
			
		if(sampleSize != n){
		    sampleSize = n;
		}
		
		runSample();
	}
		
	public void runSample(){
		nRedSamples = 0;
		resetCount();
		
		if(replacement){
		    for(int i = 0; i < sampleSize; i++)
		    		select((int)(Math.random() * population.length));
		}
		else{ //The algorithm is from Knuth's book
		    int t = 0;
		    int m = 0;
		    double u = 0.0;
				
		    while(m < sampleSize){
		    		u = Math.random();
		    		if((population.length - t) * u >= sampleSize - m)
		    			t++;
		    		else{
		    			select(t);
		    			m++;
		    			t++;
		    		}
		    }
		}
		repaint();
	}
	
	private void select(int index){
    		population[index] += (0x1 << 1);
    		if((population[index] & 0x1) == REDBIT)
    			nRedSamples++;
	}

	private void resetCount(){
    		for(int i = 0; i < population.length; i++)
    			population[i] = (population[i] & 0x1);
    }
	
	private int getCount(int index){
		int count = population[index] & ~(0x1);
		return (count >> 1);
	}
	
	public int getPopulationSize(){
		return population.length;
	}
	
	public void setPopulationSize(int n){
		if(n < sampleSize)
			throw new IllegalArgumentException("Population size should be " +
				"larger than sample size");
			
		if(n != population.length){
		    population = new int[n];
		    generatePopulation();
		    runSample();
		}
	}
	
	public int getSampleSize(){
		return sampleSize;
	}
		
	public void setSampleSize(int n){
		if(n <= 0 || n > population.length)
		    throw new IllegalArgumentException("Sample size must between 1.." +
		    		population.length);
			
		if(sampleSize != n){
		    sampleSize = n;
		    runSample();
		}
	}
		
	public double getPRed(){
		return pRed;
	}
		
	public void setPRed(double p){
		if(p < 0 || p > 1)
		    throw new IllegalArgumentException("Illegal Probability");
			
		if(Math.abs(p - pRed) > 0.001){
		    pRed = p;
		    generatePopulation();
		    runSample();
		}
	}
		
	public boolean isWithReplacement(){
		return replacement;
	}
		
	public void setWithReplacement(boolean r){
		if(replacement != r){
		    replacement = r;
		    generatePopulation();
		    runSample();
		}
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
			
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
			
		Graphics2D g2 = (Graphics2D)g;
		FontMetrics fontMetrics = g2.getFontMetrics();
		Rectangle2D maxCharSize = fontMetrics.getMaxCharBounds(g2);
			
		int lineSpacing = fontMetrics.getMaxAscent() + 
		    fontMetrics.getMaxDescent() + rowGap;
		int indent = 15;
			
		//Paint the sample information
		int x = 5;
		int y = 5 + fontMetrics.getMaxAscent();
		g2.drawString("Sample (Size = " + sampleSize + "):", x, y);
			
		y += lineSpacing;
		x += indent;
		g2.setColor(Color.red);
		g2.drawString("Red: " + nRedSamples * 100 / sampleSize + "%", x, y);
		Rectangle2D.Double rect = 
		    new Rectangle2D.Double(x + 6 * maxCharSize.getWidth(),
					   y - 5, nRedSamples * 100 / sampleSize, 5);
		g2.fill(rect);
			
		g2.setColor(Color.blue);
		y += lineSpacing;
		g2.drawString("Blue: " + 
			      (sampleSize - nRedSamples) * 100 / sampleSize + 
			      "%", x, y);
		rect.y = y - 5;
		rect.width = (sampleSize - nRedSamples) * 100 / sampleSize;
		g2.fill(rect);
			
		//Paint the population information
		x -= indent;
		y = y + lineSpacing + lineSpacing;
		g2.setColor(getForeground());
		g2.drawString("Population (Size = " + population.length +
			      "):", x, y);
		y += lineSpacing;
		x += indent;
		g2.drawString("Request  P(Red) = " + numberFormat.format(pRed) +
			      ", Real P(Red) = " + numberFormat.format(realP), x, y);
			
		y += lineSpacing;
		Arc2D.Float ball = new Arc2D.Float(x, y, ballSize, 
						   ballSize, 0, 360, Arc2D.PIE);
		int index = 0;
		g2.setFont(font);
		fontMetrics = g2.getFontMetrics();
		while(index < population.length){
		    if((population[index] & 0x1) == REDBIT)
			g.setColor(Color.red);
		    else
			g.setColor(Color.blue);
		    g2.fill(ball);

		    int count = getCount(index);
		    if(count > 0){
			g2.setColor(getForeground());
			int textX = (int)ball.x + 2;
			int textY = (int)ball.y + fontMetrics.getMaxAscent() -
			    1;
			g2.drawString(Integer.toString(count), textX, textY);
		    }

		    index++;
		    if(getWidth() - ball.x <= ballSize + 5 + indent){
			ball.x = x;
			ball.y += (ballSize + 2);
		    }
		    else
			ball.x += (ballSize + 2);
		}
	}
}
