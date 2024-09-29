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
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.View1;
import model.View2;

public class ViewTransaction implements EventHandler<ActionEvent>{
	 	BorderPane bp;
	    GridPane fp;
	    VBox vb;
	    Scene scene;
	      
	    Label titleLb2,pHolder;
	    
	    MenuBar menuBar;
	    Menu menu, menu2;
	    MenuItem viewMn, manageMn,logoutMn;
	    
	    
	    Region spacer;
	    
	    Stage stage;
	    
	    TableView<View1> tabel1;
		TableView<View2> tabel2;
		ArrayList<View1> view1List = new ArrayList<>();
		ArrayList<View2> view2List = new ArrayList<>();
		Connect con = Connect.getInstance();
	    
		
	    private void initialize() {
	     bp = new BorderPane();
	     fp = new GridPane();
	     vb = new VBox();
	     
	     titleLb2 = new Label("View Transaction");
	     pHolder = new Label("Click on a transaction header to view the transaction detail");

	     scene = new Scene(bp, 800, 600);

	     spacer = new Region();
	     spacer.setMinWidth(10);
	     
	     menuBar = new MenuBar();
	     menu = new Menu("Admin's Dashboard");
	     viewMn = new MenuItem("View Transaction");
	     manageMn = new MenuItem("Manage Products");
	     
	     menu2 = new Menu("Logout");
	     logoutMn= new MenuItem("Logout from admin");

	     
	     tabel1 = new TableView<>();
	     tabel2 = new TableView<>();
	     
	    }

	     private void set() {
	     menu.getItems().addAll(viewMn, manageMn);
	     menu2.getItems().addAll(logoutMn);
	     menuBar.getMenus().addAll(menu,menu2);
	     
	     }
	     
	     private void addTabel1() {
	     TableColumn<View1, String> idColumn = new TableColumn<>("Transaction Id");
	     idColumn.setCellValueFactory(new PropertyValueFactory<View1, String>("id"));
	     idColumn.setPrefWidth(120); 
	     tabel1.getColumns().add(idColumn);
	     
	     TableColumn<View1, String> payColumn = new TableColumn<>("Payment Type");
	     payColumn.setCellValueFactory(new PropertyValueFactory<View1, String>("type"));
	     payColumn.setPrefWidth(120);	     
	     tabel1.getColumns().add(payColumn);
	     
	     TableColumn<View1, String> nameColumn = new TableColumn<>("Username");
	     nameColumn.setCellValueFactory(new PropertyValueFactory<View1, String>("usn"));
	     nameColumn.setPrefWidth(110);	     
	     tabel1.getColumns().add(nameColumn);
	     
	     }
	    
	     private void addTabel2() {
	    	 
	     TableColumn<View2, String> idColumn2 = new TableColumn<>("Transaction Id");
	     idColumn2.setCellValueFactory(new PropertyValueFactory<View2, String>("id"));
	     tabel2.getColumns().add(idColumn2);
	     idColumn2.setMinWidth(120);
	     
	     TableColumn<View2, String> jusColumn = new TableColumn<>("Juice Id");
	     jusColumn.setCellValueFactory(new PropertyValueFactory<View2, String>("jid"));
	     tabel2.getColumns().add(jusColumn);
	     jusColumn.setMinWidth(100);
	     
	     TableColumn<View2, String> jusNameColumn = new TableColumn<>("Juice Name");
	     jusNameColumn.setCellValueFactory(new PropertyValueFactory<View2, String>("jname"));
	     tabel2.getColumns().add(jusNameColumn);
	     jusNameColumn.setMinWidth(150);
	     
	     TableColumn<View2, Integer> qtyColumn = new TableColumn<>("Quantity");
	     qtyColumn.setCellValueFactory(new PropertyValueFactory<View2, Integer>("qty"));
	     tabel2.getColumns().add(qtyColumn);
	     qtyColumn.setMinWidth(80);
	     
	     tabel2.setPlaceholder(pHolder);
	     
	     tabel1.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    	 if(newSelection == null) {
	    		 tabel2.getItems().clear();
	    		 tabel2.setPlaceholder(pHolder);
	    	 }
	    	 else {
	    		 String selected = newSelection.getId();
	    		 showDetail(selected);
	    	 }
	     });
	     
	     }
	     
	     private void showDetail(String selected) {
	    	 view2List.clear(); 
	    	 String query = "SELECT a.TransactionID, b.juiceID, b.juiceName, a.quantity FROM transactiondetail a JOIN msjuice b ON a.JuiceId = b.JuiceId WHERE TransactionId = '"+selected+"'";
	    	  con.rs = con.executeQuery(query);
	    	  
	    	  try {
	    	  while(con.rs.next()) {
					String id = con.rs.getString("TransactionID");
					String jid = con.rs.getString("JuiceId");
					String jname = con.rs.getString("JuiceName");
					int qty = con.rs.getInt("Quantity");
					
					view2List.add(new View2(id, jid, jname, qty));
				
					ObservableList<View2> viewobj = FXCollections.observableArrayList(view2List);
					tabel2.setItems(viewobj);
	    	  
	    	  } 
	    	  } catch (Exception e) {
					e.printStackTrace();
				}

	     }
	     
	    private void add() { 
	     vb.getChildren().addAll(titleLb2,tabel1,tabel2);
	     
	     bp.setTop(menuBar);
	     bp.setCenter(vb);
	     bp.setBottom(fp);
	     
	    }
	     
	    private void arrange() {
	     tabel1.setMaxSize(352, 200);
	     tabel2.setMaxSize(452, 200);
	     
	     fp.setHgap(10);
	     fp.setVgap(10);
	     fp.setAlignment(Pos.TOP_CENTER);
	     vb.setAlignment(Pos.CENTER);
	     
	     vb.setSpacing(20);
	     
	    }

	    private void style() {
	    titleLb2.setFont(Font.font ("Montserrat",FontWeight.BOLD, 20));
	 }
	    private void setEvent(){
	    	manageMn.setOnAction(this);
	    	logoutMn.setOnAction(this);
	    }
	    
	    
	    private void getData() {
			view1List.clear();
			
			String query = "SELECT * FROM transactionheader";
			con.rs = con.executeQuery(query);
			try {
				while(con.rs.next()) {
					String id = con.rs.getString("TransactionID");
					String type = con.rs.getString("PaymentType");
					String usn = con.rs.getString("Username");
					
					view1List.add(new View1(id, type, usn));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		}
	    
	    
	    private void refreshTable() {
			getData();
			ObservableList<View1> viewobj = FXCollections.observableArrayList(view1List);
			tabel1.setItems(viewobj);
	    }

		public ViewTransaction(Stage stage) {
		  	initialize();
		  	set();
			addTabel1();
			addTabel2();
			add();
			arrange();
			style();
			setEvent();
			refreshTable();
			stage.setScene(scene);
			this.stage = stage;
	  }

	public void show() {
		  stage.show();
	  }

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == manageMn) {
			ManageProduct manage = new ManageProduct(stage);
			manage.show();
		}
		if (e.getSource() == logoutMn) {
			Login login = new Login(stage);
			login.show();
		
	}
	}   
}
