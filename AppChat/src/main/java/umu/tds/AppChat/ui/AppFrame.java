package umu.tds.AppChat.ui;

import java.awt.CardLayout;
import java.awt.Color;
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
	private PanelIntermedio intermedio;
	private CardLayout actualizadorUI;

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
		intermedio = new PanelIntermedio();

		//gestion paneles
		add(login, "login");
		add(register, "register");
		add(mainPanel, "mainPanel");
		add(intermedio, "intermedio");
		actualizadorUI.show(getContentPane(), "login");
		
		//gestion botones
		login.getRegisterButton().addActionListener(e -> uiController.showRegister());
		login.getLoginButton().addActionListener(e -> uiController.doLogin());
		register.getBackButton().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	uiController.showLogin();
		    }
		});
		
		mainPanel.getLogoutBotton().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	uiController.doLogout();
		    }
		});
		
	}
	
	public void showLoginPanel() {
        this.changePanel("login");
    }

    public void showMainPanel() {
        changePanel("mainPanel");
    }
    
    public void showRegisterPanel() {
        changePanel("register");
    }
    
    public void showPanelIntermedio() {
        changePanel("intermedio");
    }

    public void resizeForMainPanel() {
        setSize(1280, 720);
    }
    
    public void resizeForLoginPanel() {
        setSize(720,420);
    }
    
    public void changePanel(String panel) {
    	this.actualizadorUI.show(getContentPane(), panel);
    }
}
