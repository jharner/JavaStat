package wvustat.table;

import wvustat.interfaces.DataSet;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Dec 13, 2003
 * Time: 9:26:12 AM
 * To change this template use Options | File Templates.
 */
public class RoleEnum
{
    public static final RoleEnum U_ROLE=new RoleEnum("None", DataSet.U_ROLE);
    public static final RoleEnum X_ROLE=new RoleEnum("X", DataSet.X_ROLE);
    public static final RoleEnum Y_ROLE=new RoleEnum("Y", DataSet.Y_ROLE);
    public static final RoleEnum Z_ROLE=new RoleEnum("Z", DataSet.Z_ROLE);
    public static final RoleEnum L_ROLE=new RoleEnum("Label",DataSet.L_ROLE);
    public static final RoleEnum F_ROLE=new RoleEnum("Frequency", DataSet.F_ROLE);

    public static RoleEnum getRoleEnum(int role)
    {
        switch(role)
        {
            case DataSet.U_ROLE:
                return U_ROLE;
            case DataSet.X_ROLE:
                return X_ROLE;
            case DataSet.Y_ROLE:
                return Y_ROLE;
            case DataSet.Z_ROLE:
                return Z_ROLE;
            case DataSet.L_ROLE:
                return L_ROLE;
            case DataSet.F_ROLE:
                return F_ROLE;
        }
        return null;
    }

    private String name;
    private int role;


    private RoleEnum(String name, int role)
    {
        this.name=name;
        this.role=role;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public int getRole()
    {
        return role;
    }

    public void setRole(int role)
    {
        this.role=role;
    }

    public String toString()
    {
        return name;
    }
}
