package wvustat.modules;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import wvustat.swing.DoubleField;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
import java.text.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Jun 12, 2005
 * Time: 2:50:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PolynomialProbInputPanel extends JPanel {
    public static final int OK_RESULT=0;
    public static final int CANCEL_RESULT=1;
    private int totalLevels;
    private String[] levelNames;
    private DoubleField[] inputFields;
    private ActionListener actionListener = new ActionHandler();
    private JButton okButton, cancelButton;
    private int result;
    private KeyHandler keyHandler=new KeyHandler();

    public PolynomialProbInputPanel(String[] levelnames) {
        this.levelNames = levelnames;
        totalLevels = levelnames.length;
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private Container createInputPanel() {
        inputFields = new DoubleField[totalLevels];
        for (int i = 0; i < levelNames.length; i++) {
            inputFields[i] = new DoubleField(8);
            if(i<levelNames.length-1)
                inputFields[i].addKeyListener(keyHandler);
        }
        inputFields[totalLevels - 1].setEnabled(false);

        JPanel panel = new JPanel();
        RowSpec[] rowSpecs = new RowSpec[2 * totalLevels + 1];
        for (int i = 0; i < rowSpecs.length; i++) {
            if (i % 2 == 0)
                rowSpecs[i] = new RowSpec("pref");
            else
                rowSpecs[i] = new RowSpec("2dlu");
        }
        ColumnSpec[] colSpecs = new ColumnSpec[3];
        colSpecs[0] = new ColumnSpec("pref");
        colSpecs[1] = new ColumnSpec("4dlu");
        colSpecs[2] = new ColumnSpec("30dlu:g");

        FormLayout formLayout = new FormLayout(colSpecs, rowSpecs);
        panel.setLayout(formLayout);
        CellConstraints cc = new CellConstraints();
        panel.add(new JLabel("Level"), cc.xy(1, 1));
        panel.add(new JLabel("Hyp Prob"), cc.xy(3, 1));
        for (int i = 0; i < totalLevels; i++) {
            panel.add(new JLabel(levelNames[i]), cc.xy(1, 2 * i + 3));
            panel.add(inputFields[i], cc.xy(3, 2 * i + 3));
        }
        Border border=BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(4,4,4,4));
        panel.setBorder(border);
        return panel;
    }

    private Container createButtonPanel() {
        JPanel panel = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener(actionListener);
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(actionListener);
        okButton.setPreferredSize(cancelButton.getPreferredSize());
        panel.add(okButton);
        panel.add(cancelButton);
        return panel;

    }

    private void dispose() {
        JDialog dialog = (JDialog) SwingUtilities.getAncestorOfClass(JDialog.class, PolynomialProbInputPanel.this);
        dialog.dispose();
    }

    private List validateInput(){
        double sum=0;
        List errors=new ArrayList();
        for(int i=0;i<inputFields.length-1;i++){
            try{
                double d=Double.parseDouble(inputFields[i].getText());
                sum+=d;
                if(d>=1){
                    errors.add("Invalid probability for category '"+levelNames[i]+"'!");
                    break;
                }

            }
            catch(NumberFormatException e){
                errors.add("Invalid probability for category '"+levelNames[i]+"'!");
            }
        }
        if(sum>1)
            errors.add("The sum of probabilities must be equal to 100%!");
        return errors;
    }

    public double[] getHypoProbs(){
        double[] array=new double[levelNames.length];
        double sum=0;
        for(int i=0;i<inputFields.length-1;i++){
            array[i]=Double.parseDouble(inputFields[i].getText());
            sum+=array[i];
        }
        array[array.length-1]=1-sum;
        return array;
    }

    public int showDialog(Component root, String title){
        result=CANCEL_RESULT;
        Frame f=(Frame)SwingUtilities.getAncestorOfClass(Frame.class, root);
        JDialog dialog=new JDialog(f, title, true);
        this.setBorder(BorderFactory.createEmptyBorder(8,10,4,10));
        dialog.getContentPane().add(this);
        dialog.getRootPane().setDefaultButton(okButton);
        dialog.pack();
        dialog.setLocationRelativeTo(f);
        dialog.show();

        return result;
    }

    private class ActionHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("OK")) {
                List errors=validateInput();
                if(errors.size()>0){
                    StringBuffer buffer=new StringBuffer("Error\n");
                    for(int i=0;i<errors.size();i++){
                        buffer.append(errors.get(i)).append("\n");
                    }
                    JOptionPane.showMessageDialog(PolynomialProbInputPanel.this, buffer.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                result=OK_RESULT;
                dispose();

            } else if (evt.getActionCommand().equals("CANCEL")) {
                result=CANCEL_RESULT;
                dispose();
            }
        }
    }

    private class KeyHandler extends KeyAdapter{
        public void keyReleased(KeyEvent event) {
            char keyChar=event.getKeyChar();
            if(!Character.isDigit(keyChar) && keyChar!='.')
                return;

            double sum=0;
            for(int i=0;i<inputFields.length-1;i++){
                try{
                    double value=Double.parseDouble(inputFields[i].getText());
                    sum+=value;
                }
                catch(NumberFormatException ex){

                }
                if(sum>1)
                    inputFields[i].setForeground(Color.red);
                else
                    inputFields[i].setForeground(Color.black);
            }
            if(sum<1){
                NumberFormat nf=NumberFormat.getInstance();
                inputFields[inputFields.length-1].setText(nf.format(1-sum));
            }
        }
    }
}
