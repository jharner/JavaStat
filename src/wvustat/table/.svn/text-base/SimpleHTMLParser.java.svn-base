/*
 * SimpleHTMLParser.java
 *
 * Created on May 24, 2001, 10:15 AM
 */

package wvustat.table;

import java.util.Vector;
/**
 * This parser will parse a string containing HTML tags. 
 *
 * @author  Hengyi Xue
 * @version 
 */
public class SimpleHTMLParser extends Object {
    private Vector tags;
    private String displayText;
    private String rawString;
    private int cursor=0;
    private AnchorTag oneTag;
    private StringBuffer oneAnchor, parsedBuffer;
    
    public SimpleHTMLParser(){
    	tags=new Vector();
    }
    
    /** Creates new SimpleHTMLParser */
    public SimpleHTMLParser(String rawString) {
        this.rawString=rawString;
        tags=new Vector();
        parse();
    }  
    
    public String getDisplayText(){
        return displayText;
    }
    
    public Vector getTags(){
        return tags;
    }
   
    
    private void parse(){
        parsedBuffer=new StringBuffer(rawString.length());
        
        try{
            while(cursor<rawString.length()){
                char ch=rawString.charAt(cursor);
                if(ch=='<'){
                    if(rawString.charAt(cursor+1)=='/')
                        processEndTag();
                    else
                        processStartTag();
                }
                else{
                    if(oneTag!=null)
                        oneAnchor.append(ch);
                    parsedBuffer.append(ch);                
                }
                cursor++;
            }
        
            displayText=parsedBuffer.toString();
        }
        catch(Exception e){
            displayText=parsedBuffer.toString();
        }
    }    
    
    /** returns how many white space characters have been skipped. 
    */
    private int skipWhitespace(){
        int count=0;
        char ch=rawString.charAt(cursor);
        while(Character.isWhitespace(ch) && cursor<rawString.length()){
            cursor++;
            count++;
            ch=rawString.charAt(cursor);
        }
        
        return count;
    }
    
    private void processStartTag(){
        cursor++;
        skipWhitespace();
       
        int spacePosition=rawString.indexOf(' ',cursor);
        int bracketPosition=rawString.indexOf('>',cursor);
        if(bracketPosition==-1){
            cursor--;
            return;
        }
        
        int endPosition=bracketPosition;
        if(spacePosition==-1)
            endPosition=bracketPosition;
        else if(spacePosition<bracketPosition)
            endPosition=spacePosition;
        
        String tagName=rawString.substring(cursor,endPosition);
        if(tagName.equalsIgnoreCase("a")){
            cursor+=1;
            oneTag=new AnchorTag();
            oneTag.setStartingPosition(parsedBuffer.length());
        }
        else if(tagName.equalsIgnoreCase("br")){
            cursor+=2;
            skipWhitespace();
            cursor++;
            parsedBuffer.append(' ');
            return;
        } 
        else{
            cursor=endPosition;
            parsedBuffer.append(' ');
            return;
        }
        
        skipWhitespace();
        String attribName=rawString.substring(cursor, cursor+4);
        if(attribName.equalsIgnoreCase("href")){
            cursor+=4;
            skipWhitespace();
            char ch=rawString.charAt(cursor);
            if(ch=='='){
                cursor++;
                skipWhitespace();
                StringBuffer buf=new StringBuffer(30);
                ch=rawString.charAt(cursor);
                while(!Character.isWhitespace(ch) && rawString.charAt(cursor)!='>'){
                    if(ch!='"')
                        buf.append(ch);
                    cursor++;
                    ch=rawString.charAt(cursor);
                }
                
                oneTag.setTargetUrl(buf.toString());
            }
        }
        
        while(rawString.charAt(cursor)!='>')
            cursor++;
        
        oneAnchor=new StringBuffer(20);
    }
    
    private void processEndTag(){
        cursor+=2;
        skipWhitespace();
        String tagName=rawString.substring(cursor, cursor+1);
        if(tagName.equalsIgnoreCase("a")){
            cursor+=1;
            oneTag.setAnchorText(oneAnchor.toString());
            oneTag.setEndPosition(oneTag.getStartingPosition()+oneAnchor.length());
            tags.addElement(oneTag);
            oneTag=null;
        }
        while(rawString.charAt(cursor)!='>')
            cursor++;
    }
    
    public static void main(String[] args){
        
        String text="Use the embedded <a href=\"histogram.gif\"> histogram </a> to answer this question."+
        " What is the mean of this dataset? What is the standard deviation of this dataset. If we change the number of "+
        "bins in this <a href=\"histogram.gif\">histogram</a>, do you think it will alter the results?";
        
        
        //String text="<b>Construct a stem-and-leaf diagram for the given data using two or five lines per stem as specified.</b>  The maximum recorded temperatures (in degrees Fahren heit) for 35 different U.S. cities are given below. <blockquote>108&#9;102&#9;11 9&#9;109&#9;112&#9;104&#9;118&#9; <br>110&#9;115&#9;113&#9;108&#9;116&#9;105&#9;113&#9;<br>100&#9;111&#9;114&#9;106&#9;112&#9;109&#9;107&#9;<br>110&#9;112&#9;104&#9;101&#9;106&#9;108&#9;103&#9;<br>105&#9;117&#9;106&#9;115&#9;110&#9;114&#9;113</blockquote><P>Construct a stem-and-leaf diagram using five lines per stem.</P>";
        
        SimpleHTMLParser parser=new SimpleHTMLParser(text);
        System.out.println(parser.getDisplayText());
        Vector v=parser.getTags();
        if(v==null)
            return;
        for(int i=0;i<v.size();i++){
            System.out.println(v.elementAt(i));
        }
    }
}