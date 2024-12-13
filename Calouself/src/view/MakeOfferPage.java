package view;

import controller.ItemController;
import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Item;

public class MakeOfferPage implements EventHandler<ActionEvent> {

	public Scene scene;
	private BorderPane borderContainer;
	private GridPane formPane;
	private VBox homePane;
	private ScrollPane scrollContainer;
	private Label titleHomeLbl, welcomeLbl, nameLbl, categoryLbl, sizeLbl, priceLbl, offerPriceLbl, currentOfferLbl;
	private Button submitOfferButton;
	private TextField offerPriceField;
	private UserController user_controller = new UserController();
	private ItemController item_controller = new ItemController();
	private Item selectedItem;

	public MakeOfferPage(Item item) {
		selectedItem = item;
		initializeComponents();
		initializeContentPanes();
		fetchItemData(selectedItem);

		// Set content
		borderContainer.setCenter(formPane);
		scrollContainer.setContent(borderContainer);

		scene = new Scene(scrollContainer, 900, 600);
		view.Main.redirect(scene);
	}

	public void initializeComponents() {
		borderContainer = new BorderPane();
		scrollContainer = new ScrollPane();
		scrollContainer.setFitToWidth(true);
	}

	public void initializeContentPanes() {
		formPane = new GridPane();
		formPane.setPadding(new Insets(20));
		formPane.setHgap(15);
		formPane.setVgap(15);
		formPane.setAlignment(Pos.CENTER);

		titleHomeLbl = new Label("Make an Offer");
		titleHomeLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

		welcomeLbl = new Label("Welcome Buyer " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
		welcomeLbl.setStyle("-fx-font-weight: bold;");

		nameLbl = new Label("Item Name: ");
		categoryLbl = new Label("Category: ");
		sizeLbl = new Label("Size: ");
		priceLbl = new Label("Price: ");
		offerPriceLbl = new Label("Offer Price: ");
		currentOfferLbl = new Label("Current Offer Price: ");
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
		formPane.add(currentOfferLbl, 0, 6);
		formPane.add(offerPriceLbl, 0, 7);
		formPane.add(offerPriceField, 1, 7);
		formPane.add(submitOfferButton, 1, 8);
	}

	public void fetchItemData(Item item) {
		if (item != null) {
			selectedItem = item_controller.getItemById(item.getItemID());

			if (selectedItem != null) {
				nameLbl.setText("Item Name: " + selectedItem.getName());
				categoryLbl.setText("Category: " + selectedItem.getCategory());
				sizeLbl.setText("Size: " + selectedItem.getSize());
				priceLbl.setText("Price: " + selectedItem.getPrice());
				String offerPrice = item_controller.getOfferedPrice(selectedItem.getItemID());
				currentOfferLbl.setText("Current Offer Price: " + offerPrice);

			}
		}
	}

	private void showSuccess(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == submitOfferButton) {
			String offerPrice = offerPriceField.getText().trim();
			String itemID = selectedItem.getItemID();
			String itemPrice = selectedItem.getPrice().toString();
			String result = item_controller.offerPrice(itemID, offerPrice, itemPrice);
			if ("Offer submitted successfully!".equals(result)) {
				showSuccess("Success", "Your offer has been submitted!");
				view.Main.redirect(new BuyerDashboard().scene);
			} else {
				showAlert("Error", result);
			}
		}
	}
}
