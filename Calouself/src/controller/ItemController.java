package controller;

import java.util.ArrayList;
import model.Item;

public class ItemController {
	private Item itemModel = new Item();

	public String acceptOffer(String itemID) {
		return itemModel.acceptOffer(itemID); 
	}

	public String declineOffer(String itemID) {
		return itemModel.declineOffer(itemID);
	}

	public String getOfferedPrice(String itemID) {
		return itemModel.getOfferedPrice(itemID);
	}

	public String offerPrice(String itemID, String offerPrice, String itemPrice) {
		if (offerPrice == null || offerPrice.trim().isEmpty()) {
			return "Offer price cannot be empty!";
		}

		double offeredPriceValue;
		double originalPrice;
		try {
			offeredPriceValue = Double.parseDouble(offerPrice);
			originalPrice = Double.parseDouble(itemPrice);

			if (offeredPriceValue <= 0) {
				return "Offer price must be more than zero!";
			}
			if (offeredPriceValue < originalPrice) {
				return "Offer price cannot be lower than the original price!";
			}

		} catch (NumberFormatException e) {
			return "Invalid offer price format!";
		}

		return itemModel.offerPrice(itemID, offerPrice, itemPrice);
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
