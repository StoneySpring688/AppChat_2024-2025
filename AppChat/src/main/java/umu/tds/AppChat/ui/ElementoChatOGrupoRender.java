package umu.tds.AppChat.ui;

import javax.swing.*;
import java.awt.*;

public class ElementoChatOGrupoRender extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof ElementoChatOGrupo) {
        	ElementoChatOGrupo ele = (ElementoChatOGrupo) value;

            // Si el elemento está seleccionado, cambia el fondo
            if (isSelected) {
                //ele.setBackground(Color.LIGHT_GRAY);
                ele.cambio_color(Color.LIGHT_GRAY);
            } else {
                //ele.setBackground(Color.GRAY);
                ele.cambio_color(Color.GRAY);
            }

            // Devuelve el elemento como componente de renderizado
            return ele;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
