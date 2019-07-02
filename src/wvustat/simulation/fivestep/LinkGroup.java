package wvustat.simulation.fivestep;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 3, 2003
 * Time: 2:09:28 PM
 * To change this template use Options | File Templates.
 */
public class LinkGroup implements PropertyChangeListener
{
    private List linkList=new ArrayList();
    private LinkLabel selectedLinkLabel;

    public LinkGroup()
    {

    }

    public void addLink(LinkLabel label)
    {
        linkList.add(label);
        label.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent event)
    {
        LinkLabel linkLabel=(LinkLabel)event.getSource();
        if(linkLabel.isActive())
        {
            selectedLinkLabel=linkLabel;
            for(int i=0;i<linkList.size();i++)
            {
                LinkLabel linkLabel2=((LinkLabel)linkList.get(i));
                if(linkLabel2!=linkLabel)
                    linkLabel2.setActive(false);
            }
        }
    }

    public LinkLabel getSelectedLinkLabel()
    {
        return selectedLinkLabel;
    }
}
