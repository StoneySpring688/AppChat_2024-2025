package umu.tds.AppChat.ui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.ModelMessage;
import umu.tds.AppChat.ui.chatInterface.ChatBox;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;


public class SearchPanel extends PanelGrande {

	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private JPanel messagePreviewPanel;
	private JScrollPane scrollBody;
	private JTextField textFieldPhone;
	private JTextField textFieldContacto;
	private JButton SearchButton;

	private final Color Gray = new Color(64, 68, 75);

	public SearchPanel() {
		
		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 660);
		this.add(fondo);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(20, 130, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		//textFieldPhone.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.fondo.add(textFieldPhone);
		textFieldPhone.setColumns(10);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(20, 112, 50, 20);
		lblPhone.setForeground(Color.WHITE);
		this.fondo.add(lblPhone);
		
		textFieldContacto = new JTextField();
		textFieldContacto.setBounds(20, 220, 285, 30);
		textFieldContacto.setBackground(this.Gray);
		textFieldContacto.setCaretColor(Color.WHITE);
		textFieldContacto.setForeground(Color.WHITE);
		//textFieldContacto.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		this.fondo.add(textFieldContacto);
		textFieldContacto.setColumns(10);
		
		JLabel lblContacto = new JLabel("Contacto");
		lblContacto.setBounds(20, 202, 60, 20);
		lblContacto.setForeground(Color.WHITE);
		this.fondo.add(lblContacto);
		
		JTextArea textFieldMessage = new JTextArea();
		textFieldMessage.setBackground(this.Gray);
		textFieldMessage.setCaretColor(Color.WHITE);
		textFieldMessage.setForeground(Color.WHITE);
		//textFieldMessage.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		textFieldMessage.setLineWrap(true);
		textFieldMessage.setWrapStyleWord(true);
		textFieldMessage.setColumns(10);
		textFieldMessage.setBounds(20, 320, 285, 70);
		JScrollPane scrollPaneMessage = new JScrollPane(textFieldMessage);
		scrollPaneMessage.setBounds(20, 302, 285, 100);
		scrollPaneMessage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPaneMessage.setVerticalScrollBar(new ScrollBar());
		scrollPaneMessage.setHorizontalScrollBar(new ScrollBar());
		this.fondo.add(scrollPaneMessage);
		
		JLabel lblMessage = new JLabel("Message");
		lblMessage.setBounds(20, 282, 60, 20);
		lblMessage.setForeground(Color.WHITE);
		this.fondo.add(lblMessage);
		
		SearchButton = new JButton("Search");
		SearchButton.setForeground(Color.WHITE);
		SearchButton.setBackground(new Color(241, 57, 83));
		SearchButton.setBounds(69, 450, 187, 35); // x = 370+(285/2)-(187/2)
		this.fondo.add(SearchButton);
		
		messagePreviewPanel = new JPanel();
		messagePreviewPanel.setLayout(new MigLayout("wrap,fillx", "[]", "[][][][][][][][][][][][][][][][][][]"));
		messagePreviewPanel.setBackground(this.Gray);
		
        scrollBody = new JScrollPane();
        scrollBody.setViewportBorder(null);
        scrollBody.setBounds(442, 112, 439, 510);
        scrollBody.setViewportView(messagePreviewPanel);
        scrollBody.setVerticalScrollBar(new ScrollBar());
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBody.getViewport().setOpaque(false);
        scrollBody.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        fondo.add(scrollBody);
		
	}
	
	public void previewMessage(ModelMessage message, BoxType type) {
		int values = scrollBody.getVerticalScrollBar().getValue();
        if (type == ChatBox.BoxType.LEFT) {
            messagePreviewPanel.add(new ChatBox(type, message), "width ::80%");
        } else {
        	messagePreviewPanel.add(new ChatBox(type, message), "al right,width ::80%");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	messagePreviewPanel.revalidate();
                scrollBody.getVerticalScrollBar().setValue(values);
            }
        });
        messagePreviewPanel.repaint();
        messagePreviewPanel.revalidate();
        scrollBody.revalidate();
    }
	
}
