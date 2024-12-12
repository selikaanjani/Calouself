package controller;

import model.Item;
import model.Transaction;

public class TransactionController {
	private Transaction trModel = new Transaction();
	private WishlistController wc = new WishlistController();
	
	public String purchasedItems(String userID, String itemID) {
		wc.removeItemFromWishlist(itemID);
		return trModel.purchasedItems(userID, itemID);
	}
	
	
}
