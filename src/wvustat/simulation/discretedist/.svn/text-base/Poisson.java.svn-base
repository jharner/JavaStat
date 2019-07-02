package wvustat.simulation.discretedist;

public class Poisson
{
        public double lamda;
        
        public Poisson(double x)
        {
                lamda=x;
        }
        
        public double f(int x)
        {
                double y;
                y=Math.exp(-lamda);
                for(int i=1;i<=x;i++)
                {
                        y=y*lamda/i;
                }
                
                return y;
        }
        
        public double F(int x)
        {
                double y=0.0;
                for(int i=0;i<=x;i++)
                {
                        y=y+f(i);
                }
                
                return y;
        }
        
        public int Quantile(double x)
        {
                int y;
                
                int i=0;
                
                x = Math.min(x, 0.999);
                while(F(i)<x)
                {
                        i++;
                }
                
                if(x>Math.exp(-lamda)) y=i;
                else y=0;
                
                return y;
        }
}
                        
                 
