package com.wingain.model;
import java.awt.Font;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.sun.jna.Library;
import com.sun.jna.Native;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
public class LabelPrinter {
    public interface TscLibDll extends Library {
        TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary ("BCP", TscLibDll.class);
        int about ();
        int openport (String pirnterName);
        int closeport ();
        int sendcommand (String printerCommand);
        int setup (String width,String height,String speed,String density,String sensor,String vertical,String offset);
        int downloadpcx (String filename,String image_name);
        int barcode (String x,String y,String type,String height,String readable,String rotation,String narrow,String wide,String code);
        int printerfont (String x,String y,String fonttype,String rotation,String xmul,String ymul,String text);
        int clearbuffer ();
        int printlabel (String set, String copy);
        int formfeed ();
        int nobackfeed ();
        int windowsfont (int x, int y, int fontheight, int rotation, int fontstyle, int fontunderline, String szFaceName, String content);
    }
    
    public static int openPrinter()
    {
    	int result = TscLibDll.INSTANCE.openport("USB");
    	//result = TscLibDll.INSTANCE.sendcommand("REM ***** This is a test by JAVA. *****");
    	result = TscLibDll.INSTANCE.downloadpcx("LOGO.PCX", "LOGO.PCX");
    	result = TscLibDll.INSTANCE.setup("70", "30", "4.0", "8", "0", "2", "0");
    	result = TscLibDll.INSTANCE.clearbuffer();
    	return result;
    	
    }
    public static int closePrinter()
    {
        return TscLibDll.INSTANCE.closeport();
    }
   
    public static final int PRINTER_DPI = 300;
    public static final int DPI_RATE = 12;
    public static final int font_height = 40;//3 * DPI_RATE;
    public static final String font = "Arial";
    public static final String label_type = "128";
    public static final String label_height = String.valueOf(5*DPI_RATE);
    
    
    public static final int hor_l_gap = 30;// 2.5*12
    public static final int hor_r_gap = 54;// 4.5*12
    public static final int hor_width = 840;// 70*12
    public static final int ver_offset = -12;// 70*12
    
    
    public static final int nokia_logo_x = hor_l_gap;
    public static final int nokia_logo_y = 6;//1 * DPI_RATE;
    
    public static final int entity_name_x = 18 * DPI_RATE + 18;
    public static final int entity_name_y = nokia_logo_y;
    
    public static final int product_code_x = hor_l_gap;
    public static final int product_code_y = 5 * DPI_RATE  + ver_offset;
    public static final int product_code_desc_x = product_code_x;
    public static final int product_code_desc_y = 10 * DPI_RATE   + ver_offset; 
    
    public static final int series_no_x = hor_l_gap;
    public static final int series_no_y = 13 * DPI_RATE   + ver_offset;
    public static final int series_no_desc_x = product_code_x;
    public static final int series_no_desc_y = 18 * DPI_RATE   + ver_offset;
    
    public static final int quantity_x = hor_l_gap;
    public static final int quantity_y = 21 * DPI_RATE   + ver_offset;
    public static final int quantity_desc_x = product_code_x ;
    public static final int quantity_desc_y = 26 * DPI_RATE   + ver_offset;
    
    public static final int matrix_size = 10 * DPI_RATE;
    public static final int matrix_x = hor_width - hor_r_gap - matrix_size;//56 * DPI_RATE;
    public static final int matrix_y = 12 * DPI_RATE + ver_offset ;
    
    
    public static int stringWidth(String font, String content, int font_s)
    {
       AffineTransform affinetransform = new AffineTransform();     
       FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
       Font defaultFont = new Font(font, Font.PLAIN, font_s);
       int textwidth = (int)(defaultFont.getStringBounds(content, frc).getWidth());
       return textwidth;
    }
    public static String matrixCodeCmd(int x, int y, int width, int height, String content)
    {
        return String.format("DMATRIX %d,%d,%d,%d,%s", x,y,width,height, content);
    }
    
    
    public static void printLabelB(String content, String nick)
    {
        ArrayList<String> labelParse = LabelBGenerator.parseLabelA(content);
        if(labelParse.size() != 3)
        {
            System.err.println("error label code");
            return;
        }
        String productCode = labelParse.get(0);
        String seriesNo = labelParse.get(1);
        String x6 = labelParse.get(2);
        String quantity = "Q1";
        String origin = "Made In China";
        
        printLabel(productCode, seriesNo,quantity, x6,origin, nick);
    }
    
