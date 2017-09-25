package com.wingain.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.wingain.model.ProductShipment;
import com.wingain.model.ProductShipmentModel;
import com.wingain.model.ShipmentPrinter;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        resourceBundle = resources;
        model = new ProductShipmentModel();
        initResultTable();

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
        resultTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resultTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProductShipment>()
        {

            @Override
            public void changed(ObservableValue<? extends ProductShipment> observable, ProductShipment oldValue, ProductShipment newValue)
            {
                //int index = resultTable.getSelectionModel().getSelectedIndex();
                if(newValue != null)
                {
                    //renderLabel(newValue.getLabelContent(), productShortName, 350, 150);
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
                    orderNo.setText("");
                    showAlertDialog("Warnning", "Order is already scaned!");
                }else
                {
                    orderNo.setDisable(true);
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
                if(!model.db().addProductCode(productCode.getText()))
                {
                    productCode.setText("");
                    showAlertDialog("Warnning", "ProductCode is already scaned!");
                }else
                {
                    productCode.setDisable(true);
                    seriesNo.requestFocus();
                }
            }
            
        }
    }

    @FXML
    public void onSeriesNoDone(KeyEvent event)
    {
        if (event.getEventType() == KeyEvent.KEY_PRESSED && event.getCode() == KeyCode.ENTER)
        {
            if(checkTextField(seriesNo) && checkTextField(productCode) && checkTextField(orderNo))
            {
                if(!model.db().addShipment(orderNo.getText(), productCode.getText(), seriesNo.getText()))
                {
                    
                    showAlertDialog("Warnning", "Serirse is already scaned!");
                }

                seriesNo.setText("");
                seriesNo.requestFocus();
                
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
        
        new Thread(new Runnable()
        {
            
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
               new ShipmentPrinter(result).printData(); 
               //TableDemo.test(null);
            }
        }).start();
       
    }

    @FXML
    public void onScanDone(ActionEvent event)
    {
        seriesNo.setText("");
        productCode.setText("");
        orderNo.setText("");
        productCode.setDisable(false);
        orderNo.setDisable(false);
        orderNo.requestFocus();
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
       return model.db().findLabelByTime(fTime.toString(), tTime.toString());
               
    }

}
