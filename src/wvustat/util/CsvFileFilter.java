package wvustat.util;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: May 9, 2004
 * Time: 2:20:29 PM
 * To change this template use Options | File Templates.
 */
public class CsvFileFilter extends javax.swing.filechooser.FileFilter
{
    public boolean accept(File file)
    {
        if(file.isDirectory())
            return true;
        else
        {
            String name=file.getName();
            if(name.toUpperCase().endsWith(".CSV"))
                return true;
            else
                return false;

        }
    }

    public String getDescription()
    {
        return "CSV (Comma Separated Values)";
    }
}
