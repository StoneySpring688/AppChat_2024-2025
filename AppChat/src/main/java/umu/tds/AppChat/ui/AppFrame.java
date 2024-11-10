package umu.tds.AppChat.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;

import umu.tds.AppChat.controllers.UIController;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	int posX=0;
	int posY=0;
	private LoginPanel login;
	private RegisterPanel register;
	private MainPanel mainPanel;
	private CardLayout actualizadorUI;

	/**
	 * Launch the application.
	 
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppFrame frame = new AppFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	/**
	 * Create the frame.
	 */
	public AppFrame(UIController uiController) {
		
		//configuración del frame
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 420);
		setSize(720,420);
		setResizable(false);
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		//movilidad del frame
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
			}
		});
		
		//layout
		actualizadorUI = new CardLayout();
		setLayout(actualizadorUI);
		
		//paneles
		login = new LoginPanel(uiController);
		register = new RegisterPanel(uiController);
		mainPanel = new MainPanel(uiController);

		add(login, "login");
		add(register, "register");
		add(mainPanel, "mainPanel");
		
		actualizadorUI.show(getContentPane(), "login");
		
		login.getRegisterButton().addActionListener(e -> actualizadorUI.show(getContentPane(), "register"));
		login.getLoginButton().addActionListener(e -> { uiController.doLogin(); });
		
		register.getBackButton().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	actualizadorUI.show(getContentPane(), "login");
		    }
		});
		
	}
	
	public void showLoginPanel() {
        this.actualizadorUI.show(getContentPane(), "login");
    }

    public void showMainPanel() {
        this.actualizadorUI.show(getContentPane(), "mainPanel");
    }

    public void resizeForMainPanel() {
        setSize(1280, 720);
    }
    
    public void resizeForLoginPanel() {
        setSize(720,420);
    }

}
