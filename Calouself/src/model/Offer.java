package model;

public class Offer {
	private String offerID;
	private String userID;
	private String itemID;
	private String offerPrice;

	private Item item;

	public Item getItem() {
		return item;
	}

	public Offer() {
		// default constructor
	}

	public Offer(String offerID, String userID, String itemID, String offerPrice) {
		super();
		this.offerID = offerID;
		this.userID = userID;
		this.itemID = itemID;
		this.offerPrice = offerPrice;
	}

	public String getOfferID() {
		return offerID;
	}

	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

}
