package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import model.User;

public class UserController {
	private Connect connect = Connect.getInstance();
	private static User currentlyLoggedInUser;

	public ArrayList<User> getUsers(ArrayList<User> users) {
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
//		String query = "SELECT * FROM user";
//		connect.rs = connect.execQuery(query);
//		
//		try {
//			while (connect.rs.next()) {
//				String id = connect.rs.getString("UserID");
//				String username = connect.rs.getString("Username");
//				String password = connect.rs.getString("Password");
//				String phoneNumber = connect.rs.getString("PhoneNumber");
//				String address = connect.rs.getString("Address");
//				String role = connect.rs.getString("Role");
//				users.add(new User(id, username, password, phoneNumber, address, role));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		return users;
	}

	public ArrayList<String> loginUser(String username, String password) {
		ArrayList<User> users = new ArrayList<>();
		users = getUsers(users);
//		String alert = "";
		ArrayList<String> temp = new ArrayList<String>();
		if (username.length() == 0) {
			temp.add("username can't be empty!");
			return temp;
		}

		if (password.length() == 0) {
			temp.add("password can't be empty!");
			return temp;
		}

		for (User user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				currentlyLoggedInUser = user;
//				System.out.println(user);
				temp.add("user login successful!");
				temp.add(user.getRole());
				return temp;
			}
		}
		temp.add("user not found!");
		return temp;
	}

	public String registerUser(String username, String password, String phoneNumber, String address, String role) {
		ArrayList<User> users = new ArrayList<>();
		users = getUsers(users);
		String alert = "";
		if (username.length() == 0) {
			return "username can't be empty!";
		}

		if (username.length() < 3) {
			return "username must at least be 3 characters long!";
		}

		for (User user : users) {
			if (user.getUsername().equals(username)) {
				return "username must be unique, this username has been taken already!";
			}
		}

		if (password.length() == 0) {
			return "password can't be empty!";
		}

		if (password.length() < 8) {
			return "password must at least be 8 characters long!";
		}

		// phone number ga dicek empty apa ga krna di soal ga ditulis cannot be empty
		// jdi aku buatnya phone number bisa dikosongin jdi optional

		if (specialCharactersExist(password) == false) {
			return "password must contain a special character!";
		}

		if (phoneNumber.length() != 0) {
			if (phoneNumber.length() < 3) {
				return "phone number is too short, must start with +62!";
			}

			if (startsWithPlus62(phoneNumber) == false) {
				return "phone number must start with +62!";
			}

			if ((phoneNumber.length() - 3 + 1) < 10) {
				return "phone number must at least be 10 characters long where +62 is considered as one character only!";
			}
		}

		if (address.length() == 0) {
			return "address can't be empty!";
		}

		if (role.length() == 0) {
			return "role must be picked!";
		}

		// format = US001
		String lastID = users.isEmpty() ? "US000" : users.get(users.size() - 1).getUserID();
		int numericPart = Integer.parseInt(lastID.substring(2));
		String newID = String.format("US%03d", numericPart + 1);

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
			e.printStackTrace();
		}

//		String query = "INSERT INTO User " + "VALUES ('"+ newID +"', '"+ username +"', '"+ password +"', '"+ phoneNumber +"', '"+ address +"', '"+ role +"')";
//		connect.execUpdate(query);
		alert = "user registration successful!";

		return alert;
	}

	private boolean specialCharactersExist(String word) {
		char[] specialCharacters = { '!', '@', '#', '$', '%', '^', '&', '*' };

		for (int i = 0; i < word.length(); i++) {
			for (char specialChar : specialCharacters) {
				if (word.charAt(i) == specialChar) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean startsWithPlus62(String word) {
		String temp = "" + word.charAt(0) + word.charAt(1) + word.charAt(2);
		if (temp.equals("+62")) {
			return true;
		}
		return false;
	}

	public User getCurrentlyLoggedInUser() {
		return currentlyLoggedInUser;
	}

}
