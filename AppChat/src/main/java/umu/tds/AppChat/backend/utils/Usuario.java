package umu.tds.AppChat.backend.utils;

import java.time.LocalDate;

public class Usuario extends EntidadComunicable {

	private String passwd;
	private String signature;
	private LocalDate birthDate;
	
	
	public Usuario(String name, int number, String passwd, LocalDate birthDate, String profilePicUrl, String signature) {
		super(number, name, profilePicUrl);
		this.passwd = passwd;
		this.signature = signature;
		this.birthDate = birthDate;
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
