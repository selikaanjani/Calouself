package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.Connect;
import model.Wishlist;

public class WishlistController {
	private static Connect connect = Connect.getInstance();
	private Wishlist wishlists;

	public String addToWishlist(String userID, String itemID) {
		// Generate new Wishlist ID with format WL001
		String queryID = "SELECT MAX(WishlistID) AS lastID FROM Wishlist";
		String lastID = "WL000";

		try {
			connect.rs = connect.execQuery(queryID);
			if (connect.rs.next() && connect.rs.getString("lastID") != null) {
				lastID = connect.rs.getString("lastID");
			}
		} catch (SQLException e) {
			return "Error fetching last Wishlist ID: " + e.getMessage();
		}

		// Generate the new ID for the wishlist
		int numericPart = Integer.parseInt(lastID.substring(2));
		String newID = String.format("WL%03d", numericPart + 1);

		// Insert into the database
		String query = "INSERT INTO Wishlist (WishlistID, UserID, ItemID) VALUES (?, ?, ?)";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, newID);
			prepQuery.setString(2, userID);
			prepQuery.setString(3, itemID);
			int rowsAffected = prepQuery.executeUpdate();
			if (rowsAffected > 0) {
				return "Item successfully added to wishlist!";
			} else {
				return "Error adding item to wishlist!";
			}
		} catch (SQLException e) {
			return "Error adding item to wishlist: " + e.getMessage();
		}
	}

	public String removeFromWishlist(String wishlistID) {
		String query = "DELETE FROM Wishlist WHERE WishlistID = ?";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, wishlistID);
			int rowsAffected = prepQuery.executeUpdate();
			if (rowsAffected > 0) {
				return "Item successfully removed from wishlist!";
			} else {
				return "Wishlist entry not found!";
			}
		} catch (SQLException e) {
			return "Error removing item from wishlist: " + e.getMessage();
		}

	}

	public String getWishlistID(String userID, String itemID) {
		// SQL query to select the WishlistID for a given UserID and ItemID
		String query = "SELECT WishlistID FROM Wishlist WHERE UserID = ? AND ItemID = ?";

		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			// Set parameters for the UserID and ItemID in the query
			prepQuery.setString(1, userID);
			prepQuery.setString(2, itemID);

			// Execute the query and get the result set
			connect.rs = prepQuery.executeQuery();

			// Check if a result was found
			if (connect.rs.next()) {
				// Return the WishlistID
				return connect.rs.getString("WishlistID");
			} else {
				return "Wishlist entry not found!";
			}
		} catch (SQLException e) {
			return "Error retrieving WishlistID: " + e.getMessage();
		}
	}

}
