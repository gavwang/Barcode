package com.wingain.controller;

import javafx.fxml.Initializable;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

import com.wingain.label.LabelBGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage; 

public class BarcodeTranslatorController implements Initializable {
    @FXML
    private Label app;

    @FXML TextField orderNumber;

    @FXML Button startScanBtn;

    @FXML Button stopScanBtn;

    @FXML TextArea logText;

    @FXML ImageView barImage;

    @FXML TextField imageW;

    @FXML TextField imgHeight;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //bundles = ResourceBundle.getBundle("bundles.string", new Locale("en", "US"));
        
       // app.setText(bundles.getString("app_name"));
    
    }

    @FXML public void onStartScan(ActionEvent event) {
        logText.setText("Start Scan");
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                BufferedImage img = LabelBGenerator.labelB("test", 350, 150);
                WritableImage image = new WritableImage(img.getWidth(),img.getHeight());
                javafx.embed.swing.SwingFXUtils.toFXImage(img, image);
                barImage.setImage(image);
            }
        }).start();
        
    }

    @FXML public void onStopScan(ActionEvent event) {
        logText.setText("Stop Scan");
        barImage.setImage(null);
    }
}
