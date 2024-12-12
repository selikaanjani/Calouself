package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;

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

	public User() {
		// default constructor
	}

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

	// ACCESS TO DB
	private Connect connect = Connect.getInstance();

	public ArrayList<User> getUsers(ArrayList<User> users) {
		// return all users from database
		String query = "SELECT * FROM User";

		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			connect.rs = prepQuery.executeQuery();
			while (connect.rs.next()) {
				String id = connect.rs.getString("UserID");
				String username = connect.rs.getString("Username");
				String password = connect.rs.getString("Password");
				String phoneNumber = connect.rs.getString("PhoneNumber");
				String address = connect.rs.getString("Address");
				String role = connect.rs.getString("Role");
				users.add(new User(id, username, password, phoneNumber, address, role));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public String generateUserID() {
		ArrayList<User> users = new ArrayList<>();
		// mau bikin newID dgn format ID = US001
		String lastID = users.isEmpty() ? "US000" : users.get(users.size() - 1).getUserID();
		int numericPart = Integer.parseInt(lastID.substring(2));
		String newID = String.format("US%03d", numericPart + 1);
		return newID;
	}

	public String registerUser(String username, String password, String phoneNumber, String address, String role) {
		String newID = generateUserID();
		String query = "INSERT INTO User (UserID, Username, Password, PhoneNumber, Address, Role) "
				+ "VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, newID);
			prepQuery.setString(2, username);
			prepQuery.setString(3, password);
			prepQuery.setString(4, phoneNumber);
			prepQuery.setString(5, address);
			prepQuery.setString(6, role);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			return "error register user: " + e.getMessage();
		}
		return "User registration successful!";
	}

	
}
