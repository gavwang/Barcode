package com.wingain.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.omg.CORBA.INTERNAL;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;


public class LabelBGenerator
{
    public static final int Min_Label_Width = 50;
    public static final int Min_Label_Height = 25;
    public final static int LABEL_B_WIDTH = 350;
    public final static int LABEL_B_HEIGHT = 150;
    
    public final static int HORIZONTAL_GAP =350 * 25/700;
    public final static int HORIZONTAL_R_GAP = HORIZONTAL_GAP/2;
    public final static int VERTICAL_GAP = LABEL_B_HEIGHT * 1/30;
    

    public final static int NOKIA_LOGO_X = HORIZONTAL_GAP;
    public final static int NOKIA_LOGO_Y = 0;
    public final static int NOKIA_LOGO_WIDTH =350 * 14/70;
    public final static int NOKIA_LOGO_HEIGHT = LABEL_B_HEIGHT * 4/30;
    
    public final static int LEGAL_ENTITY_X = HORIZONTAL_GAP + NOKIA_LOGO_WIDTH;
    public final static int LEGAL_ENTITY_Y = 0;
    public final static int LEGAL_ENTITY_WIDTH = LABEL_B_WIDTH * 37/70;
    public final static int LEGAL_ENTITY_HEIGHT = NOKIA_LOGO_HEIGHT;
    
    public final static int PRODUCT_CODE_LABEL_X = HORIZONTAL_GAP;
    public final static int PRODUCT_CODE_LABEL_Y = NOKIA_LOGO_Y + NOKIA_LOGO_HEIGHT + VERTICAL_GAP;
    public final static int PRODUCT_CODE_LABEL_HEIGHT =LABEL_B_HEIGHT * 5/30;
    
    public final static int PRODUCT_CODE_TEXT_X = HORIZONTAL_GAP;
    public final static int PRODUCT_CODE_TEXT_Y = PRODUCT_CODE_LABEL_Y + PRODUCT_CODE_LABEL_HEIGHT;
    public final static int PRODUCT_CODE_TEXT_HEIGHT =LABEL_B_HEIGHT * 3/30;
            
    public final static int SERIES_NUM_LABEL_X = HORIZONTAL_GAP;
    public final static int SERIES_NUM_LABLE_Y = PRODUCT_CODE_TEXT_Y + PRODUCT_CODE_TEXT_HEIGHT ;
    public final static int SERIES_NUM_LABLE_HEIGHT = PRODUCT_CODE_LABEL_HEIGHT;
    
    public final static int SERIES_NUM_TEXT_X = HORIZONTAL_GAP;
    public final static int SERIES_NUM_TEXT_Y = SERIES_NUM_LABLE_Y + SERIES_NUM_LABLE_HEIGHT;
    public final static int SERIES_NUM_TEXT_HEIGHT = PRODUCT_CODE_TEXT_HEIGHT;
    
