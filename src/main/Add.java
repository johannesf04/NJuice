package main;

import java.sql.SQLException;
import java.util.ArrayList;

import connect.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Juice;
import model.Manage;

public class Add implements EventHandler<ActionEvent> {
	 Window window;
	 StackPane sp;
	 BorderPane bp;
	 VBox vb;
	 HBox hb;
	 GridPane fp;
	 Scene scene;

	    
	  Label juiceLb, priceLb, quantityLb, totalLb;
	  Button addBtn;
	  
	  ComboBox<String> juiceBox;
	  
	  ArrayList<String> JuiceName = new ArrayList<>();
	  ArrayList<Manage> Juice = new ArrayList<>();
	  ArrayList<Juice> selectedList = new ArrayList<>();
	    
	  Spinner<Integer> quantitySpinner;
	  private Stage stage;
	  
	  Connect con = Connect.getInstance();
	  
	  String usn, jid;
	  Text descT;
		

	 	private void initialize() {
	 		   window = new Window();
	 		   bp = new BorderPane();
	 		   fp = new GridPane();
	 		   vb = new VBox();
	 		   sp = new StackPane();
	 		   hb = new HBox();
	 		   
	 		  
	 		   juiceLb = new Label("Juice: ");
	 		   priceLb = new Label("Juice Price: ");
	 		   descT = new Text("Description: -");
	 		   quantityLb = new Label("Quantity: ");
	 		   totalLb = new Label("Total Price: ");
	 		   

	 		   
	 		   addBtn = new Button("Add Item");
	 		   
	 		   juiceBox = new ComboBox<>();
	 		   
	 		   quantitySpinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
	 		   
	 		   scene = new Scene(sp, 400, 500);
	 	}
	 		
	
	 	private void add() {
	 	   
	 	   fp.addRow(0, juiceBox, priceLb);
	 	  
	 	   vb.getChildren().addAll(juiceLb, fp, descT, quantityLb, quantitySpinner, totalLb, addBtn);
	 	   
	 	   bp.setTop(hb);
	 	   bp.setCenter(vb);
	 	   window.setTitle("Add new item");
	 	   window.getContentPane().setStyle("-fx-background-color: white;");
	 	   window.getContentPane().getChildren().add(bp);
	 	   sp.getChildren().add(window);
	 	}
	 	
	 	
	 	private void arrange() {
	 		descT.setTextAlignment(TextAlignment.CENTER);
	 	   hb.setAlignment(Pos.CENTER);
	 	   vb.setAlignment(Pos.CENTER);
	 	   vb.setSpacing(10);
	 	   fp.setAlignment(Pos.CENTER);
	 	   fp.setHgap(5);
	 	}
	   
	 	private void style() {
	 		descT.setWrappingWidth(300);
			juiceBox.setMinWidth(150);
			
		}
	     
	 	private void refreshTable() {
			getdata();
			ObservableList<String> viewname = FXCollections.observableArrayList(JuiceName); 
			juiceBox.setItems(viewname);
	 	}


		private void getdata() {
			Juice.clear();
			JuiceName.clear();
			
			String query = "SELECT * FROM msjuice";
			con.rs = con.executeQuery(query);
			try {
				while(con.rs.next()) {
					String id = con.rs.getString("JuiceId");
					String name = con.rs.getString("JuiceName");
					Integer price = con.rs.getInt("Price");
					String desc = con.rs.getString("JuiceDescription");
					JuiceName.add(name);
					Juice.add(new Manage(id, name, price, desc));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		private void select() {
			juiceBox.valueProperty().addListener((obs, oldSelection, newSelection) ->{
				if (newSelection != null) {
					String selected = juiceBox.getValue();
					showDetail(selected);
				}
			});
			
			quantitySpinner.valueProperty().addListener((obs, oldSelection, newSelection) ->{
				if (newSelection != null) {
					String selected = juiceBox.getValue();
					showDetail(selected);
				}
			});
			
		}
		
		private void showDetail(String selected) {
			String query = String.format("SELECT * FROM msjuice WHERE JuiceName = '%s'", selected);
			con.rs = con.executeQuery(query);
			
			try {
				while(con.rs.next()) {
					String id = con.rs.getString("JuiceId");
					String name = con.rs.getString("JuiceName");
					Integer price = con.rs.getInt("Price");
					String desc = con.rs.getString("JuiceDescription");
					Juice j = new Juice(id, name, price, desc);
					int qty = quantitySpinner.getValue();
					selectedList.add(j);
					juiceLb.setText("Juice: "+j.getName());
					priceLb.setText("Juice Price: "+j.getPrice());
					descT.setText("Description: "+j.getDesc());
					totalLb.setText("Total Price: "+(j.getPrice()*qty));
					jid = j.getId();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}


		private void setEvent() {
			addBtn.setOnAction(this);
			
		}
	private Stage primarystage;
	public Add(Stage stage, String usn, Stage primarystage) {
	this.primarystage=primarystage;
	this.usn=usn;
	this.stage = stage;
	initialize();
	add();
	arrange();
	style();
	setEvent();
	refreshTable();
	select();
	stage.setScene(scene);
	}


	public void show() {
	stage.show();
	
	}


	@Override
	public void handle(ActionEvent e) {
		if(e.getSource() == addBtn) {
			String query = "SELECT * FROM cartdetail WHERE Username = '"+usn+"' AND JuiceId = '"+jid+"'";
			con.rs = con.executeQuery(query);
			
			try {
				if(con.rs.next()) {
					int q = con.rs.getInt("Quantity");
					query = String.format("UPDATE cartdetail SET Quantity = %d WHERE Username = '%s' AND JuiceId = '%s'", quantitySpinner.getValue()+q,usn, jid);
					con.executeUpdate(query);
				}
				else {
					query = String.format("INSERT INTO cartdetail VALUES('%s', '%s', '%d')", usn, jid, quantitySpinner.getValue());
					con.executeUpdate(query);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			new Home(primarystage, usn);
			
			stage.close();
		
		}
		
	}
}