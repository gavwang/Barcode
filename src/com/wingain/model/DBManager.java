package com.wingain.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager
{
    private Connection connection = null;
    private static DBManager instance = null;
    public DBManager()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection()
    {
        if(instance == null)
        {
            instance = new DBManager();
            instance.openDB("label.db");
            if(instance.connection == null)
            {
                System.err.println("can't open db file");
                assert(false);
            }
        }
        
        return instance.connection;
    }

    private void openDB(String uri)
    {
        String db = "jdbc:sqlite:" + uri;
        try
        {
            Properties pro = new Properties();
            pro.put("date_string_format", "yyyy-MM-dd HH:mm:ss");
            connection = DriverManager.getConnection(db,pro);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void Close()
    {
        try
        {
            connection.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
