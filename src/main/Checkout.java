package main;

import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Check;

public class Checkout implements EventHandler<ActionEvent>{
	  BorderPane bp;
	  VBox vb;
	  GridPane gp;
	  HBox hbox, hbox2;
	  Scene scene;
	  
	  ToolBar toolBar;
	  Region spacer, spacer2, spacer3;
	  
	  Label titleLb, cusLb, itemLb, totPriceLb, payLb, cash, debit, credit, errorLb;
	  String usn;
	  Button loutBtn, cancelBtn, coBtn;
	  RadioButton payBtn1, payBtn2, payBtn3;
	  
	  Stage stage;
	  
	  ToggleGroup pToggle;
	  
	  Connect con = Connect.getInstance();
	  ArrayList<Check> clist = new ArrayList<>();
	  StringBuilder details = new StringBuilder();
	  
	  int totalfinal;
	
	  int price;
	  
	  private void initialize() {
	  bp = new BorderPane();
	  vb = new VBox();
	  gp = new GridPane();
	  hbox = new HBox(10);
	  hbox2 = new HBox(10);
	  
	  titleLb = new Label("Checkout");
	  cusLb = new Label("Hi, "+usn);
	  itemLb = new Label("1x Avocado");
	  totPriceLb = new Label("Total Price: "+totalfinal);
	  payLb = new Label("Payment Type:");
	  cash = new Label("Cash");
	  debit = new Label("Debit");
	  credit = new Label("Credit");
	  errorLb = new Label();
	  loutBtn = new Button("Logout");
	  cancelBtn = new Button("Cancel");
	  coBtn = new Button("Checkout");
	  payBtn1 = new RadioButton();
	  payBtn2 = new RadioButton();
	  payBtn3 = new RadioButton();
	  
	  pToggle = new ToggleGroup();
	  
	  payBtn1.setToggleGroup(pToggle); 
      payBtn2.setToggleGroup(pToggle); 
      payBtn3.setToggleGroup(pToggle); 
      
	  spacer = new Region();
	  spacer2 = new Region();
	  spacer3 = new Region();
	  toolBar = new ToolBar(loutBtn, spacer, cusLb);
	  
	  scene = new Scene(bp, 800, 600);
	  
	  
	  
	  }
	  
	  private void add() {
	  bp.setTop(toolBar);
	  bp.setCenter(vb);
	  gp.addColumn(0, itemLb, totPriceLb, payLb, hbox, hbox2);
	  vb.getChildren().addAll(titleLb, gp);
	  hbox.getChildren().addAll(payBtn1, cash, spacer2, payBtn2, debit, spacer3, payBtn3, credit);
	  hbox2.getChildren().addAll(cancelBtn, coBtn);
	  
	  }
	  
	  private void arrange() {
	  spacer.setMinWidth(650);
	  spacer2.setMinWidth(30);
	  spacer3.setMinWidth(30);
	  
	  hbox2.setAlignment(Pos.CENTER);
	  gp.setAlignment(Pos.CENTER);
	  gp.setVgap(10);
	  vb.setAlignment(Pos.CENTER);
	  vb.setSpacing(0);
	  hbox.setSpacing(0);
	  
	  }
	  
	  private void style() {
	  errorLb.setStyle("-fx-text-fill: red");
	  titleLb.setFont(Font.font ("Montserrat", FontWeight.BOLD, 45));
	  payLb.setFont(Font.font(14));
	  
	  cancelBtn.setPrefWidth(70);
	  cancelBtn.setPrefHeight(50);
	  coBtn.setPrefWidth(90);
	  coBtn.setPrefHeight(50);
	  
	  }
	  
	  private void setEvent() {
			loutBtn.setOnAction(this);
			coBtn.setOnAction(this);
			cancelBtn.setOnAction(this);
		}
	  
	  public Checkout(Stage stage, String usn) {
		  	this.usn = usn;
		  	initialize();
		  	cart();
			add();
			arrange();
			style();
			setEvent();
			stage.setScene(scene);
			this.stage = stage;
	
	  }
	  
