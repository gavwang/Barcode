package com.wingain.controller;

import javafx.fxml.Initializable;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.wingain.model.DataExporter;
import com.wingain.model.LabelBGenerator;
import com.wingain.model.LabelDB;
import com.wingain.model.LabelItem;
import com.wingain.model.LabelPrinter;
import com.wingain.utility.PasswordHash;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.Spinner; 
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateStringConverter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView; 

public class BarcodeTranslatorController implements Initializable {
    
    private Thread scanThread = null; 
    private String productShortName= "AMIA";
    private static boolean authenticated = false;
    private int copies = 1;
    private ResourceBundle resourceBundle;
    private final ObservableList<LabelItem> searchResult = FXCollections.observableArrayList();
    
    @FXML private Label app;
    @FXML Node root;
    @FXML TextField orderNumber;
    @FXML Button startScanBtn;
    @FXML Button stopScanBtn;
    @FXML ImageView barImage;
    @FXML TextField imageW;
    @FXML TextField imgHeight;
    @FXML TextField shortName;
    @FXML Spinner<Integer> copiesSpinner;
    @FXML TextField test;
    @FXML Button password;
    @FXML TextField labelContent;
    @FXML TextField orderSearch;
    @FXML TextField productCodeSearch;
    @FXML DatePicker fromTime;
    @FXML DatePicker toTime;
    @FXML TableView<LabelItem> resultTable;
    
    private LabelItem selectedItem = null;

         
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authenticated = false;
        resourceBundle = rb;
        LabelDB.instance();
        toggleCopiesSpiner();
        
        
        initResultTable();
        //shortName.setText("AMIA");
        //resultList.s
        
