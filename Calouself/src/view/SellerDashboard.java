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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.Item;

public class SellerDashboard implements EventHandler<ActionEvent> {
    public Scene scene;
    private BorderPane borderContainer;
    private MenuBar menuBar;
    private Menu homeMenu, itemsMenu;
    private MenuItem homeMenuItem, manageItemsMenuItem, uploadItemMenuItem, viewAllItemsHistoryMenuItem;
    private VBox homePane, manageItemsPane, uploadItemPane, viewAllItemsHistoryPane;
    private TableView<Item> homePageItemsTable, sellerAcceptedItemsTable, historyItemsTable;
    private Label titleHomeLbl, titleManageItemLbl, titleUploadItemLbl, titleViewAllItemsHistoryLbl;
    private ScrollPane scrollContainer;
    private GridPane gridContainer, gridContainer2;
    private Button editBtn, uploadBtn, deleteBtn;
    private HBox manageButtons;
    private Label nameLbl, categoryLbl, sizeLbl, priceLbl, welcomeLbl1, welcomeLbl2, welcomeLbl3, welcomeLbl4;
    private TextField nameTF, categoryTF, sizeTF, priceTF;
    private Label nameLbl2, categoryLbl2, sizeLbl2, priceLbl2;
    private TextField nameTF2, categoryTF2, sizeTF2, priceTF2;
    private UserController user_controller = new UserController();
    private ItemController item_controller = new ItemController();
    private ArrayList<Item> acceptedItems = new ArrayList<>();
    private ArrayList<Item> sellerAcceptedItems = new ArrayList<>();
    private ArrayList<Item> sellerHistoryItems = new ArrayList<>();
    private String tempID; //to store the itemID on the mouse clicked event

    public SellerDashboard() {
    	//constructor, when called it will switch to this scene
        initializeComponents();
        initializeMenuBar();
        initializeContentPanes();

        // Default content: home pane
        borderContainer.setCenter(homePane);
        scrollContainer.setContent(borderContainer);

        scene = new Scene(scrollContainer, 650, 400);
        view.Main.redirect(scene);
    }

    private void initializeComponents() {
    	//initialize all main components
    	scrollContainer = new ScrollPane();
    	borderContainer = new BorderPane();
        menuBar = new MenuBar();
        homeMenu = new Menu("Home");
        itemsMenu = new Menu("Items");
        homeMenuItem = new MenuItem("Home Page");
        manageItemsMenuItem = new MenuItem("Manage Items");
        uploadItemMenuItem = new MenuItem("Upload New Item");
        viewAllItemsHistoryMenuItem = new MenuItem("View All Items History");
    }

    private void initializeMenuBar() {
    	//initialize the menu bar
        homeMenuItem.setOnAction(this);
        manageItemsMenuItem.setOnAction(this);
        uploadItemMenuItem.setOnAction(this);
        viewAllItemsHistoryMenuItem.setOnAction(this);

        homeMenu.getItems().add(homeMenuItem);
        itemsMenu.getItems().add(manageItemsMenuItem);
        itemsMenu.getItems().add(uploadItemMenuItem);
        itemsMenu.getItems().add(viewAllItemsHistoryMenuItem);

        menuBar.getMenus().addAll(homeMenu, itemsMenu);
        borderContainer.setTop(menuBar);
    }

