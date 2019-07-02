package wvustat.modules;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 5, 2005
 * Time: 3:11:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportPanel extends JPanel{
    private JEditorPane editorPane;

    public ReportPanel(){
        init();

    }

    private void init(){
        editorPane=new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setMargin(new Insets(5,10,0,0 ));
        this.setLayout(new BorderLayout());
        JScrollPane jsp=new JScrollPane(editorPane);
        jsp.setPreferredSize(new Dimension(360,80));
        this.add(jsp, BorderLayout.CENTER);
    }

    public void setText(String content){
        editorPane.setText(content);
    }

    public void addHyperLinkListener(HyperlinkListener l){
        editorPane.addHyperlinkListener(l);
    }

}