    public static void printLabel(String productCode, String seriesNo, String quantity, String x6, String origin, String nickName )
    {

        String bmp = "PUTPCX 30,6,\"LOGO.PCX\"";
        LabelPrinter.TscLibDll.INSTANCE.sendcommand(bmp);
        
        String entity_name = "Nokia Solutions and Networks";
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(entity_name_x, entity_name_y, font_height, 0, 0, 0, font, entity_name);
       
        //
        LabelPrinter.TscLibDll.INSTANCE.barcode(String.valueOf(product_code_x), String.valueOf(product_code_y), label_type, label_height,"0","0", "2", "6", productCode);
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(product_code_desc_x, product_code_desc_y, font_height, 0, 0, 0, font, LabelBGenerator.parseProductCode(productCode));
        
        LabelPrinter.TscLibDll.INSTANCE.barcode(String.valueOf(series_no_x), String.valueOf(series_no_y), label_type, label_height,"0","0", "2", "4", seriesNo);
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(series_no_desc_x, series_no_desc_y, font_height, 0, 0, 0, font, LabelBGenerator.parseSeries(seriesNo));
        
        LabelPrinter.TscLibDll.INSTANCE.barcode(String.valueOf(quantity_x), String.valueOf(quantity_y), label_type, label_height,"0","0", "2", "4", quantity);
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(quantity_desc_x, quantity_desc_y, font_height, 0, 0, 0, font, LabelBGenerator.parseQuantity(quantity));
        
        // print short name
        int nick_x = hor_width -hor_r_gap - stringWidth(font, nickName, font_height);
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(nick_x, product_code_y, font_height, 0, 2, 0, font, nickName);
        
        //print matrixContent
        String matrixContent = LabelBGenerator.matrixDataString(productCode, seriesNo, quantity, x6);    
        String cmd = String.format("DMATRIX %d,%d,%d,%d,\"%s\"", matrix_x, matrix_y, matrix_size, matrix_size, matrixContent);
        LabelPrinter.TscLibDll.INSTANCE.sendcommand(cmd);
        
        //
        int origin_x = hor_width - stringWidth(font, origin, font_height);
        LabelPrinter.TscLibDll.INSTANCE.windowsfont(origin_x, quantity_desc_y , font_height, 0, 0, 0, font, origin);
        
        LabelPrinter.TscLibDll.INSTANCE.printlabel("1", "1");

    }   
    
    public static void playRepeatWaring()
    {
        Media hit = new Media(new File("repeat.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
        
    }
    
    public static void test()
    {
    	int restust = openPrinter();
     	closePrinter();
    }

}


//package com.wingain.model;
//
//import java.awt.image.BufferedImage;
//
//
//import com.sun.jna.Native;
//import com.sun.jna.win32.StdCallLibrary;
//
//public class LabelPrinter
//{
//    public interface TscLibDll extends StdCallLibrary
//    {
//        TscLibDll INSTANCE = (TscLibDll) Native.loadLibrary("TSCLIB", TscLibDll.class);  
//        
//        int about();  
//  
//        int openport(String pirnterName);  
//  
//        int closeport();  
//  
//        int sendcommand(String printerCommand);  
//  
//        int setup(String width, String height, String speed, String density, String sensor, String vertical, String offset);  
//  
//        int downloadpcx(String filename, String image_name);  
//  
//        int barcode(String x, String y, String type, String height, String readable, String rotation, String narrow, String wide, String code);  
//  
//        int printerfont(String x, String y, String fonttype, String rotation, String xmul, String ymul, String text);  
//  
//        int clearbuffer();  
//  
//        int printlabel(String set, String copy);  
//  
//        int formfeed();  
//  
//        int nobackfeed();  
//  
//        int windowsfont(int x, int y, int fontheight, int rotation, int fontstyle, int fontunderline, String szFaceName, String content);  
//    
//    }
//    
//    public static void init()
//    {
//        System.setProperty("jna.encoding", "GBK");// support Chinese
//    }
//    
//    public static void printLabel(String productCode, String seriesNo, String quantity, String x6, String origin, String nickickName )
//    {
//
//    }
//    public static void printLabel(BufferedImage img)
//    {
//        System.setProperty("jna.encoding", "GBK");// support Chinese
//        // TscLibDll.INSTANCE.about();  
//        TscLibDll.INSTANCE.openport("TSC TTP-244 Pro");  
//        // TscLibDll.INSTANCE.downloadpcx("C:\\UL.PCX", "UL.PCX");  
//        // TscLibDll.INSTANCE.sendcommand("REM ***** This is a test by JAVA. *****");  
//        TscLibDll.INSTANCE.setup("60", "40", "5", "15", "0", "2", "0");  
//  
//        TscLibDll.INSTANCE.sendcommand("SET TEAR ON");  
//        TscLibDll.INSTANCE.clearbuffer();  
//  
//        String command = "QRCODE 300,70,L,6,A,0,M2,S3,\"123456\"";// print QR
//        TscLibDll.INSTANCE.sendcommand(command);  
//        // TscLibDll.INSTANCE.sendcommand("PUTPCX 550,10,\"UL.PCX\"");  
//  
//        // TscLibDll.INSTANCE.printerfont("100", "50", "TSS24.BF2", "0", "1", "1", "Technology");  
//        TscLibDll.INSTANCE.barcode("70", "140", "128", "90", "0", "0", "2", "2", "A123456789");// 打印内容，参数是位置和字体  
//        TscLibDll.INSTANCE.windowsfont(15, 15, 40, 0, 2, 1, "Arial", "网络科技公司");  
//        TscLibDll.INSTANCE.windowsfont(30, 90, 32, 0, 2, 0, "Arial", "--- 研发部");  
//        TscLibDll.INSTANCE.windowsfont(120, 240, 32, 0, 2, 0, "Arial", "A123456789");  
//        TscLibDll.INSTANCE.printlabel("1", "1");  
//        TscLibDll.INSTANCE.closeport(); 
//    }
//
//}
