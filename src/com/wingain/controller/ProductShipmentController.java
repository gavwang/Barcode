package com.wingain.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.wingain.model.DataExporter;
import com.wingain.model.LabelPrinter;
import com.wingain.model.ProductShipment;
import com.wingain.model.ProductShipmentModel;
import com.wingain.model.ShipmentPrinter;
import com.wingain.view.NumberTextField;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.converter.LocalDateStringConverter;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

public class ProductShipmentController implements Initializable
{
    private ResourceBundle resourceBundle;
    private ProductShipmentModel model;

    private final ObservableList<ProductShipment> searchResult = FXCollections.observableArrayList();
    @FXML
    AnchorPane root;
    @FXML
    TextField orderNo;
    @FXML
    TextField productCode;
    @FXML
    TextField seriesNo;
    @FXML
    TableView<ProductShipment> resultTable;
    @FXML
    DatePicker fromTime;
    @FXML
    DatePicker toTime;
    @FXML TextField series_no_txt;
   
    @FXML
    NumberTextField initNumTxt;
    @FXML
    NumberTextField currentNumTxt;
    
    private ProductShipment selectedItem ;
    private int scan_num = 1;
    private int current_num = 0;
    private boolean start_scan = false; 
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        resourceBundle = resources;
        model = new ProductShipmentModel();
        initResultTable();
        
