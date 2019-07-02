package wvustat.simulation.launcher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 16, 2003
 * Time: 10:11:43 AM
 * To change this template use Options | File Templates.
 */
public class JProgressMonitor extends JPanel
{
    private JLabel statusLabel;
    private JProgressBar progressBar;

    private static JProgressMonitor instance;

    private JProgressMonitor()
    {
        statusLabel=new JLabel();
        progressBar=new JProgressBar();
        setLayout(new GridBagLayout());
        statusLabel.setPreferredSize(new Dimension(240, 28));
        add(statusLabel, new GridBagConstraints(0,0,GridBagConstraints.RELATIVE,0,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0));
        add(progressBar, new GridBagConstraints(1,0,0,0,1.0,0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(2,2,2,20),0,0));

    }

    public static void createInstance()
    {
        instance=new JProgressMonitor();
    }

    public static JProgressMonitor getInstance()
    {
        if(instance==null)
            createInstance();
        return instance;
    }

    public void setStatus(String status)
    {
        statusLabel.setText(status);
    }

    public void startIndeterminateProgress(String status)
    {
        setStatus(status);
        //progressBar.setIndeterminate(true);
    }

    public void stopIndeterminateProgress()
    {
        setStatus("Done");
        //progressBar.setIndeterminate(false);
        progressBar.setValue(0);
    }

    public void increateProgress()
    {

    }
}
