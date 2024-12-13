package view;

import java.util.ArrayList;
import java.util.Optional;

import controller.ItemController;
import controller.TransactionController;
import controller.UserController;
import controller.WishlistController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Item;

public class BuyerDashboard implements EventHandler<ActionEvent> {

	public Scene scene;
	private BorderPane borderContainer;
	private MenuBar menuBar;
	private Menu homeMenu, wishlistMenu, purchaseMenu;
	private MenuItem homeItem, wishlistItem, purchaseItem;
	private VBox homePane;
	private ScrollPane scrollContainer;
	private Label titleHomeLbl, welcomeLbl;
	private TableView<Item> homePageItemsTable;
	private ArrayList<Item> acceptedItems = new ArrayList<>();
	private UserController user_controller = new UserController();
	private ItemController item_controller = new ItemController();
	private WishlistController wishlist_controller = new WishlistController();
	private TransactionController transaction_controller = new TransactionController();

	public BuyerDashboard() {
		initializeComponents();
		createMenuBar();
		initializeContentPanes();

		// Default content: Home Page
		borderContainer.setCenter(homePane);
		scrollContainer.setContent(borderContainer);

		// scene = new Scene(scrollContainer, 650, 400);
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

		homeItem.setOnAction(this);
		wishlistItem.setOnAction(this);
		purchaseItem.setOnAction(this);

		borderContainer.setTop(menuBar);
	}

	public void initializeContentPanes() {
		// Home Pane
		homePane = new VBox(10);
		homePane.setPadding(new Insets(20));
		homePane.setAlignment(Pos.CENTER);
		titleHomeLbl = new Label("Home Page: All Items Approved By Admin");
		titleHomeLbl.setStyle("-fx-font-weight: bold;");
		welcomeLbl = new Label("Welcome Buyer " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
		welcomeLbl.setStyle("-fx-font-weight: bold;");

		// Table displaying items
		homePageItemsTable = new TableView<>();
		TableColumn<Item, String> nameCol = createTableColumn("Name", "name");
		TableColumn<Item, Integer> categoryCol = createTableColumn("Category", "category");
		TableColumn<Item, String> sizeCol = createTableColumn("Size", "size");
		TableColumn<Item, String> priceCol = createTableColumn("Price", "price");

		// Actions column: Add to Wishlist, Purchase, Make Offer
		TableColumn<Item, Void> actionCol = new TableColumn<>("Actions");
		actionCol.setMinWidth(300);
		actionCol.setCellFactory(param -> new TableCell<Item, Void>() {
			private final Button addToWishlistBtn = new Button("Add to Wishlist");
			private final Button purchaseBtn = new Button("Purchase");
			private final Button makeOfferBtn = new Button("Make Offer");
			private final HBox hbox = new HBox(10);

			{
				addToWishlistBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					addToWishlist(item);
				});

				purchaseBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					purchaseItem(item);
				});
				
				makeOfferBtn.setOnAction(event -> {
		            Item item = getTableView().getItems().get(getIndex());
		            view.Main.redirect(new MakeOfferPage(item).scene);
		        });

				hbox.getChildren().addAll(addToWishlistBtn, purchaseBtn, makeOfferBtn);
				hbox.setAlignment(Pos.CENTER);
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					setGraphic(hbox); 
				} else {
					setGraphic(null);
				}
			}
		});

		refreshHomeTable();

		homePageItemsTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, actionCol);
		homePageItemsTable.setMaxWidth(800);
		homePageItemsTable.setMinHeight(500);
		homePane.getChildren().add(welcomeLbl);
		homePane.getChildren().add(titleHomeLbl);
		homePane.getChildren().add(homePageItemsTable);
	}

	// Method to create a TableColumn
	private <T> TableColumn<Item, T> createTableColumn(String title, String property) {
		TableColumn<Item, T> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<>(property));
		column.setMinWidth(150);
		return column;
	}

	private void refreshHomeTable() {
		acceptedItems.removeAll(acceptedItems);
		acceptedItems = item_controller.getAcceptedItems(acceptedItems);
		ObservableList<Item> accItemsObs = FXCollections.observableArrayList(acceptedItems);
		homePageItemsTable.setItems(accItemsObs);
	}

	private void addToWishlist(Item item) {
		String userID = user_controller.getCurrentlyLoggedInUser().getUserID();
		String itemID = item.getItemID();

		String isAdded = wishlist_controller.addItemToWishlist(userID, itemID);

		if (isAdded.equals("Item successfully added to wishlist!")) {
			showSuccess("Success", "Item successfully added to wishlist!");
		} else {
			showAlert("Failed", "Error adding item to wishlist");
		}
	}

	private void purchaseItem(Item item) {
		String userID = user_controller.getCurrentlyLoggedInUser().getUserID();
		String itemID = item.getItemID();
		String transactionID = transaction_controller.generateTransactionID();

		// Confirmation pop up
		Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		confirmationAlert.setTitle("Confirm Purchase");
		confirmationAlert.setHeaderText("Are you sure you want to purchase " + item.getName() + "?");
		confirmationAlert.setContentText("This action cannot be undone.");

		ButtonType confirmButton = new ButtonType("Confirm");
		ButtonType cancelButton = new ButtonType("Cancel");
		confirmationAlert.getButtonTypes().setAll(confirmButton, cancelButton);

		Optional<ButtonType> result = confirmationAlert.showAndWait();

		if (result.isPresent() && result.get() == confirmButton) {
			String isAdded = transaction_controller.createTransaction(transactionID, userID, itemID);
			if (isAdded.contains("Transaction successful")) {
				showSuccess("Purchase Successful", "Item successfully purchased!");
			} else {
				showAlert("Purchase Failed", isAdded);
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
		if (e.getSource() == wishlistItem) {
			// Redirect to the WishlistPage
			view.Main.redirect(new WishlistPage().scene);
		} else if (e.getSource() == homeItem) {
			borderContainer.setCenter(homePane);
			scrollContainer.setContent(borderContainer);
		} else if (e.getSource() == purchaseItem) {
			// Redirect to PurchaseHistoryPage
			view.Main.redirect(new PurchaseHistoryPage().scene);
		}
	}
}
