package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import model.Item;

public class WishlistController {
	private static Connect connect = Connect.getInstance();

	public String addItemToWishlist(String userID, String itemID) {
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

	public String removeItemFromWishlist(String wishlistID) {
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

	public ArrayList<Item> viewWishlist(String userID) {
		ArrayList<Item> wishlistItems = new ArrayList<>();
		String query = "SELECT I.ItemID, I.Name, I.Category, I.Size, I.Price " + "FROM Wishlist W "
				+ "JOIN Item I ON W.ItemID = I.ItemID " + "WHERE W.UserID = ?";
		
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, userID);
			connect.rs = prepQuery.executeQuery();

			while (connect.rs.next()) {
				Item item = new Item();
				item.setItemID(connect.rs.getString("ItemID"));
				item.setName(connect.rs.getString("Name"));
				item.setCategory(connect.rs.getString("Category"));
				item.setSize(connect.rs.getString("Size"));
				item.setPrice(connect.rs.getString("Price"));
				wishlistItems.add(item);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching wishlist: " + e.getMessage());
		}

		return wishlistItems;
	}

}
