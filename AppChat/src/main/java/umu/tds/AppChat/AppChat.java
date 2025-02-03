package umu.tds.AppChat;

import java.awt.EventQueue;

import umu.tds.AppChat.controllers.MainController;

/**
 * @author StoneySpring688*/
public class AppChat {
	
    public static void main(String[] args) {
    	
    	EventQueue.invokeLater(()-> {
    		
    		try {
                MainController.startApp();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    	});
        
    }
}