    private void initializeContentPanes() {
        // initialize home pane
        homePane = new VBox(10);
        homePane.setPadding(new Insets(20));
        homePane.setAlignment(Pos.CENTER);
        titleHomeLbl = new Label("Home Page: All Items Approved By Admin");
        titleHomeLbl.setStyle("-fx-font-weight: bold;");
        welcomeLbl1 = new Label("Welcome Seller " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
        welcomeLbl1.setStyle("-fx-font-weight: bold;");
        
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
		
		refreshHomeTable(); //refresh/obtain table data
		
		homePageItemsTable.getColumns().addAll(nameCol, categoryCol, sizeCol, priceCol);
		homePageItemsTable.setMaxWidth(600);
		homePageItemsTable.setMinHeight(300);
		homePane.getChildren().add(welcomeLbl1);
        homePane.getChildren().add(titleHomeLbl);
        homePane.getChildren().add(homePageItemsTable);

        // initialzie manage items pane
        welcomeLbl2 = new Label("Welcome Seller " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
        welcomeLbl2.setStyle("-fx-font-weight: bold;");
        
        manageItemsPane = new VBox(10);
        manageItemsPane.setPadding(new Insets(20));
        manageItemsPane.setAlignment(Pos.CENTER);

        titleManageItemLbl = new Label("Manage Your Accepted Items As A Seller");
        titleManageItemLbl.setStyle("-fx-font-weight: bold;");
        
        sellerAcceptedItemsTable = new TableView<>();
        TableColumn<Item, String> nameCol2 = new TableColumn("Name");
		nameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol2.setMinWidth(150);
		
		TableColumn<Item, Integer> categoryCol2 = new TableColumn("Category");
		categoryCol2.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol2.setMinWidth(150);
		
		TableColumn<Item, String> sizeCol2 = new TableColumn("Size");
		sizeCol2.setCellValueFactory(new PropertyValueFactory<>("size"));
		sizeCol2.setMinWidth(150);
		
		TableColumn<Item, String> priceCol2 = new TableColumn("Price");
		priceCol2.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol2.setMinWidth(150);
		
		sellerAcceptedItemsTable.getColumns().addAll(nameCol2, categoryCol2, sizeCol2, priceCol2);
		sellerAcceptedItemsTable.setMaxWidth(600);
		sellerAcceptedItemsTable.setMinHeight(300);
		sellerAcceptedItemsTable.setOnMouseClicked(tableMouseEvent());
		refreshManageItemTable(); //refresh/obtain table data
		
		//this is the form to update/delete
		nameLbl2 = new Label("Name:");
        categoryLbl2 = new Label("Category:");
        sizeLbl2 = new Label("Size:");
        priceLbl2 = new Label("Price:");

        nameTF2 = new TextField();
        categoryTF2 = new TextField();
        sizeTF2 = new TextField();
        priceTF2 = new TextField();
		
		gridContainer2 = new GridPane();
        gridContainer2.setHgap(10);
        gridContainer2.setVgap(10);
        gridContainer2.setPadding(new Insets(20, 20, 20, 20));
        
        gridContainer2.setMinWidth(600);
        
        gridContainer2.add(nameLbl2, 0, 0);
		gridContainer2.add(nameTF2, 1, 0);
		
		gridContainer2.add(categoryLbl2, 0, 1);
		gridContainer2.add(categoryTF2, 1, 1);
		
		gridContainer2.add(sizeLbl2, 0, 2);
		gridContainer2.add(sizeTF2, 1, 2);
		
		gridContainer2.add(priceLbl2, 0, 3);
		gridContainer2.add(priceTF2, 1, 3);
		
        editBtn = new Button("Edit Item");
        deleteBtn = new Button("Delete Item");
        editBtn.setOnAction(this);
        deleteBtn.setOnAction(this);

        manageButtons = new HBox(10, editBtn, deleteBtn);
        manageButtons.setAlignment(Pos.CENTER);

        manageItemsPane.getChildren().addAll(welcomeLbl2, titleManageItemLbl, sellerAcceptedItemsTable, gridContainer2, manageButtons);
        
        //initialize upload new item pane
        welcomeLbl3 = new Label("Welcome Seller " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
        welcomeLbl3.setStyle("-fx-font-weight: bold;");
        nameLbl = new Label("Name:");
        categoryLbl = new Label("Category:");
        sizeLbl = new Label("Size:");
        priceLbl = new Label("Price:");

        nameTF = new TextField();
        categoryTF = new TextField();
        sizeTF = new TextField();
        priceTF = new TextField();

        uploadBtn = new Button("Upload");
        uploadBtn.setOnAction(this);
        
        uploadItemPane = new VBox(10);
        uploadItemPane.setPadding(new Insets(20));
        uploadItemPane.setAlignment(Pos.CENTER);
        
        titleUploadItemLbl = new Label("Upload New Item");
        titleUploadItemLbl.setStyle("-fx-font-weight: bold;");
        
        gridContainer = new GridPane();
        gridContainer.setHgap(10);
        gridContainer.setVgap(10);
        gridContainer.setPadding(new Insets(20, 20, 20, 20));
        
        gridContainer.setMinWidth(600);
        
        gridContainer.add(nameLbl, 0, 0);
		gridContainer.add(nameTF, 1, 0);
		
		gridContainer.add(categoryLbl, 0, 1);
		gridContainer.add(categoryTF, 1, 1);
		
		gridContainer.add(sizeLbl, 0, 2);
		gridContainer.add(sizeTF, 1, 2);
		
		gridContainer.add(priceLbl, 0, 3);
		gridContainer.add(priceTF, 1, 3);
		
		gridContainer.add(uploadBtn, 0, 4);
        GridPane.setMargin(uploadBtn, new Insets(10, 0, 0, 0));
        
        uploadItemPane.getChildren().addAll(welcomeLbl3, titleUploadItemLbl, gridContainer);
        
        //initialize viewAllItemsHistoryMenuItem Pane
        viewAllItemsHistoryPane = new VBox(10);
        viewAllItemsHistoryPane.setPadding(new Insets(20));
        viewAllItemsHistoryPane.setAlignment(Pos.CENTER);
        viewAllItemsHistoryPane.setMinWidth(650);
        
        titleViewAllItemsHistoryLbl = new Label("View All My Items History");
        titleViewAllItemsHistoryLbl.setStyle("-fx-font-weight: bold;");
        
        welcomeLbl4 = new Label("Welcome Seller " + user_controller.getCurrentlyLoggedInUser().getUsername() + "!");
        welcomeLbl4.setStyle("-fx-font-weight: bold;");
        
        historyItemsTable = new TableView<>();
        TableColumn<Item, String> nameCol3 = new TableColumn("Name");
		nameCol3.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameCol3.setMinWidth(120);
		
		TableColumn<Item, Integer> categoryCol3 = new TableColumn("Category");
		categoryCol3.setCellValueFactory(new PropertyValueFactory<>("category"));
		categoryCol3.setMinWidth(120);
		
		TableColumn<Item, String> sizeCol3 = new TableColumn("Size");
		sizeCol3.setCellValueFactory(new PropertyValueFactory<>("size"));
		sizeCol3.setMinWidth(120);
		
		TableColumn<Item, String> priceCol3 = new TableColumn("Price");
		priceCol3.setCellValueFactory(new PropertyValueFactory<>("price"));
		priceCol3.setMinWidth(120);
		
		TableColumn<Item, String> statusCol = new TableColumn("ItemStatus");
		statusCol.setCellValueFactory(new PropertyValueFactory<>("itemStatus"));
		statusCol.setMinWidth(120);
		
		historyItemsTable.getColumns().addAll(nameCol3, categoryCol3, sizeCol3, priceCol3, statusCol);
		historyItemsTable.setMaxWidth(600);
		historyItemsTable.setMinHeight(300);
		refreshHistoryItemTable(); //refresh/obtain table data
		
		viewAllItemsHistoryPane.getChildren().addAll(welcomeLbl4, titleViewAllItemsHistoryLbl, historyItemsTable);

    }
    
    private void showAlert(String title, String message) {
    	//show error alert
	    Alert alert = new Alert(Alert.AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	private void showSuccess(String title, String message) {
		//show success alert
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	private void refreshUploadForm() {
		//refresh upload form to become empty again
		nameTF.setText("");
		categoryTF.setText("");
		sizeTF.setText("");
		priceTF.setText("");
	}
	
	private void refreshManageItemForm() {
		//refresh manage item form to become empty again
		nameTF2.setText("");
		categoryTF2.setText("");
		sizeTF2.setText("");
		priceTF2.setText("");
		tempID = null; 
	}
	
	private void refreshHomeTable() {
		//refresh/re-obtain table data
		acceptedItems.removeAll(acceptedItems);
		acceptedItems = item_controller.getAcceptedItems(acceptedItems);
		ObservableList<Item> accItemsObs = FXCollections.observableArrayList(acceptedItems);
		homePageItemsTable.setItems(accItemsObs);
	}

	private void refreshHistoryItemTable() {
		//refresh/re-obtain table data
		sellerHistoryItems.removeAll(sellerHistoryItems);
		sellerHistoryItems = item_controller.getSellerHistoryItems(sellerHistoryItems, user_controller.getCurrentlyLoggedInUser().getUserID());
		ObservableList<Item> sellerHistoryItemsObs = FXCollections.observableArrayList(sellerHistoryItems);
		historyItemsTable.setItems(sellerHistoryItemsObs);
	}
	
	private void refreshManageItemTable() {
		//refresh/re-obtain table data
		sellerAcceptedItems.removeAll(sellerAcceptedItems);
		sellerAcceptedItems = item_controller.getSellerAcceptedItems(sellerAcceptedItems, user_controller.getCurrentlyLoggedInUser().getUserID());
		ObservableList<Item> sellerAccItemsObs = FXCollections.observableArrayList(sellerAcceptedItems);
		sellerAcceptedItemsTable.setItems(sellerAccItemsObs);
	}
	
	private EventHandler<MouseEvent> tableMouseEvent() {
		//so that manage item table row can be clicked and we can obtain that particular data
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				TableSelectionModel<Item> tableSelectionModel = sellerAcceptedItemsTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				Item item = tableSelectionModel.getSelectedItem();
				
				nameTF2.setText(item.getName());
				categoryTF2.setText(item.getCategory());
				sizeTF2.setText(item.getSize());
				priceTF2.setText(item.getPrice());
				tempID = item.getItemID();
			}
			
		};
	}

    @Override
    public void handle(ActionEvent e) {
    	//event handling
        if (e.getSource() == homeMenuItem) {
        	//if user click home menu
        	borderContainer.setCenter(homePane);
        } else if (e.getSource() == manageItemsMenuItem) {
        	//if user click manage item menu
        	borderContainer.setCenter(manageItemsPane);
        } else if (e.getSource() == uploadItemMenuItem) {
        	//if user click upload new item menu
        	borderContainer.setCenter(uploadItemPane);
        } else if (e.getSource() == uploadBtn) {
        	//if user click upload button
        	String name = nameTF.getText();
        	String category = categoryTF.getText();
        	String size = sizeTF.getText();
        	String price = priceTF.getText();
        	
        	String alert = item_controller.uploadItem(name, category, size, price, user_controller.getCurrentlyLoggedInUser().getUserID());
        	
        	if (alert.equals("item upload successful!")) {
        		//upload success
				showSuccess("Item Upload", alert + " You can view it in the view all items history menu to check on its status.");
				refreshUploadForm();
				refreshManageItemTable();
				refreshHomeTable();
				refreshHistoryItemTable();
			} else {
				//upload failure
				showAlert("Item Upload", alert);
			}
        } else if (e.getSource() == editBtn) {
        	//if user click edit button
        	String name = nameTF2.getText();
        	String category = categoryTF2.getText();
        	String size = sizeTF2.getText();
        	String price = priceTF2.getText();
        	
        	String alert = item_controller.editItem(name, category, size, price, tempID);
        	if (alert.equals("item edit successful!")) {
        		//edit success
				showSuccess("Item Edit", alert);
				refreshManageItemForm();
				refreshManageItemTable();
				refreshHomeTable();
				refreshHistoryItemTable();
			} else {
				//edit failure
				showAlert("Item Edit", alert);
			}
        } else if (e.getSource() == viewAllItemsHistoryMenuItem) {
        	//if user click view all items historu menu
        	borderContainer.setCenter(viewAllItemsHistoryPane);
        } else if (e.getSource() == deleteBtn) {
        	//if user click delete button
        	String alert = item_controller.deleteItem(tempID);
        	if (alert.equals("item delete successful!")) {
        		//delete success
        		showSuccess("Item Delete", alert);
				refreshManageItemForm();
				refreshManageItemTable();
				refreshHomeTable();
				refreshHistoryItemTable();
        	} else {
        		//delete failure
        		showAlert("Item Delete", alert);
        	}
        }
    }
}
