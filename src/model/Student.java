package model;

import java.util.UUID;

public class Student {
	private UUID uuid;
	private String name,college,roomno,rollno;

	public Student(UUID uuid, String name, String college, String roomno, String rollno) {
		this.uuid = uuid;
		this.name = name;
		this.college = college;
		this.roomno = roomno;
		this.rollno = rollno;
	}

	public Student(String name, String college, String roomno, String rollno) {
		this.name = name;
		this.college = college;
		this.roomno = roomno;
		this.rollno = rollno;
		this.uuid = UUID.randomUUID();
	}

	public Student() {

	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getRoomno() {
		return roomno;
	}

	public void setRoomno(String roomno) {
		this.roomno = roomno;
	}

	public String getRollno() {
		return rollno;
	}

	public void setRollno(String rollno) {
		this.rollno = rollno;
	}
}

