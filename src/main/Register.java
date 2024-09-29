package main;

import java.awt.Desktop.Action;
import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Register implements EventHandler<ActionEvent>{

		Stage stage;
		Scene scene;
		
		BorderPane bp;
		VBox vb;
		GridPane gp;
		
		Label usnLb, passLb, errorLb, titleLb, stitleLb;
		TextField usnFl;
		PasswordField passFl;
		Button regBtn;
		
		MenuBar menuBar;
		Menu menu;
		MenuItem loginMn, registerMn;
		
		CheckBox cbterm;
		
		Region spacer;
		
		ArrayList<String> unuser = new ArrayList<>();
		


		

		



		private void initialize() {
			bp = new BorderPane();
			vb = new VBox();
			gp = new GridPane();
			
			
			titleLb = new Label("Register");
			stitleLb = new Label("NJuice");
			usnLb = new Label("Username");
			passLb = new Label("Password");
			errorLb = new Label();
			usnFl = new TextField();
			passFl = new PasswordField();
			
			regBtn = new Button("Register");
			
			scene = new Scene(bp, 800, 600);
			
			menuBar = new MenuBar();
			menu = new Menu("Dashboard");
			loginMn = new MenuItem("Login");
			registerMn = new MenuItem("Register");
			
			cbterm = new CheckBox("I agree to the terms and conditions of NJuice!");
			
			spacer = new Region();
		
		
			
			
		}
		
		private void add() {
			gp.addColumn(0, usnLb, usnFl, passLb, passFl,cbterm, errorLb);
			
			vb.getChildren().addAll(titleLb, stitleLb, gp, spacer, regBtn);
						
			bp.setTop(menuBar);
			bp.setCenter(vb);
			
		}
		
		private void set() {
			menu.getItems().addAll(loginMn, registerMn);
			menuBar.getMenus().add(menu);
			
			usnFl.setPromptText("Enter new Username...");
			passFl.setPromptText("Enter new Password...");
			
		}
		
		
		private void arrage() {
			gp.setAlignment(Pos.CENTER);
			gp.setVgap(10);
			
			
			vb.setAlignment(Pos.CENTER);
			vb.setSpacing(5);
			
		}

		private void style() {
			errorLb.setStyle("-fx-text-fill: red");
			stitleLb.setAlignment(Pos.CENTER);
			titleLb.setFont(Font.font ("Montserrat", FontWeight.BOLD, 50));
			stitleLb.setFont(Font.font("Montserrat", 20));
			
			regBtn.setTextAlignment(TextAlignment.CENTER);
			
			usnFl.setMinWidth(250);
			passFl.setPrefWidth(250);
			
			spacer.setMinHeight(10);
			
		}
		
			
		private void setEvent() {
			loginMn.setOnAction(this);
			regBtn.setOnAction(this);
		}

public Register(Stage stage) {
	initialize();
	set();
	add();
	arrage();
	style();
	setEvent();
	this.stage = stage;
	stage.setScene(scene);
}

public void show() {
	stage.show();
}

@Override
public void handle(ActionEvent e) {
	if (e.getSource() == loginMn) {
		Login login = new Login(stage);
		login.show();
	}
	
	if (e.getSource() == regBtn) {
		Connect con = Connect.getInstance();
		String usn = usnFl.getText();
		String pass = passFl.getText();
		
		if(usn.isEmpty()||pass.isEmpty()||!cbterm.isSelected()) {
			errorLb.setText("Please input all the field");
		}
		else {
			boolean flag = true;
			String query = "SELECT * FROM msuser WHERE Username = '"+usn+"'";
			con.rs = con.executeQuery(query);
			try {
				if(con.rs.next()) {
					errorLb.setText("Username is already taken");
				}
				else {
					try {
						
						query = "INSERT INTO msuser VALUES('"+usnFl.getText()+"','"+passFl.getText()+"','Customer' )";
						con.executeUpdate(query);			
						
						Login login = new Login(stage);
						login.show();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			
			
			
			
			
			
		}
		
		
		
			
		
	}
	
	
}

}




