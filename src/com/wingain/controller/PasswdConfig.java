package com.wingain.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class PasswdConfig  implements Initializable
{
    private DialogCallback callback;
    @FXML PasswordField old_pass;
    @FXML PasswordField new_pass;
    @FXML PasswordField new_pass_confirm;
    @FXML AnchorPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // TODO Auto-generated method stub
        
    }

    private void showAlertDialog(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.initOwner(root.getScene().getWindow());
        alert.show();
    }
    @FXML public void onPassOK() {
        String old = old_pass.getText();
        
        if(BarcodeTranslatorController.checkAuthen(old))
        {
            if(new_pass.getText().length() > 0 && new_pass.getText().equals(new_pass_confirm.getText()))
            {
                if(callback != null && callback.onOK(new_pass.getText()))
                {
                    root.getScene().getWindow().hide();
                }
            }else
            {
                showAlertDialog("Erro", "password is not match");
            }
            
        }else {
            showAlertDialog("Erro", "wrong password");
        }
    }

    @FXML public void onPassCancle() {
        root.getScene().getWindow().hide();
    }

    public void setCallback(DialogCallback dialogCallback)
    {
        callback = dialogCallback;
    }

}