	  private void cart() {
		String query = "SELECT * FROM msjuice a JOIN cartdetail b ON a.JuiceId = b.JuiceId WHERE Username = '"+usn+"'";
		con.rs = con.executeQuery(query);
		try {
			while(con.rs.next()) {
				int qty = con.rs.getInt("Quantity");
				String name = con.rs.getString("JuiceName");
				int price = con.rs.getInt("Price");
				int total = qty*price;
				String id = con.rs.getString("JuiceId");
				clist.add(new Check(qty, name, price, total, id));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(Check c : clist) {
			details.append(c.getQty()).append("x ").append(c.getName()).append("   [").append(c.getQty()).append("x Rp. ").append(c.getPrice()).append(",- = Rp. ").append(c.getTotal()).append(",-]").append("\n");
			totalfinal+=c.getTotal();
		}
		
		itemLb.setText(details.toString());
		 totPriceLb.setText("Total Price: Rp. "+totalfinal+",-"); 
	}

	public void show() {
		  stage.show();
	  }
	  
	  @Override
		public void handle(ActionEvent e) {
			if(e.getSource() == loutBtn) {
			     Login login = new Login(stage);
			     login.show();
			}
			
			if(e.getSource() == coBtn) {
				
				int num =0;
				String query = "SELECT * FROM transactionheader ORDER BY TransactionId DESC ";
				con.rs = con.executeQuery(query);
				try {
					while(con.rs.next()) {
						String id = con.rs.getString("TransactionId");
						num = Integer.parseInt(id.substring(2, 5));
						break;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(payBtn1.isSelected()) {
					
					
					query = String.format("INSERT INTO transactionheader VALUES('%s','%s','%s')", String.format("TR%03d", num+1), usn, "Cash" );
					con.executeUpdate(query);
										
					for(Check c : clist) {
						query = String.format("INSERT INTO transactiondetail VALUES('%s','%s','%d')", String.format("TR%03d", num+1), c.getId(), c.getQty());
					    con.executeUpdate(query);
					}
					
					query = String.format("DELETE FROM cartdetail WHERE Username = '%s'", usn);
					con.executeUpdate(query);
					
					 Alert alert = new Alert(AlertType.INFORMATION);
					 alert.setTitle("Message");
				     alert.setContentText("All items checked out succesfully, please proceed your...");
				     alert.showAndWait();
				     
				     Home home = new Home(stage, usn);
				     home.show();
				}

				else if(payBtn2.isSelected()) {
					
					
					query = String.format("INSERT INTO transactionheader VALUES('%s','%s','%s')", String.format("TR%03d", num+1), usn, "Debit" );
					con.executeUpdate(query);
										
					for(Check c : clist) {
						query = String.format("INSERT INTO transactiondetail VALUES('%s','%s','%d')", String.format("TR%03d", num+1), c.getId(), c.getQty());
					    con.executeUpdate(query);
					}
					

					query = String.format("DELETE FROM cartdetail WHERE Username = '%s'", usn);
					con.executeUpdate(query);
					
					 Alert alert = new Alert(AlertType.INFORMATION);
					 alert.setTitle("Message");
				     alert.setContentText("All items checked out succesfully, please proceed your...");
				     alert.showAndWait();
				     
				     Home home = new Home(stage, usn);
				     home.show();
				}
				
				else if(payBtn3.isSelected()) {
					
					
					query = String.format("INSERT INTO transactionheader VALUES('%s','%s','%s')", String.format("TR%03d", num+1), usn, "Credit" );
					con.executeUpdate(query);
										
					for(Check c : clist) {
						query = String.format("INSERT INTO transactiondetail VALUES('%s','%s','%d')", String.format("TR%03d", num+1), c.getId(), c.getQty());
					    con.executeUpdate(query);
					}
					
					query = String.format("DELETE FROM cartdetail WHERE Username = '%s'", usn);
					con.executeUpdate(query);
					
					 Alert alert = new Alert(AlertType.INFORMATION);
					 alert.setTitle("Message");
				     alert.setContentText("All items checked out succesfully, please proceed your...");
				     alert.showAndWait();
				     
				     Home home = new Home(stage, usn);
				     home.show();
				}
				
				
				
				else {
					 Alert alert = new Alert(AlertType.ERROR);
					 alert.setTitle("Error");
				     alert.setContentText("Please select payment type");
				     alert.showAndWait();
				}
				
			}
			
			if(e.getSource() == cancelBtn) {
				Home h = new Home(stage, usn);
				h.show();
			}
			
		}
	  
}
	