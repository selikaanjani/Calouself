package model;

public class User {
//	UserID VARCHAR(255) NOT NULL PRIMARY KEY,
//	 Username VARCHAR(255) NOT NULL,
//	 Password VARCHAR(255) NOT NULL,
//	 PhoneNumber VARCHAR(255), //krna di soal gaada tulisan cannot be empty
//	 Address VARCHAR(255) NOT NULL,
//	 Role VARCHAR(255) NOT NULL

	private String userID;
	private String username;
	private String password;
	private String phoneNumber;
	private String address;
	private String role;
	
	public User(String userID, String username, String password, String phoneNumber, String address, String role) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.role = role;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	
	
}
