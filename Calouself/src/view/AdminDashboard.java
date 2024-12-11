package view;

import java.util.ArrayList;

import controller.ItemController;
import controller.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import model.Item;

public class AdminDashboard implements EventHandler<ActionEvent> {
    public Scene scene;
    private BorderPane borderContainer;
    private MenuBar menuBar;
    private Menu homeMenu, manageMenu;
    private MenuItem homeMenuItem, viewItemsMenuItem;
    private VBox homePane, viewItemsPane;
    private TableView<Item> itemsTable;
    private Label titleHomeLbl, titleViewItemsLbl, welcomeLbl1;
    private ScrollPane scrollContainer;
    private ItemController itemController = new ItemController();
    private UserController user_controller = new UserController();
    private TableView<Item> homePageItemsTable;
    private ArrayList<Item> acceptedItems = new ArrayList<>();

    public AdminDashboard() {
        initializeComponents();
        initializeMenuBar();
        initializeContentPanes();

        // Default content: Home Page
        borderContainer.setCenter(homePane);
        scrollContainer.setContent(borderContainer); // Menambahkan konten ke scroll container

        scene = new Scene(scrollContainer, 650, 400);
        view.Main.redirect(scene);
    }

    private void initializeComponents() {
        // Inisialisasi semua komponen yang diperlukan
        scrollContainer = new ScrollPane();
        borderContainer = new BorderPane();
        menuBar = new MenuBar();
        homeMenu = new Menu("Home");
        manageMenu = new Menu("Manage");
        homeMenuItem = new MenuItem("Home Page");
        viewItemsMenuItem = new MenuItem("View Items");
    }

    private void initializeMenuBar() {
        // Menambahkan aksi untuk menu item
        homeMenuItem.setOnAction(this);
        viewItemsMenuItem.setOnAction(this);

        // Menambahkan item ke dalam menu
        homeMenu.getItems().add(homeMenuItem);
        manageMenu.getItems().add(viewItemsMenuItem);

        // Menambahkan menu ke dalam menu bar
        menuBar.getMenus().addAll(homeMenu, manageMenu);
        borderContainer.setTop(menuBar); // Menambahkan menu bar ke bagian atas border container
    }

    private void initializeContentPanes() {
        // Home Pane
        homePane = new VBox(10);
        homePane.setPadding(new Insets(20));
        homePane.setAlignment(Pos.CENTER);
        titleHomeLbl = new Label("Home Page: All Items Approved By Admin");
        titleHomeLbl.setStyle("-fx-font-weight: bold;");
        welcomeLbl1 = new Label("Welcome Seller " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
        welcomeLbl1.setStyle("-fx-font-weight: bold;");

        // Menampilkan nama, kategori, ukuran, dan harga item
        homePageItemsTable = new TableView<>();
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);

        TableColumn<Item, Integer> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryCol.setMinWidth(150);

        TableColumn<Item, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setMinWidth(150);

        TableColumn<Item, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(150);

        refreshHomeTable();

        homePageItemsTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol);
        homePageItemsTable.setMaxWidth(600);
        homePageItemsTable.setMinHeight(300);
        homePane.getChildren().add(welcomeLbl1);
        homePane.getChildren().add(titleHomeLbl);
        homePane.getChildren().add(homePageItemsTable);


        // View Items Pane
        viewItemsPane = new VBox(10);
        viewItemsPane.setPadding(new Insets(20));
        viewItemsPane.setAlignment(Pos.CENTER);

        titleViewItemsLbl = new Label("View All Submitted Items");
        titleViewItemsLbl.setStyle("-fx-font-weight: bold;");

        // Membuat tabel untuk menampilkan item
        itemsTable = new TableView<>();
        TableColumn<Item, String> itemNameCol = new TableColumn<>("Item Name");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        itemNameCol.setMinWidth(200);

        TableColumn<Item, String> statusCol = new TableColumn<>("Item Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("itemStatus"));
        statusCol.setMinWidth(200);

        itemsTable.getColumns().addAll(itemNameCol, statusCol);
        refreshItemsTable();

        // Menambahkan kolom aksi (approve/reject) di dalam tabel
        TableColumn<Item, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setMinWidth(200);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button approveButton = new Button("Approve");
            private final Button rejectButton = new Button("Reject");
            private final HBox buttonBox = new HBox(5, approveButton, rejectButton);
            

            {
                approveButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    item.setItemStatus("Accepted");
                    itemController.approveItem(item.getItemID());
                    showSuccess("Item Approved", "The item has been approved.");
                    refreshItemsTable();
                    refreshHomeTable();
                });

                rejectButton.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    String result = itemController.deleteItem(item.getItemID());
                    if (result.equals("item delete successful!")) {
                        showSuccess("Item Deleted", "The item has been deleted.");
                        refreshItemsTable();
                        refreshHomeTable();
                    } else {
                        showAlert("Error", "There was an issue deleting the item.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
        
        itemsTable.getColumns().add(actionCol);

        viewItemsPane.getChildren().addAll(titleViewItemsLbl, itemsTable);
    }

    private void refreshHomeTable() {
		acceptedItems.removeAll(acceptedItems);
		acceptedItems = itemController.getAcceptedItems(acceptedItems);
		ObservableList<Item> accItemsObs = FXCollections.observableArrayList(acceptedItems);
		homePageItemsTable.setItems(accItemsObs);
	}

    private void refreshItemsTable() {
        ObservableList<Item> items = FXCollections.observableArrayList(itemController.getAllItems());
        itemsTable.setItems(items); // Mengupdate tabel dengan data item
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == homeMenuItem) {
            borderContainer.setCenter(homePane);
        } else if (event.getSource() == viewItemsMenuItem) {
            borderContainer.setCenter(viewItemsPane);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
