package com.wingain.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductShipmentDB
{
    private final static String TABLE_ORDERS="orders";
    private final static String TABLE_PRODUCT_CODES="product_codes";
    private final static String TABLE_SERIES="series";
    private final static String TABLE_SHIPMENT="shipment";
    
    private final static String TABLE_ORDERS_ID="id";
    private final static String TABLE_ORDERS_ORDER="order_no";
    
    private final static String TABLE_PRODUCT_CODES_ID="id";
    private final static String TABLE_PRODUCT_CODES_CODE="product_code";
    
    private final static String TABLE_SHIPMENT_ID="id";
    private final static String TABLE_SHIPMENT_ORDER="order_id";
    private final static String TABLE_SHIPMENT_PCODE="product_id";
    private final static String TABLE_SHIPMENT_SCODE="series_no";
    private final static String TABLE_SHIPMENT_TIME = "time";
    
    public ProductShipmentDB()
    {
        DBManager.getConnection();
    }
    
    
    public boolean addOrderNo(String order)
    {
        String sql = String.format("INSERT INTO %s VALUES (NULL, '%s')", TABLE_ORDERS, order);
        if(execute(sql) == 1)
            return true;
        else
            return false;
    }
    
    public int orderID(String order)
    {
        String sql = String.format("SELECT %s.%s FROM %s WHERE %s.%s = '%s'", 
                TABLE_ORDERS ,TABLE_ORDERS_ID,TABLE_ORDERS,TABLE_ORDERS,TABLE_ORDERS_ORDER,order);
        int id;
        Statement statement;
        try
        {
            statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                id = rs.getInt(TABLE_ORDERS_ID);
                return id;
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return -1;
        
    }
    
    public int productCodeID(String pCode)
    {
        String sql = String.format("SELECT %s.%s FROM %s WHERE %s.%s = '%s'", 
                TABLE_PRODUCT_CODES ,TABLE_PRODUCT_CODES_ID,TABLE_PRODUCT_CODES,TABLE_PRODUCT_CODES,TABLE_PRODUCT_CODES_CODE,pCode);
        int id;
        Statement statement;
        try
        {
            statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                id = rs.getInt(TABLE_PRODUCT_CODES_ID);
                return id;
            }
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return -1;
        
    }
    
    public boolean addProductCode(String code)
    {
        String sql = String.format("INSERT INTO %s VALUES (NULL, '%s')", TABLE_PRODUCT_CODES, code);
        if(execute(sql) == 1)
            return true;
        else
            return false;
    }
    
    public ProductShipment addShipment(String order, String product, String series)
    {
        if(order == null || product == null || series == null)
            return null;
        addOrderNo(order);
        addProductCode(product);
        int orderID = orderID(order);
        int productCodeID = productCodeID(product);
        if(orderID == -1 || productCodeID == -1)
            return null;
        
        String sql = String.format("INSERT INTO %s (%s,%s,%s) VALUES(%d , %d, '%s')", 
                TABLE_SHIPMENT,
                TABLE_SHIPMENT_ORDER,TABLE_SHIPMENT_PCODE,TABLE_SHIPMENT_SCODE,
                orderID, productCodeID,series);
        
        int r = execute(sql);
        if(r != 1)
        {
            return null;
        }else
        {
            //SELECT shipment.id FROM shipment WHERE shipment.order_id = 11 AND shipment.product_id = 7 AND  shipment.series_no = 'sdfgjhk5re'
            sql = String.format("SELECT %s.%s, %s.%s FROM %s WHERE %s.%s = %d AND %s.%s = %d AND  %s.%s = '%s'",
                    TABLE_SHIPMENT,TABLE_SHIPMENT_ID,TABLE_SHIPMENT,TABLE_SHIPMENT_TIME, TABLE_SHIPMENT, TABLE_SHIPMENT, TABLE_SHIPMENT_ORDER, orderID, TABLE_SHIPMENT,TABLE_SHIPMENT_PCODE, productCodeID,
                    TABLE_SHIPMENT, TABLE_SHIPMENT_SCODE, series);
            
            try
            {
                Statement statement = DBManager.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(sql);

                while (rs.next())
                {
                    ProductShipment tmp = new ProductShipment();
                    tmp.setIndex(rs.getInt(TABLE_SHIPMENT_ID));
                    tmp.setOrderNo(order);
                    tmp.setProductCode(product);
                    tmp.setSeriesNo(series);
                    tmp.setTime(rs.getString(TABLE_SHIPMENT_TIME));
                    
                    rs.close();
                    return tmp;
                                   
                }
            } catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            return null;            
        }
    }
      
    public ProductShipment searchShipment(String order, String product, String series)
    {
        return null;
        
    }
    private static int execute(String sql)
    {
        Statement statement;
        int result = -1;
        try
        {
            statement = DBManager.getConnection().createStatement();
            result = statement.executeUpdate(sql);

            statement.close();
        }
        catch (SQLException e)
        {
            //e.printStackTrace();
        }
        
        return result;
    }
    
    public static void test()
    {
        String sql = "insert into orders values (NULL, 'tes2t')";
        
        int r = execute(sql);
        
        System.out.println(r);
                
    }

    public List<ProductShipment> findShipmentbyOrder(String text)
    {
        return findShipment("order", text);
    }
    public List<ProductShipment> findShipmentbyProductCode(String text)
    {
        return findShipment("productCode", text);
    }
    
    public List<ProductShipment> findShipmentbySeriesNo(String text)
    {
        return findShipment("series", text);
    }
    
    private List<ProductShipment> findShipment(String type, String text)
    {
        /*
            SELECT shipment.id, orders.order_no, product_codes.product_code, shipment.series_no, shipment.time 
            FROM shipment INNER JOIN orders, product_codes
            ON shipment.order_id = orders.id AND shipment.product_id = product_codes.id
            WHERE orders.order_no LIKE '%sf%'
         */
        ArrayList<ProductShipment> ret = new ArrayList<>();
        try
        {
            String table = TABLE_ORDERS;
            String pro = TABLE_ORDERS_ORDER;
            if(type.equals("order"))
            {
                table = TABLE_ORDERS;
                pro = TABLE_ORDERS_ORDER;
            }else if(type.equals("productCode"))
            {
                table = TABLE_PRODUCT_CODES;
                pro = TABLE_PRODUCT_CODES_CODE;
            }else
            {
            	table = TABLE_SHIPMENT;
                pro = TABLE_SHIPMENT_SCODE;
            }
            
            String sql = "SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s "
                    + "FROM %s INNER JOIN %s, %s "
                    + "ON %s.%s = %s.%s AND %s.%s = %s.%s "
                    + "WHERE %s.%s LIKE '%%%s%%'";
            sql = String.format(sql, TABLE_SHIPMENT,TABLE_SHIPMENT_ID, 
                    TABLE_ORDERS,TABLE_ORDERS_ORDER, 
                    TABLE_PRODUCT_CODES, TABLE_PRODUCT_CODES_CODE,
                    TABLE_SHIPMENT,TABLE_SHIPMENT_SCODE, 
                    TABLE_SHIPMENT, TABLE_SHIPMENT_TIME,
                    TABLE_SHIPMENT, TABLE_ORDERS, TABLE_PRODUCT_CODES,
                    TABLE_SHIPMENT, TABLE_SHIPMENT_ORDER, TABLE_ORDERS, TABLE_ORDERS_ID,
                    TABLE_SHIPMENT, TABLE_SHIPMENT_PCODE, TABLE_PRODUCT_CODES, TABLE_PRODUCT_CODES_ID,
                    table,pro, text);
            Statement statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                ProductShipment tmp = new ProductShipment();
                tmp.setIndex(rs.getInt(TABLE_SHIPMENT_ID));
                tmp.setOrderNo(rs.getString(TABLE_ORDERS_ORDER));
                tmp.setProductCode(rs.getString(TABLE_PRODUCT_CODES_CODE));
                tmp.setSeriesNo(rs.getString(TABLE_SHIPMENT_SCODE));
                tmp.setTime(rs.getString(TABLE_SHIPMENT_TIME));
                ret.add(tmp);                
            }

            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }


    public List<ProductShipment> findLabelByTime(String fTime, String tTime)
    {
        ArrayList<ProductShipment> ret = new ArrayList<>();
        try
        {   
            String sql = "SELECT %s.%s, %s.%s, %s.%s, %s.%s, %s.%s "
                    + "FROM %s INNER JOIN %s, %s "
                    + "ON %s.%s = %s.%s AND %s.%s = %s.%s "
                    + "where %s.%s BETWEEN '%s' AND '%s' ";
            sql = String.format(sql, TABLE_SHIPMENT,TABLE_SHIPMENT_ID, 
                    TABLE_ORDERS,TABLE_ORDERS_ORDER, 
                    TABLE_PRODUCT_CODES, TABLE_PRODUCT_CODES_CODE,
                    TABLE_SHIPMENT,TABLE_SHIPMENT_SCODE, 
                    TABLE_SHIPMENT, TABLE_SHIPMENT_TIME,
                    TABLE_SHIPMENT, TABLE_ORDERS, TABLE_PRODUCT_CODES,
                    TABLE_SHIPMENT, TABLE_SHIPMENT_ORDER, TABLE_ORDERS, TABLE_ORDERS_ID,
                    TABLE_SHIPMENT, TABLE_SHIPMENT_PCODE, TABLE_PRODUCT_CODES, TABLE_PRODUCT_CODES_ID,
                    TABLE_SHIPMENT,TABLE_SHIPMENT_TIME, fTime, tTime);
            Statement statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                ProductShipment tmp = new ProductShipment();
                tmp.setIndex(rs.getInt(TABLE_SHIPMENT_ID));
                tmp.setOrderNo(rs.getString(TABLE_ORDERS_ORDER));
                tmp.setProductCode(rs.getString(TABLE_PRODUCT_CODES_CODE));
                tmp.setSeriesNo(rs.getString(TABLE_SHIPMENT_SCODE));
                tmp.setTime(rs.getString(TABLE_SHIPMENT_TIME));
                ret.add(tmp);                
            }

            rs.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }


    public void deleteShipment(int index)
    {
        String sql = String.format("DELETE from %s where %s.%s = %d",
                TABLE_SHIPMENT, TABLE_SHIPMENT, TABLE_SHIPMENT_ID, index);
        execute(sql);
    }

}
