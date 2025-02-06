package umu.tds.AppChat.ui.chatInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javaswingdev.FontAwesome;
import javaswingdev.FontAwesomeIcon;
import javaswingdev.GoogleMaterialDesignIcon;
import javaswingdev.GoogleMaterialIcon;
import javaswingdev.GradientType;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.ui.ElementoChatOGrupo;
import umu.tds.AppChat.ui.ImageAvatar;

public class ChatArea extends JPanel {

    private static final long serialVersionUID = 1L;
    
	private AnimationScroll animationScroll;
    private AnimationFloatingButton animationFloatingButton;
    private List<ChatEvent> events = new ArrayList<>();
    private MigLayout layout;
    private MigLayout layoutLayered;
    private JLayeredPane layeredPane;
    private JPanel header;
    private JPanel body;
    private JPanel bottom;
    private TextField textMessage;
    private JScrollPane scrollBody;
    private Button floatingButton;
    private JLabel headerUserName;
    private ImageAvatar headerAvatar;
    private EmojiPanel emojiPanel;
    private ElementoChatOGrupo currentChat;
    private boolean emojiPanelVisibility;

    public void addChatEvent(ChatEvent event) {
        events.add(event);
    }

    public ChatArea(EmojiPanel emojiPanel) {
        init(emojiPanel);
        initAnimator();
    }

