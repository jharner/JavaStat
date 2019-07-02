/*
 * XMLFileFilter.java
 *
 * Created on April 12, 2002, 2:54 PM
 */

package wvustat.util;

import java.io.File;
/**
 *
 * @author  James Harner
 * @version 
 */
public class XMLFileFilter extends javax.swing.filechooser.FileFilter {

    /** Creates new XMLFileFilter */
    public XMLFileFilter() {
    }
    
    public boolean accept(File file){
        if(file.isDirectory())
            return true;
        
        if(file.getName().toLowerCase().endsWith("xml")){
            return true;
        }
        else
            return false;
    }
    
    public String getDescription(){
        return "XML (eXtensible Markup Language)";
        
    }
}
