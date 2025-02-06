package umu.tds.AppChat.ui.chatInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.Emoji;
import umu.tds.AppChat.ui.ScrollBar;

public class EmojiPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
    private int round;
    private JPanel emojiPanel;
    private JScrollPane scroll;
    private List<Emoji> emojiList;
    private List<EmojiClickListener> listeners = new ArrayList<>();
	
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
    	
    	this.emojiList = new ArrayList<Emoji>(Emoji.emojiList);
    	
    	//System.out.println("lista_global " + Emoji.emojiList.size() + "lista_local "+ this.emojiList.size());
    	
    	List<Emoji> emojiButtons = new ArrayList<>(); //buffer
    	//int id = 0;
        for (Emoji emojiButton : emojiList) {
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
                    for (EmojiClickListener listener : listeners) {
                        listener.emojiClicked((ImageIcon) emojiButton.getIcon(), emojiButton.getId()); // Notifica a cada listener del evento
                    }
                }
            });

            
            emojiButtons.add(emojiButton); //añadir al buffer
            
            //id++;
                
        }
        
     // Añadir los emojis del buffer
        SwingUtilities.invokeLater(() -> {
        	for (Emoji button : emojiButtons) {
        		emojiPanel.add(button);
        	}
        	emojiPanel.revalidate();
        	emojiPanel.repaint();
        });
        
    }
    
	public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        repaint();
    }
	
    public void addEmojiClickListener(EmojiClickListener listener) {
        listeners.add(listener);
    }

    public void removeEmojiClickListener(EmojiClickListener listener) {
        listeners.remove(listener);
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
