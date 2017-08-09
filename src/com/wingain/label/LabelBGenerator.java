package com.wingain.label;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;


public class LabelBGenerator
{
    public static final int Min_Label_Width = 50;
    public static final int Min_Label_Height = 25;
    public final static int LABEL_B_WIDTH = 350;
    public final static int LABEL_B_HEIGHT = 150;
    
    public final static int HORIZONTAL_GAP =350 * 25/700;
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
    
     private static Image getNokiaLogo()
     {
         //Nokia Logo
         BufferedImage nokiaLogo = null;
         try
         {
             nokiaLogo = ImageIO.read(new File("nokia.png"));
         } catch (IOException e1)
         {
             // TODO Auto-generated catch block
             e1.printStackTrace();
         }
         
         
         return nokiaLogo.getScaledInstance(NOKIA_LOGO_WIDTH,NOKIA_LOGO_HEIGHT, BufferedImage.SCALE_SMOOTH);
     }
    
    public static BufferedImage labelB(String content, int width, int height )
    {
        
        String productCode = "1P473098A.101";
        String seriesNo = "SRK1628000111";
        String quantity = "Q1";
        String textF = "Arial";
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
    

}
