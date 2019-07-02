/*
 * Launcher.java
 *
 * Created on March 20, 2002, 2:10 PM
 */

package wvustat.simulation.launcher;

import wvustat.math.UI.Grapher;
import wvustat.simulation.boxmodel.BoxModelPanel;
import wvustat.simulation.chisquare.ChiSquare;
import wvustat.simulation.correlation.Testcorrcoeff;
import wvustat.simulation.fivestep.FiveStep;
import wvustat.simulation.poker.Poker;
import wvustat.swing.*;
import wvustat.table.MainPanel;
import wvustat.util.AppFrame;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

/**
 *
 * @author  hxue
 * @version
 */
public class Launcher extends AppFrame implements java.awt.event.ActionListener, TreeSelectionListener, java.awt.event.MouseListener
{
    private JTree tree;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JSplitPane splitPane;
    private JMenuBar menuBar=new JMenuBar();

    /** Creates new Launcher */
    public Launcher()
    {
        super("myJavaStat (v. 0.6)");
        setJMenuBar(menuBar);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int thisW = 900;
        int thisH = 680;
        this.setBounds(d.width / 2 - thisW / 2, d.height / 2 - thisH / 2, thisW, thisH);

        initComponent();

    }

    private void initComponent()
    {
        IconTreeNode root = new IconTreeNode("Chapter Companion", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode p1 = new IconTreeNode("Part I  Describing Data", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode p2 = new IconTreeNode("Part II Probability Modeling and Statistical Inference", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode p3 = new IconTreeNode("Part III  Statistical Inference: Estimation and Hypothesis testing", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap1 = new IconTreeNode("Chap 1 Describing Data", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap2 = new IconTreeNode("Chap 2 Summarizing Data", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap3 = new IconTreeNode("Chap 3 Linear Relationships", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap4 = new IconTreeNode("Chap 4 Probability & Simulation", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap5 = new IconTreeNode("Chap 5 Expected Values", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap6 = new IconTreeNode("Chap 6 Probability Distributions", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap7 = new IconTreeNode("Chap 7 Obtaining Data", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap8 = new IconTreeNode("Chap 8 Confidence Intervals", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap9 = new IconTreeNode("Chap 9 Hypothesis Testing", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap10 = new IconTreeNode("Chap 10 Chi-square Testing", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);
        IconTreeNode chap11 = new IconTreeNode("Chap 11 Regression", IconTreeNode.FOLDER, ImageIcons.FOLDER, true);

        chap5.insert(new IconTreeNode("Five-step Method", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap6.insert(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap8.insert(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap8.insert(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 1);
        chap9.insert(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap9.insert(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 1);

        chap10.insert(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap10.insert(new IconTreeNode("Chi Square", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 1);
        chap10.insert(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 2);

        chap11.insert(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 0);
        chap11.insert(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), 1);

        DefaultTreeModel treeModel = new DefaultTreeModel(root, true);
        treeModel.insertNodeInto(p1, root, 0);
        treeModel.insertNodeInto(p2, root, 1);
        treeModel.insertNodeInto(p3, root, 2);
        treeModel.insertNodeInto(chap1, p1, 0);
        treeModel.insertNodeInto(chap2, p1, 1);
        treeModel.insertNodeInto(chap3, p1, 2);
        treeModel.insertNodeInto(chap4, p2, 0);
        p2.insert(chap5, 1);
        p2.insert(chap6, 2);
        p2.insert(chap7, 3);
        p2.insert(chap8, 4);
        p2.insert(chap9, 5);
        p2.insert(chap10, 6);

        p3.insert(chap11, 0);

        treeModel.insertNodeInto(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap1, 0);
        treeModel.insertNodeInto(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap1, 1);
        treeModel.insertNodeInto(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap2, 0);
        treeModel.insertNodeInto(new IconTreeNode("Glossay", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap2, 1);
        treeModel.insertNodeInto(new IconTreeNode("Data", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap3, 0);
        treeModel.insertNodeInto(new IconTreeNode("Correlation", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap3, 1);
        treeModel.insertNodeInto(new IconTreeNode("Grapher", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap3, 2);
        treeModel.insertNodeInto(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap3, 3);
        treeModel.insertNodeInto(new IconTreeNode("Box Model", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap4, 0);
        treeModel.insertNodeInto(new IconTreeNode("Five-step Method", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap4, 1);
        treeModel.insertNodeInto(new IconTreeNode("Poker", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap4, 2);
        treeModel.insertNodeInto(new IconTreeNode("Glossary", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), chap4, 3);

        treeModel.insertNodeInto(new IconTreeNode("Choose a Topic", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), root, 3);
        treeModel.insertNodeInto(new IconTreeNode("Help", IconTreeNode.FOLDER, ImageIcons.CONTENT, false), root, 4);

        tree = new JTree(treeModel);
        tree.setCellRenderer(new DefaultTreeRenderer());
        //tree.setRootVisible(false);
        tree.expandRow(0);
        tree.setShowsRootHandles(true);
        tree.putClientProperty("JTree.lineStyle", "Angled");
        //tree.setCellRenderer(new MyTreeCellRenderer());
        tree.getSelectionModel().addTreeSelectionListener(this);

        leftPanel = new JPanel(new StackLayout());
        leftPanel.setBackground(Color.white);

        JLabel title = new JLabel("  Chapter Companion");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(title);
        leftPanel.add(tree);
        leftPanel.setMinimumSize(new Dimension(200, 300));
        leftPanel.setPreferredSize(new Dimension(200, 300));
        leftPanel.setMaximumSize(new Dimension(200, 300));

        JLabel label2 = new JLabel("Choose a Topic");
        label2.setFont(new Font("Dialog", Font.PLAIN, 14));
        label2.setForeground(Color.blue);
        label2.setBorder(new javax.swing.border.EmptyBorder(0, 16, 0, 0));
        JLabel label3 = new JLabel("Help");
        label3.setFont(new Font("Dialog", Font.PLAIN, 14));
        label3.setForeground(Color.blue);
        label3.setBorder(new javax.swing.border.EmptyBorder(0, 16, 0, 0));

        label2.addMouseListener(this);
        label3.addMouseListener(this);
        leftPanel.add(new JLabel("   "));

        leftPanel.add(label2);
        leftPanel.add(label3);

        JScrollPane jsp = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setMinimumSize(new Dimension(180, 600));

        rightPanel = new RightPanel(600, 600);
        rightPanel.setLayout(new GridBagLayout());
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsp, rightPanel);
        splitPane.setDividerLocation(185);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        getContentPane().add(JProgressMonitor.getInstance(), BorderLayout.SOUTH);
    }

    public static void main(String[] args)
    {
        UIManager.put("Table.focusCellBackground", Color.white);
        UIManager.put("Table.focusCellForeground", Color.black);

        Launcher launcher = new Launcher();
        launcher.show();

    }

    public void actionPerformed(final java.awt.event.ActionEvent p1)
    {
        String cmd = p1.getActionCommand();

        if (cmd.equals("NATIVE"))
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            }
            catch (Exception e)
            {

            }

        }
        else if (cmd.equals("METAL"))
        {
            try
            {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            }
            catch (Exception e)
            {

            }
        }
        else if (cmd.equals("MOTIF"))
        {
            try
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                SwingUtilities.updateComponentTreeUI(this);
            }
            catch (Exception e)
            {

            }
        }
        else if (cmd.equals("Exit"))
        {
            System.exit(0);
        }
    }

    public void valueChanged(TreeSelectionEvent e)
    {
        TreePath path = e.getNewLeadSelectionPath();
        if (path == null)
        {
            return;
        }

        IconTreeNode node = (IconTreeNode) path.getLastPathComponent();
        String label = node.getUserObject().toString();
        loadModule(label);
    }

    private void loadModule(final String moduleName)
    {
        this.getJMenuBar().removeAll();
        JProgressMonitor.getInstance().startIndeterminateProgress("Loading " + moduleName);

        wvustat.swing.SwingWorker worker = new wvustat.swing.SwingWorker()
        {
            Component component = null;

            public Object construct()
            {

                if (moduleName.equals("Box Model"))
                {
                    BoxModelPanel boxModel = new BoxModelPanel();
                    //boxModel.init();
                    component = boxModel;
                }
                else if (moduleName.equals("Five-step Method"))
                {
                    FiveStep fStep = new FiveStep();
                    //fStep.init();
                    component = fStep;
                }
                else if (moduleName.equals("Data"))
                {
                    MainPanel mp = new MainPanel();
                    //mp.init();
                    component = mp;
                }
                else if (moduleName.equals("Grapher"))
                {
                    Grapher gr = new Grapher(false);
                    gr.init();
                    component = gr;
                }
                else if (moduleName.equals("Poker"))
                {
                    Poker poker = new Poker();
                    //poker.init();
                    component = poker;
                }
                else if (moduleName.equals("Correlation"))
                {
                    Testcorrcoeff corr = new Testcorrcoeff();
                    //corr.init();
                    component = corr;
                }
                else if (moduleName.equalsIgnoreCase("CHI SQUARE"))
                {
                    ChiSquare chiSquare = new ChiSquare();
                    //chiSquare.init();
                    component = chiSquare;
                }
                return component;
            }

            public void finished()
            {
                JProgressMonitor.getInstance().stopIndeterminateProgress();
                if (component == null)
                {
                    rightPanel.removeAll();
                }
                else
                {
                    rightPanel.removeAll();
                    rightPanel.add(component, new GridBagConstraints(0, 0, 0, 0, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
                    if(component instanceof LauncherPlugin)
                        ((LauncherPlugin)component).buildMenu(Launcher.this.getJMenuBar());
                }
                Launcher.this.getRootPane().revalidate();
            }
        };

        worker.start();
    }

    public void mouseReleased(java.awt.event.MouseEvent p1)
    {
    }

    public void mouseEntered(java.awt.event.MouseEvent p1)
    {
    }

    public void mouseClicked(java.awt.event.MouseEvent p1)
    {
        JLabel label = (JLabel) p1.getSource();
        String text = label.getText();

        splitPane.setRightComponent(rightPanel);
        if (text.equals("Help"))
        {
        }
        else if (text.equals("Choose a Topic"))
        {
        }
        else if (text.equals("Appendices"))
        {
        }

    }

    public void mousePressed(java.awt.event.MouseEvent p1)
    {
    }

    public void mouseExited(java.awt.event.MouseEvent p1)
    {
    }

    static class RightPanel extends JPanel
    {
        private int width, height;

        public RightPanel(int width, int height)
        {
            this.width = width;
            this.height = height;

        }

        public Dimension getMaximumSize()
        {
            return getPreferredSize();
        }

        public Dimension getMinimumSize()
        {
            return getPreferredSize();
        }

        public Dimension getPreferredSize()
        {
            return new Dimension(width, height);
        }
    }

}