    private void init(EmojiPanel emojiPanel) {
    	setLayout(null);
    	setOpaque(false);
    	
    	this.emojiPanel = emojiPanel;
    	emojiPanel.setSize(850, 244);
    	emojiPanel.setLocation(30, 345);
    	this.setEmojiPanelVisibility(false);
    	this.add(emojiPanel);
    	
    	JPanel principal = new JPanel();
    	principal.setBounds(0, 0, 910, 650);
    	principal.setOpaque(false);
    	this.add(principal);
    	
        layout = new MigLayout("fill, wrap, inset 0", "[fill]", "[fill,40!][fill, 100%][shrink 0,::30%]");
        header = createHeader();
        body = createBody();
        bottom = createBottom();
        layeredPane = createLayeredPane();
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        scroll.setViewportBorder(null);
        scrollBody = scroll;
        scrollBody.setViewportView(body);
        scrollBody.setVerticalScrollBar(new ScrollBarChat());
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBody.getViewport().setOpaque(false);
        scrollBody.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private int oldValues;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int value = scrollBody.getVerticalScrollBar().getValue();
                int extent = scrollBody.getVerticalScrollBar().getModel().getExtent();
                if ((value + extent) >= scrollBody.getVerticalScrollBar().getMaximum() - 300) {
                    animationFloatingButton.hide();
                } else if (oldValues <= e.getValue()) {
                    if (!animationScroll.isRunning()) {
                        animationFloatingButton.show();
                    }
                }

            }
        });
        floatingButton = createFloatingButton();
        layeredPane.setLayer(floatingButton, JLayeredPane.POPUP_LAYER);
        layeredPane.add(floatingButton, "pos 100%-50 100%,h 40,w 40");
        layeredPane.add(scrollBody);
        principal.setLayout(layout);
        principal.add(header);
        principal.add(layeredPane);
        principal.add(bottom);
    }

    private void initAnimator() {
        animationScroll = new AnimationScroll(body);
        animationFloatingButton = new AnimationFloatingButton(layoutLayered, floatingButton);
    }

    private JPanel createHeader() {
        RoundPanel panel = new RoundPanel();
        panel.setLayout(new MigLayout("align left","[][]"));
        panel.setBackground(new Color(255, 255, 255, 20));
        
        //ImageIcon headerDefaultIcon = new ImageIcon(getClass().getResource("/assets/ProfilePic.png"));
        headerAvatar = new ImageAvatar();
        headerAvatar.setBorderSize(1);
        headerAvatar.setBorderSpace(1);
        //headerAvatar.setImage(headerIcon);
        
        headerUserName = new JLabel("headerUserName");
        headerUserName.setFont(headerUserName.getFont().deriveFont(14f));
        headerUserName.setBorder(new EmptyBorder(2, 10, 2, 2));
        headerUserName.setForeground(new Color(240, 240, 240));
        
        panel.add(headerAvatar, "height 40,width 40");
        panel.add(headerUserName);
        return panel;
    }

    private JPanel createBody() {
        RoundPanel panel = new RoundPanel();
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setLayout(new MigLayout("wrap,fillx", "[]", "[][][][][][][][][][][][][][][][][][]"));
        return panel;
    }

    private JPanel createBottom() {
        RoundPanel panel = new RoundPanel();
        panel.setBackground(new Color(255, 255, 255, 20));
        panel.setLayout(new MigLayout("fill, inset 2", "[fill,34!]2[fill]2[fill,34!]", "[bottom]"));
        //GoogleMaterialIcon iconFile = new GoogleMaterialIcon(GoogleMaterialDesignIcon.ATTACH_FILE, GradientType.VERTICAL, new Color(210, 210, 210), new Color(255, 255, 255), 20);
        GoogleMaterialIcon iconSend = new GoogleMaterialIcon(GoogleMaterialDesignIcon.SEND, GradientType.VERTICAL, new Color(0, 133, 237), new Color(90, 182, 255), 20);
        GoogleMaterialIcon iconEmot = new GoogleMaterialIcon(GoogleMaterialDesignIcon.INSERT_EMOTICON, GradientType.VERTICAL, new Color(210, 210, 210), new Color(255, 255, 255), 20);
        Button cmdEmoji = new Button();
        Button cmdSend = new Button();
        cmdEmoji.setFocusable(false);
        cmdSend.setFocusable(false);
        cmdEmoji.setIcon(iconEmot.toIcon());
        cmdSend.setIcon(iconSend.toIcon());
        textMessage = new TextField();
        textMessage.setHint("Escribe un Mensaje ...");
        textMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                revalidate();
            }

            @Override
            public void keyTyped(KeyEvent ke) {
                runEventKeyTyped(ke);
            }
        });
        cmdSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runEventMousePressedSendButton(e);
            }
        });
        cmdEmoji.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runEventMousePressedEmojiButton(e);
            }
        });
        //JScrollPane scroll = createScroll();
        JScrollPane scrollBarraBotton = new JScrollPane();
        scrollBarraBotton.setBorder(null);
        scrollBarraBotton.setViewportBorder(null);

        scrollBarraBotton.setViewportView(textMessage);
        scrollBarraBotton.getViewport().setOpaque(false);
        scrollBarraBotton.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollBarraBotton.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(cmdEmoji, "height 34!");
        panel.add(scrollBarraBotton); 
        panel.add(cmdSend, "height 34!");
        return panel;
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layer = new JLayeredPane();
        layoutLayered = new MigLayout("fill,inset 0", "[fill]", "[fill]");
        layer.setLayout(layoutLayered);
        return layer;
    }

    private Button createFloatingButton() {
        Button button = new Button();
        button.setBorder(null);
        FontAwesomeIcon icon = new FontAwesomeIcon(FontAwesome.ANGLE_DOWN, GradientType.VERTICAL, new Color(79, 79, 79, 240), new Color(248, 248, 248, 240), 35);
        button.setIcon(icon.toIcon());
        button.setRound(40);
        button.setBackground(new Color(100, 100, 100, 100));
        button.setPaintBackground(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationScroll.scrollVertical(scrollBody, scrollBody.getVerticalScrollBar().getMaximum());
            }
        });
        return button;
    }

    public void addChatBox(ModelMessage message, ChatBox.BoxType type) {
        int values = scrollBody.getVerticalScrollBar().getValue();
        if (type == ChatBox.BoxType.LEFT) {
            body.add(new ChatBox(type, message), "width ::80%");
        } else {
            body.add(new ChatBox(type, message), "al right,width ::80%");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                body.revalidate();
                scrollBody.getVerticalScrollBar().setValue(values);
                bottom.revalidate();
            }
        });
        body.repaint();
        body.revalidate();
        scrollBody.revalidate();
        scrollToBottom();
    }

    public void clearChatBox() {
        body.removeAll();
        body.repaint();
        body.revalidate();
    }

    private void scrollToBottom() {
        animationScroll.scrollVertical(scrollBody, scrollBody.getVerticalScrollBar().getMaximum());
    }

    private void runEventMousePressedSendButton(ActionEvent evt) {
        for (ChatEvent event : events) {
            event.mousePressedSendButton(evt);
        }
    }

    private void runEventMousePressedEmojiButton(ActionEvent evt) {
        for (ChatEvent event : events) {
            event.mousePressedEmojiButton(evt);
        }
        //showHideEmojiPanel();
    }

    private void runEventKeyTyped(KeyEvent evt) {
        for (ChatEvent event : events) {
            event.keyTyped(evt);
        }
    }
    
    private void setEmojiPanelVisibility(boolean visibility) {
    	this.emojiPanelVisibility = visibility;
    	emojiPanel.setVisible(visibility);
    	emojiPanel.repaint();
    	emojiPanel.revalidate();
    }
    
    protected void showHideEmojiPanel() {
    	this.setEmojiPanelVisibility(!this.getEmojiPanelVisibility());
    }

    public String getText() {
        return textMessage.getText();
    }

    public void setUserName(String title) {
    	headerUserName.setText(title);
    }

    public String getUserName() {
        return headerUserName.getText();
    }

    public void setText(String text) {
        textMessage.setText(text);
    }

    public void textGrabFocus() {
        textMessage.grabFocus();
    }

    public void clearTextAndGrabFocus() {
        textMessage.setText("");
        textMessage.grabFocus();
    }
    
    public void resetText() {
        textMessage.setText("");
    }
    
    private boolean getEmojiPanelVisibility() {
    	return this.emojiPanelVisibility;
    }
    
    protected ElementoChatOGrupo getCurrentChat() {
    	return this.currentChat;
    }
    
    protected Icon getUserAvatar() {
    	return this.headerAvatar.getImage();
    }
    
    protected void setUserAvatar(ImageIcon image) {
    	this.headerAvatar.setImage(image);
    }
    
    protected void setCurrentChat(ElementoChatOGrupo chat) {
    	this.currentChat = chat;
    	
    	ImageIcon headerIcon = new ImageIcon(chat.getProfilePic().getImage());
        setUserAvatar(headerIcon);      
        
        String nombreOId = chat.getNombre() != null ? chat.getNombre() : Integer.toString(chat.getNumero());
        setUserName(nombreOId);
        
        
        header.repaint();
        header.revalidate();
    }
    
}
