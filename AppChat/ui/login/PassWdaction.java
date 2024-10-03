package login;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JPasswordField;
import javax.swing.JRadioButton;

public class PassWdaction implements ItemListener {
	JRadioButton rb;
	JPasswordField passWd;

	public PassWdaction(JRadioButton rb, JPasswordField passWd) {
		this.rb = rb;
		this.passWd = passWd;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(this.rb.isSelected()) {
			hidePassWd();
		}else {
			showPassWd();
		}
		
	}
	
	private void hidePassWd() {
		this.passWd.setEchoChar((char)0);
	}
	private void showPassWd() {
		this.passWd.setEchoChar('•');
	}

}
