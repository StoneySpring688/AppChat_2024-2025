package umu.tds.AppChat.ui.chatInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.ui.ScrollBar;

public class EmojiPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
    private int round;
    private JPanel emojiPanel;
    private JScrollPane scroll;
    String[] emojiFiles = {
    		"HuTao1.png", "HuTao2.png", "HuTao3.png", "HuTao4.png", 
    		"Tartaglia1.png", "Tartaglia2.png", "Tartaglia3.png",
    		"Xiao1.png", "Xiao2.png",
    		"Yanfei1.png", "Yanfei2.png", "Yanfei3.png",
    		"Barbara1.png", "Barbara2.png", "Barbara3.png",
    		"Clara1.png",
    		"Huohuo1.png", "Huohuo2.png",
    		"Acheron1.png", "Acheron2.png",
    		"Luka1.png", 
    		"March7th1.png", "March7th2.png", "March7th3.png", "March7th4.png", "March7th5.png",  "March7th6.png", "March7th7.png"
    		}; // TODO pasar a config
	
    public EmojiPanel() {
    	this.setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
    	this.setOpaque(false);
    	
    	//panel emojis
    	emojiPanel = new RoundPanel();
        emojiPanel.setLayout(new  MigLayout("wrap 7, gapx 10, gapy 20","50[][][][][][][]50","[][]"));
        emojiPanel.setOpaque(false);
        emojiPanel.setBackground(new Color(54, 57, 63, 212));
        
        //añadir emojis al panel
        this.cargarEmojisAsync();
        
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
    
    private void cargarEmojisAsync() {
        new Thread(() -> cargarEmojis()).start();
    }
    
    private void cargarEmojis() {
    	
    	List<Button> emojiButtons = new ArrayList<>(); //buffer
        for (String fileName : emojiFiles) {
        	ImageIcon icon = cargarIconoEmoji(fileName);
        	if (icon == null) continue;
                
            // Crear un botón con el icono del emoji
            Button emojiButton = new Button();
            emojiButton.setIcon(icon);
            emojiButton.setContentAreaFilled(false);
            emojiButton.setBorder(BorderFactory.createEmptyBorder());
            //emojiButton.setBorder(BorderFactory.createLineBorder(Color.RED)); //debug
            emojiButton.setFocusable(false);
            emojiButton.applyEffectSettings(0.2f, 600, 0.7, new Color(200, 200, 200, 100), 0.2f, 0.2f);
            emojiButton.setRound(20);
            
            //funcionalidad
            emojiButton.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				System.out.println("TODO emoji");
    			}
    		});
            
            emojiButtons.add(emojiButton); //añadir al buffer
                
        }
        
     // Añadir los emojis del buffer
        SwingUtilities.invokeLater(() -> {
        	for (Button button : emojiButtons) {
        		emojiPanel.add(button);
        	}
        	emojiPanel.revalidate();
        	emojiPanel.repaint();
        });
        
    }
    
    private ImageIcon cargarIconoEmoji(String fileName) {
        try {
            // Obtener el recurso desde la carpeta "emojis"
            URL imageUrl = getClass().getClassLoader().getResource("emojis/" + fileName);
            if (imageUrl == null) {
                //System.err.println("No se encontró el archivo: " + fileName); //debug
                return null;
            }

            // Crear el ImageIcon
            ImageIcon icon = new ImageIcon(imageUrl);
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            //System.out.println("Archivo cargado exitosamente: " + fileName); //debug
            return scaledIcon;
        } catch (Exception e) {
            //System.err.println("Error al cargar el archivo: " + fileName); //debug
            e.printStackTrace();
            return null;
        }
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
