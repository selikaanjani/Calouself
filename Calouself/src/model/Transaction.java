package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Transaction {
	private String transactionID;
	private String userID;
	private String itemID;

	public Transaction() {
		// default constructor
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

	public String generateTransactionID() {
		// You can use your existing code to generate a new transaction ID
		String queryID = "SELECT MAX(TransactionID) AS lastID FROM Transaction";
		String lastID = "TR000";

		try {
			connect.rs = connect.execQuery(queryID);
			if (connect.rs.next() && connect.rs.getString("lastID") != null) {
				lastID = connect.rs.getString("lastID");
			}
		} catch (SQLException e) {
			return null;
		}

		int numericPart = Integer.parseInt(lastID.substring(2));
		String newID = String.format("TR%03d", numericPart + 1);
//		System.out.println(lastID);
//		System.out.println(newID);
		return newID;
	}

	public String createTransaction(String transactionID, String userID, String itemID) {
		if (transactionID == null || userID == null || itemID == null) {
			return "Error: Invalid transaction details!";
		}

		String insertQuery = "INSERT INTO Transaction (TransactionID, UserID, ItemID) VALUES (?, ?, ?)";
		String deleteWishlistQuery = "DELETE FROM Wishlist WHERE ItemID = ?";
		String updateItemQuery = "UPDATE item SET BoughtStatus = 'yes' WHERE itemID = ?";

		try (PreparedStatement prepQuery = connect.preparedStatement(insertQuery)) {
			prepQuery.setString(1, transactionID);
			prepQuery.setString(2, userID);
			prepQuery.setString(3, itemID);

			int rowsAffected = prepQuery.executeUpdate();

			if (rowsAffected > 0) {
				// Hapus item dari wishlist
				try (PreparedStatement deleteQuery = connect.preparedStatement(deleteWishlistQuery)) {
					deleteQuery.setString(1, itemID);
					int wishlistRowsAffected = deleteQuery.executeUpdate();
					
					try (PreparedStatement updateQuery = connect.preparedStatement(updateItemQuery)) {
						updateQuery.setString(1, itemID);
						int itemRowsAffected = updateQuery.executeUpdate();
						
						if (wishlistRowsAffected > 0 && itemRowsAffected > 0) {
							return "Transaction successful and item removed from wishlist!";
						} else {
							return "Transaction successful, but item not found in wishlist.";
						}
					}

					
				}
			}
		} catch (SQLException e) {
			return "Error adding transaction: " + e.getMessage();
		}

		return "Unknown error occurred!";
	}

	public ObservableList<Transaction> viewHistory(String userID) {
		String query = "SELECT t.TransactionID, t.ItemID FROM Transaction t WHERE t.UserID = ?";
		ObservableList<Transaction> transactionHistory = FXCollections.observableArrayList();

		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, userID);
			connect.rs = prepQuery.executeQuery();

			while (connect.rs.next()) {
				String transactionID = connect.rs.getString("TransactionID");
				String itemID = connect.rs.getString("ItemID");
				Transaction transaction = new Transaction(transactionID, userID, itemID);
				transactionHistory.add(transaction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return transactionHistory;
	}

}
