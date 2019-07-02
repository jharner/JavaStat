package wvustat.util;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 31, 2003
 * Time: 4:12:54 PM
 * To change this template use Options | File Templates.
 */
public class RandomInt
{
    private int min, max;
    public RandomInt(int min, int max)
    {
        this.min=min;
        this.max=max;
    }

    public int nextRandom()
    {
        double v=Math.random();

        return (int)(min+v*(max-min));
    }
}
