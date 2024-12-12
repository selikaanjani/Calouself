package view;

import java.util.ArrayList;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

public class LoginPage implements EventHandler<ActionEvent>{
	public Scene scene;
	private BorderPane borderContainer;
	private GridPane gridContainer;
	private Label titleLbl, usernameLbl, passwordLbl, dontHvAccLbl;
	private TextField usernameTF;
	private PasswordField passwordPF;
	private Button loginBtn, registerBtn;
	private FlowPane flowContainer;
	private UserController user_controller = new UserController();
	
	private void initializeComponent() {
		//initialize all components
		borderContainer = new BorderPane();
		flowContainer = new FlowPane();
		gridContainer = new GridPane();
		
		scene = new Scene(borderContainer, 650, 400);
		
		titleLbl = new Label("Login Form");
		usernameLbl = new Label("Username:");
		passwordLbl = new Label("Password:");
		usernameTF = new TextField();
		passwordPF = new PasswordField();
		loginBtn = new Button("Login");
		registerBtn = new Button("Register");
		loginBtn.setOnAction(this);
		registerBtn.setOnAction(this);
		dontHvAccLbl = new Label("Don't have an account?");
	}
	
	private void initializeForm() {
		//initialize the form by arranging the components and do styling
		gridContainer.setHgap(10);
        gridContainer.setVgap(10);
        gridContainer.setPadding(new Insets(20, 20, 20, 20));
		
		titleLbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        BorderPane.setAlignment(titleLbl, Pos.CENTER);
        
        gridContainer.add(usernameLbl, 0, 0);
		gridContainer.add(usernameTF, 1, 0);
		
		gridContainer.add(passwordLbl, 0, 1);
		gridContainer.add(passwordPF, 1, 1);
		
		gridContainer.add(loginBtn, 0, 2);
        GridPane.setMargin(loginBtn, new Insets(10, 0, 0, 0));
       
		flowContainer.setPadding(new Insets(0, 20, 20, 20));
		flowContainer.getChildren().add(dontHvAccLbl);
		flowContainer.getChildren().add(registerBtn);
		flowContainer.setHgap(10);
		
		borderContainer.setTop(titleLbl);
		borderContainer.setCenter(gridContainer);
		borderContainer.setBottom(flowContainer);
	}
	
	public LoginPage() {
		//constructor, when called will automatically switch scene to this one
		initializeComponent();
		initializeForm();
		view.Main.redirect(scene);
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

	@Override
	public void handle(ActionEvent e) {
		//event handling
		if (e.getSource() == registerBtn) {
			//if user click register
			view.Main.redirect(new RegisterPage().scene);
		} else if (e.getSource() == loginBtn) {
			//if user click login
			String username = usernameTF.getText();
			String password = passwordPF.getText();
			
			ArrayList<String> alert = user_controller.loginUser(username, password);
			if (alert.get(0).equals("user login successful!")) {
				//login success
				showSuccess("Login", alert.get(0));
				String role = alert.get(1);
				if (role.equals("Seller")) {
					view.Main.redirect(new SellerDashboard().scene);
				} else if (role.equals("Admin")) {
	                view.Main.redirect(new AdminDashboard().scene);  // Arahkan ke AdminDashboard jika role-nya Admin
	            }
				}
				else if (role.equals("Buyer")) {
					view.Main.redirect(new BuyerDashboard().scene);
				}
			} else {
				//login failure
				showAlert("Login", alert.get(0));
			}
		}
	}
	
}
