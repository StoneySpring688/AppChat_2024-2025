package umu.tds.AppChat.backend;

import java.util.Optional;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ModelMessage {

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
    
    public Optional<ImageIcon> getEmoji(){
    	Optional<ImageIcon> s = this.emoji.isPresent() ? Optional.of(this.emoji.get()) : Optional.empty();
        return s;
    }
    
    public void setEmoji(ImageIcon emoji) {
    	this.emoji = Optional.of(emoji);
    }

    public ModelMessage(Icon icon, String name, String date, Optional<String> message, Optional<ImageIcon> emoji) {
        this.icon = icon;
        this.name = name;
        this.date = date;
        this.message = message.isPresent() ? Optional.of(message.get()) : Optional.empty();
        this.emoji = emoji.isPresent() ? Optional.of(emoji.get()) : Optional.empty();
    }

    public ModelMessage() {
    }

    private Icon icon;
    private String name;
    private String date;
    private Optional<String> message;
    private Optional<ImageIcon> emoji;
}
