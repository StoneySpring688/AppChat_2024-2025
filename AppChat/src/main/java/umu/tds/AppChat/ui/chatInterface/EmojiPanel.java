package umu.tds.AppChat.ui.chatInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.ui.ScrollBar;

public class EmojiPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
    private int round;
    private JPanel emojiPanel;
    private JScrollPane scroll;
	
    public EmojiPanel() {
    	this.setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
    	this.setOpaque(false);
    	
    	//panel emojis
    	emojiPanel = new RoundPanel();
        emojiPanel.setLayout(new  MigLayout("wrap 6, gapx 10, gapy 100","[][][][][][]","[][]"));
        emojiPanel.setOpaque(false);
        emojiPanel.setBackground(new Color(54, 57, 63, 212));
        
        //añadir emojis al panel
        this.cargarEmojis();
        
        //añadir el panel dentro de un JScrollPane
        scroll = new JScrollPane(emojiPanel);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBar(new ScrollBar());
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setOpaque(false);

        //añadir el JScrollPane al EmojiPanel   
        this.add(scroll, "grow");
	}
    
    private void cargarEmojis() {
        String[] emojiFiles = {"HuTao1.png", "Tartaglia1.png", "Tartaglia2.png"}; // Lista de nombres de archivos

        for (String fileName : emojiFiles) {
            try {
                // Obtener el recurso desde la carpeta emojis
                URL imageUrl = getClass().getClassLoader().getResource("assets/emojis/" + fileName);
                if (imageUrl == null) {
                    System.err.println("No se encontró el archivo: " + fileName);
                    continue;
                }

                // Crear el ImageIcon
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                // Crear un botón con el icono del emoji
                JButton emojiButton = new JButton(scaledIcon);
                emojiButton.setContentAreaFilled(false);
                //emojiButton.setBorder(BorderFactory.createEmptyBorder());
                emojiButton.setBorder(BorderFactory.createLineBorder(Color.RED));
                emojiButton.setFocusable(false);

                // Añadir el botón al panel
                emojiPanel.add(emojiButton);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        URL imageUrl = getClass().getClassLoader().getResource("assets/ProfilePic.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JButton emojiButton = new JButton(scaledIcon);
        emojiButton.setContentAreaFilled(false);
        emojiButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        emojiButton.setFocusable(false);
        emojiPanel.add(emojiButton);
        
        
    }
    
	public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }
	
	@Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        g2.setPaint(new Color(255, 255, 255, 76));
        g2.fillRoundRect(0, 0, width, height, round, round);
        g2.dispose();
        super.paintComponent(grphcs);
    }
    

}
