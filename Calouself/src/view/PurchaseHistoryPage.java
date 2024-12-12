package view;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
import controller.WishlistController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

public class PurchaseHistoryPage implements EventHandler<ActionEvent> {

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

	public PurchaseHistoryPage() {
		initializeComponents();
		createMenuBar();
		initializeContentPanes();

		// Default content: Home Page
		borderContainer.setCenter(homePane);
		scrollContainer.setContent(borderContainer);

		// scene = new Scene(scrollContainer, 650, 400);
		scene = new Scene(scrollContainer, 800, 600);
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
		titleHomeLbl = new Label("Purchase History");
		titleHomeLbl.setStyle("-fx-font-weight: bold;");
		welcomeLbl = new Label(user_controller.getCurrentlyLoggedInUser().getUsername() + "'s Purchase History");
		welcomeLbl.setStyle("-fx-font-weight: bold;");

		// Table displaying items
		homePageItemsTable = new TableView<>();
		TableColumn<Item, String> nameCol = createTableColumn("Name", "name");
		TableColumn<Item, Integer> categoryCol = createTableColumn("Category", "category");
		TableColumn<Item, String> sizeCol = createTableColumn("Size", "size");
		TableColumn<Item, String> priceCol = createTableColumn("Price", "price");

		refreshHomeTable();

		homePageItemsTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol);
		homePageItemsTable.setMaxWidth(800);
		homePageItemsTable.setMinHeight(500);
		homePane.getChildren().add(welcomeLbl);
		homePane.getChildren().add(titleHomeLbl);
		homePane.getChildren().add(homePageItemsTable);
	}

	private <T> TableColumn<Item, T> createTableColumn(String title, String property) {
		TableColumn<Item, T> column = new TableColumn<>(title);
		column.setCellValueFactory(new PropertyValueFactory<>(property));
		column.setMinWidth(200);
		return column;
	}

	private void refreshHomeTable() {
		acceptedItems.removeAll(acceptedItems);
		acceptedItems = item_controller.getAcceptedItems(acceptedItems);
		ObservableList<Item> accItemsObs = FXCollections.observableArrayList(acceptedItems);
		homePageItemsTable.setItems(accItemsObs);
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == wishlistItem) {
			// Reload to wishlist page
			view.Main.redirect(new WishlistPage().scene);
		} else if (e.getSource() == homeItem) {
			// Redirect to Home Page
			view.Main.redirect(new BuyerDashboard().scene);
		} else if (e.getSource() == purchaseItem) {
			// Redirect to the Purchase History Page
			view.Main.redirect(new PurchaseHistoryPage().scene);
		}
	}

}
