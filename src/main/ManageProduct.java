package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Juice;
import model.Manage;
import model.View1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import connect.Connect;

public class ManageProduct implements EventHandler<ActionEvent> {
    private Stage stage;
    private Scene scene;

    private ComboBox<String> opt;
    private TextField txpName;
    private Spinner<Integer> priceSpin;
    private TextArea txpDesc;

    private TableView<Manage> tabel;
    private ArrayList<String> JuiceId = new ArrayList<>();
    private ArrayList<Manage> Juice = new ArrayList<>();
    Connect con = Connect.getInstance();

    
    private MenuBar menuBar;
    private Menu menu, menu2;
    private MenuItem viewMn, JuiceMn, logoutMn;

    private Region spacer;
    
    BorderPane bp;
    VBox vb;
    GridPane gp;
    
    Button addBtn;
    Button updateBtn;
    Button delBtn;
    Label titleLb, idLb, nameLb, priceLb;
    
    private String tempselected;
    
  

    private void initialize() {
    	bp = new BorderPane();
        vb = new VBox();
        gp = new GridPane();
    	
        opt = new ComboBox<>();
        txpName = new TextField();
        priceSpin = new Spinner<>(10000, Integer.MAX_VALUE, 10000);
        txpDesc = new TextArea();
        

        tabel = new TableView<>();

        tabelV();

        mb();
        spacer = new Region();
        spacer.setMinWidth(10);
        
        scene = new Scene(bp, 1000, 600);
    }

    private void tabelV() {
    	
    	 TableColumn<Manage, String> idColumn = new TableColumn<>("Juice ID");
	     idColumn.setCellValueFactory(new PropertyValueFactory<Manage, String>("id"));
	     idColumn.setPrefWidth(90); 
	     tabel.getColumns().add(idColumn);
	     
	     TableColumn<Manage, String> nameColumn = new TableColumn<>("Juice Name");
	     nameColumn.setCellValueFactory(new PropertyValueFactory<Manage, String>("name"));
	     nameColumn.setPrefWidth(150);	     
	     tabel.getColumns().add(nameColumn);
	     
	     TableColumn<Manage, Integer> priceColumn = new TableColumn<>("Price");
	     priceColumn.setCellValueFactory(new PropertyValueFactory<Manage, Integer>("price"));
	     priceColumn.setPrefWidth(90);	     
	     tabel.getColumns().add(priceColumn);
    	
	     TableColumn<Manage, String> descColumn = new TableColumn<>("Juice Description");
	     descColumn.setCellValueFactory(new PropertyValueFactory<Manage, String>("desc"));
	     descColumn.setPrefWidth(200);	     
	     tabel.getColumns().add(descColumn);
	     
    }

    private void mb() {
        menuBar = new MenuBar();
        menu = new Menu("Admin's Dashboard");
        viewMn = new MenuItem("View Transaction");
        JuiceMn = new MenuItem("Juice Products");

        menu.getItems().addAll(viewMn, JuiceMn);
        menu2 = new Menu("Logout");
        logoutMn = new MenuItem("Logout");
        menu2.getItems().addAll(logoutMn);
        menuBar.getMenus().addAll(menu, menu2);
    }

    private void set() {
        titleLb = new Label("Juice Products");
        titleLb.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        idLb = new Label("Product ID to delete/remove:");
        priceLb = new Label("Price:");
        nameLb = new Label("Product Name:");

        addBtn = new Button("Insert Juice");
        updateBtn = new Button("Update Price");
        delBtn = new Button("Remove Juice");

        txpName.setPromptText("Insert product name to be created");
        txpDesc.setPromptText("Insert the new product's text description, min. 10 & max.100");
        
        
    }
    
    private void add() {
    	
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(10));

        gp.addColumn(0, idLb, priceLb, nameLb);
        gp.addColumn(1, opt, priceSpin, txpName, txpDesc);
        gp.addColumn(2, addBtn, updateBtn, delBtn);

