package com.wingain.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class LabelDB
{
    public static final String table_label_name = "label_records";
    public static final String table_label_No = "No";
    public static final String table_label_order = "orderNo";
    public static final String table_label_product = "productNo";
    public static final String table_label_series = "seriesNo";
    public static final String table_label_content =  "labelContent";
    public static final String table_label_time =  "time";
    
    
    public static final String table_account = "account";
    public static final String table_account_name = "name";
    public static final String table_account_password = "password";
    
    
    
    public static LabelDB inst = null;
   
    public static LabelDB instance(){
        if(inst == null)
        {
            inst = new LabelDB();
            DBManager.getConnection();
        }
        return inst;
            
    }

     
    public List<Label> findLabel(String label)
    {
        return findLabel(table_label_content, label);
    }
    public List<Label> findLabelByTime(String fTime, String toTime)
    {
        ArrayList<Label> ret = new ArrayList<>();
        try
        {
            String sql = "SELECT * FROM %s where %s.%s BETWEEN '%s' AND '%s' ";
            sql = String.format(sql, table_label_name, table_label_name, table_label_time,fTime , toTime);
            Statement statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                Label tmp = new Label();
                tmp.index = rs.getInt(table_label_No);
                tmp.orderNum = rs.getString(table_label_order);
                tmp.productCode = rs.getString(table_label_product);
                tmp.seriesNo = rs.getString(table_label_series);
                tmp.labelContent = rs.getString(table_label_content);
                tmp.time = rs.getString(table_label_time);//.toString();
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
    public List<Label> findLabel(String tag, String value)
    {
        ArrayList<Label> ret = new ArrayList<>();
        try
        {
            String sql = "SELECT * FROM %s where %s.%s LIKE '%%%s%%'";
            sql = String.format(sql, table_label_name, table_label_name, tag, value);
            Statement statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                Label tmp = new Label();
                tmp.index = rs.getInt(table_label_No);
                tmp.orderNum = rs.getString(table_label_order);
                tmp.productCode = rs.getString(table_label_product);
                tmp.seriesNo = rs.getString(table_label_series);
                tmp.labelContent = rs.getString(table_label_content);
                tmp.time = rs.getString(table_label_time);//.toString();
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
    
    public List<Label> findAll()
    {
        ArrayList<Label> result = new ArrayList<>();
        
        return result;
    }
    
    private void execute(String sql)
    {
        Statement statement;
        try
        {
            statement = DBManager.getConnection().createStatement();
            statement.executeUpdate(sql);

            statement.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public boolean insertLabel(String orderNo, String content)
    {
        if(!findLabel(content).isEmpty())
            return false;
        
        ArrayList<String> result = LabelBGenerator.parseLabelA(content);
        if(result.size() != 3)
            return false;
        
        return insertLabel(orderNo, result.get(0), result.get(1), content);
               
    }
    public boolean insertLabel(String orderNo, String product ,String series, String content)
    {
        
        String nullStr = "NULL";
        String sql = "INSERT OR REPLACE INTO %s VALUES (%s, '%s', '%s', '%s', '%s', %s)";
        sql = String.format(sql, table_label_name, nullStr, orderNo, product,series,content,nullStr);
        execute(sql);
        return true;
    }


    public String getPassword(String name)
    {
        name = "admin";
        String passwd = null;

        try
        {
            String sql = "SELECT %s FROM %s WHERE %s = '%s'";
            sql = String.format(sql, table_account_password, table_account, table_account_name, name);
            Statement statement;
            statement = DBManager.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next())
            {
                passwd = rs.getString(table_account_password);
                rs.close();
                return passwd;
            }
            
        } catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }


    public void setPassword(String hashP)
    {
            String sql = "SELECT %s FROM %s WHERE %s = '%s'";
            sql = "UPDATE %s SET %s = '%s' where %s = '%s'";
            sql = String.format(sql, table_account, table_account_password,hashP ,table_account_name, "admin");
            execute(sql);
    }


    public void deleteLabel(int index)
    {
        String sql = "DELETE from %s where %s.%s = %d";
        sql = String.format(sql, table_label_name, table_label_name,table_label_No ,index);
        execute(sql);
        
    }
  
//    CREATE TABLE `label_records` (
//            `No`    INTEGER PRIMARY KEY AUTOINCREMENT,
//            `orderNo`   TEXT NOT NULL,
//            `productNo` TEXT NOT NULL,
//            `seriesNo`  INTEGER NOT NULL,
//            `labelContent`  TEXT NOT NULL UNIQUE,
//            `time`  TIMESTAMP NOT NULL DEFAULT (DATETIME('now','localtime'))
//        );

    
    //INSERT OR REPLACE INTO LABELS VALUES (1, 'ORDERNMAE', 'PRODUCTCODE', 'SERIESNO', 'LABELCONTENT', select datetime('now'));
}
