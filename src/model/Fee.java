package model;

import java.util.UUID;

public class Fee {
	private UUID uuid;
	private String rollno;
	private double amount;

	public Fee() {
	}

	public Fee(String rollno, double amount) {
		this.rollno = rollno;
		this.amount = amount;
		this.uuid = UUID.randomUUID();
	}

	public Fee(UUID uuid, String rollno, double amount) {
		this.uuid = uuid;
		this.rollno = rollno;
		this.amount = amount;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getRollno() {
		return rollno;
	}

	public void setRollno(String rollno) {
		this.rollno = rollno;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
