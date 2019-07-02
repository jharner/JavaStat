package wvustat.table;

import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Dec 13, 2003
 * Time: 9:03:51 AM
 * To change this template use Options | File Templates.
 */
public class ColumnAttributes
{
    private String name;
    //private Class columnClass;
    private int role;
    private int type;
    private int numOfDigits=-1; //Number of digits after decimal points
    private boolean ordinal, levelCheck;
    private List levels;

    public ColumnAttributes()
    {

    }



    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    /*public Class getColumnClass()
    {
        return columnClass;
    }

    public void setColumnClass(Class cls)
    {
        this.columnClass=cls;
    }*/

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role = role;
    }

    /**
     * @return Numerical-0, Categorical-1
     */
    public int getType()
    {
        return type;
    }

    /**
     * @param type Numerical-0, Categorical-1
     */
    public void setType(int type)
    {
        this.type = type;
    }

    public int getNumOfDigits()
    {
        return numOfDigits;
    }

    public void setNumOfDigits(int numOfDigits)
    {
        this.numOfDigits = numOfDigits;
    }
    
    public void setOrdinal(boolean b) {
    	this.ordinal = b;
    }
    
    public boolean isOrdinal() {
    	return ordinal;
    }
    
    /**
     * Apply list checking on levels.
     */
    public void setLevelCheck(boolean b) {
    	this.levelCheck = b;
    }
    
    /**
     * List checking on levels.
     */
    public boolean isLevelCheck() {
    	return levelCheck;
    }
    
    public void setLevels(List levels) {
    	this.levels = levels;
    }
    
    public List getLevels() {
    	return levels;
    }
}
