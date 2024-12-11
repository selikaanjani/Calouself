package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import javafx.util.Callback;
import model.Item;

public class ItemController {
	private Connect connect = Connect.getInstance();
	
	
	
	public ArrayList<Item> getAcceptedItems(ArrayList<Item> items) {
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
		String query = "DELETE FROM item WHERE ItemID = ?";
		PreparedStatement prepQuery = connect.preparedStatement(query);
		
		try {
			prepQuery.setString(1, itemID);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "item delete successful!";
	}
	
	public ArrayList<Item> getSellerHistoryItems(ArrayList<Item> items, String userID) {
//		String query = "SELECT * FROM item WHERE item.USERID = '"+ userID +"';";
//		connect.rs = connect.execQuery(query);
//		try {
//			while (connect.rs.next()) {
//				String id = connect.rs.getString("ItemID");
//				String name = connect.rs.getString("Name");
//				String category = connect.rs.getString("Category");
//				String size = connect.rs.getString("Size");
//				String price = connect.rs.getString("Price");
//				String itemStatus = connect.rs.getString("ItemStatus");
//				String itemWishlist = connect.rs.getString("ItemWishlist");
//				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
//				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
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
//		String query = "SELECT * FROM item";
//		connect.rs = connect.execQuery(query);
//		try {
//			while (connect.rs.next()) {
//				String id = connect.rs.getString("ItemID");
//				String name = connect.rs.getString("Name");
//				String category = connect.rs.getString("Category");
//				String size = connect.rs.getString("Size");
//				String price = connect.rs.getString("Price");
//				String itemStatus = connect.rs.getString("ItemStatus");
//				String itemWishlist = connect.rs.getString("ItemWishlist");
//				String itemOfferStatus = connect.rs.getString("ItemOfferStatus");
//				items.add(new Item(id, name, category, size, price, itemStatus, itemWishlist, itemOfferStatus));
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
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
		ArrayList<Item> items = new ArrayList<>();
		items = getItems(items);
		String alert = "";
		
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
	    
	    //format = IT001
	    String lastID = items.isEmpty() ? "IT000" : items.get(items.size() - 1).getItemID();
	  	int numericPart = Integer.parseInt(lastID.substring(2));
	  	String newID = String.format("IT%03d", numericPart + 1);
	  	String status = "Pending";
	  		
//	  	String query = "INSERT INTO item (ItemID, Name, Category, Size, Price, UserID, ItemStatus) " + "VALUES ('"+ newID +"', '"+ name +"', '"+ category +"', '"+ size +"', '"+ price +"', '"+ userID +"', '"+ status +"')";
//	  	connect.execUpdate(query);
	  	
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
			e.printStackTrace();
		}
		
	    return "item upload successful!";
	}
	
	public String editItem(String name, String category, String size, String price, String itemID) {
		String alert = "";
		
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
	  		
//	    String query = String.format("UPDATE item\n" +
//				"SET Name = '%s', Category = '%s', Size = '%s', Price = '%s'\n" +
//				"WHERE item.ItemID = '%s'", name, category, size, price, itemID);
//				
//		connect.execUpdate(query);
	    
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
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return "item edit successful!";
	}
	
	//void untuk mengubah status item menjadi Approve
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


	
	//untuk get semua items baik yg sudah accepted ataupun belum accepted
	public ArrayList<Item> getAllItems() {
	    ArrayList<Item> items = new ArrayList<>();
	    String query = "SELECT * FROM item";  // Ambil semua item
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
