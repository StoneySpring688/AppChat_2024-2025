package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
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
import umu.tds.AppChat.controllers.UIController;
import umu.tds.AppChat.ui.chatInterface.ChatBox;
import umu.tds.AppChat.ui.chatInterface.ChatBox.BoxType;


public class SearchPanel extends PanelGrande {

	private static final long serialVersionUID = 1L;
	
	private Background fondo;
	private JPanel messagePreviewPanel;
	private JScrollPane scrollBody;
	private JTextField textFieldPhone;
	private JTextField textFieldContacto;
	private JTextArea textFieldMessage;
	private JButton SearchButton;
	private JButton exportAllPDF;
	private JButton exportListPDF;

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
		
		textFieldMessage = new JTextArea();
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
		SearchButton.addActionListener(e->doSearch(getNum(), getContact(), getMsgSubString()));
		this.fondo.add(SearchButton);
		
		messagePreviewPanel = new JPanel();
		messagePreviewPanel.setLayout(new MigLayout("wrap,fillx", "[]", "[][][][][][][][][][][][][][][][][][]"));
		messagePreviewPanel.setBackground(this.Gray);
		
        scrollBody = new JScrollPane();
        scrollBody.setViewportBorder(null);
        scrollBody.setBounds(442, 52, 439, 510);
        scrollBody.setViewportView(messagePreviewPanel);
        scrollBody.setVerticalScrollBar(new ScrollBar());
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBody.getViewport().setOpaque(false);
        scrollBody.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        fondo.add(scrollBody);
        
        exportAllPDF = new JButton("export all chats to PDF");
        exportAllPDF.setForeground(Color.WHITE);
        exportAllPDF.setBackground(new Color(241, 57, 83));
        exportAllPDF.setBounds(69, 497, 187, 35); // x = 370+(285/2)-(187/2)
		this.fondo.add(exportAllPDF);
		
		exportListPDF = new JButton("export list to PDF");
		exportListPDF.setForeground(Color.WHITE);
		exportListPDF.setBackground(new Color(241, 57, 83));
		exportListPDF.setBounds(568, 582, 187, 35); // x = (439/2-187/2)+442
		this.fondo.add(exportListPDF);
		
		//boton para limpiar la lista
		JLabel lblButtonCleanList =  new JLabel("clean list");
		lblButtonCleanList.setForeground(new Color(255, 255, 255));
		lblButtonCleanList.setBounds(464, 31, 60, 22);
		this.fondo.add(lblButtonCleanList);
		
		JLabel buttonCleanList = new JLabel();
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/Btn_FairyBook_Cancel.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH) );
		buttonCleanList.setIcon(deleteIcon);
		buttonCleanList.setBounds(442, 31, 22, 22);
		buttonCleanList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					clearPreview();
				}
			});
		UIController.addHoverEffect(buttonCleanList, 20, 20);
		this.fondo.add(buttonCleanList);
		
	}
	
	private int getNum() {
		try {
			return this.textFieldPhone.getText().isBlank() ? 0 : Integer.parseInt(this.textFieldPhone.getText());
		} catch (Exception e) {
			return 0;
		}
		
	}
	
	private String getContact() {
		return this.textFieldContacto.getText().isBlank() ? null : this.textFieldContacto.getText();
	}
	
	private String getMsgSubString() {
		return this.textFieldMessage.getText().isBlank() ? null : this.textFieldMessage.getText();
	}
	
	private void doSearch(int num, String contact, String msg) {
		UIController.doSearch(num, contact, msg);
	}
	
	public void previewMessage(ModelMessage message, BoxType type) {
		int values = scrollBody.getVerticalScrollBar().getValue();
		ChatBox msg = new ChatBox(type, message);
		
		msg.addMouseListener(new MouseAdapter() { // Eliminar el msg al hacer doble clic
	        @Override
	        public void mouseClicked(MouseEvent e) {
	        	if (e.getClickCount() == 2) {
	                messagePreviewPanel.remove(msg);
	                messagePreviewPanel.revalidate();
	                messagePreviewPanel.repaint();
	            }
	        }
	    });

        if (type == ChatBox.BoxType.LEFT) {
            messagePreviewPanel.add(msg, "width ::80%");
        } else {
        	messagePreviewPanel.add(msg, "al right,width ::80%");
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
	
	public void reset() {
		this.textFieldContacto.setText("");
		this.textFieldMessage.setText("");
		this.textFieldPhone.setText("");
		this.messagePreviewPanel.removeAll();
	}
	
	private void clearPreview() {
		this.messagePreviewPanel.removeAll();
		this.messagePreviewPanel.repaint();
		this.messagePreviewPanel.revalidate();
		
	}
	
}
