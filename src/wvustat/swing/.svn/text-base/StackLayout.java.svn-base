/*
 * StackLayout.java
 *
 * Created on March 13, 2002, 10:44 AM
 */

package wvustat.swing;

import java.awt.*;
/**
 * StackLayout is a LayoutManager that simply stacks component together in a column. Unlike other
 * LayoutManager, it will leave the unused space blank instead of resizing the components to occupy
 * it.
 *
 * @author  Hengyi Xue
 * @version
 */
public class StackLayout extends Object implements LayoutManager {
    //private Dimension fixedSize;
    private int vgap=2;

    /** Creates new StackLayout */
    public StackLayout() {
        //this(0,0);
        this(0);
    }
    
    public StackLayout(int vgap){
        this.vgap=vgap;
    }

    /*
    public StackLayout(int w, int h){
        this(new Dimension(w,h),2);
    }

    public StackLayout(Dimension d, int vgap){
        fixedSize=d;
        this.vgap=vgap;
    }
    */
    
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
        return minimumLayoutSize(parent);
    }
    /** Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container parent) {
        Insets insets=parent.getInsets();
        int n=parent.getComponentCount();
        int w=0;
        int h=6;
        
        for(int i=0;i<n;i++){
            Component c=parent.getComponent(i);
            Dimension d=c.getPreferredSize();
            if(d.width>w)
                w=d.width;
            h+=d.height+vgap;
        }
        
        w=w+insets.left+insets.right;
        h=h-vgap+insets.top+insets.bottom;        
        
        return new Dimension(w,h);
    }
    /** Lays out the container in the specified panel.
     * @param parent the component which needs to be laid out
     */
    public void layoutContainer(Container parent) {
        Insets insets=parent.getInsets();
        int n=parent.getComponentCount();

        int x0=insets.left;
        int y0=insets.top+1;

        for(int i=0;i<n;i++){
            Component c = parent.getComponent(i);
            if (c.isVisible()) {
                Dimension d = c.getPreferredSize();

                // Set the component's size and position.
                c.setBounds(x0, y0, d.width, d.height);
                y0+=vgap+d.height;
            }
        }
    }
}
