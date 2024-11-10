package umu.tds.AppChat.ui;

import javax.swing.*;
import java.awt.*;

public class ScrollBar extends JScrollBar {
    private static final long serialVersionUID = 1L;

    public ScrollBar() {
        setUI(new ModernScrollBarUI());
        setPreferredSize(new Dimension(8, 8));
        setOpaque(false);
    }
}
