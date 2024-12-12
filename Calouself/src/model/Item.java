package model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.loading.PrivateClassLoader;

import controller.ItemController;
import database.Connect;

public class Item {
//	ItemID VARCHAR(255) NOT NULL PRIMARY KEY,
//	 Name VARCHAR(255) NOT NULL,
//	 Category VARCHAR(255) NOT NULL,
//	 Size VARCHAR(255) NOT NULL,
//	 Price VARCHAR(255) NOT NULL,
//	 UserID VARCHAR(255) NOT NULL, //buat seller nya siapa
//	 ItemStatus VARCHAR(255) NOT NULL, //pending accepted denied
//	 ItemWishlist VARCHAR(255), //?
//	 ItemOfferStatus VARCHAR(255), //ini not null dlu krna blm tentu bkl ada offer apa lgi pas baru create udah psti blm ada
//	 FOREIGN KEY (UserID) REFERENCES User(UserID))

	private String itemID;
	private String name;
	private String category;
	private String size;
	private String price;
	private String itemStatus;
	private String itemWishlist;
	private String itemOfferStatus;
	private String transactionID;

	public String getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public Item(String itemID, String name, String category, String size, String price, String itemStatus,
			String itemWishlist, String itemOfferStatus) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.category = category;
		this.size = size;
		this.price = price;
		this.itemStatus = itemStatus;
		this.itemWishlist = itemWishlist;
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
		// itemwishlist krg tau apa, itemofferstatus klo baru create krna emg blm ada
		// offer jdi kosong dlu
	}
	
	public Item(String itemID, String name, String category, String size) {
		super();
		this.itemID = itemID;
		this.name = name;
		this.category = category;
		this.size = size;
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

	public String getItemWishlist() {
		return itemWishlist;
	}

	public void setItemWishlist(String itemWishlist) {
		this.itemWishlist = itemWishlist;
	}

	public String getItemOfferStatus() {
		return itemOfferStatus;
	}

	public void setItemOfferStatus(String itemOfferStatus) {
		this.itemOfferStatus = itemOfferStatus;
	}

	// ACCESS TO DB
	private Connect connect = Connect.getInstance();

//	public String acceptOffer(String itemID) {
//		Transaction tr = new Transaction();
//		User user = new User();
//		String userID = user.getUserID();
//		String transactionID = tr.generateTransactionID();
//		tr.createTransaction(transactionID, userID, itemID);
//		return itemID;
//	}

	public String acceptOffer(String itemID) {
		Transaction tr = new Transaction();
		User user = new User();
		String userID = user.getUserID();
		String transactionID = tr.generateTransactionID();
		tr.createTransaction(transactionID, userID, itemID);
		return "Offer for item " + itemID + " has been accepted.";
	}

	public String declineOffer(String itemID) {
		String query = "DELETE FROM Offer WHERE ItemID = ?";

		try (PreparedStatement stmt = connect.preparedStatement(query)) {
			stmt.setString(1, itemID);

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Offer for item " + itemID + " has been declined.");
				return "Offer for item " + itemID + " has been declined.";
			} else {
				System.out.println("No offer found for item " + itemID);
				return "No offer found for item " + itemID;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error: Unable to decline offer for item " + itemID;
		}
	}

	public List<Offer> getOfferedItems() {
		String query = "SELECT o.offerID, o.userID, o.itemID, o.offerPrice, i.name, i.category, i.size "
				+ "FROM Offer o " + "JOIN Item i ON o.itemID = i.itemID";

		List<Offer> offers = new ArrayList<>();

		try (PreparedStatement stmt = connect.preparedStatement(query)) {

			while (connect.rs.next()) {
				String offerID = connect.rs.getString("offerID");
				String userID = connect.rs.getString("userID");
				String itemID = connect.rs.getString("itemID");
				String offerPrice = connect.rs.getString("offerPrice");
				String name = connect.rs.getString("name");
				String category = connect.rs.getString("category");
				String size = connect.rs.getString("size");

				Item item = new Item(itemID, name, category, size);
				Offer offer = new Offer(offerID, userID, itemID, offerPrice);

				offers.add(offer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return offers;
	}

	public String highestOffer(String itemID) {
		String currentHighestOfferQuery = "SELECT MAX(OfferPrice) FROM Offer WHERE ItemID = ?";
		String currentHighestOffer = "0";
		try (PreparedStatement stmt = connect.preparedStatement(currentHighestOfferQuery)) {
			stmt.setString(1, itemID);
			connect.rs = stmt.executeQuery();
			if (connect.rs.next()) {
				currentHighestOffer = String.valueOf(connect.rs.getDouble(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error fetching the highest offer price.";
		}
		return currentHighestOffer;
	}

	public String offerPrice(String itemID, String userID, String offerPrice) {
		String offerID = "";
		String query = "SELECT MAX(OfferID) FROM Offer";

		try (PreparedStatement prepQuery = connect.preparedStatement(query)) {
			connect.rs = prepQuery.executeQuery();

			if (connect.rs.next()) {
				String lastOfferID = connect.rs.getString(1);
				// generate offer id
				if (lastOfferID != null) {
					int lastNumber = Integer.parseInt(lastOfferID.substring(2));
					offerID = String.format("OF%03d", lastNumber + 1);
				} else {
					offerID = "OF001";
				}
			}

			String insertQuery = "INSERT INTO Offer (OfferID, ItemID, UserID, OfferPrice) VALUES (?, ?, ?, ?)";
			try (PreparedStatement insertStmt = connect.preparedStatement(insertQuery)) {
				insertStmt.setString(1, offerID);
				insertStmt.setString(2, itemID);
				insertStmt.setString(3, userID);
				insertStmt.setString(4, offerPrice);
				int rowsAffected = insertStmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Offer submitted successfully with OfferID: " + offerID);
					return "success";
				} else {
					System.out.println("Failed to submit the offer.");
					return "error";
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return "Error while inserting the offer.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error while retrieving the last OfferID.";
		}
	}

	public Item getItemById(String itemID) {
		String query = "SELECT * FROM Item WHERE ItemID = ?";
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
				prepQuery.setString(1, "Approved");
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
				String itemWishlist = connect.rs.getString("ItemWishlist");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
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
				String itemWishlist = connect.rs.getString("ItemWishlist");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
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
				String itemWishlist = connect.rs.getString("ItemWishlist");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
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
				String itemWishlist = connect.rs.getString("ItemWishlist");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
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
				return "Item edit successful!";
			} else {
				return "No such item found. update failed.";
			}
		} catch (SQLException e) {
			return "error updating item: " + e.getMessage();
		}
	}

	public void approveItem(String itemID) {
		String query = "UPDATE item SET ItemStatus = 'Approved' WHERE ItemID = ?";
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
				String itemWishlist = connect.rs.getString("ItemWishlist");
				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

}
