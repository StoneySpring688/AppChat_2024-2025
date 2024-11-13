package umu.tds.AppChat.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import umu.tds.AppChat.controllers.UIController;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	int posX=0;
	int posY=0;
	private JPanel movilidad;
	private JPanel panelIntercambiable;
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
		getContentPane().setLayout(null);
		
		//movilidad del frame
		this.movilidad = new JPanel();
		this.movilidad.setOpaque(false);
		this.movilidad.setBackground(new Color(0, 0, 0, 0)); //transparente, alpha a 0
		this.movilidad.setBounds(0, 0, 684, 22);
		this.movilidad.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		this.movilidad.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
			}
		});
		this.movilidad.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
			}
		});
		getContentPane().add(this.movilidad);
		
		//layout
		panelIntercambiable = new JPanel();
		panelIntercambiable.setBounds(0, 0, 720, 420);
		actualizadorUI = new CardLayout();
		panelIntercambiable.setLayout(actualizadorUI);
		getContentPane().add(panelIntercambiable);
		
		//paneles
		login = new LoginPanel(uiController);
		register = new RegisterPanel(uiController);
		mainPanel = new MainPanel(uiController);
		intermedio = new PanelIntermedio();

		//gestion paneles
		panelIntercambiable.add(login, "login");
		panelIntercambiable.add(register, "register");
		panelIntercambiable.add(mainPanel, "mainPanel");
		panelIntercambiable.add(intermedio, "intermedio");
		this.actualizadorUI.show(panelIntercambiable, "login");
		
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
        this.movilidad.setBounds(0, 0, 684, 22);
    }

    public void showMainPanel() {
        changePanel("mainPanel");
    }
    
    public void showRegisterPanel() {
        changePanel("register");
        this.movilidad.setBounds(42, 0, 642, 22);
    }
    
    public void showPanelIntermedio() {
        changePanel("intermedio");
    }
    
    public void showAnyadirContactoPanel() {
        this.mainPanel.changePanelPrincipal("anyadirContacto");
    }
    
    public void showAnyadirCrearGrupoPanel() {
        this.mainPanel.changePanelPrincipal("crearGrupo");
    }

    public void resizeForMainPanel() {
        setSize(1280, 720);
        this.panelIntercambiable.setSize(1280, 720);
        this.movilidad.setSize(1244, 22);
    }
    
    public void resizeForLoginPanel() {
        setSize(720,420);
        this.panelIntercambiable.setSize(720,420);
        this.movilidad.setSize(684, 22);
    }
    
    public void changePanel(String panel) {
    	this.actualizadorUI.show(panelIntercambiable, panel);
    }
    
    @SuppressWarnings("unchecked")
	public void llamarMetodo(int numMetodo, Optional<Object> ... arg) {
		
		switch (numMetodo) {
		// inicializar lista de usuarios de crearGrupo
		case 1: {
			
			if (arg[0].isPresent() && arg[0].get() instanceof DefaultListModel) {
				DefaultListModel<ElementoChatOGrupo> lista = (DefaultListModel<ElementoChatOGrupo>) arg[0].get();
				mainPanel.accederMetodoNoVisible(1, Optional.of(lista));
			}
			break;
			
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + numMetodo);
		}
		
	}
    
}
