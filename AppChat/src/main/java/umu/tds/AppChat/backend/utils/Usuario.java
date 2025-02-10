package umu.tds.AppChat.backend.utils;

import java.time.LocalDate;
import java.util.Optional;

public class Usuario extends EntidadComunicable {

	private String passwd;
	private String signature;
	private LocalDate birthDate;
	private boolean premium;
	private Optional<LocalDate> endPremium;
	
	public Usuario(String name, int number, String passwd, LocalDate birthDate, String profilePicUrl, String signature) {
		super(number, name, profilePicUrl);
		this.passwd = passwd;
		this.signature = signature;
		this.birthDate = birthDate;
		this.premium = false;
		this.endPremium = Optional.empty();
	}
	
	public Usuario(Usuario user, boolean backend) {
		super(user.getNumero(), user.getNombre(), user.getIconUrl());
		this.passwd = backend ? null : user.getPasswd();
		this.signature = backend ? null : user.getSignature();
		this.birthDate = backend ? null : user.getBirthDate();
		this.premium = user.isPremium();
		this.endPremium = user.isPremium() ? Optional.of(user.endPremium.get()) : Optional.empty();
	}

	public boolean isPremium() {
		return premium;
	}
	
	public Optional<LocalDate> getEndPremiumDate() {
		return this.endPremium;
	}
	
	public String getPasswd() {
		return passwd;
	}

	public String getSignature() {
		return signature;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public void mekePremium() {
		this.premium = true;
		this.endPremium = Optional.of(LocalDate.now().plusYears(1));
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
}
