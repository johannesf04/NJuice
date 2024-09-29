package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import connect.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class Login implements EventHandler<ActionEvent>{
	Stage stage;
	Scene scene;
	
	
	
	
	BorderPane bp;
	VBox vb;
	GridPane gp;
	
	Label usnLb, passLb, errorLb, titleLb, stitleLb;
	TextField usnFl;
	PasswordField passFl;
	Button loginBtn;
	
	MenuBar menuBar;
	Menu menu;
	MenuItem loginMn, registerMn;
	
	Region spacer;

	boolean flag;

	

	


	private void initialize() {
		bp = new BorderPane();
		vb = new VBox();
		gp = new GridPane();
		
		
		titleLb = new Label("Login");
		stitleLb = new Label("NJuice");
		usnLb = new Label("Username");
		passLb = new Label("Password");
		errorLb = new Label();
		usnFl = new TextField();
		passFl = new PasswordField();
		
		loginBtn = new Button("Login");
		
		scene = new Scene(bp, 800, 600);
		
		menuBar = new MenuBar();
		menu = new Menu("Dashboard");
		loginMn = new MenuItem("Login");
		registerMn = new MenuItem("Register");
		
		spacer = new Region();
	}
	
	private void set() {
		menu.getItems().addAll(loginMn, registerMn);
		menuBar.getMenus().add(menu);
		
		usnFl.setPromptText("Enter Username...");
		passFl.setPromptText("Enter Password...");
		
	}
		
	private void add() {
		gp.addColumn(0, usnLb, usnFl, passLb, passFl, errorLb);
		
		vb.getChildren().addAll(titleLb, stitleLb, gp, spacer, loginBtn);
					
		bp.setTop(menuBar);
		bp.setCenter(vb);
		
	}
	
	private void arrage() {
		gp.setAlignment(Pos.CENTER);
		gp.setVgap(10);
		
		
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(0);
		
	}
		
		
	private void style() {
		errorLb.setStyle("-fx-text-fill: red");
		stitleLb.setAlignment(Pos.CENTER);
		titleLb.setFont(Font.font ("Montserrat", FontWeight.BOLD, 50));
		stitleLb.setFont(Font.font("Montserrat", 20));
		
		loginBtn.setTextAlignment(TextAlignment.CENTER);
		
		usnFl.setMinWidth(250);
		passFl.setPrefWidth(250);
		
		spacer.setMinHeight(10);
		
	}
	

		private void setEvent() {
			registerMn.setOnAction(this);
			loginBtn.setOnAction(this);
		}
		
		
		public Login(Stage stage) {
			initialize();
			set();
			add();
			arrage();
			style();
			setEvent();
			stage.setScene(scene);
			this.stage = stage;
		}


		public void show() {
		stage.show();
		
		}

		@Override
		public void handle(ActionEvent e) {
				if (e.getSource() == registerMn) {
					Register regis = new Register(stage);
					regis.show();
				}
			
				if (e.getSource() == loginBtn) {
					String usn = usnFl.getText();
					String pass = passFl.getText();
					
					if(usn.isEmpty()||pass.isEmpty()) {
						errorLb.setText("Please input all the field");
					}
					
					else {
						try {
							Connect con = Connect.getInstance();
							flag = false;
							String query = "SELECT * FROM msuser WHERE username = '"+usnFl.getText()+"' AND password ='"+passFl.getText()+"' AND role ='Admin'";                  
							con.rs = con.executeQuery(query);
							if(con.rs.next()) {
								flag=true;
							}
							
							if(flag == false) {
								errorLb.setText("Credentials failed!");
							}
							else {
								
									ViewTransaction vt = new ViewTransaction(stage);
									vt.show();
								
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						try {
							Connect con = Connect.getInstance();
							flag = false;
							String query = "SELECT * FROM msuser WHERE username = '"+usnFl.getText()+"' AND password ='"+passFl.getText()+"' AND role ='Customer'";                  
							con.rs = con.executeQuery(query);
							if(con.rs.next()) {
								flag=true;
							}
							
							if(flag == false) {
								errorLb.setText("Credentials failed!");
							}
							else {
								Home h = new Home(stage, usnFl.getText());
								h.show();
								
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						
					}
				}

		
		}
		
		
}
