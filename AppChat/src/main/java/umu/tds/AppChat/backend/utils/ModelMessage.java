package umu.tds.AppChat.backend.utils;

import java.util.Optional;

import javax.swing.Icon;

public class ModelMessage {
	
	private Icon icon;
	private int sender;
	private int reciver;
    private String name;
    private String date;
    private Optional<String> message;
    private Optional<Integer> emoji;

    public ModelMessage(Icon icon, String name, String date, Optional<String> message, Optional<Integer> emoji) {
        this.icon = icon;
        this.name = name;
        this.date = date;
        this.message = message.isPresent() ? Optional.of(message.get()) : Optional.empty();
        this.emoji = emoji.isPresent() ? Optional.of(emoji.get()) : Optional.empty();
    }

    public ModelMessage() {
    }
    
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Optional<String> getMessage() {
    	Optional<String> s = this.message.isPresent() ? Optional.of(this.message.get()) : Optional.empty();
        return s;
    }

    public void setMessage(String message) {
        this.message = Optional.of(message);
    }
    
    public Optional<Integer> getEmoji(){
    	Optional<Integer> s = this.emoji.isPresent() ? Optional.of(this.emoji.get()) : Optional.empty();
        return s;
    }
    
    public void setEmoji(Integer emoji) {
    	this.emoji = Optional.of(emoji);
    }
    
}
