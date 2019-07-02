package wvustat.modules.genome;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

public class PreprocessDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private final String[] bgcorrectOptions = {"mas", "none", "rma", "rma2"};
	private final String[] normalizeOptions = {"constant", "contrasts", "invariantset", "loess", "qspline", "quantiles", "quantiles.robust"};
	private final String[] pmcorrectOptions = {"mas", "pmonly", "subtractmm"};
	private final String[] summaryOptions = {"avgdiff", "liwong", "mas", "medianpolish", "playerout"};
	
	private JComboBox bgcorrectList, normalizeList, pmcorrectList, summaryList;
	
	public static final int OK_OPTION = 0;
    public static final int CANCEL_OPTION = 1;

    private int option = CANCEL_OPTION;
    
    /**
     * constants for property 'type'
     */
    public static final int BGCORRECT = 0;
    public static final int NORMALIZE = 1;
    public static final int SUMMARY = 2;    
    
    private int type;
    private String bgcorrectMethod, normalizeMethod, pmcorrectMethod, summaryMethod;
    
    
    public PreprocessDialog(Frame frame, int type){
    	super(frame, "Preprocess Methods", true);
    	this.type = type;
    	
    	JPanel contentPane=(JPanel)this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createCenterPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);	
    }
    
    private Container createCenterPanel(){
    	JLabel label1 = new JLabel("Bg Correct: ",JLabel.LEFT);
    	JLabel label2 = new JLabel("Normalize: ",JLabel.LEFT);
    	JLabel label3 = new JLabel("PM Correct: ",JLabel.LEFT);
    	JLabel label4 = new JLabel("Summarize: ",JLabel.LEFT);
    	
    	bgcorrectList = new JComboBox(bgcorrectOptions);
     	normalizeList = new JComboBox(normalizeOptions);
    	pmcorrectList = new JComboBox(pmcorrectOptions);
    	summaryList = new JComboBox(summaryOptions);
    	
    	int n = (type == SUMMARY ? 2 : 1);
    	    	
    	JPanel panel1=new JPanel(new GridLayout(n,1,10,2));
    	JPanel panel2=new JPanel(new GridLayout(n,1,10,2));
    	
    	switch (type)
    	{
    		case BGCORRECT:
    			panel1.add(label1);
    	    	panel2.add(bgcorrectList);
    	    	break;
    		case NORMALIZE:
    			panel1.add(label2);
    	    	panel2.add(normalizeList);
    	    	break;
    		case SUMMARY:
    			panel1.add(label3);
    	    	panel2.add(pmcorrectList);
    	    	panel1.add(label4);
    	    	panel2.add(summaryList);
    	    	break;
    	}
    	    	
    	JPanel panel=new JPanel(new BorderLayout());
    	panel.add(panel1, BorderLayout.WEST);
    	panel.add(panel2, BorderLayout.CENTER);
    	
    	panel.setBorder(BorderFactory.createEmptyBorder(8,20,8,20));
    	return panel;    	
    }
    
    private Container createButtonPanel()
    {
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel panel=new JPanel(new GridLayout(1,2,30,2));

        panel.add(okButton);
        panel.add(cancelButton);
        panel.setBorder(BorderFactory.createEmptyBorder(2,20,2,20));
        return panel;
    }
    
    public void actionPerformed(ActionEvent e)
    {
    	if (e.getActionCommand() == "Ok")
        {
    		switch (type)
        	{
        		case BGCORRECT:
        			this.bgcorrectMethod = (String)bgcorrectList.getSelectedItem();
        			break;
        		case NORMALIZE:
        			this.normalizeMethod = (String)normalizeList.getSelectedItem();
        			break;
        		case SUMMARY:
        			this.pmcorrectMethod = (String)pmcorrectList.getSelectedItem();
            		this.summaryMethod   = (String)summaryList.getSelectedItem();
            		break;
        	} 		
 
    		option = OK_OPTION;
            dispose();
        }
    	else if (e.getActionCommand() == "Cancel")
        {
            option = CANCEL_OPTION;
            dispose();
        }
    }
    
    public int getOption()
    {
        return option;
    }
    
    public String getBgcorrectMethod()
    {
    	return bgcorrectMethod;
    }
    
    public String getNormalizeMethod()
    {
    	return normalizeMethod;
    }
    
    public String getPmcorrectMethod()
    {
    	return pmcorrectMethod;
    }
    
    public String getSummaryMethod()
    {
    	return summaryMethod;
    }
    
    public static void main(String[] args)
    {
        JFrame fr=new JFrame("Test");
        PreprocessDialog pd = new PreprocessDialog(fr, PreprocessDialog.SUMMARY);
        pd.pack();
        pd.show();
    }

}
