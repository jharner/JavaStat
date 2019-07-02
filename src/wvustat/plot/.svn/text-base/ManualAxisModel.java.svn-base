/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Dec 8, 2002
 * Time: 9:40:15 AM
 * To change this template use Options | File Templates.
 */
package wvustat.plot;

import wvustat.modules.BaseAxisModel;

public class ManualAxisModel extends BaseAxisModel
{
    private int numOfMinorTicks;
    private boolean isManual;

    public boolean isManual()
    {
        return isManual;
    }

    public void setManual(boolean manual)
    {
        isManual = manual;
    }

    public double getStartValue()
    {
        return startValue;
    }

    public void setStartValue(double startValue)
    {
        this.startValue = startValue;
    }

    public double getEndValue()
    {
        return endValue;
    }

    public void setEndValue(double endValue)
    {
        this.endValue = endValue;
    }

    public double getInterval()
    {
        return interval;
    }

    public void setInterval(double interval)
    {
        this.interval = interval;
    }
    
    public double getMinorIncrement()
    {
        return interval/(numOfMinorTicks+1);
    }

    public int getNumOfMinorTicks()
    {
        return numOfMinorTicks;
    }

    public void setNumOfMinorTicks(int numOfMinorTicks)
    {
        this.numOfMinorTicks = numOfMinorTicks;
    }
}
