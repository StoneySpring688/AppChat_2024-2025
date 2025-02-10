package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ElementoMembershipRender extends DefaultListCellRenderer{

	private static final long serialVersionUID = 1L;
	private int ratonIndex = -1;
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public ElementoMembershipRender(JList<?> list) {
        list.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index != ratonIndex) {
                    ratonIndex = index;
                    list.repaint();
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                ratonIndex = -1;
                list.repaint();
            }
        });
	}
	
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof ElementoMembership) {
        	ElementoMembership ele = (ElementoMembership) value;

            if (index == ratonIndex) {
                ele.cambio_color(Color.LIGHT_GRAY);
                //ele.cambio_color(Color.DARK_GRAY);  // TODO decidir que hacer con esto (no me convence)
            } else {
                ele.cambio_color(this.darkPorDefecto);
            }

            return ele;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
	
}
