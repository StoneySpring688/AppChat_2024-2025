package pruebitas;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Elemento extends JPanel{
	
	public Elemento(String fileName, String nombre, int fu, int agi, int in) {
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		fixSize(this,300,110);
		this.setBackground(Color.WHITE);
		this.setBorder(new TitledBorder(nombre));
		
		JLabel lblimagen=new JLabel();
		lblimagen.setIcon(new ImageIcon(getClass().getResource("/resources/"+fileName)));
		fixSize(lblimagen,75,84);
		lblimagen.setBorder(new LineBorder(Color.BLACK,2));
		
		JPanel stats=new JPanel();
		stats.setLayout(new BoxLayout(stats,BoxLayout.Y_AXIS));
		fixSize(stats,200,100);
		stats.setOpaque(false);
		
		Stat fue=new Stat("Fuerza: ",fu);
		Stat ag=new Stat("Agilidad: ",agi);
		Stat inte=new Stat("Inteligencia: ",in);
		
		stats.add(new JLabel("Estadísticas"));
		stats.add(fue); stats.add(ag); stats.add(inte);
		
		this.add(lblimagen);
		this.add(stats);
		
	}
	
	private void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x,y));
		c.setMaximumSize(new Dimension(x,y));
		c.setPreferredSize(new Dimension(x,y));
	}
}
