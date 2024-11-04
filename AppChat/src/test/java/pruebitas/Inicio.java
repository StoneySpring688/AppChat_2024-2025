package pruebitas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.EtchedBorder;
import javax.swing.*;

public class Inicio {

	private JFrame ventana;
	private JPanel contenedor;
	private JTextField txtTexto;
	private JList<Elemento> lista;
	private JLabel personaje;
	private ArrayList<ImageIcon> imagenes;
	private JPanel pantalla, pantalla2=new Pantalla2();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicio window = new Inicio();
					window.ventana.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Inicio() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//-------------------------------
		imagenes=new ArrayList<ImageIcon>();
		imagenes.add(new ImageIcon(getClass().getResource("/resources/pikachu.png")));
		imagenes.add(new ImageIcon(getClass().getResource("/resources/Mujermaravilla.png")));
		imagenes.add(new ImageIcon(getClass().getResource("/resources/shongoku.png")));
		imagenes.add(new ImageIcon(getClass().getResource("/resources/raro.png")));
		//-------------------------------
		ventana = new JFrame();
		ventana.setSize(new Dimension(700, 600));
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contenedor=(JPanel) ventana.getContentPane();
		/*--menu--*/
		JMenuBar menubar=new JMenuBar();
		ventana.setJMenuBar(menubar);
		JMenu menuArchivo=new JMenu("Archivo");
		menubar.add(menuArchivo);
		JMenu menuOtro=new JMenu("Otro");
		menubar.add(menuOtro);
		JMenuItem itemAbrir=new JMenuItem("Abrir");
		JMenuItem itemCerrar=new JMenuItem("Cerrar");
		JMenuItem itemSalir=new JMenuItem("Salir");
		
		menuArchivo.add(itemAbrir);
		menuArchivo.add(itemCerrar);
		menuArchivo.add(new JSeparator());
		menuArchivo.add(itemSalir);
		
