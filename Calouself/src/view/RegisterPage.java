package view;

import controller.UserController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class RegisterPage implements EventHandler<ActionEvent> {
	private Label titleLbl;
	private BorderPane borderContainer;
	private GridPane gridContainer;
	private FlowPane flowContainer;
	private FlowPane flowLogin;
	public Scene scene;
	
	//usn pw num address role
	private Label usernameLbl, passwordLbl, phoneNumberLbl, addressLbl, roleLbl;
	private Label alrHaveAccLbl;
	private Button loginBtn;
	
	private TextField usernameTF;
	private PasswordField passwordPF;
	private TextField phoneNumberTF;
	private TextArea addressTA;
	private RadioButton buyerRadio, sellerRadio, adminRadio;
	private ToggleGroup roleGroup;	
	private Button registerBtn;
	private UserController user_controller = new UserController();
	
	private void initializeComponent() {
		borderContainer = new BorderPane();
		gridContainer = new GridPane();	
		flowContainer = new FlowPane();
		flowLogin = new FlowPane();
		
		scene = new Scene(borderContainer, 650, 400);
		
		titleLbl = new Label("Registration Form");
		usernameLbl = new Label("Username:");
		passwordLbl = new Label("Password:");
		phoneNumberLbl = new Label("Phone Number:");
		addressLbl = new Label("Address:");
		roleLbl = new Label("Role:");
		alrHaveAccLbl = new Label("Already have an account?");
		loginBtn = new Button("Login");
		
		usernameTF = new TextField();
		passwordPF = new PasswordField();
		phoneNumberTF = new TextField();
		addressTA = new TextArea();
		roleGroup = new ToggleGroup();
		
		buyerRadio = new RadioButton("Buyer");
		sellerRadio = new RadioButton("Seller");
		//adminRadio = new RadioButton("Admin");
		
		buyerRadio.setToggleGroup(roleGroup);
		sellerRadio.setToggleGroup(roleGroup);
		//adminRadio.setToggleGroup(roleGroup);
		
		
		
		registerBtn = new Button("Register");
		registerBtn.setOnAction(this); 
		loginBtn.setOnAction(this);
		
	}
	
	private void initializeForm() {
		flowContainer.getChildren().add(buyerRadio);
		flowContainer.getChildren().add(sellerRadio);
		//flowContainer.getChildren().add(adminRadio);
		flowContainer.setHgap(10);
		gridContainer.setHgap(10);
        gridContainer.setVgap(10);
        gridContainer.setPadding(new Insets(20, 20, 20, 20));

        titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLbl, Pos.CENTER);
       	
		gridContainer.add(usernameLbl, 0, 0);
		gridContainer.add(usernameTF, 1, 0);
		
		gridContainer.add(passwordLbl, 0, 1);
		gridContainer.add(passwordPF, 1, 1);
		
		gridContainer.add(phoneNumberLbl, 0, 2);
		gridContainer.add(phoneNumberTF, 1, 2);
		
		gridContainer.add(addressLbl, 0, 3);
		gridContainer.add(addressTA, 1, 3);
		
		gridContainer.add(roleLbl, 0, 4);
		gridContainer.add(flowContainer, 1, 4);
		
		gridContainer.add(registerBtn, 0, 5);
        GridPane.setMargin(registerBtn, new Insets(10, 0, 0, 0));
        
        flowLogin.getChildren().add(alrHaveAccLbl);
        flowLogin.getChildren().add(loginBtn);
		flowLogin.setHgap(10);
		flowLogin.setPadding(new Insets(0, 20, 20, 20));
        
		borderContainer.setTop(titleLbl);
		borderContainer.setCenter(gridContainer);
		borderContainer.setBottom(flowLogin);
		
//		borderContainer.setStyle("-fx-border-color: red;");
//		gridContainer.setStyle("-fx-border-color: blue;");
//		flowContainer.setStyle("-fx-border-color: green;"); buat debug


	}
	
	public RegisterPage() {
		initializeComponent();
		initializeForm();
		view.Main.redirect(scene);
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

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == registerBtn) {
			//usn pw phonenumber address role
			String username = usernameTF.getText();
			String password = passwordPF.getText();
			String phoneNumber = phoneNumberTF.getText();
			String address = addressTA.getText();
			RadioButton selectedRole = (RadioButton)roleGroup.getSelectedToggle();
	        String role = (selectedRole != null) ? selectedRole.getText() : "";
			
			String alert = user_controller.registerUser(username, password, phoneNumber, address, role);
			if (alert.equals("user registration successful!")) {
				showSuccess("Registration", alert);
				view.Main.redirect(new LoginPage().scene);
			} else {
				showAlert("Registration", alert);
			}
		} else if (e.getSource() == loginBtn) {
			view.Main.redirect(new LoginPage().scene);
		}
	}
	
	
}

