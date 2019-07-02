/*
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Nov 3, 2002
 * Time: 9:21:54 AM
 * To change template for new interface use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package wvustat.simulation.model;

import java.util.Set;

public interface IGenerator {

    //Constants for describing trials
    public static final int DRAW_N_W_REP=1;
    public static final int DRAW_N_WO_REP=2;
    public static final int DRAW_ONE=3;
    public static final int CEREAL_BOX=4;
    public static final int DRAW_UNTIL_X=5;
    public static final int BOOTSTRAP=7;
    public static final int RANDOMIZE=8;
    public static final int PERMUTE=9;

    public static final String[] trial_names={"","Draw n With Replacement", "Draw n Without Replacement", "Draw Once",
    "Draw Until All X Values Occur", "Draw Until the Specified X Value Occurs", "", "Bootstrap", "Randomize", "Permute"};

    public void setTrialType(int type);

    public int getTrialType();

    public void setParam(Object param);

    public Object runTrial(int numOfRun, double goal);

    public double getAverage();

    public int getNumberOfDraws();

    public boolean isDepletable();

    public double getSD();

    public boolean isGoalReachable(double goal);

    public int getCapacity();

    public boolean isDiscrete();

    public int getOutcomeCount();

    public double getProbability(double val);

    public Set getAllOutcomes();

    /**
     * Get the count of occurence of each distinct observation
     *
     * @return
     */
    public int[] getCount();

    /**
     * Get the indexed attribute values of the observations
     *
     * @return
     */
    public Object[] getAttributes(int index);


    public int getObservationAttributeCount();

    public String[] getAttribueNames();

    public String getModelDescription();

    public double getGoal();
}
