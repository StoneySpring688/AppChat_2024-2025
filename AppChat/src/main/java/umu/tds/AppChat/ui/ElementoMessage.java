package umu.tds.AppChat.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.JSeparator;

public class ElementoMessage extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private String Message;
	private String From;
	private String To;
	private JPanel panelInfo;
	
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public ElementoMessage(String Message, String From, String To) {
		setBackground(this.darkPorDefecto);
		this.setBorder(new MatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
		this.Message = Message;
		this.From = From;
		this.To = To;
		
		this.setLayout(new BorderLayout());
		this.setBorder(new LineBorder(this.darkPorDefecto, 1));
		this.setSize(240,65);
		
		panelInfo = new JPanel();
		panelInfo.setBackground(this.darkPorDefecto);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        
        JLabel lblFrom = new JLabel("From : "+ this.From);
        lblFrom.setForeground(Color.WHITE);
        JLabel lblTo = new JLabel("To : " + this.To);
        lblTo.setForeground(Color.WHITE);
        JLabel lblMessage = new JLabel(this.Message);
        lblMessage.setForeground(Color.WHITE);
        lblMessage.setSize(210, 30);

        panelInfo.add(lblFrom);
        panelInfo.add(lblTo);
        panelInfo.add(lblMessage);
        this.add(panelInfo, BorderLayout.CENTER);
        
        JSeparator separator = new JSeparator();
        add(separator, BorderLayout.SOUTH);
	}
	
	public void cambio_color(Color color) {
		this.panelInfo.setBackground(color);
		this.setBackground(color);
	}
	
}
