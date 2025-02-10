package umu.tds.AppChat.backend.utils;

public class Membership {
	private MembershipType type;
	private String name;
	private double price;
	
	public Membership(MembershipType type, String name, double price) {
		this.type = type;
		this.name = name;
		this.price = price;
	}
	
	public Membership(Membership mship) {
		this.type = mship.getType();
		this.name = mship.getName();
		this.price = mship.getPrice();
	}
	
	public double getPrice() {
		return price;
	}
	
	public MembershipType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static enum MembershipType {
		STANDAR, SPECIAL, CELEBRATION
	}
}

