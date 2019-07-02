/*
 * ImageTag.java
 *
 * Created on May 24, 2001, 10:32 AM
 */

package wvustat.table;

import java.awt.Rectangle;
import java.util.Vector;
/**
 * ImageTag encapsulates essential information about a HTML image tag, including the starting position
 * of the anchor text ( in a paragraph), ending position, the anchor text and the URL of the image.
 *
 * @author  Hengyi Xue
 * @version 
 */
public class AnchorTag extends Object {
    private int startingPosition, endPosition;
    private String anchorText;
    private String targetUrl;
    private Vector bounds=new Vector();
    private int startLine=-1, endLine=-1; 

    public AnchorTag(){
    }
    
    /** Creates new ImageTag */
    public AnchorTag(int start, int end, String anchor, String targetUrl) {
        this.startingPosition=start;
        this.endPosition=end;
        this.anchorText=anchor;
        this.targetUrl=targetUrl;
    }
    
    public void setStartingPosition(int x){
        this.startingPosition=x;
    }
    
    public int getStartingPosition(){
        return startingPosition;
    }
    
    public void setEndPosition(int x){
        this.endPosition=x;
    }
    
    public int getEndPosition(){
        return endPosition;
    }
    
    public void setAnchorText(String txt){
        this.anchorText=txt;
    }
    
    public String getAnchorText(){
        return anchorText;
    }
    
    public void setTargetUrl(String url){
        this.targetUrl=url;
    }
    
    public String getTargetUrl(){
        return targetUrl;
    }
    
    public String toString(){
        StringBuffer ret=new StringBuffer();
        ret.append("Anchor: "+anchorText+"\n");
        ret.append("Start: "+startingPosition+", end: "+endPosition+"\n");
        ret.append("URL: "+targetUrl+"\n");
        return ret.toString();
    }
    
    public void addBounds(int x, int y, int width, int height){
        Rectangle rect=new Rectangle(x,y,width,height);
        bounds.addElement(rect);
    }
    
    public void setStartLine(int lineNum){
        startLine=lineNum;
    }
    
    public int getStartLine(){
        return startLine;
    }
    
    public void setEndLine(int lineNum){
        endLine=lineNum;
    }
    
    public int getEndLine(){
        return endLine;
    }
    
    public boolean contains(int x, int y){
        boolean ret=false;
        int i=0;
        
        while(!ret && i<bounds.size()){
            Rectangle rect=(Rectangle)bounds.elementAt(i);
            ret=rect.contains(x,y);
            i++;
        }
        
        return ret;
    }
}