        vb.getChildren().addAll(titleLb, tabel, gp);
        vb.setSpacing(20);
        
        
    }
    
    private void arrange() {
    	tabel.setMaxSize(532, 300);
    	
    	vb.setSpacing(20);
        vb.setPadding(new Insets(10));
    	
        vb.setAlignment(Pos.TOP_CENTER);

        bp.setTop(menuBar);
        bp.setCenter(vb);
        gp.setAlignment(Pos.CENTER);
    }

    private void setEventHandlers() {
    	viewMn.setOnAction(this);
 		logoutMn.setOnAction(this);
    	addBtn.setOnAction(this);
    	delBtn.setOnAction(this);
    	updateBtn.setOnAction(this);
    	}
    
			

    
    private void getData() {
		Juice.clear();
		JuiceId.clear();
		
		String query = "SELECT * FROM msjuice";
		con.rs = con.executeQuery(query);
		try {
			while(con.rs.next()) {
				String id = con.rs.getString("JuiceId");
				String name = con.rs.getString("JuiceName");
				Integer price = con.rs.getInt("Price");
				String desc = con.rs.getString("JuiceDescription");
				JuiceId.add(id);
				Juice.add(new Manage(id, name, price, desc));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    
    private void refreshTable() {
		getData();
		ObservableList<Manage> viewobj = FXCollections.observableArrayList(Juice);
		tabel.setItems(viewobj);
		ObservableList<String> viewopt = FXCollections.observableArrayList(JuiceId);
		opt.setItems(viewopt);
		
    }
    

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public ManageProduct(Stage stage) {
        initialize();
        set();
        add();
        arrange();
        setEventHandlers();
        refreshTable();
        stage.setScene(scene);
        this.stage = stage;
    }

    

	public void show() {
        stage.show();
    }
    

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == viewMn) {
			ViewTransaction vt = new ViewTransaction(stage);
			vt.show();
		}
		if (e.getSource() == logoutMn) {
			Login login = new Login(stage);
			login.show();
		}
		
		int num=0;
		if(e.getSource() == addBtn) {
			String jname = txpName.getText();
			String jdesc = txpDesc.getText();
			int jprice = priceSpin.getValue();
			if(jname.isEmpty()||jdesc.isEmpty()) {
				showAlert("Error", "Please fill all the field");
			}
			else if(jprice<10000){
				showAlert("Error", "Price must not be below 10000");
			}
			
			else if(jdesc.length()<10||jdesc.length()>100) {
				showAlert("Error", "Description input length must not be below 10 and over 100");
			}
			else {
				String query = "SELECT * FROM msjuice ORDER BY JuiceId DESC ";
				con.rs = con.executeQuery(query);
				try {
					while(con.rs.next()) {
						String id = con.rs.getString("JuiceId");
						num = Integer.parseInt(id.substring(2, 5));
						break;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
				String id2 = String.format("JU%03d", num+1);
				String name = txpName.getText();
				int price = priceSpin.getValue();
				String desc = txpDesc.getText();
				query = String.format("INSERT INTO msjuice VALUES('%s','%s','%d','%s')", id2, name, price,desc );
				con.executeUpdate(query);
				refreshTable();	
			}
			
		}
		else if(e.getSource() == updateBtn) {
			String jname = txpName.getText();
			String jdesc = txpDesc.getText();
			int jprice = priceSpin.getValue();
			tempselected = opt.getValue();
			if(jname.isEmpty()||jdesc.isEmpty()) {
				showAlert("Error", "Please fill all the field");
			}
			else if(jprice<10000){
				showAlert("Error", "Price must not be below 10000");
			}
			
			else if(jdesc.length()<10||jdesc.length()>100) {
				showAlert("Error", "Description input length must not be below 10 and over 100");
			}
			else if(opt.getSelectionModel().getSelectedItem() == null) {
				showAlert("Error", "Select a juice id");
			}
			else {
//				Manage selected = tabel.getSelectionModel().getSelectedItem();
//				tempselected = selected.getId();
						
			String query = String.format("UPDATE msjuice SET JuiceName = '%s', Price = %d, JuiceDescription = '%s' WHERE JuiceId = '%s'", jname, jprice, jdesc, tempselected);
			con.executeUpdate(query);
			refreshTable();
			}
		}
		
		else if(e.getSource() == delBtn) {
			String jname = txpName.getText();
			String jdesc = txpDesc.getText();
			int jprice = priceSpin.getValue();
			tempselected = opt.getValue();
			if(opt.getSelectionModel().getSelectedItem() == null) {
				showAlert("Error", "Select a juice id");
			}
			else {
//				Manage selected = tabel.getSelectionModel().getSelectedItem();
//				tempselected = selected.getId();
						
			String query = String.format("DELETE FROM msjuice WHERE JuiceId = '%s'", tempselected);
			con.executeUpdate(query);
			refreshTable();
			}
		}
		
		
	}
}