package umu.tds.AppChat.ui.chatInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.ModelMessage;
import umu.tds.AppChat.ui.ImageAvatar;

public class ChatBox extends JComponent {

    private static final long serialVersionUID = 1L;
	private final BoxType boxType;
    private final ModelMessage message;

    public ChatBox(BoxType boxType, ModelMessage message) {
        this.boxType = boxType;
        this.message = message;
        init();
    }

    private void init() {
    	if(message.getMessage().isPresent()) {
    		initBox(message.getMessage().get());
    	}else {
    		addImageMessage(message.getEmoji().get(), boxType);
    	}
        
    }

    private void initBox(String messageText) {
        String rightToLeft = boxType == BoxType.RIGHT ? ",rtl" : "";
        setLayout(new MigLayout("inset 5" + rightToLeft, "[40!]5[]", "[top]"));
        ImageAvatar avatar = new ImageAvatar();
        avatar.setBorderSize(1);
        avatar.setBorderSpace(1);
        avatar.setImage(message.getIcon());
        JTextPane text = new JTextPane();
        text.setEditorKit(new AutoWrapText());
        text.setText(messageText);
        text.setBackground(new Color(0, 0, 0, 0));
        text.setForeground(new Color(242, 242, 242));
        text.setSelectionColor(new Color(200, 200, 200, 100));
        text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        text.setOpaque(false);
        text.setEditable(false);
        JLabel labelDate = new JLabel(message.getName() + " | " + message.getDate());
        labelDate.setForeground(new Color(127, 127, 127));
        add(avatar, "height 40,width 40");
        add(text, "gapy 20, wrap");
        add(labelDate, "gapx 20,span 2");
    }
    
    public void addImageMessage(ImageIcon image, BoxType boxType) {
    	 // Ajustar layout dependiendo del tipo de mensaje
        String rightToLeft = boxType == BoxType.RIGHT ? ",rtl" : "";
        setLayout(new MigLayout("inset 5" + rightToLeft, "[40!]5[]", "[top]"));

        // Crear JLabel para el avatar
        ImageAvatar avatar = new ImageAvatar();
        avatar.setBorderSize(1);
        avatar.setBorderSpace(1);
        avatar.setImage(message.getIcon());

        // Crear JLabel para el emoji
        JLabel emojiLabel = new JLabel(image);
        emojiLabel.setHorizontalAlignment(JLabel.CENTER);
        emojiLabel.setVerticalAlignment(JLabel.CENTER);
        emojiLabel.setBackground(new Color(0, 0, 0, 0));
        emojiLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        emojiLabel.setOpaque(false);

        // Escalar dinámicamente el tamaño del box según el emoji
        int emojiSize = Math.max(image.getIconWidth(), image.getIconHeight()) + 20;
        emojiLabel.setPreferredSize(new Dimension(emojiSize, emojiSize));

        // Crear JLabel para la fecha y nombre
        JLabel labelDate = new JLabel(message.getName() + " | " + message.getDate());
        labelDate.setForeground(new Color(127, 127, 127));

        // Añadir elementos al layout según el tipo de mensaje
        add(avatar, "height 40,width 40");
        if (boxType == BoxType.LEFT) {
            add(emojiLabel, "gapx 10, align left, wrap");
        } else {
            add(emojiLabel, "gapx 10, align right, wrap");
        }
        add(labelDate, "gapx 20, span 2");

        // Redibujar el componente
        SwingUtilities.invokeLater(() -> {
            revalidate();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        if(message.getEmoji().isEmpty()) {
        	
        	Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //establecer antialiasing (suuavizar bordes)
            int width = getWidth();
            int height = getHeight();
            
            if (boxType == BoxType.LEFT) {
            	Area area = new Area(new RoundRectangle2D.Double(25, 25, width - 25, height - 25 - 16 - 10, 5, 5));
                area.subtract(new Area(new Ellipse2D.Double(5, 5, 45, 45)));
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 94, 98, 240), width, 0, new Color(255, 153, 102, 240)));
                g2.fill(area);
            } else {
                Area area = new Area(new RoundRectangle2D.Double(0, 25, width - 25, height - 25 - 16 - 10, 5, 5));
                area.subtract(new Area(new Ellipse2D.Double(width - 50, 5, 45, 45)));
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fill(area);
            }
            g2.dispose();
            super.paintComponent(g);
        }
          
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public ModelMessage getMessage() {
        return message;
    }

    public static enum BoxType {
        LEFT, RIGHT
    }
}
