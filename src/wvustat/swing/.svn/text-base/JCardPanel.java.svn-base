package wvustat.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: hengyi
 * Date: Aug 15, 2004
 * Time: 2:19:47 PM
 * To change this template use Options | File Templates.
 */
public class JCardPanel extends JPanel
{
    private static final String VISIBLE_PANEL_KEY="0";
    private static final String INVISIBLE_PANEL_KEY="1";
    private CardLayout cardLayout=new CardLayout();

    public JCardPanel(Container visiblePanel)
    {
        super();
        setLayout(cardLayout);
        add(visiblePanel, VISIBLE_PANEL_KEY);
        add(new JPanel(), INVISIBLE_PANEL_KEY);
    }

    public void showVisiblePanel()
    {
        cardLayout.show(this, VISIBLE_PANEL_KEY);
    }

    public void hideVisiblePanel()
    {
        cardLayout.show(this, INVISIBLE_PANEL_KEY);
    }
}
