/*
 * StretchedLayout.java
 *
 * Created on June 21, 2001, 10:29 AM
 */

package wvustat.util;

import java.awt.*;
/**
 * StretchedLayout is a Layout Manager that ensures the first component will be put at the left most position of
 * the container and the last component will be put at the right most position in the container. This layout manager only
 * supports single row.
 *
 * @author  Hengyi Xue
 * @version 
 */
public class StretchedLayout extends Object implements LayoutManager {
    private int hgap;
    private Insets margin;

    /** Creates new StrechedLayout */
    public StretchedLayout() {
        this(new Insets(0,0,0,0),0);
    }
    
    public StretchedLayout(Insets insets, int hgap){
        this.margin=insets;
        this.hgap=hgap;
    }

    /** Adds the specified component with the specified name to
     * the layout.
     * @param name the component name
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name,Component comp) {
    }
    /** Removes the specified component from the layout.
     * @param comp the component to be removed
     */
    public void removeLayoutComponent(Component comp) {
    }
    /** Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     *
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container parent) {
        Insets insets=parent.getInsets();
        int n=parent.getComponentCount();
        int w=0;
        int h=0;
        
        for(int i=0;i<n;i++){
            Component c=parent.getComponent(i);
            Dimension d=c.getPreferredSize();
            w+=d.width+hgap;
            if(d.height>h)
                h=d.height;
        }
        
        w=w-hgap+insets.left+insets.right+margin.left+margin.right;
        h=h+insets.top+insets.bottom+margin.top+margin.bottom;
  
        return new Dimension(w,h);
    }
    /** Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent) {
        int n=parent.getComponentCount();
        int w=0;
        int h=0;
        
        for(int i=0;i<n;i++){
            Component c=parent.getComponent(i);
            Dimension d=c.getPreferredSize();
            w+=d.width+hgap;
            if(d.height>h)
                h=d.height;
        }
        
        w=w-hgap;
  
        return new Dimension(w,h);
    }
    /** Lays out the container in the specified panel.
     * @param parent the component which needs to be laid out
     */
    public void layoutContainer(Container parent) {
        Insets insets=parent.getInsets();
        int n=parent.getComponentCount();
        Dimension size=parent.getSize();

        Component comp=parent.getComponent(n-1);
        Dimension d=comp.getPreferredSize();
        comp.setBounds(size.width-insets.right-d.width-margin.right, insets.top+margin.top, d.width, d.height);
        
        int x0=insets.left+margin.left;
        int y0=insets.top+margin.top;
        
        for(int i=0;i<n-1;i++){
            comp=parent.getComponent(i);
            d=comp.getPreferredSize();
            comp.setBounds(x0,y0,d.width, d.height);
            x0+=d.width+hgap;
        }            
    }
}
