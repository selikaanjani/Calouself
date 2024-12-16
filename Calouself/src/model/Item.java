package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import controller.UserController;
import database.Connect;

public class Item {
//	ItemID VARCHAR(255) NOT NULL PRIMARY KEY,
//	 Name VARCHAR(255) NOT NULL,
//	 Category VARCHAR(255) NOT NULL,
//	 Size VARCHAR(255) NOT NULL,
//	 Price VARCHAR(255) NOT NULL,
//	 UserID VARCHAR(255) NOT NULL, //buat seller nya siapa
//	 ItemStatus VARCHAR(255) NOT NULL, //pending accepted denied
//	 OfferPrice VARCHAR(255), // offer price
//	 ItemOfferStatus VARCHAR(255), //ini not null dlu krna blm tentu bkl ada offer apa lgi pas baru create udah psti blm ada
//	 FOREIGN KEY (UserID) REFERENCES User(UserID))

	private String itemID;
	private String name;
	private String category;
	private String size;
	private String price;
	private String itemStatus;
	private String offerPrice;
	private String itemOfferStatus;

	private String transactionID;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Item(String itemID, String name, String category, String size, String price, String itemStatus,
			String offerPrice, String itemOfferStatus) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.category = category;
		this.size = size;
		this.price = price;
		this.itemStatus = itemStatus;
		this.offerPrice = offerPrice;
		this.itemOfferStatus = itemOfferStatus;
	}

	public Item(String itemID, String name, String category, String size, String price, String itemStatus) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.category = category;
		this.size = size;
		this.price = price;
		this.itemStatus = itemStatus;
		// itemofferstatus klo baru create krna emg blm ada
		// offer jdi kosong dlu
	}

	public Item(String itemID, String name, String category, String size, String price, String offerPrice,
			String itemOfferStatus) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.category = category;
		this.size = size;
		this.price = price;
		this.offerPrice = offerPrice;
		this.itemOfferStatus = itemOfferStatus;
		// constructor untuk create offer
	}

	public Item() {
		// default constructor
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getItemOfferStatus() {
		return itemOfferStatus;
	}

	public void setItemOfferStatus(String itemOfferStatus) {
		this.itemOfferStatus = itemOfferStatus;
	}

	Transaction tr = new Transaction();
	User user = new User();
	UserController uc = new UserController();

	// ACCESS TO DB
	private Connect connect = Connect.getInstance();

	public String acceptOffer(String itemID) {
		String query = "UPDATE item SET ItemOfferStatus = 'Accepted' WHERE ItemID = ?";

		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, itemID);

			int affectedRows = prepQuery.executeUpdate();

			if (affectedRows > 0) {
				String transactionID = tr.generateTransactionID();
				User curr = uc.getCurrentlyLoggedInUser();
				String userID = curr.getUserID();
				String alert = tr.createTransaction(transactionID, userID, itemID);

				return alert;
			} else {
				return "Failed to accept offer.";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Error accepting offer.";
		}
	}

	public String declineOffer(String itemID) {
	    System.out.println("Declining offer for item: " + itemID);

		String query = "UPDATE item SET ItemOfferStatus = 'Declined', OfferPrice = 0 WHERE ItemID = ?";
		//ini kita kek mau refresh aja gitu loh

		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, itemID);

			int affectedRows = prepQuery.executeUpdate();

			if (affectedRows > 0) {
				return "Offer declined successfully!";
			} else {
				return "Failed to decline offer.";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return "Error declining offer.";
		}
	}

	public String getOfferedPrice(String itemID) {
		String query = "SELECT OfferPrice FROM item WHERE ItemID = ?";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, itemID);
			connect.rs = prepQuery.executeQuery();

			if (connect.rs.next()) {
				String offerPrice = connect.rs.getString("OfferPrice");
                return (offerPrice != null && !offerPrice.trim().isEmpty()) ? offerPrice : "0";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "0";
	}

	public String offerPrice(String itemID, String offerPrice) {
		String query = "UPDATE item SET OfferPrice = ?, ItemOfferStatus = ? WHERE ItemID = ?";
		String status = "Pending";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, offerPrice);
			prepQuery.setString(2, status);
			prepQuery.setString(3, itemID);

			int rowsUpdated = prepQuery.executeUpdate();
			if (rowsUpdated > 0) {
				return "Offer submitted successfully!";
			} else {
				return "Item not found!";
			}
		} catch (SQLException e) {
			return "Database error: " + e.getMessage();
		}
	}

	public String getCurrentOfferPrice(String itemID) {
		String query = "SELECT item.OfferPrice AS offerPrice FROM item WHERE ItemID = ?";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, itemID);
			if (connect.rs.next()) {
				return connect.rs.getString("offerPrice");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "0";
	}
	
	public ArrayList<Item> getSellerOfferedItems(String userID) {
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT * FROM item WHERE UserID = ? AND item.ItemOfferStatus = 'Pending';";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, userID);
			connect.rs = prepQuery.executeQuery();

			if (connect.rs.next()) {
				String itemID = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String itemOfferPrice = connect.rs.getString("OfferPrice");

				items.add(new Item(itemID, name, category, size, price,  itemOfferPrice, itemStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public Item getItemById(String itemID) {
		String query = "SELECT * FROM item WHERE ItemID = ?";
		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			prepQuery.setString(1, itemID);
			connect.rs = prepQuery.executeQuery();

			if (connect.rs.next()) {
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");

				return new Item(itemID, name, category, size, price, itemStatus);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Item> getAcceptedItems(ArrayList<Item> items) {
		// return all items that have been accepted by admin
		String query = "SELECT * FROM item WHERE item.ItemStatus = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, "Accepted");
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<Item> getSellerAcceptedItems(ArrayList<Item> items, String userID) {
		// return all accepted items of a specific seller
		String query = "SELECT * FROM item WHERE item.ItemStatus = ? AND item.USERID = ?;";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, "Accepted");
				prepQuery.setString(2, userID);
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public String deleteItem(String itemID) {
		String query = "DELETE FROM item WHERE ItemID = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, itemID);
			int rowsAffected = prepQuery.executeUpdate();
			if (rowsAffected > 0) {
				return "item delete successful!";
			} else {
				return "item not found. no deletion performed.";
			}
		} catch (SQLException e) {
			return "error deleting item: " + e.getMessage();
		}
	}

	public ArrayList<Item> getSellerHistoryItems(ArrayList<Item> items, String userID) {
		String query = "SELECT * FROM item WHERE item.USERID = ?";

		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, userID);
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<Item> getItems(ArrayList<Item> items) {
		// return all items from database
		String query = "SELECT * FROM item";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			connect.rs = prepQuery.executeQuery();
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public String uploadItem(String name, String category, String size, String price, String userID) {
		// Generate new ID
		String newID = generateNewID();

		// do insert
		String query = "INSERT INTO item (ItemID, Name, Category, Size, Price, UserID, ItemStatus) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, newID);
			prepQuery.setString(2, name);
			prepQuery.setString(3, category);
			prepQuery.setString(4, size);
			prepQuery.setString(5, price);
			prepQuery.setString(6, userID);
			prepQuery.setString(7, "Pending"); // by default pending bcs not yet reviewed by admin
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			return "error uploading item: " + e.getMessage();
		}
		return "item upload successful!";
	}

	private String generateNewID() {
		ArrayList<Item> items = getItems(new ArrayList<>());
		String lastID = items.isEmpty() ? "IT000" : items.get(items.size() - 1).getItemID();
		int numericPart = Integer.parseInt(lastID.substring(2));
		return String.format("IT%03d", numericPart + 1);
	}

	public String editItem(String name, String category, String size, String price, String itemID) {
		String query = "UPDATE item\n" + "SET Name = ?, Category = ?, Size = ?, Price = ?\n" + "WHERE item.ItemID = ?";

		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, name);
			prepQuery.setString(2, category);
			prepQuery.setString(3, size);
			prepQuery.setString(4, price);
			prepQuery.setString(5, itemID);
			int rowsAffected = prepQuery.executeUpdate();

			if (rowsAffected > 0) {
				return "item edit successful!";
			} else {
				return "No such item found. update failed.";
			}
		} catch (SQLException e) {
			return "error updating item: " + e.getMessage();
		}
	}

	public void approveItem(String itemID) {
		String query = "UPDATE item SET ItemStatus = 'Accepted' WHERE ItemID = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			prepQuery.setString(1, itemID);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// untuk get semua items baik yg sudah accepted ataupun belum accepted
	public ArrayList<Item> getAllItems() {
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT * FROM item"; // Ambil semua item
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			connect.rs = prepQuery.executeQuery();
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<Item> getPendingItems() {
		// return all items that have havent been reviewed by the admin
		ArrayList<Item> items = new ArrayList<>();
		String query = "SELECT * FROM item WHERE item.ItemStatus = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, "Pending");
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while (connect.rs.next()) {
				String id = connect.rs.getString("ItemID");
				String name = connect.rs.getString("Name");
				String category = connect.rs.getString("Category");
				String size = connect.rs.getString("Size");
				String price = connect.rs.getString("Price");
				String itemStatus = connect.rs.getString("ItemStatus");
				String offerPrice = connect.rs.getString("OfferPrice");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, offerPrice, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}
	

}
