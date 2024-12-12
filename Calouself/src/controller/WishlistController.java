package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Connect;
import model.Item;
import model.Wishlist;

public class WishlistController {
	private Wishlist wishlistModel = new Wishlist();

	public String addItemToWishlist(String userID, String itemID) {
		return wishlistModel.addItemToWishlist(userID, itemID);
	}

	public String removeItemFromWishlist(String wishlistID) {
		return wishlistModel.removeItemFromWishlist(wishlistID);
	}

	public String getWishlistID(String userID, String itemID) {
		return wishlistModel.getWishlistID(userID, itemID);
	}

	public ArrayList<Item> viewWishlist(String userID) {
		return wishlistModel.viewWishlist(userID);
	}

}
