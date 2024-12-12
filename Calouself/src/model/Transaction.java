package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.Connect;

public class Transaction {
	private String transactionID;
	private String userID;
	private String itemID;
	
	public Transaction() {
		//default constructor
	}

	public Transaction(String transactionID, String userID, String itemID) {
		super();
		this.transactionID = transactionID;
		this.userID = userID;
		this.itemID = itemID;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	// ACCESS TO DB
	private Connect connect = Connect.getInstance();

	public String purchasedItems(String userID, String itemID) {
		// Generate new Transaction ID with format TR001
		String queryID = "SELECT MAX(TransactionID) AS lastID FROM Transaction";
		String lastID = "TR000";

		try {
			connect.rs = connect.execQuery(queryID);
			if (connect.rs.next() && connect.rs.getString("lastID") != null) {
				lastID = connect.rs.getString("lastID");
			}
		} catch (SQLException e) {
			return "Error fetching last Transaction ID: " + e.getMessage();
		}

		// Generate the new ID for the transaction
		int numericPart = Integer.parseInt(lastID.substring(2));
		String newID = String.format("TR%03d", numericPart + 1);

		// Insert into the database
		String query = "INSERT INTO Transaction (TransactionID, UserID, ItemID) VALUES (?, ?, ?)";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, newID);
			prepQuery.setString(2, userID);
			prepQuery.setString(3, itemID);
			int rowsAffected = prepQuery.executeUpdate();
			if (rowsAffected > 0) {
				return "Item successfully added to transaction!";
			} else {
				return "Error adding item to transaction!";
			}
		} catch (SQLException e) {
			return "Error adding item to transaction: " + e.getMessage();
		}
	}

	
	
}
