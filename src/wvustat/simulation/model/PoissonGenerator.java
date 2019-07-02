package wvustat.simulation.model;

/**
 * Created by IntelliJ IDEA.
 * User: hxue
 * Date: Feb 23, 2003
 * Time: 4:38:47 PM
 * To change this template use Options | File Templates.
 */
public class PoissonGenerator extends BinomialGenerator {

    public Object runTrial(int numOfRun, double goal) {
        this.numOfRuns = numOfRun;
        this.goal = goal;
        double[] array = new double[numOfRun];
        for (int i = 0; i < numOfRun; i++) {
            array[i] = poisson(p);
        }
        return array;
    }

    public int poisson(double c) {
        int x = 0;
        double t = 0.0;
        for (; ;) {
            t = t - Math.log(Math.random()) / c;
            if (t > 1.0)
                break;
            x = x + 1;
        }
        return x;
    }

    public double getSD() {
        return Math.sqrt(p);
    }
}
