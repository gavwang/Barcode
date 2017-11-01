package com.wingain.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductShipment
{
    public final static String SHIPMENT_ID = "index";
    public final static String SHIPMENT_NO = "no";
    public final static String SHIPMENT_OrderNum = "orderNo";
    public final static String SHIPMENT_ProductCode = "productCode";
    public final static String SHIPMENT_SeriesNo = "seriesNo";
    public final static String SHIPMENT_Time = "time";
    private IntegerProperty index;
    private StringProperty orderCode;
    private StringProperty productCode;
    private StringProperty seriesNo;
    private StringProperty time;   
    private IntegerProperty no;
    public ProductShipment()
    {
        
    }
    public ProductShipment(int i, String order, String product, String label, String time)
    {
        this.setIndex(i);
        this.setOrderNo(order);
        this.setProductCode(product);
        this.setSeriesNo(label);
        this.setTime(time);
    }
    
    public void setIndex(int _index)
    {
        indexProperty().set(_index);
    }
    public int getIndex()
    {
        return indexProperty().get();
    }
    
    private IntegerProperty indexProperty()
    {
        if(index == null)  index = new SimpleIntegerProperty(this,"index", 0);
        return index;
    }
    
    public void setNo(int _no)
    {
        noProperty().set(_no);
    }
    public int getNo()
    {
        return noProperty().get();
    }
    
    private IntegerProperty noProperty()
    {
        if(no == null)  no = new SimpleIntegerProperty(this,"no", 0);
        return no;
    }
    
    public void setOrderNo(String order)
    {
        orderNoProperty().set(order);
    }
    public String getOrderNo()
    {
        return orderNoProperty().get();
    }
    
    private StringProperty orderNoProperty()
    {
        if(orderCode == null)  orderCode = new SimpleStringProperty(this,"orderCode");
        return orderCode;
    }
    
    public void setProductCode(String pcode)
    {
        productCodeProperty().set(pcode);
    }
    public String getProductCode()
    {
        return productCodeProperty().get();
    }
    
    private StringProperty productCodeProperty()
    {
        if(productCode == null)  productCode = new SimpleStringProperty(this,"productCode");
        return productCode;
    }
       
    public void setSeriesNo(String label)
    {
        seriesNoProperty().set(label);
    }
    public String getSeriesNo()
    {
        return seriesNoProperty().get();
    }
    
    private StringProperty seriesNoProperty()
    {
        if(seriesNo == null)  seriesNo = new SimpleStringProperty(this,"labelContent");
        return seriesNo;
    }
    
    public void setTime(String t)
    {
        timeProperty().set(t);
    }
    public String getTime()
    {
        return timeProperty().get();
    }
    
    private StringProperty timeProperty()
    {
        if(time == null)  time = new SimpleStringProperty(this,"time");
        return time;
    }

}