    public final static int QUANTITY_LABLE_X = HORIZONTAL_GAP;
    public final static int QUANTITY_LABLE_Y = SERIES_NUM_TEXT_Y + SERIES_NUM_TEXT_HEIGHT;
    public final static int QUANTITY_LABLE_HEIGHT = PRODUCT_CODE_LABEL_HEIGHT;
    public final static int QUANTITY_TEXT_X = HORIZONTAL_GAP;
    public final static int QUANTITY_TEXT_Y = QUANTITY_LABLE_Y + QUANTITY_LABLE_HEIGHT;
    
    
    public final static char record_seprator = 30;
    public final static char group_seprator = 29;
    public final static char transmission = 4;
    
    
    public static BufferedImage labelBGenerator(String contents, int width, int height)
    {
        BufferedImage result = null;
        try
        {
            Map<EncodeHintType,Integer> margin = new HashMap<>();
            margin.put(EncodeHintType.MARGIN, 0);
            
            BitMatrix bitMatrix = new Code128Writer().encode(contents, BarcodeFormat.CODE_128, width, height,margin);
            result = MatrixToImageWriter.toBufferedImage(bitMatrix);
            
        } catch (WriterException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
    public static ArrayList<String> parseLabelA(String content)
    {
        
        if(content.contains(String.valueOf(record_seprator)))
        {
            return labelContentParse1(content);
        }else
            return labelContentParse0(content);
    }
    public static final String PRODUCT_CODE_HEADER = "1P";
    public static final String SERIES_NO_HEADER = "S";
    public static final String QUANTITY_HEADER = "Q";
    //public static final String X6 = "1633";
    
    
    private static ArrayList<String> labelContentParse0(String content)
    {
        ArrayList<String> result = new ArrayList<>();
        int productCodePos = content.indexOf(PRODUCT_CODE_HEADER);
        int seriesNoPos = content.indexOf(SERIES_NO_HEADER);
        //int quantityPos = content.indexOf(QUANTITY_HEADER);
        int x6Pos = content.length() - 4;
        if(productCodePos > content.length() 
                || seriesNoPos > content.length()
                //|| quantityPos > content.length()
                || productCodePos == -1
                || seriesNoPos == -1
                //|| quantityPos == -1 
                )
        {
            return result;
        }
        result.add(content.substring(productCodePos, seriesNoPos));
        result.add(content.substring(seriesNoPos, seriesNoPos + 12));
        //result.add(content.substring(quantityPos, quantityPos + 2));
        result.add(content.substring(x6Pos));
        return result; 
              
    }
    private static ArrayList<String> labelContentParse1(String content)
    {
        ArrayList<String> result = new ArrayList<>();
        String[] split = content.split(String.valueOf(group_seprator));
        if(split.length == 7)
        {
            result.add(split[1]);
            result.add(split[2]);
            String x6 = split[3];
            x6 = x6.substring(3);
            result.add(x6);
        }
        
        return result;
    }
    public static BufferedImage stringToBImage(String text, String f, int size)
    {
        
        Font font = new Font(f, Font.PLAIN, size);
        FontRenderContext frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON,  RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        TextLayout layout = new TextLayout(text, font, frc);
        
        Rectangle r = layout.getPixelBounds(frc, 0, 0);
        System.out.println(r);
        BufferedImage bi = new BufferedImage(r.width + 2, r.height + 2, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.setBackground(Color.WHITE);
        g2d.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 140);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawString(text, -r.x, -r.y + 1);
        
        g2d.dispose();
        return bi;
    }

    private static void drawString(String str, Graphics2D graphics, int x , int y, String f, int size)
    {
        BufferedImage bi = stringToBImage(str, f, size);
        Image tmp = bi.getScaledInstance(bi.getWidth()*8/10, bi.getHeight(), BufferedImage.SCALE_SMOOTH);
        graphics.drawImage(tmp, x, y, null);
        
    }
    
    private static void drawString(String str, Graphics2D graphics, int y, String f, int size)
    {
        BufferedImage bi = stringToBImage(str, f, size);
        Image tmp = bi.getScaledInstance(bi.getWidth()*8/10, bi.getHeight(), BufferedImage.SCALE_SMOOTH);
        graphics.drawImage(tmp, LABEL_B_WIDTH - tmp.getWidth(null) - HORIZONTAL_R_GAP, y, null);
    }
    
     private static Image getNokiaLogo()
     {
         //Nokia Logo
         BufferedImage nokiaLogo = null;
         try
         {
             nokiaLogo = ImageIO.read(new File("NOKIA_LOGO_BLACK_HR.png"));
         } catch (IOException e1)
         {
             // TODO Auto-generated catch block
             e1.printStackTrace();
         }
         
         
         return nokiaLogo.getScaledInstance(NOKIA_LOGO_WIDTH,NOKIA_LOGO_HEIGHT, BufferedImage.SCALE_SMOOTH);
     }
    
    public static BufferedImage labelB(String content, String name,int width, int height )
    {
        
        String productCode = "1P473098A.101";
        String seriesNo = "SRK162800011";
        String quantity = "Q1";
        String x6 = "1633";
        String textF = "Arial";
        String origin = "Made In China";
        String productNickName = name;
        
        ArrayList<String> labelParse = parseLabelA(content);
        if(labelParse.size() != 3)
        {
            System.err.println("error label code");
            return null;
        }
        productCode = labelParse.get(0);
        seriesNo = labelParse.get(1);
        x6 = labelParse.get(2);
        

        
        int textSize = 13;
       // width +=20;
        
        BufferedImage canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D graphics = canvas.createGraphics();
        graphics.setColor(Color.WHITE);
        //graphics.setBackground(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);
        
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        Font font = new Font(textF,Font.PLAIN, 20);
        graphics.setFont(font);
        
 
        graphics.drawImage(getNokiaLogo(),NOKIA_LOGO_X,NOKIA_LOGO_Y, null);
        String entityName = " Nokia Solutions and Networks";
        drawString(entityName, graphics,LEGAL_ENTITY_X + 5, LEGAL_ENTITY_Y,textF, 20);
      
        
        BufferedImage productCodeLabel = labelBGenerator(productCode, Min_Label_Width,Min_Label_Height);
        graphics.drawImage(productCodeLabel, PRODUCT_CODE_LABEL_X,PRODUCT_CODE_LABEL_Y,null);        
        drawString(parseProductCode(productCode), graphics, PRODUCT_CODE_TEXT_X, PRODUCT_CODE_TEXT_Y,textF , textSize);
        
        
        BufferedImage seriesLabel = labelBGenerator(seriesNo, Min_Label_Width,Min_Label_Height);
        graphics.drawImage(seriesLabel, SERIES_NUM_LABEL_X, SERIES_NUM_LABLE_Y ,null);
        drawString(parseSeries(seriesNo), graphics, SERIES_NUM_TEXT_X, SERIES_NUM_TEXT_Y, textF, textSize);
       
        BufferedImage quantityLabel= labelBGenerator(quantity, Min_Label_Width,Min_Label_Height);
        graphics.drawImage(quantityLabel, QUANTITY_LABLE_X,QUANTITY_LABLE_Y,null);
        drawString(parseQuantity(quantity),graphics, QUANTITY_TEXT_X, QUANTITY_TEXT_Y ,textF ,textSize);
        
        drawString(productNickName, graphics, PRODUCT_CODE_LABEL_Y, textF, 20);// 305,
        drawString( origin, graphics, QUANTITY_TEXT_Y, textF, 20);//240
        
        
        String matrixContent = matrixDataString(productCode, seriesNo, quantity, x6);       
        BufferedImage matrix = matrixCode(matrixContent, 50, 50);   
        Image tmp = matrix.getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
        graphics.drawImage(tmp, LABEL_B_WIDTH - HORIZONTAL_R_GAP - tmp.getWidth(null), 60,  null);
        graphics.dispose();
        try
        {
            ImageIO.write(canvas, "png", new File("labelB.png"));
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return canvas;
    }
    
    public static String parseProductCode(String product)
    {
        if (!product.startsWith("1P"))
        {
            System.out.println("error product code");
            return null;
        }
        return "(1P)  " + product.substring(2);
        
    }
    
    public static String parseSeries(String series)
    {
        if (!series.startsWith("S"))
        {
            System.out.println("error Series No code");
            return null;
        }
        return "(S)  " + series.substring(1);
        
    }
    
    public static String parseQuantity(String quantity)
    {
        if (!quantity.startsWith("Q"))
        {
            System.out.println("error Quantity");
            return null;
        }
        return "(Q)  " + quantity.substring(1);
        
    }
    
    
    public static BufferedImage matrixCode(String content, int width, int height)
    {
        BitMatrix bitMatrix = new DataMatrixWriter().encode(content, BarcodeFormat.DATA_MATRIX, width, height);
    
        return  MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
    
    public static String matrixDataString(String productCode, String seriesNo ,String quantity, String x6)
    {
        String data_id = "06";
        String constant_str1 = "18VLENSN";
        String constant_str2 = "4LCN";
        String constant_str3 = "10D";
        
        
        String matrix = "[)>" + record_seprator 
                        + data_id + group_seprator 
                        + productCode + group_seprator 
                        + seriesNo + group_seprator 
                        + quantity + group_seprator 
                        + constant_str1 + group_seprator
                        + constant_str2 + group_seprator
                        + constant_str3 + group_seprator
                        + x6
                        + record_seprator + transmission;
        return matrix;
    }
    
    public static String testLabelAString(String productCode, String seriesNo , String x6)
    {
        String data_id = "06";
        String constant_str1 = "18VLENSN";
        String constant_str2 = "4LCN";
        String constant_str3 = "10D";
        
        
        String matrix = "[)>" + record_seprator 
                        + data_id + group_seprator 
                        + productCode + group_seprator 
                        + seriesNo + group_seprator 
                        + constant_str1 + group_seprator
                        + constant_str2 + group_seprator
                        + constant_str3 + group_seprator
                        + x6
                        + record_seprator + transmission;
        return matrix;
    }

}
