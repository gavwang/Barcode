package com.wingain.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LabelItem
{
   public final static String LabelItemIndex = "index";
   public final static String LabelItemOrderNum = "orderNo";
   public final static String LabelItemProductCode = "productCode";
   public final static String LabelItemLabelContent = "labelContent";
   public final static String LabelItemTime = "time";
   private IntegerProperty index;
   private StringProperty orderCode;
   private StringProperty productCode;
   private StringProperty labelContent;
   private StringProperty time;
   
   public LabelItem()
   {
       
   }
   public LabelItem(int i, String order, String product, String label, String time)
   {
       this.setIndex(i);
       this.setOrderNo(order);
       this.setProductCode(product);
       this.setLabelContent(label);
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
      
   public void setLabelContent(String label)
   {
       labelContentProperty().set(label);
   }
   public String getLabelContent()
   {
       return labelContentProperty().get();
   }
   
   private StringProperty labelContentProperty()
   {
       if(labelContent == null)  labelContent = new SimpleStringProperty(this,"labelContent");
       return labelContent;
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
