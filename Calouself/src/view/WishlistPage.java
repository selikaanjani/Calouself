package view;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
import controller.WishlistController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.Item;

public class WishlistPage implements EventHandler<ActionEvent> {

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

	public WishlistPage() {
		initializeComponents();
		createMenuBar();
		initializeContentPanes();

		// Default content: Home Page
		borderContainer.setCenter(homePane);
		scrollContainer.setContent(borderContainer);

		scene = new Scene(scrollContainer, 650, 400);
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
		titleHomeLbl = new Label(
				"Wishlist Page: " + user_controller.getCurrentlyLoggedInUser().getUsername() + "'s wishlist");
		titleHomeLbl.setStyle("-fx-font-weight: bold;");
		welcomeLbl = new Label("Welcome Buyer " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
		welcomeLbl.setStyle("-fx-font-weight: bold;");

		// Table showing items with actions
		homePageItemsTable = new TableView<>();
		TableColumn<Item, String> nameCol = new TableColumn("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol.setMinWidth(150);

		TableColumn<Item, Integer> categoryCol = new TableColumn("Category");
		categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol.setMinWidth(150);

		TableColumn<Item, String> sizeCol = new TableColumn("Size");
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
		sizeCol.setMinWidth(150);

		TableColumn<Item, String> priceCol = new TableColumn("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol.setMinWidth(150);

		// Add remove from wishlist button
		TableColumn<Item, Void> actionCol = new TableColumn<>("Actions");
		actionCol.setMinWidth(150);
		actionCol.setCellFactory(param -> new TableCell<Item, Void>() {
			private final Button removeFromWishlistBtn = new Button("Remove from Wishlist");

			{
				removeFromWishlistBtn.setOnAction(event -> {
					Item item = getTableView().getItems().get(getIndex());
					removeFromWishlist(item);
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					setGraphic(removeFromWishlistBtn);
				} else {
					setGraphic(null);
				}
			}
		});

		refreshWishlistTable();

		homePageItemsTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol, actionCol);
		homePageItemsTable.setMaxWidth(600);
		homePageItemsTable.setMinHeight(300);
		homePane.getChildren().add(welcomeLbl);
		homePane.getChildren().add(titleHomeLbl);
		homePane.getChildren().add(homePageItemsTable);
	}

	private void refreshWishlistTable() {
		acceptedItems.removeAll(acceptedItems);
		acceptedItems = item_controller.getAcceptedItems(acceptedItems);
		ObservableList<Item> accItemsObs = FXCollections.observableArrayList(acceptedItems);
		homePageItemsTable.setItems(accItemsObs);
	}

	private void removeFromWishlist(Item item) {
		String userID = user_controller.getCurrentlyLoggedInUser().getUserID();
		String itemID = item.getItemID();
		String wishlistID = wishlist_controller.getWishlistID(userID, itemID);
		if (!wishlistID.equals("Wishlist entry not found!")) {
			String result = wishlist_controller.removeFromWishlist(wishlistID);
			if (result.startsWith("Item successfully removed")) {
				acceptedItems.remove(item); // Remove item from table view
				homePageItemsTable.setItems(FXCollections.observableArrayList(acceptedItems));
			}
			showSuccess("Success", result);
		} else {
			showAlert("Error", "Wishlist entry not found for the selected item.");
		}
	}

	private void showSuccess(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == wishlistItem) {
			return;
		} else if (e.getSource() == homeItem) {
			borderContainer.setCenter(homePane);
			scrollContainer.setContent(borderContainer);
			view.Main.redirect(new BuyerDashboard().scene);
		} else if (e.getSource() == purchaseItem) {
			// Handle "View Purchase History" menu item if needed
		}
	}
}
