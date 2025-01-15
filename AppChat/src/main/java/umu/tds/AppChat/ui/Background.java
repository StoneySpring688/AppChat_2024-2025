package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Background extends JPanel {

    private static final long serialVersionUID = 1L;

	public Background() {
        this.setOpaque(false);
        this.setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        int width = getWidth();
        int height = getHeight();
        g2.setPaint(new GradientPaint(0, 0, new Color(40, 43, 48), width, 0, new Color(64, 68, 75)));
        //g2.setPaint(new GradientPaint(0, 0, new Color(40, 43, 48), width, 0, new Color(54, 57, 63)));
        //g2.setPaint(new GradientPaint(0, 0, new Color(37, 81, 149), width, 0, new Color(9, 35, 75)));
        g2.fillRect(0, 0, width, height);
        g2.dispose();
        super.paintComponent(grphcs);
    }
}
