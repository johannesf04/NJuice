package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import connect.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Home implements EventHandler<ActionEvent> {

    BorderPane bp;
    VBox vbox;
    Scene scene;

    Label titleLb2, stitleLb2, cusLb;
    String usn;
    Button addBtn, delBtn, checkBtn, logBtn;

    ToolBar toolBar;
    Region spacer;

    ListView<String> listCart;
    ObservableList<String> cart = FXCollections.observableArrayList();

    HBox hbox;
    Stage stage;

    private Connect con = Connect.getInstance();

    private void initialize() {
        bp = new BorderPane();
        vbox = new VBox(10);
        hbox = new HBox(10);

        titleLb2 = new Label("Your Cart");
        stitleLb2 = new Label("Total Price: ");

        addBtn = new Button("Add new item to cart");
        delBtn = new Button("Delete item from cart");
        checkBtn = new Button("Checkout");

        scene = new Scene(bp, 800, 600);

        cusLb = new Label("Hi, " + usn);
        logBtn = new Button("Logout");
        spacer = new Region();
        spacer.setMinWidth(650);
        listCart = new ListView<>();
        toolBar = new ToolBar(logBtn, spacer, cusLb);
    }

    private void setButton() {
        addBtn.setPrefWidth(150);
        addBtn.setPrefHeight(50);

        delBtn.setPrefWidth(150);
        delBtn.setPrefHeight(50);

        checkBtn.setPrefWidth(100);
        checkBtn.setPrefHeight(50);

        hbox.getChildren().addAll(addBtn, delBtn, checkBtn);
    }

    private StackPane createListPane() {
        StackPane stackPane = new StackPane();

        if (listCart.getItems().isEmpty()) {
            stitleLb2.setText("Your cart is empty");
            
        } else {
            stackPane.getChildren().add(listCart);
            
        }

        stackPane.setAlignment(Pos.CENTER);
        listCart.setMaxHeight(250);
        listCart.setMaxWidth(450);

        return stackPane;
    }

    private void add() {
        vbox.getChildren().clear(); 
        vbox.getChildren().addAll(titleLb2, createListPane(), stitleLb2, hbox);
        bp.setTop(toolBar);
        bp.setCenter(vbox);
    }

    private void arrange() {
        vbox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
    }

    private void style() {
        stitleLb2.setAlignment(Pos.CENTER);
        titleLb2.setFont(Font.font("Montserrat", FontWeight.BOLD, 50));
        stitleLb2.setFont(Font.font("Montserrat", 15));
        listCart.setMaxHeight(250);
        listCart.setMaxWidth(450);
    }

    private void setEvent() {
        delBtn.setOnAction(this);
        checkBtn.setOnAction(this);
        logBtn.setOnAction(this);
        addBtn.setOnAction(this);
    }

    public Home(Stage stage, String usn) {
        this.usn = usn;
        this.stage = stage;
        initialize();
        getData();
        add();
        setButton();
        arrange();
        style();
        setEvent();
        refresh();
        stage.setScene(scene);
    }

    public void show() {
        stage.show();
    }

    private void getData() {
        listCart.getItems().clear();

        String query = "SELECT * FROM cartdetail c JOIN msjuice m ON c.JuiceId = m.JuiceId WHERE Username = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, usn);
            con.rs = preparedStatement.executeQuery();

            int total2 = 0;
            while (con.rs.next()) {
                String jName = con.rs.getString("JuiceName");
                int qty = con.rs.getInt("Quantity");
                int price = con.rs.getInt("Price");
                String x = "" + qty + "x " + jName + " - [Rp. " + price * qty + "]";
                int total1= price * qty;
                cart.add(x);
                listCart.setItems(cart);
                total2 +=total1;
                stitleLb2.setText("Total Price: " + total2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void refresh() {
		cart.clear();
		getData();
	}


    @Override
    public void handle(ActionEvent e) {
    	Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");

        if (e.getSource() == delBtn) {
        	
        	String selected = listCart.getSelectionModel().getSelectedItem();
        	
        	if(selected == null) {
				alert.setContentText("Select a juice");
                alert.showAndWait();
			}
			else {
				String name = selected.substring(selected.indexOf("x")+2, selected.indexOf("-")-1);
				String query = String.format("DELETE FROM cartdetail WHERE JuiceId IN (SELECT JuiceId FROM msjuice WHERE JuiceName = '%s') AND Username = '%s'", name, usn);
				con.executeUpdate(query);
				refresh();
				add();
			}
        	
        }

        if (e.getSource() == checkBtn) {
            if (listCart.getItems().isEmpty()) {
                alert.setContentText("Your cart is empty");
                alert.showAndWait();
            } else {
                Checkout co = new Checkout(stage, usn);
                co.show();
            }
        }

        if (e.getSource() == logBtn) {
            Login login = new Login(stage);
            login.show();
        }

        if (e.getSource() == addBtn) {
        	Stage addStage = new Stage();
			Add add = new Add(addStage, usn, stage);
			add.show();
			
			
        }
 	 
}
    
}