       // resultTable.
        
    }
    
    private void toggleCopiesSpiner()
    {
        copiesSpinner.setEditable(authenticated);
        copiesSpinner.setDisable(!authenticated);
        
    }
    
    @FXML public void onStartScan(ActionEvent event) {
               
        if(shortName.getText() != null && shortName.getText().length()>0)
            productShortName = shortName.getText();
        
        stopScanBtn.setDisable(false);
        startScanBtn.setDisable(true);
        labelContent.setDisable(false);
        labelScanFocus();
        LabelPrinter.openPrinter();
        
//        scanThread = new Thread(new Runnable()
//        {
//            
//            @Override
//            public void run()
//            {               
//                scan();
//            }
//        });
//        scanThread.start();
        
    }

    @FXML public void onStopScan(ActionEvent event) {
        
        barImage.setImage(null);
        labelContent.setDisable(true);
        startScanBtn.setDisable(false);
        stopScanBtn.setDisable(true);
        LabelPrinter.closePrinter();
        
//        if(scanThread != null)
//        {
//            scanThread.interrupt();
//            scanThread = null;
//        }
    }
    
    private void scan()
    {
        Scanner scanner = new Scanner(System.in);
        String character  = null;
        do
        {
            character = scanner.next();
            labelTranslate(character);
        }while(!Thread.currentThread().isInterrupted());            
        scanner.close();

    }

    @FXML public void onChangeCopies(MouseEvent event) {
        System.out.println("on copies changed " + copiesSpinner.getValue());
    }
    @FXML public void onMouseTest(MouseEvent event) {}
    @FXML public void onPassword(ActionEvent event) {
        if(copiesSpinner.isDisable())
            showPasswordDialog();
        else
        {
            authenticated = !authenticated;
            toggleCopiesSpiner();
        }
    }
    
    private void showPasswordDialog()
    {
        Stage dialogStage = new Stage();
        dialogStage.setResizable(false);
        try
        {
            ResourceBundle bundles = ResourceBundle.getBundle("com.wingain.bundles.string");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Password.fxml") ,bundles);
            
           // loader.setRoot(dialogStage);
            Parent root = loader.load();//FXMLLoader.load(getClass().getResource("../view/Password.fxml"));
            PasswordAuthDialog pController = (PasswordAuthDialog)loader.getController();
            
            pController.setCallback(new DialogCallback()
            {
                @Override
                public boolean onOK(String txt)
                {
                    return authenticate(txt);
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
    
    public static void updateAuthen(String text)
    {
        String hashP = PasswordHash.passwordHash(text); 
        LabelDB.instance().setPassword(hashP);
        
    }
    
    public static boolean checkAuthen(String passwd)
    {
        String hashP = PasswordHash.passwordHash(passwd);     
        String pass = LabelDB.instance().getPassword("admin");
        if(hashP.equals(pass))
        {
            return true;
        }       
        return false;
    }
    public boolean authenticate(String password)
    {  
        if(checkAuthen(password))
        {
            authenticated = true;
            toggleCopiesSpiner(); 
        }       
        return authenticated;
    }
    
    private void showAlertDialog(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.initOwner(root.getScene().getWindow());
        alert.show();
    }
    
    private void labelScanFocus()
    {
        labelContent.clear();
        labelContent.requestFocus();
    }
    
    @FXML public void onLabelScanDone(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            BufferedImage image = labelTranslate( labelContent.getText());   
            if(image != null)
            {
                LabelPrinter.printLabelB(labelContent.getText(),productShortName);
                System.out.println(labelContent.getText());
            }
            labelScanFocus();
            
        }
    }
    
   
    private boolean labelCheck(String l)
    {
        if(l.length() > 20)
            return true;
        else
            return false;
    }
    private BufferedImage labelTranslate(String character)
    {
        if(character.startsWith("[)"))
        {
            String result = character;
            if(!labelCheck(character))
            {
                showAlertDialog("error", "not Label format");
                return null;
            }
            if(!LabelDB.instance().insertLabel(orderNumber.getText(), result))
            {
                showAlertDialog("Warnning", "The Label already be scanned");
                return null;
                
            }
            
            if(shortName.getText() != null && shortName.getText().length()>0)
                productShortName = shortName.getText();
            
            return renderLabel(result, productShortName, 350, 150);
        }
        
        return null;
    }
    
    private BufferedImage renderLabel(String l, String pShortName, int w, int h)
    {
        BufferedImage img = LabelBGenerator.labelB(l, pShortName , w, h);
        WritableImage image = new WritableImage(img.getWidth(),img.getHeight());
        javafx.embed.swing.SwingFXUtils.toFXImage(img, image);
        barImage.setImage(image);
        return img;
    }

    private void initResultTable()
    {
        ObservableList<TableColumn<LabelItem,?>> tableColumns = resultTable.getColumns();
        tableColumns.get(0).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemIndex));
        tableColumns.get(1).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemOrderNum));
        tableColumns.get(2).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemProductCode));
        tableColumns.get(3).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemLabelContent));
        tableColumns.get(4).setCellValueFactory(new PropertyValueFactory<>(LabelItem.LabelItemTime));
        
        resultTable.setItems(searchResult);
        resultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedItem = null;
        resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LabelItem>()
        {

            @Override
            public void changed(ObservableValue<? extends LabelItem> observable, LabelItem oldValue, LabelItem newValue)
            {
                //int index = resultTable.getSelectionModel().getSelectedIndex();
                if(newValue != null)
                {
                    renderLabel(newValue.getLabelContent(), productShortName, 350, 150);
                    selectedItem = newValue;
                }
            }
        });
        
    }
    
    /*****************************************************************************************************************
     *
     * Lable Message Handler 
     * 
     ****************************************************************************************************************/
    
    @FXML public void onLabelSearch(ActionEvent event) {
        
        searchResult.clear();
        List<com.wingain.model.Label> result = search();
        
        if(result == null)
            return;
        selectedItem = null;
        for(com.wingain.model.Label l:result)
        {
            searchResult.add(new LabelItem(l.index, l.orderNum, l.productCode, l.labelContent, l.time));
        }

    }
    private List<com.wingain.model.Label> search()
    {
        List<com.wingain.model.Label> result = null;
        if(orderSearch.getText() != null && orderSearch.getText().length() > 0)
        {
            result = LabelDB.instance().findLabel(LabelDB.table_label_order,orderSearch.getText());
        }else if (productCodeSearch.getText()!= null && productCodeSearch.getText().length() > 0)
        {
            result = LabelDB.instance().findLabel(LabelDB.table_label_product,productCodeSearch.getText());
        }else 
        {
            result = getTimeSearchResult();
        }
        return result;
    }
    
    @FXML public void onLabelExport(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file!=null)
        {
            List<com.wingain.model.Label> result = search();
            DataExporter.exportLabels(result, file);
        }
        showAlertDialog("Success", "File save done");
        
    }
    
    /**
     * Handle Enter event for OrderName code search
     * @param event
     */
    @FXML public void onOrderSearchDone(KeyEvent event) {
        if(event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            onLabelSearch(null);
        }
    }
    /**
     * Handle Enter event for Product code search
     * @param event
     */
    @FXML public void onProductSearchDone(KeyEvent event) {
        onOrderSearchDone(event);
    }
    
    /**
     * Get the Labels by time 
     * @return
     */
    private List<com.wingain.model.Label> getTimeSearchResult()
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fTime = new LocalDateStringConverter(format, null).fromString("2000-01-01");
        LocalDate tTime = new LocalDateStringConverter(format, null).fromString("2999-01-01");
        
        if(fromTime.getValue() == null && toTime.getValue() == null)
            return null;
        
        
        if(fromTime.getValue() != null)
        {
            fTime = fromTime.getValue();
            //fTime.format(format);
        }
        if(toTime.getValue() != null)
        {
            tTime = toTime.getValue();
        }
        
        if(fTime.compareTo(tTime) > 0)
        {
            LocalDate tmp = fTime;
            fTime = tTime;
            tTime = tmp;
        }
       return LabelDB.instance().findLabelByTime(fTime.toString(), tTime.toString());
               
    }

    @FXML public void onLabelPrinter() {
        if(selectedItem != null)
        {
            LabelPrinter.openPrinter();
            LabelPrinter.printLabelB(selectedItem.getLabelContent(),productShortName );
            System.out.println(selectedItem.getLabelContent());
            LabelPrinter.closePrinter();
        }
    }



}
