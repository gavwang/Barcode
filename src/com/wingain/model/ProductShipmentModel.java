package com.wingain.model;

public class ProductShipmentModel
{
    private String orderNo;
    private String productCode;
    private String seriesNo;
    private ProductShipmentDB db;
    public ProductShipmentModel()
    {
        db = new ProductShipmentDB();
    }
    
    public ProductShipmentDB db(){
        return db;
    }
    

}
