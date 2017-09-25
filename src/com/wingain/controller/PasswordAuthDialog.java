package com.wingain.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class PasswordAuthDialog implements Initializable
{
    private DialogCallback dialogCallback = null;
    

    @FXML PasswordField password;
    @FXML Node root;
    @FXML Label logininfo;
    
    public void setCallback(DialogCallback cb){dialogCallback = cb;}
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        logininfo.setText("");
        
    }

    @FXML public void onPasswordOK() {
        System.out.println("onPasswordOK");
        
        if(dialogCallback != null && dialogCallback.onOK(password.getText()))
        {
            root.getScene().getWindow().hide();
        }else
        {
            logininfo.setText("wrong password");
        }
    }

    @FXML public void onPasswordCancle() {
        System.out.println("onPasswordCancle");
        root.getScene().getWindow().hide();
    }

    @FXML public void onPasswordReset() {
        System.out.println("onPasswordReset");
        showPasswordDialog();
        
        
    }
    @FXML public void onPasswordInputDone(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            onPasswordOK();
        }
    }
    
    
    private void showPasswordDialog()
    {
        Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        try
        {
            ResourceBundle bundles = ResourceBundle.getBundle("com.wingain.bundles.string");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PasswdConfig.fxml") ,bundles);
            
           // loader.setRoot(dialogStage);
            Parent root = loader.load();//FXMLLoader.load(getClass().getResource("../view/Password.fxml"));
            PasswdConfig pController = (PasswdConfig)loader.getController();
            
            pController.setCallback(new DialogCallback()
            {
                @Override
                public boolean onOK(String txt)
                {
                    BarcodeTranslatorController.updateAuthen(txt);
                    return true;//authenticate(txt);
                }
                
                @Override
                public boolean onCancle()
                {
                    return false;
                }
            });
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
 
}