		JMenuItem itemCambiar=new JMenuItem("Cambiar pantalla");
		menuOtro.add(itemCambiar);
		itemCambiar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BorderLayout layout =(BorderLayout) contenedor.getLayout();			
				JPanel dentro=(JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
				contenedor.remove(dentro);
				if (dentro==pantalla) {
					contenedor.add(pantalla2,BorderLayout.CENTER);
					ventana.revalidate();
					ventana.repaint();
				} else {
					contenedor.add(pantalla,BorderLayout.CENTER);
					ventana.revalidate();
					ventana.repaint();
				}
			}
			
		});
		
		/*--fin menu--*/
		JPanel cajaArriba = new JPanel();
		cajaArriba.setBackground(Color.YELLOW);
		cajaArriba.setPreferredSize(new Dimension(700, 120));
		cajaArriba.setMinimumSize(new Dimension(700, 120));
		cajaArriba.setMaximumSize(new Dimension(700, 120));
		ventana.getContentPane().add(cajaArriba, BorderLayout.NORTH);
		cajaArriba.setLayout(new BoxLayout(cajaArriba, BoxLayout.X_AXIS));
		
		JLabel lblimagen = new JLabel("Este es Paco");
		lblimagen.setOpaque(true);
		lblimagen.setBackground(Color.PINK);
		lblimagen.setPreferredSize(new Dimension(240, 80));
		lblimagen.setMinimumSize(new Dimension(240, 80));
		lblimagen.setMaximumSize(new Dimension(240, 80));
		lblimagen.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		lblimagen.setIcon(new ImageIcon(getClass().getResource("/resources/raro.png")));
		cajaArriba.add(lblimagen);
		
		Component horizontalStrut = Box.createHorizontalStrut(10);
		cajaArriba.add(horizontalStrut);
		
		JPanel cajaRadio = new JPanel();
		cajaRadio.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Elige uno", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		cajaArriba.add(cajaRadio);
		cajaRadio.setLayout(new BoxLayout(cajaRadio, BoxLayout.Y_AXIS));
		
		JRadioButton opcion1 = new JRadioButton("blanco");
		opcion1.setSelected(true);
		cajaRadio.add(opcion1);
		
		JRadioButton opcion2 = new JRadioButton("negro");
		cajaRadio.add(opcion2);
		
		ButtonGroup grupo=new ButtonGroup();
		grupo.add(opcion1);
		grupo.add(opcion2);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(10);
		cajaArriba.add(horizontalStrut_1);
		
		txtTexto = new JTextField();
		txtTexto.setBackground(Color.CYAN);
		txtTexto.setEditable(false);
		txtTexto.setText("Esto me vale");
		txtTexto.setForeground(Color.BLUE);
		txtTexto.setPreferredSize(new Dimension(200, 20));
		txtTexto.setMinimumSize(new Dimension(200, 20));
		txtTexto.setMaximumSize(new Dimension(200, 20));
		cajaArriba.add(txtTexto);
		txtTexto.setColumns(10);
		
		Component horizontalStrut_2 = Box.createHorizontalStrut(10);
		cajaArriba.add(horizontalStrut_2);
		
		JCheckBox check = new JCheckBox("acepto");
		check.setOpaque(false);
		cajaArriba.add(check);
		
		pantalla = new JPanel();
		pantalla.setBackground(Color.ORANGE);
		ventana.getContentPane().add(pantalla, BorderLayout.CENTER);
		pantalla.setLayout(new BoxLayout(pantalla, BoxLayout.X_AXIS));
		
		JPanel cajaIzquierda = new JPanel();
		cajaIzquierda.setBackground(new Color(200, 100, 100));
		cajaIzquierda.setPreferredSize(new Dimension(320, 480));
		cajaIzquierda.setMinimumSize(new Dimension(320, 480));
		cajaIzquierda.setMaximumSize(new Dimension(320, 480));
		pantalla.add(cajaIzquierda);
		cajaIzquierda.setLayout(new BoxLayout(cajaIzquierda, BoxLayout.Y_AXIS));
		
		JPanel visor = new JPanel();
		visor.setBackground(Color.PINK);
		visor.setPreferredSize(new Dimension(380, 480));
		visor.setMinimumSize(new Dimension(380, 480));
		visor.setMaximumSize(new Dimension(380, 480));
		pantalla.add(visor);
		
		personaje = new JLabel("");
		visor.add(personaje);
		
		JComboBox<String> combo= new JComboBox();
		combo.setPreferredSize(new Dimension(100, 25));
		combo.setMinimumSize(new Dimension(100, 25));
		combo.setMaximumSize(new Dimension(100, 25));
		combo.addItem("Uno");
		combo.addItem("Dos");
		combo.addItem("Tres");
		
		cajaIzquierda.add(Box.createVerticalStrut(10));
		cajaIzquierda.add(combo);
		
		//Elemento aux= new Elemento("SMV.png","Mujer Maravilla",50,85,85);
		//cajaIzquierda.add(aux);
		
		//Lista de valores
		lista =new JList<Elemento>();
		DefaultListModel<Elemento> model=new DefaultListModel<Elemento>();
		model.addElement(new Elemento("SPK.png","Pikachu",15,25,90));
		model.addElement(new Elemento("SMV.png","Mujer Maravilla",50,85,85));
		model.addElement(new Elemento("SSH.png","Shongoku",100,60,10));
		model.addElement(new Elemento("raro.png","Emoji",10,5,15));
		lista.setModel(model);
		lista.setCellRenderer(new ElementoListRenderer());
		
		lista.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2)
				  personaje.setIcon(imagenes.get(lista.getSelectedIndex()));	
			}
		});
		
		JScrollPane scroll=new JScrollPane(lista);
		scroll.setMinimumSize(new Dimension(320,320));
		scroll.setMaximumSize(new Dimension(320,320));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		cajaIzquierda.add(Box.createVerticalStrut(48));
		cajaIzquierda.add(scroll);
	}



}
