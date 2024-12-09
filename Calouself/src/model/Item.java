package model;

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
		//itemwishlist krg tau apa, itemofferstatus klo baru create krna emg blm ada offer jdi kosong dlu
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
	
	
}
