package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import model.Item;

public class ItemController {
	private Connect connect = Connect.getInstance();
	
	public ArrayList<Item> getAcceptedItems(ArrayList<Item> items) {
		//return all items that have been accepted by admin
		String query = "SELECT * FROM item WHERE item.ItemStatus = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, "Accepted");
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while(connect.rs.next()) {
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
		//return all accepted items of a specific seller
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
			while(connect.rs.next()) {
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
		//delete item
		//return string alert
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
		//return all items of a specific seller, doesn't do any sorting
		String query = "SELECT * FROM item WHERE item.USERID = ?";
		
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			try {
				prepQuery.setString(1, userID);
				connect.rs = prepQuery.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			while(connect.rs.next()) {
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
		//return all items from database
		String query = "SELECT * FROM item";
		PreparedStatement prepQuery = connect.preparedStatement(query);

		try {
			connect.rs = prepQuery.executeQuery();
			while(connect.rs.next()) {
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
		//validate and do insert new item
		//return string alert
		ArrayList<Item> items = new ArrayList<>();
		items = getItems(items);
		
		if (name.length() == 0) {
			return "name cannot be empty!";
		}
		
		if (name.length() < 3) {
			return "name must be at least 3 characters long!";
		}
		
		if (category.length() == 0) {
			return "category cannot be empty!";
		}
		
		if (category.length() < 3) {
			return "category must be at least 3 characters long!";
		}
		
		if (size.length() == 0) {
			return "size cannot be empty!";
		}
		
		if (price.length() == 0) {
			return "price cannot be empty!";
		}
		
		int parsedPrice;
	    try {
	        parsedPrice = Integer.parseInt(price);
	        if (parsedPrice <= 0) {
	            return "Price must be greater than 0!";
	        }
	    } catch (NumberFormatException e) {
	        return "Price must be a number!";
	    }
	    
	    //generate new ID with format = IT001
	    String lastID = items.isEmpty() ? "IT000" : items.get(items.size() - 1).getItemID();
	  	int numericPart = Integer.parseInt(lastID.substring(2));
	  	String newID = String.format("IT%03d", numericPart + 1);
	  	String status = "Pending"; //by default pending bcs not yet reviewed by admin
	  	
	  	//do insert
	  	String query = "INSERT INTO item (ItemID, Name, Category, Size, Price, UserID, ItemStatus) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement prepQuery = connect.preparedStatement(query);
			
		try {
			prepQuery.setString(1, newID);
			prepQuery.setString(2, name);
			prepQuery.setString(3, category);
			prepQuery.setString(4, size);
			prepQuery.setString(5, price);
			prepQuery.setString(6, userID);
			prepQuery.setString(7, status);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			return "error uploading item: " + e.getMessage();
		}
		
	    return "item upload successful!";
	}
	
	public String editItem(String name, String category, String size, String price, String itemID) {
		//validate input and edit item
		//return string alert
		if (name.length() == 0) {
			return "name cannot be empty!";
		}
		
		if (name.length() < 3) {
			return "name must be at least 3 characters long!";
		}
		
		if (category.length() == 0) {
			return "category cannot be empty!";
		}
		
		if (category.length() < 3) {
			return "category must be at least 3 characters long!";
		}
		
		if (size.length() == 0) {
			return "size cannot be empty!";
		}
		
		if (price.length() == 0) {
			return "price cannot be empty!";
		}
		
		int parsedPrice;
	    try {
	        parsedPrice = Integer.parseInt(price);
	        if (parsedPrice <= 0) {
	            return "Price must be greater than 0!";
	        }
	    } catch (NumberFormatException e) {
	        return "Price must be a number!";
	    }
	    
	    //do update/edit
	    String query = "UPDATE item\n" +
				"SET Name = ?, Category = ?, Size = ?, Price = ?\n" +
				"WHERE item.ItemID = ?";

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
	            return "no such item found. update failed.";
	        }
		} catch (SQLException e) {
			return "error updating item: " + e.getMessage();
		}
	}
}
