package umu.tds.AppChat.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;
import umu.tds.AppChat.backend.utils.EntidadComunicable;
import umu.tds.AppChat.backend.utils.Grupo;
import umu.tds.AppChat.controllers.BackendController;
import umu.tds.AppChat.controllers.UIController;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OptionsPane extends PanelGrande {

	private static final long serialVersionUID = 1L;

	private Background fondo;
	private JScrollPane scrollPanel;
	
	// contacts settings
	private DefaultListModel<ElementoChatOGrupo> contactos;
	private JList<ElementoChatOGrupo> listaContactos;
	private JPanel contactsPreview;
	private JTextField textFieldPhone;
	private JTextField textNombreContacto;
	private JButton editContactButton;
	private JButton deleteContactButton;
	
	// groups settings
	private DefaultListModel<ElementoChatOGrupo> grupos;
	private JList<ElementoChatOGrupo> listaGrupos;
	private JPanel groupsPreview;
	private JPanel groupPreview;
	private JPanel editarGrupoPanelContactos;
	private DefaultListModel<ElementoChatOGrupo> editarGrupoContactos;
	private JList<ElementoChatOGrupo> editarGrupoListaContactos;
	private JPanel editarGrupoPanelMiembros;
	private DefaultListModel<ElementoChatOGrupo> editarGrupoMiembros;
	private JList<ElementoChatOGrupo> editarGrupoListaMiembros;
	private JTextField textNombreGrupo;
	private JTextField urlField;
	private ImageAvatar lblProfile;
	private JButton editGroupButton;
	private JButton deleteGroupButton;
	private JButton leaveGroupButton;
	private Grupo selectedGroup;
	private boolean validImage;
	
	//pdf
	private JButton exportToPdfButton;
	
	// utils
	private final static String defaultProfileImage = "/assets/ProfilePic.png";
	private final static String defaultGroupUrl = "https://github.com/StoneySpring688/AppChat_2024-2025/blob/main/AppChat/src/main/resources/assets/ProfilePic.png?raw=true";
	
	// colors
	private final Color Gray = new Color(64, 68, 75);
	private final Color darkPorDefecto = new Color(54, 57, 63);
	
	public OptionsPane() {

		this.fondo = new Background();
		this.fondo.setBounds(0, 60, 920, 1200);
		this.fondo.setPreferredSize(new Dimension(920, 1200+20));
		
		this.scrollPanel = new JScrollPane();
		this.scrollPanel.setViewportView(this.fondo);
		this.scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPanel.setVerticalScrollBar(new ScrollBar());
		this.scrollPanel.setBorder(BorderFactory.createEmptyBorder());
		this.scrollPanel.setBackground(this.Gray);
		this.scrollPanel.setBounds(0, 60, 920, 660);
		
		this.add(scrollPanel);
		
		// ### header
		JLabel lblOptions = new JLabel("Options");
		lblOptions.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 15));
		lblOptions.setBounds(435, 5, 60, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblOptions.setForeground(Color.WHITE);
		
		this.fondo.add(lblOptions);
		
		// ### contacts settings
		JLabel lblContacts = new JLabel("Contacts Settings");
		lblContacts.setFont(new Font("Dialog", Font.BOLD, 12));
		lblContacts.setBounds(10, 40, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblContacts.setForeground(Color.WHITE);
		
		this.fondo.add(lblContacts);
		
		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(410, 111, 42, 20);
		lblPhone.setForeground(Color.WHITE);
		this.fondo.add(lblPhone);
		
		textFieldPhone = new JTextField();
		textFieldPhone.setBounds(410, 129, 285, 30);
		textFieldPhone.setBackground(this.Gray);
		textFieldPhone.setCaretColor(Color.WHITE);
		textFieldPhone.setForeground(Color.WHITE);
		textFieldPhone.setColumns(10);
		textFieldPhone.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFieldPhone.getForeground().equals(Color.RED)) {
                    textFieldPhone.setText(""); // Borrar el mensaje de error
                    textFieldPhone.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
		
		this.fondo.add(textFieldPhone);
		
		JLabel lblContacto = new JLabel("Nombre Contacto");
		lblContacto.setBounds(410, 201, 120, 20);
		lblContacto.setForeground(Color.WHITE);
		this.fondo.add(lblContacto);
		
		textNombreContacto = new JTextField();
		textNombreContacto.setBounds(410, 220, 285, 30);
		textNombreContacto.setBackground(this.Gray);
		textNombreContacto.setCaretColor(Color.WHITE);
		textNombreContacto.setForeground(Color.WHITE);
		textNombreContacto.setColumns(10);
		textNombreContacto.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textNombreContacto.getForeground().equals(Color.RED)) {
                	textNombreContacto.setText(""); // Borrar el mensaje de error
                	textNombreContacto.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
		this.fondo.add(textNombreContacto);
		
		editContactButton = new JButton("Edit");
		editContactButton.setForeground(Color.WHITE);
		editContactButton.setBackground(new Color(241, 57, 83));
		editContactButton.setBounds(459, 310, 187, 35); // x = 370+(285/2)-(187/2)
		editContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//numero inexistente o ya en contactos
				if(editContact()) {
					resetContactSettings();
				}
			}
		});
		
		this.fondo.add(editContactButton);
		
		deleteContactButton = new JButton("Remove");
		deleteContactButton.setForeground(Color.WHITE);
		deleteContactButton.setBackground(new Color(241, 57, 83));
		deleteContactButton.setBounds(459, 365, 187, 35); // x = 370+(285/2)-(187/2)
		deleteContactButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(removeContact()) {
					resetContactSettings();
				}
			}
		});
		
		this.fondo.add(deleteContactButton);
		
		contactsPreview = new JPanel();
		contactsPreview.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contactsPreview.setBounds(10, 80, 285, 385);
		this.fondo.add(contactsPreview);
		
		this.contactos = new DefaultListModel<>();
		contactsPreview.setLayout(new BoxLayout(contactsPreview, BoxLayout.X_AXIS));
		this.listaContactos = new JList<>(contactos);
		this.listaContactos.setCellRenderer(new ElementoChatOGrupoRender(this.listaContactos));
		this.listaContactos.setBackground(this.darkPorDefecto);
		JScrollPane scrollListaContactos = new JScrollPane(this.listaContactos);
		scrollListaContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollListaContactos.setVerticalScrollBar(new ScrollBar());
		scrollListaContactos.setBorder(BorderFactory.createEmptyBorder());
		scrollListaContactos.setBackground(this.darkPorDefecto);
		contactsPreview.add(scrollListaContactos);
		listaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		    	listaContactos.requestFocusInWindow();
		        int selectedIndex = listaContactos.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = contactos.getElementAt(selectedIndex);
		            textFieldPhone.setText(Integer.toString(selectedContact.getNumero()));
		            textFieldPhone.setForeground(Color.WHITE);
		            textNombreContacto.setText(selectedContact.getNombre());
                	textNombreContacto.setForeground(Color.WHITE);

		            
		        }
		    }
		});
			
		// ### groups settings
		JLabel lblGroups = new JLabel("Groups Settings");
		lblGroups.setFont(new Font("Dialog", Font.BOLD, 12));
		lblGroups.setBounds(10, 520, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblGroups.setForeground(Color.WHITE);
		
		this.fondo.add(lblGroups);
		
		groupsPreview = new JPanel();
		groupsPreview.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		groupsPreview.setBounds(10, 560, 285, 385);
		this.fondo.add(groupsPreview);
		
		this.grupos = new DefaultListModel<ElementoChatOGrupo>();
		groupsPreview.setLayout(new BoxLayout(groupsPreview, BoxLayout.X_AXIS));
		this.listaGrupos = new JList<>(grupos);
		this.listaGrupos.setCellRenderer(new ElementoChatOGrupoRender(this.listaGrupos));
		this.listaGrupos.setBackground(this.darkPorDefecto);
		JScrollPane scrollListaGrupos = new JScrollPane(this.listaGrupos);
		scrollListaGrupos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollListaGrupos.setVerticalScrollBar(new ScrollBar());
		scrollListaGrupos.setBorder(BorderFactory.createEmptyBorder());
		scrollListaGrupos.setBackground(this.darkPorDefecto);
		groupsPreview.add(scrollListaGrupos);
		listaGrupos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = listaGrupos.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedGroup = grupos.getElementAt(selectedIndex);  
		            //System.out.println(selectedGroup.getNombre() + " ," + selectedGroup.getGroupID());
		            groupPreview(selectedGroup);
		            prepareEditGroup(selectedGroup);
		            
		        }
		    }
		});
		
		this.groupPreview = new JPanel();
		this.groupPreview.setLayout(new MigLayout("fill, insets 0, gap 0"));
		this.groupPreview.setBounds(33, 965, 240, 65); // b  = a + (w_1/2) - (w_2/2)
		this.groupPreview.setBackground(darkPorDefecto);
		this.groupPreview.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		this.fondo.add(groupPreview);
		
		//lista de contactos
		JLabel lblContactosGrupo = new JLabel("Contactos");
		lblContactosGrupo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblContactosGrupo.setBounds(315, 540, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblContactosGrupo.setForeground(Color.WHITE);
		
		this.fondo.add(lblContactosGrupo);
		
		editarGrupoPanelContactos = new JPanel();
		editarGrupoPanelContactos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		editarGrupoPanelContactos.setBounds(315, 560, 285, 385);
		this.fondo.add(editarGrupoPanelContactos);
		
		this.editarGrupoContactos = new DefaultListModel<>();
		editarGrupoPanelContactos.setLayout(new BoxLayout(editarGrupoPanelContactos, BoxLayout.X_AXIS));
		this.editarGrupoListaContactos = new JList<>(editarGrupoContactos);
		this.editarGrupoListaContactos.setCellRenderer(new ElementoChatOGrupoRender(this.editarGrupoListaContactos));
		this.editarGrupoListaContactos.setBackground(this.darkPorDefecto);
		JScrollPane editarGrupoScrollListaContactos = new JScrollPane(this.editarGrupoListaContactos);
		editarGrupoScrollListaContactos.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editarGrupoScrollListaContactos.setVerticalScrollBar(new ScrollBar());
		editarGrupoScrollListaContactos.setBorder(BorderFactory.createEmptyBorder());
		editarGrupoScrollListaContactos.setBackground(this.darkPorDefecto);
		editarGrupoPanelContactos.add(editarGrupoScrollListaContactos);
		editarGrupoListaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = editarGrupoListaContactos.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = editarGrupoContactos.getElementAt(selectedIndex);
	
		            // Removerlo de la lista de contactos y añadirlo a la lista de miembros
		            editarGrupoContactos.removeElementAt(selectedIndex);
		            editarGrupoMiembros.add(0, selectedContact);
		            
		            if(((TitledBorder) editarGrupoPanelMiembros.getBorder()).getTitleColor() == Color.RED) {
		            	editarGrupoPanelMiembros.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		            }
		        }
		    }
		});
		
		//lista de miembros
		JLabel lblMiembrosGrupo = new JLabel("Miembros");
		lblMiembrosGrupo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblMiembrosGrupo.setBounds(620, 540, 120, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblMiembrosGrupo.setForeground(Color.WHITE);
		
		this.fondo.add(lblMiembrosGrupo);
		
		editarGrupoPanelMiembros = new JPanel();
		editarGrupoPanelMiembros.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		editarGrupoPanelMiembros.setBounds(620, 560, 285, 385);
		this.fondo.add(editarGrupoPanelMiembros);
		
		this.editarGrupoMiembros = new DefaultListModel<>();
		editarGrupoPanelMiembros.setLayout(new BoxLayout(editarGrupoPanelMiembros, BoxLayout.X_AXIS));
		this.editarGrupoListaMiembros = new JList<>(editarGrupoMiembros);
		this.editarGrupoListaMiembros.setCellRenderer(new ElementoChatOGrupoRender(this.editarGrupoListaMiembros));
		this.editarGrupoListaMiembros.setBackground(this.darkPorDefecto);
		JScrollPane editarGrupoScrollListaMiembros = new JScrollPane(this.editarGrupoListaMiembros);
		editarGrupoScrollListaMiembros.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		editarGrupoScrollListaMiembros.setVerticalScrollBar(new ScrollBar());
		editarGrupoScrollListaMiembros.setBorder(BorderFactory.createEmptyBorder());
		editarGrupoScrollListaMiembros.setBackground(this.darkPorDefecto);
		editarGrupoPanelMiembros.add(editarGrupoScrollListaMiembros);
		editarGrupoListaMiembros.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        int selectedIndex = editarGrupoListaMiembros.getSelectedIndex();
		        if (selectedIndex != -1) {
		            // Obtener el elemento seleccionado
		            ElementoChatOGrupo selectedContact = editarGrupoMiembros.getElementAt(selectedIndex);
	
		            // Removerlo de la lista de contactos y añadirlo a la lista de miembros
		            editarGrupoMiembros.removeElementAt(selectedIndex);
		            editarGrupoContactos.add(0, selectedContact);
		            
		            if(((TitledBorder) editarGrupoPanelContactos.getBorder()).getTitleColor() == Color.RED) {
		            	editarGrupoPanelContactos.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		            }
		        }
		    }
		});
		
		//campo para el nombre del grupo
		JLabel lblGrupo = new JLabel("Nombre Grupo");
		lblGrupo.setBounds(315, 965, 120, 20);
		lblGrupo.setForeground(Color.WHITE);
		this.fondo.add(lblGrupo);
		
		textNombreGrupo = new JTextField();
		textNombreGrupo.setBounds(315, 985, 285, 30);
		textNombreGrupo.setBackground(this.Gray);
		textNombreGrupo.setCaretColor(Color.WHITE);
		textNombreGrupo.setForeground(Color.WHITE);
		//textNombreGrupo.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		textNombreGrupo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textNombreGrupo.getForeground().equals(Color.RED)) {
                	textNombreGrupo.setText(""); // Borrar el mensaje de error
                	textNombreGrupo.setForeground(Color.WHITE); // Restaurar color normal
                }
            }
        });
		this.fondo.add(textNombreGrupo);
		textNombreGrupo.setColumns(10);
		
		// label para ver la imagen del grupo
		lblProfile = new ImageAvatar();
		ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
		lblProfile.setImage(image);
		lblProfile.setBorderSize(1);
		lblProfile.setBorderSpace(1);
		lblProfile.setBounds(620, 965, 44, 44);
		this.fondo.add(lblProfile);
		
		//campo para la url de la imagen del grupo
		JLabel lblUrlImage = new JLabel("ImageUrl");
		lblUrlImage.setBounds(674, 962, 60, 20);
		lblUrlImage.setForeground(Color.WHITE);
		this.fondo.add(lblUrlImage);
		
		this.validImage = false;
		
		urlField = new JTextField();
		urlField.setBackground(this.Gray);
		urlField.setCaretColor(Color.WHITE);
		urlField.setForeground(Color.WHITE);
		//urlField.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
		urlField.setBounds(674, 982, 202, 30);
		this.fondo.add(urlField);
		
		//boton para borra directamente la url
		JLabel buttonDeleteUrl = new JLabel();
		ImageIcon deleteIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/Btn_FairyBook_Cancel.png")).getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH) );
		buttonDeleteUrl.setIcon(deleteIcon);
		buttonDeleteUrl.setBounds(832, 1012, 22, 22);
		buttonDeleteUrl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				urlField.setText(""); //eliminar el texto en la url
				try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
				}
			});		
		UIController.addHoverEffect(buttonDeleteUrl, 20, 20);
		this.fondo.add(buttonDeleteUrl);
		
		//boton para actualizar la imagen
		JLabel buttonUpdateImage = new JLabel();
		ImageIcon updateProfileIcon = new ImageIcon(new ImageIcon(getClass().getResource("/assets/UI_MarkPoint_Sign_Inside.png")).getImage().getScaledInstance(22, 22,  Image.SCALE_SMOOTH));
		buttonUpdateImage.setIcon(updateProfileIcon);
		buttonUpdateImage.setBounds(855, 1012, 22, 22);
		buttonUpdateImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					actualizarImagen();
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				}
			}
		});
		UIController.addHoverEffect(buttonUpdateImage, 20, 20);
		this.fondo.add(buttonUpdateImage);
		
		editGroupButton = new JButton("Edit");
		editGroupButton.setForeground(Color.WHITE);
		editGroupButton.setBackground(Color.GRAY);
		editGroupButton.setBounds(364, 1045, 187, 35);
		editGroupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedGroup != null && selectedGroup.isAdmin(BackendController.getUserNumber()) && editGrupo(getEditGroup().getID())) {
					resetGroupSettings();
				}
			}
		});
		
		this.fondo.add(editGroupButton);
		
		leaveGroupButton = new JButton("Leave");
		leaveGroupButton.setForeground(Color.WHITE);
		leaveGroupButton.setBackground(Color.GRAY);  
		leaveGroupButton.setBounds(60, 1045, 187, 35);
		leaveGroupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedGroup != null && !selectedGroup.isAdmin(BackendController.getUserNumber()) && leaveGroup(getEditGroup())) {
					resetGroupSettings();
				}
			}
		});
		
		this.fondo.add(leaveGroupButton);
		
		deleteGroupButton = new JButton("Delete");
		deleteGroupButton.setForeground(Color.WHITE);
		deleteGroupButton.setBackground(Color.GRAY);
		deleteGroupButton.setBounds(669, 1045, 187, 35);
		deleteGroupButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedGroup != null && selectedGroup.isAdmin(BackendController.getUserNumber()) && deleteGroup(getEditGroup())) {
					resetGroupSettings();
				}
			}
		});
		
		this.fondo.add(deleteGroupButton);
		
		// ### PDF
		JLabel lblPdf = new JLabel("make PDF for premium users");
		lblPdf.setFont(new Font("Dialog", Font.BOLD, 12));
		lblPdf.setBounds(10, 1130, 220, 20); //this.fondo.getWidth()/2-lblOptions.getWidth()/2
		lblContacts.setForeground(Color.WHITE);
		
		this.fondo.add(lblPdf);
		
		exportToPdfButton = new JButton("Export to PDF");
		exportToPdfButton.setForeground(Color.WHITE);
		exportToPdfButton.setBackground(Color.GRAY);  
		exportToPdfButton.setBounds(60, 1170, 187, 35);
		exportToPdfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO
			}
		});
		
		this.fondo.add(exportToPdfButton);
		
	}
	
	public String getNumeroEditContact() {
		return textFieldPhone.getForeground().equals(Color.RED) ? "" : this.textFieldPhone.getText();
	}
	
	public String getNombreEditContact() {
		return textNombreContacto.getForeground().equals(Color.RED) ? "" : this.textNombreContacto.getText();
	}
	
	public static String getDefaultProfileImage() {
		return new String(defaultGroupUrl);
	}
	
	public String getProfilePicUrlEditGroup() {
		return this.validImage == true ? this.urlField.getText() : getDefaultProfileImage();
	}
	
	public String getNombreEditGroup() {
		return textNombreGrupo.getText();
	}
	
	public Grupo getEditGroup() {
		return this.selectedGroup;
	}
	
	// ### UiPreparation and reset
	
	public void prepareOptions(List<EntidadComunicable> contactos, List<Grupo> grupos, boolean isPremium) {
		loadContactsAndGroups(contactos, grupos);
		if(isPremium) this.exportToPdfButton.setBackground(new Color(241, 57, 83));
		else this.exportToPdfButton.setBackground(Color.GRAY);
	}
	
	public List<Integer> getListaMiembrosEditGroup(){
		int size = this.editarGrupoMiembros.size();
		List<Integer> lista = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++) {
	        lista.add(this.editarGrupoMiembros.get(i).getNumero());
	    }
		lista.add(BackendController.getUserNumber()); // se añade el numero del usuario actual (se elimino de la visualización para evitar errores)
		
 		return lista;
	}
	
	public void loadContactsAndGroups(List<EntidadComunicable> contactos, List<Grupo> grupos) {
		this.contactos.clear();
		contactos.forEach(e -> this.contactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
	    this.listaContactos.setModel(this.contactos);
	    
	    this.grupos.clear();
	    grupos.forEach(e -> this.grupos.addElement(new ElementoChatOGrupo(Optional.empty(), Optional.of(e))));
	    this.listaGrupos.setModel(this.grupos);
	    
	    this.repaint();
	    this.revalidate();
		
	}
	
	private void prepareEditGroup(ElementoChatOGrupo grupo) {
		if(grupo.getGrupo().isPresent() && grupo.getGrupo().get().isAdmin(BackendController.getUserNumber())) {
			loadEditGroup(BackendController.getListaContactos(), grupo.getGrupo().get().getIntegrantes());
			this.textNombreGrupo.setText(grupo.getNombre());
			this.urlField.setText(grupo.getGrupo().get().getIconUrl());
			try {
				actualizarImagen();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			
			this.editGroupButton.setBackground(new Color(241, 57, 83));
			
			this.deleteGroupButton.setBackground(new Color(241, 57, 83));
			
			this.leaveGroupButton.setBackground(Color.GRAY);  
			
			this.repaint();
			this.revalidate();
			
		}else{
			this.editGroupButton.setBackground(Color.GRAY);
			
			this.deleteGroupButton.setBackground(Color.GRAY);
			
			leaveGroupButton.setBackground(new Color(241, 57, 83));  
			
			this.repaint();
			this.revalidate();
			
		}if(grupo.getGrupo().isPresent()) this.selectedGroup = grupo.getGrupo().get();
	}
	
	private void loadEditGroup(List<EntidadComunicable> listaContactos, List<EntidadComunicable> listaMiembros){
		this.editarGrupoContactos.clear();
		this.editarGrupoMiembros.clear();
		List<EntidadComunicable> listContactos = new ArrayList<EntidadComunicable>(listaContactos);
		List<EntidadComunicable> listMiembros = new ArrayList<EntidadComunicable>(listaMiembros);
		// ojo, como se modifica la lista de contactos se trabajar con una copia
		listContactos.removeIf(contacto -> listaMiembros.stream().anyMatch(e -> e.getNumero() == contacto.getNumero()));
		// elimino el usuario actual de la lista de miembros que se muestra para  evitar errores
		listMiembros.removeIf(e -> e.getNumero() == BackendController.getUserNumber());
		
		listContactos.forEach(e -> this.editarGrupoContactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
		this.editarGrupoListaContactos.setModel(this.editarGrupoContactos);
		
		listMiembros.forEach(e -> this.editarGrupoMiembros.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
		this.editarGrupoListaMiembros.setModel(this.editarGrupoMiembros);
		
		this.repaint();
	    this.revalidate();
	}
	
	private void groupPreview(ElementoChatOGrupo group) {
		this.groupPreview.removeAll();
		this.groupPreview.add(new ElementoChatOGrupo(Optional.empty(), group.getGrupo()), "wrap");
		this.groupPreview.repaint();
		this.groupPreview.revalidate();
	}
	
	public void reset() {
		resetContactSettings();
		resetGroupSettings();
	}
	
	// ### contacts
	
	private boolean editContact() {
	    return UIController.verificarContactoYEditarContacto(getNumeroEditContact(), getNombreEditContact());		
	}
	
	private boolean removeContact() {
		return UIController.removeContact(getNumeroEditContact());
	}
	
	private void resetContactSettings() {
		textFieldPhone.setText("");
        textFieldPhone.setForeground(Color.WHITE);
        textNombreContacto.setText("");
        textNombreContacto.setForeground(Color.WHITE);
        
        this.contactos = new DefaultListModel<>();
		this.listaContactos.setModel(this.contactos);
		
		this.contactos.clear();
		BackendController.getListaContactos().forEach(e -> this.contactos.addElement(new ElementoChatOGrupo(Optional.of(e), Optional.empty())));
	    this.listaContactos.setModel(this.contactos);
	    this.repaint();
	    this.revalidate();
		
	}
	
	// ### groups
	
	private boolean editGrupo(long grupID) {
		if(grupID == 0) return false;
		return UIController.verificarContactoYEditarGrupo(getProfilePicUrlEditGroup(),getNombreEditGroup() , getListaMiembrosEditGroup(), grupID);
	}
	
	private boolean leaveGroup(Grupo group) {
		boolean success = false;
		if(group != null) {
			success = true;
			UIController.leaveGroup(group);
		}
		
		return success;
	}
	
	private boolean deleteGroup(Grupo group) {
		boolean success = false;
		if(group != null) {
			success = true;
			UIController.removeGroup(group);
		}
		return success;
	}
	
	protected void resetGroupSettings() {
		this.textNombreGrupo.setText("");
		this.urlField.setText("");
		this.editarGrupoPanelMiembros.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		try {
			actualizarImagen();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.editarGrupoMiembros = new DefaultListModel<>();
		this.editarGrupoListaMiembros.setModel(this.editarGrupoMiembros);
		
		this.grupos.clear();
		BackendController.getGrupos().forEach(e -> this.grupos.addElement(new ElementoChatOGrupo(Optional.empty(), Optional.of(e))));
	    this.listaGrupos.setModel(this.grupos);
	    
	    this.editarGrupoContactos.clear();
	    
	    
	    this.groupPreview.removeAll();
		this.groupPreview.repaint();
		this.groupPreview.revalidate();
		
		this.editGroupButton.setBackground(Color.GRAY);
		this.deleteGroupButton.setBackground(Color.GRAY);
		leaveGroupButton.setBackground(Color.GRAY);
		
		this.selectedGroup = null;
		
	    this.repaint();
	    this.revalidate();
		
	}
	
	// ### errors
	
	public void contactSettingsErrors(byte code) {
		switch (code) {
		case 1: {
			//System.err.println("[Error] : El valor no es un número válido.");
		    textFieldPhone.setForeground(Color.RED);
            textFieldPhone.setText("Invalid number");
			break;
		}
		case 2: {
			//System.err.println("[Error] : contacto ya existente.");
		    textFieldPhone.setForeground(Color.RED);
            textFieldPhone.setText("contact do not exists");
            textNombreContacto.setForeground(Color.RED);
            textNombreContacto.setText("contact do not exists");
			break;
		}
		case 3: {
			//System.err.println("[Error] : nombre invalido.");
			textNombreContacto.setForeground(Color.RED);
			textNombreContacto.setText("invalid name");
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + code);
		}
	}
	
	public void groupSettingsErrors(byte code) {
		//System.out.println("[DEBUG] Errors" + code);
		switch (code) {
		case 1: {
			this.textNombreGrupo.setForeground(Color.RED);
			this.textNombreGrupo.setText("invalid name");
			break;
		}
		case 2: {
			editarGrupoPanelMiembros.setBorder(new TitledBorder(new LineBorder(Color.RED, 1), null, TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, Color.RED));
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + code);
		}
	}

	// ### utils
	
    private void actualizarImagen() throws MalformedURLException {
        String urlText = urlField.getText();
        URL url;
        
		try {
			url = new URL(urlText);
			ImageIcon icono = new ImageIcon(url);
			
	        if (icono.getIconWidth() > 0 && icono.getIconHeight() > 0) { // Verifica que la imagen sea válida
	            Image imagenEscalada = icono.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
	            //image.setImage(imagenEscalada);
	            lblProfile.setImage(new ImageIcon(imagenEscalada));
	            lblProfile.revalidate();
	            lblProfile.repaint();
	            
	            validImage = true;
	            //System.out.println("Imagen cargada exitosamente.");
	            
	        } else {
	            //System.out.println("Texto ingresado no contiene una imagen válida.");
	            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
	    		lblProfile.setImage(image);
	    		lblProfile.revalidate();
	            lblProfile.repaint();
	            validImage = false;
	        }
	        
		} catch (MalformedURLException e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		}catch (NullPointerException e){
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		} catch (Exception e) {
            ImageIcon image = new ImageIcon( new ImageIcon(RegisterPanel.class.getResource(defaultProfileImage)).getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH) );
            lblProfile.setImage(image);
            lblProfile.revalidate();
            lblProfile.repaint();
            validImage = false;
		}
    }
	
}
