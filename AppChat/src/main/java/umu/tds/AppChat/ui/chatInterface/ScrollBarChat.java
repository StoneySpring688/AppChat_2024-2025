package umu.tds.AppChat.ui.chatInterface;

import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarChat extends JScrollBar {

    private static final long serialVersionUID = 1L;

	public ScrollBarChat() {
        setUI(new ModernScrollBarUIChat());
        setPreferredSize(new Dimension(5, 5));
        setOpaque(false);
        setUnitIncrement(20);
    }
}
