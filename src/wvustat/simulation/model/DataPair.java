package wvustat.simulation.model;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 1:56:06 PM
 * To change this template use Options | File Templates.
 */
public class DataPair
{
    protected Double x;
    protected Double y;

    public DataPair()
    {

    }

    public DataPair(Double x, Double y)
    {
        this.x=x;
        this.y=y;
    }

    public DataPair(double x, double y)
    {
        this.x=new Double(x);
        this.y=new Double(y);
    }

    public Double getX()
    {
        return x;
    }

    public Double getY()
    {
        return y;
    }

    public boolean isValid()
    {
        return x!=null && y!=null;
    }

    public void setX(Double x)
    {
        this.x=x;
    }

    public void setY(Double y)
    {
        this.y=y;
    }
}
