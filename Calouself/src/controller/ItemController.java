package controller;

import java.util.ArrayList;
import java.util.List;

import model.Item;
import model.Offer;

public class ItemController {
	private Item itemModel = new Item();
	
	public String acceptOffer(String itemID) {
		return itemModel.acceptOffer(itemID);
	}
	
	public String declineOffer(String itemID) {
		return itemModel.declineOffer(itemID);
	}
	
	public List<Offer> getOfferedItems() {
		return itemModel.getOfferedItems();
	}

	public String offerPrice(String itemID, String userID, String offerPrice) {
		String highOffer = itemModel.highestOffer(itemID);
		double parsedOfferPrice = Double.parseDouble(offerPrice);
		double parsedHighestPrice = Double.parseDouble(highOffer);
		// validasi
		if (offerPrice.isEmpty()) {
			return "Price can't be empty";
		} else if (parsedOfferPrice <= 0) {
			return "Offer price must be more than 0";
		} else if (parsedOfferPrice <= parsedHighestPrice) {
			return "Offer price must be higher than current highest price";
		}
		offerPrice = String.valueOf(parsedOfferPrice);
		return itemModel.offerPrice(itemID, userID, offerPrice);
	}

	public Item getItemById(String itemID) {
		return itemModel.getItemById(itemID);
	}

	public String uploadItem(String name, String category, String size, String price, String userID) {
		// validate and do insert new item
		// return string alert
		ArrayList<Item> items = new ArrayList<>();
		items = itemModel.getItems(items);

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

		// call uploadItem from model
		String result = itemModel.uploadItem(name, category, size, price, userID);

		return "item upload successful!";
	}

	public String editItem(String name, String category, String size, String price, String itemID) {
		// validate input and edit item
		// return string alert
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

		// do update/edit
		String result = itemModel.editItem(name, category, size, price, itemID);

		return "Item edit successful!";
	}

	// void untuk mengubah status item menjadi Approve
	public void approveItem(String itemID) {
		itemModel.approveItem(itemID);
	}

	public ArrayList<Item> getAcceptedItems(ArrayList<Item> items) {
		return itemModel.getAcceptedItems(items);
	}

	public String deleteItem(String itemID) {
		return itemModel.deleteItem(itemID);
	}

	public ArrayList<Item> getAllItems() {
		return itemModel.getAllItems();
	}

	public ArrayList<Item> getSellerAcceptedItems(ArrayList<Item> items, String userID) {
		return itemModel.getSellerAcceptedItems(items, userID);
	}

	public ArrayList<Item> getSellerHistoryItems(ArrayList<Item> items, String userID) {
		return itemModel.getSellerHistoryItems(items, userID);
	}

}
