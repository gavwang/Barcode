package com.wingain.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DataExporter
{
    private final static String  LABEL_INDEX="No.";
    private final static String  LABEL_ORDER_NUM="Order No.";
    private final static String  LABEL_PRODUCT_CODE="Product Code";
    private final static String  LABEL_LABEL_CONTENT="Label Info";
    private final static String  LABEL_SERIES="Series";
    private final static String  LABEL_TIME="Time";
    public static File exportLabels(List<Label> labels , String path)
    {
        return exportLabels(labels, new File(path));
    }
    public static File exportLabels(List<Label> labels , File file)
    {
        if(labels == null || file == null)
            return null;
        try
        {
            FileOutputStream output = new FileOutputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("labels");
            int rownum = initLabelSheetHeader(sheet);
            for(Label l :labels)
            {
                rownum = insertlabel(l, sheet, rownum);
            }
            workbook.write(output);
            workbook.close();
           
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch ( IOException e)
        {
            
        }
  
        return null;
    }
    
    /**
     * 
     * @param sheet
     * @return row num
     */
    private static int initLabelSheetHeader(XSSFSheet sheet)
    {
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int clonum = 0;
        Cell cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_INDEX);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_ORDER_NUM);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_PRODUCT_CODE);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_LABEL_CONTENT);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_TIME);
        return rownum;
    }
    
    private static int insertlabel(Label l,XSSFSheet sheet, int rownum)
    {
        Row row = sheet.createRow(rownum++);
        int clonum = 0;
        Cell cell = row.createCell(clonum++);
        cell.setCellValue(l.index);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.orderNum);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.productCode);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.labelContent);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.time);
        return rownum;
    }
    
    
    public static File exportShipMent(List<ProductShipment> shipments , File file)
    {
        if(shipments == null || file == null)
            return null;
        try
        {
            FileOutputStream output = new FileOutputStream(file);
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("shipment");
            int rownum = initShipmentSheetHeader(sheet);
            for(ProductShipment l :shipments)
            {
                rownum = insertShipment(l, sheet, rownum);
            }
            workbook.write(output);
            workbook.close();
           
        } catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch ( IOException e)
        {
            
        }
  
        return null;
    }
    private static int insertShipment(ProductShipment l,XSSFSheet sheet, int rownum)
    {
        Row row = sheet.createRow(rownum++);
        int clonum = 0;
        Cell cell = row.createCell(clonum++);
        cell.setCellValue(l.getIndex());
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.getOrderNo());
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.getProductCode());
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.getSeriesNo());
        
        cell = row.createCell(clonum++);
        cell.setCellValue(l.getTime());
        return rownum;
    }
    private static int initShipmentSheetHeader(XSSFSheet sheet)
    {
        int rownum = 0;
        Row row = sheet.createRow(rownum++);
        int clonum = 0;
        Cell cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_INDEX);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_ORDER_NUM);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_PRODUCT_CODE);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_SERIES);
        
        cell = row.createCell(clonum++);
        cell.setCellValue(LABEL_TIME);
        return rownum;
    }
    
    
    private static boolean printShipment(List<ProductShipment> p)
    {
        return true;
    }
	public static void exportShipments(List<ProductShipment> result, File file) {
		// TODO Auto-generated method stub
		
	}
}
