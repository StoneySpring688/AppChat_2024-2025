package umu.tds.AppChat.backend.utils;

import umu.tds.AppChat.ui.chatInterface.Button;

public class Emoji extends Button{
	private static final long serialVersionUID = 1L;
	
	private final int id;
	
	public Emoji(int id) {
		super();
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
}
