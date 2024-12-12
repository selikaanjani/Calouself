package model;

public class Wishlist {
	private String wishlistID;
	private String itemID;
	private String userID;

	public Wishlist(String wishlistID, String itemID, String userID) {
		super();
		this.wishlistID = wishlistID;
		this.itemID = itemID;
		this.userID = userID;
	}

	public String getWishlistID() {
		return wishlistID;
	}

	public void setWishlistID(String wishlistID) {
		this.wishlistID = wishlistID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
