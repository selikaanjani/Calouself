package view;

import controller.ItemController;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Item;

public class MakeOfferPage implements EventHandler<ActionEvent> {

	public Scene scene;
	private BorderPane borderContainer;
	private GridPane formPane;
	private MenuBar menuBar;
	private Menu homeMenu, wishlistMenu, purchaseMenu;
	private MenuItem homeItem, wishlistItem, purchaseItem;
	private VBox homePane;
	private ScrollPane scrollContainer;
	private Label titleHomeLbl, welcomeLbl, nameLbl, categoryLbl, sizeLbl, priceLbl, offerPriceLbl;
	private Button submitOfferButton;
	private TextField offerPriceField;
	private UserController user_controller = new UserController();
	private ItemController item_controller = new ItemController();
	private Item selectedItem;

	public MakeOfferPage(Item item) {
	    selectedItem = item;
	    initializeComponents();
	    createMenuBar();
	    initializeContentPanes();
	    fetchItemData(selectedItem);

	    // Default content: Home Page
	    borderContainer.setCenter(formPane);
	    scrollContainer.setContent(borderContainer);

	    scene = new Scene(scrollContainer, 900, 600);
	    view.Main.redirect(scene);
	}

	public void initializeComponents() {
		borderContainer = new BorderPane();
		menuBar = new MenuBar();
		homeMenu = new Menu("Home");
		homeItem = new MenuItem("Home Page");
		wishlistMenu = new Menu("Wishlist");
		wishlistItem = new MenuItem("View Wishlist");
		purchaseMenu = new Menu("Purchase");
		purchaseItem = new MenuItem("View Purchase History");
		scrollContainer = new ScrollPane();
	}

	public void createMenuBar() {
		homeMenu.getItems().add(homeItem);
		wishlistMenu.getItems().add(wishlistItem);
		purchaseMenu.getItems().add(purchaseItem);
		menuBar.getMenus().addAll(homeMenu, wishlistMenu, purchaseMenu);

		homeItem.setOnAction(e -> {
			borderContainer.setCenter(homePane);
			scrollContainer.setContent(borderContainer);
		});
		wishlistItem.setOnAction(this);
		purchaseItem.setOnAction(this);

		borderContainer.setTop(menuBar);
	}

	public void initializeContentPanes() {
		borderContainer = new BorderPane();
		formPane = new GridPane();
		formPane.setPadding(new Insets(20));
		formPane.setHgap(10);
		formPane.setVgap(10);
		formPane.setAlignment(Pos.CENTER);

		titleHomeLbl = new Label("Make an Offer");
		titleHomeLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

		welcomeLbl = new Label("Welcome Buyer " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
		welcomeLbl.setStyle("-fx-font-weight: bold;");

		nameLbl = new Label("Item Name: ");
		categoryLbl = new Label("Category: ");
		sizeLbl = new Label("Size: ");
		priceLbl = new Label("Price: ");
		offerPriceLbl = new Label("Offer Price: ");
		offerPriceField = new TextField();
		offerPriceField.setPromptText("Enter your offer price");

		submitOfferButton = new Button("Submit Offer");
		submitOfferButton.setOnAction(this);

		formPane.add(titleHomeLbl, 0, 0, 2, 1);
		formPane.add(welcomeLbl, 0, 1, 2, 1);
		formPane.add(nameLbl, 0, 2);
		formPane.add(categoryLbl, 0, 3);
		formPane.add(sizeLbl, 0, 4);
		formPane.add(priceLbl, 0, 5);
		formPane.add(offerPriceLbl, 0, 6);
		formPane.add(offerPriceField, 1, 6);
		formPane.add(submitOfferButton, 1, 7);
	}

	public void fetchItemData(Item item) {
		if (item != null) {
			selectedItem = item;
			selectedItem = item_controller.getItemById(selectedItem.getItemID());

			if (selectedItem != null) {
				nameLbl.setText("Item Name: " + selectedItem.getName());
				categoryLbl.setText("Category: " + selectedItem.getCategory());
				sizeLbl.setText("Size: " + selectedItem.getSize());
				priceLbl.setText("Price: " + selectedItem.getPrice());
			} else {
				nameLbl.setText("Item not found.");
				categoryLbl.setText("");
				sizeLbl.setText("");
				priceLbl.setText("");
			}
		}
	}

	private void showSuccess(String title, String message) {
		// show success alert
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert(String title, String message) {
		// show error alert
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == submitOfferButton) {
			String offerPrice = offerPriceField.getText().toString();
			String itemID = selectedItem.getItemID();
			String userID = user_controller.getCurrentlyLoggedInUser().getUserID();
			String result = item_controller.offerPrice(itemID, userID, offerPrice);
			if ("success".equals(result)) {
				showSuccess("Success", "Your offer has been submitted for the item: " + selectedItem.getName());
				view.Main.redirect(new BuyerDashboard().scene);
			} else {
				showAlert("Error", "There was an issue submitting your offer. Please try again.");
			}
		} else {
			if (e.getSource() == wishlistItem) {
				view.Main.redirect(new WishlistPage().scene);
			} else if (e.getSource() == homeItem) {
				borderContainer.setCenter(homePane);
				scrollContainer.setContent(borderContainer);
			} else if (e.getSource() == purchaseItem) {
				view.Main.redirect(new PurchaseHistoryPage().scene);
			}
		}
	}

}
