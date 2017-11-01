package com.wingain;
	
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    
//		    if(true)
//		    {
//		       ProductShipmentDB.test();
//		       return; 
//		    }
		    
		    
		    ResourceBundle bundles = ResourceBundle.getBundle("com.wingain.bundles.string");
		    //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/BarcodeTranslator.fxml") ,bundles);
		    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ProductShipment.fxml") ,bundles);
			//loader.setResources(bundles);
		    
		    //AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("view/BarcodeTranslator.fxml"));
			Parent root = loader.load();
		    Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		//LabelPrinter.openPrinter();
		//LabelBGenerator.printLabel("1P473098A.102", "SRK173607585", "Q1", "1633", "Made In China", "ACIM");
		//LabelPrinter.closePrinter();
		launch(args);
	}
}