        series_no_txt.setEditable(false);
        initNumTxt.setDisable(false);
        currentNumTxt.setDisable(true);
        initNumTxt.setText("1");
        
    }
    
    private void initResultTable()
    {
        ObservableList<TableColumn<ProductShipment,?>> tableColumns = resultTable.getColumns();
        tableColumns.get(0).setCellValueFactory(new PropertyValueFactory<>(ProductShipment.SHIPMENT_ID));
        tableColumns.get(1).setCellValueFactory(new PropertyValueFactory<>(ProductShipment.SHIPMENT_OrderNum));
        tableColumns.get(2).setCellValueFactory(new PropertyValueFactory<>(ProductShipment.SHIPMENT_ProductCode));
        tableColumns.get(3).setCellValueFactory(new PropertyValueFactory<>(ProductShipment.SHIPMENT_SeriesNo));
        tableColumns.get(4).setCellValueFactory(new PropertyValueFactory<>(ProductShipment.SHIPMENT_Time));
        
        resultTable.setItems(searchResult);
        selectedItem = null;
        resultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProductShipment>()
        {

            @Override
            public void changed(ObservableValue<? extends ProductShipment> observable, ProductShipment oldValue, ProductShipment newValue)
            {
                if(newValue != null)
                {
                    selectedItem = newValue;
                }
            }
        });
        
    }
    

    @FXML
    public void onOrderNoDone(KeyEvent event)
    {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            if(checkTextField(orderNo))
            {
                if(!model.db().addOrderNo(orderNo.getText()))
                {
                    //orderNo.setText("");
                    //showAlertDialog("Warnning", "Order is already scaned!");
                	System.out.println("Order is already scaned");
                }else
                {
                    //orderNo.setDisable(true);
                    productCode.requestFocus();
                }
                   
            }
            
        }
    }

    @FXML
    public void onProductCodeDone(KeyEvent event)
    {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            if(checkTextField(productCode))
            {
                /*if(!model.db().addProductCode(productCode.getText()))
                {
                    productCode.setText("");
                    showRepeatAlertDialog("Warnning", "ProductCode is already scaned!" , "ProductCode: " + productCode.getText());
                }else
                {
                    //productCode.setDisable(true);
                    searchResult.clear();
                    
                    seriesNo.requestFocus();
                }*/
                model.db().addProductCode(productCode.getText());
                searchResult.clear();
                seriesNo.requestFocus();
            }
            
        }
    }

    @FXML
    public void onSeriesNoDone(KeyEvent event)
    {
    	
        if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
        	if(!start_scan)
        	{
        		showAlertDialog("Warning", "please set scan num and start");
        		seriesNo.setText("");
        		return;
        	}
        	
            series_no_txt.setText(seriesNo.getText());
            if(checkTextField(seriesNo) && checkTextField(productCode) && checkTextField(orderNo))
            {
                ProductShipment p = model.db().addShipment(orderNo.getText(), productCode.getText(), seriesNo.getText());
                if(p == null)
                {             
                    showRepeatAlertDialog("Warnning", "Serirse: is already scaned!" , "SeriesNo: " + seriesNo.getText());
                }else
                {
                    current_num ++;
                    currentNumTxt.setText(String.valueOf(current_num));
                    
                    searchResult.add(p);
                }
                
                if(scan_num == current_num)
                {
                	onScanDone(null);
                	showAlertDialog("Warning", "Scaned num " + current_num);
                }
                
                seriesNo.setText("");
                seriesNo.requestFocus();
                
            }else
            {
            	showAlertDialog("Warnning", "error");
            }
            
        }
    }

    @FXML
    public void onSearch(ActionEvent event)
    {
        searchResult.clear();
        List<ProductShipment> result = search();
        
        if(result == null)
            return;
     
        searchResult.addAll(result);

    }

    @FXML
    public void onOrderExport(ActionEvent event)
    {
        searchResult.clear();
        List<ProductShipment> result = search();
        
        if(result == null)
            return;
     
        searchResult.addAll(result);
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(root.getScene().getWindow());
        if(file!=null)
        {
            DataExporter.exportShipMent(result, file);
        }
        showAlertDialog("Success", "File save done");
       
    }
    @FXML
    public void onOrderPrint(ActionEvent event)
    {
        searchResult.clear();
        List<ProductShipment> result = search();
        
        if(result == null)
            return;
     
        searchResult.addAll(result);
        
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
               new ShipmentPrinter(result).printData(); 
            }
        }).start();
       
    }
    @FXML
    public void onScanDone(ActionEvent event)
    {
     	String num = currentNumTxt.getText();
    	
    	if(scan_num != Integer.valueOf(num))
    	{
    		showAlertDialog("Error", "Setting num is " + scan_num +" currently scaned is " +num );
    		return;
    	}
    	start_scan = false;
    	initNumTxt.setDisable(false);
    	    	
        seriesNo.setText("");
        productCode.setText("");
        orderNo.setText("");
        productCode.setDisable(false);
        orderNo.setDisable(false);
        orderNo.requestFocus();
        searchResult.clear();
        //seriesNo.setDisable(true);
    }
    

    
    @FXML
    public void onScanStart(ActionEvent event)
    {
                
        String num = initNumTxt.getText();
        if(num.length() == 0)
        {
        	showAlertDialog("Error", "Please set scan number!");
        	return;
        }
        
        start_scan = true;
        seriesNo.setDisable(false);
        scan_num = Integer.valueOf(num);
        initNumTxt.setDisable(true);
        
        current_num = 0;
        currentNumTxt.setText(String.valueOf(current_num));
        
    }
    private boolean checkTextField(TextField t)
    {
        return (t.getText() != null && t.getText().length() > 0);
    }
    
    private void showAlertDialog(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        alert.setTitle(title);
        alert.setHeaderText(content);
        alert.initOwner(root.getScene().getWindow());
        alert.show();
    }
    private void showRepeatAlertDialog(String title, String header, String content )
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        LabelPrinter.playRepeatWaring();
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initOwner(root.getScene().getWindow());
        alert.show();
    }
    
    
    private List<ProductShipment> search()
    {
        List<ProductShipment> result = null;
        if(checkTextField(orderNo))
        {
            result = model.db().findShipmentbyOrder(orderNo.getText());
        }else if (checkTextField(productCode))
        {
            result = model.db().findShipmentbyProductCode(productCode.getText());
        }else 
        {
            result = getTimeSearchResult();
        }
        return result;
    }
    
    /**
     * Get the Labels by time 
     * @return
     */
    private List<ProductShipment> getTimeSearchResult()
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fTime = new LocalDateStringConverter(format, null).fromString("2000-01-01");
        LocalDate tTime = new LocalDateStringConverter(format, null).fromString("2999-01-01");
        
        //if(fromTime.getValue() == null && toTime.getValue() == null)
          //  return null;
        
        
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
        LocalDateTime newF = LocalDateTime.of(fTime, LocalTime.of(0,0,0));
        LocalDateTime newT = LocalDateTime.of(tTime, LocalTime.of(23,59,59));
       return model.db().findLabelByTime(newF.toString().replace('T', ' '), newT.toString().replace('T', ' '));
               
    }

    @FXML public void onDelete() {
        if(selectedItem != null)
        {
            model.db().deleteShipment(selectedItem.getIndex());
            ProductShipment tmp = selectedItem;
            selectedItem = null;
            searchResult.remove(tmp);
        }
    }

}
