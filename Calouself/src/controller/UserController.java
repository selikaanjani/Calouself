package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import model.User;

public class UserController {
	private User userModel = new User();
	private static User currentlyLoggedInUser; // static supaya yg diakses dari class" lain sama

	public ArrayList<User> getUsers(ArrayList<User> users) {
		return userModel.getUsers(users);
	}

	public ArrayList<String> loginUser(String username, String password) {
		// validate user credentials and do login
		// return an arraylist of string, index 0 return string alert, index 1 return
		// role nya (klo berhasil login)
		ArrayList<User> users = new ArrayList<>();
		users = getUsers(users);
//		String alert = "";

		ArrayList<String> temp = new ArrayList<String>();

		// Validasi untuk admin
		if (username.equals("admin") && password.equals("admin")) {
			currentlyLoggedInUser = new User("US000", "admin", "admin", "", "Admin Address", "Admin");
			temp.add("User login successful!");
			temp.add(currentlyLoggedInUser.getRole());
			return temp;
		}

		if (username.length() == 0) {
			temp.add("Username can't be empty!");
			return temp;
		}

		if (password.length() == 0) {
			temp.add("Password can't be empty!");
			return temp;
		}

		for (User user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				// login success
				currentlyLoggedInUser = user;
				temp.add("User login successful!");
				temp.add(user.getRole());
				return temp;
			}
		}
		// login failure
		temp.add("User not found!");
		return temp;
	}

	public String registerUser(String username, String password, String phoneNumber, String address, String role) {
		// validate and insert new user to database
		// return string alert (keterangannya)
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
		// udah slsi validasi

		// do insert user
		userModel.registerUser(username, password, phoneNumber, address, role);
		alert = "User registration successful!";
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
		// check if phone number starts with +62
		String temp = "" + word.charAt(0) + word.charAt(1) + word.charAt(2);
		if (temp.equals("+62")) {
			return true;
		}
		return false;
	}

	public User getCurrentlyLoggedInUser() {
		// get user that's currently logged in
		return currentlyLoggedInUser;
	}

}
