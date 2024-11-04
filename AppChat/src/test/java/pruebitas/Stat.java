package pruebitas;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class Stat extends JPanel{

	public Stat(String name, int val) {
		setMinimumSize(new Dimension(200,20));
		setMaximumSize(new Dimension(200,20));
		setPreferredSize(new Dimension(200,20));
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		//this.setBackground(new Color(200,0,0));
		this.setOpaque(false);
		//this.setBackground(new Color(200,200,100));
		JLabel sta=new JLabel(name,JLabel.RIGHT);
		sta.setMinimumSize(new Dimension(75,20));
		sta.setMaximumSize(new Dimension(75,20));
		sta.setPreferredSize(new Dimension(75,20));
		JProgressBar bar= new JProgressBar(1,100);
		bar.setMinimumSize(new Dimension(125,20));
		bar.setMaximumSize(new Dimension(125,20));
		bar.setPreferredSize(new Dimension(125,20));
		bar.setValue(val);
		this.add(sta);
		this.add(bar);
	}
}
