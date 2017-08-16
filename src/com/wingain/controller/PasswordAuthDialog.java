package com.wingain.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
    }
    @FXML public void onPasswordInputDone(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            onPasswordOK();
        }
    }
    
 
}
