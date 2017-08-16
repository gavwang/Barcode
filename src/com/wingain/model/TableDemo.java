package com.wingain.model;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**  
 *  
 * @author xiaoquan  
 */  
public class TableDemo implements Printable {   
    
    private TableModel model = null;   
    private String info;   
    private int totalRow = 0;   
    private static final int LEFT = 0;   
    private static final int RIGHT = 1;   
    private static final int CENTER = 2;   
    private static final int AUTO = 3;   
    
    public void printTable(TableModel model,   
            String info) {   
        this.model = model;   
        this.info = info;   
        totalRow = model.getRowCount();   
        PrinterJob printJob = PrinterJob.getPrinterJob();   
        printJob.setPrintable(this);   
        if (printJob.printDialog()) {   
            try {   
                printJob.print();   
            } catch (Exception ex) {   
                ex.printStackTrace();   
            }   
        }   
    }   
    private static final double paper_offset_x = 20;   
    private static final double paper_offset_y = 20;   
    private static final double title_time_margin = 10;   
    private static final double time_body_margin = 2;   
    private static final double cell_padding_y = 3;   
    private static final double cell_padding_x = 2;   
    private static final double body_btm_margin = 20;   
    private static final double body_cell_height = 20;   
    private static final Font title_font = new Font("黑体", Font.PLAIN, 18);   
    private static final Font time_font = new Font("Dialog", Font.PLAIN, 10);   
    private static final Font body_font = new Font("Dialog", Font.PLAIN, 10);   
    
        
    public int print(Graphics g, PageFormat pf, int pageIndex) throws  
            PrinterException {   
        //纸张宽   
        double pageWidth = pf.getImageableWidth();   
        //纸张高   
        double pageHeight = pf.getImageableHeight();   
        //打印的内容起始X   
        double pageStartX = pf.getImageableX();   
        //打印的内容起始Y   
        double pageStartY = pf.getImageableY();   
    
        //表头高   
        double tableHeadH = 0;   
        //Cell高   
        double cellH = 0;   
    
        //计算表头高度和单元格高度   
        g.setFont(body_font);   
        FontMetrics cellFm = g.getFontMetrics();   
        cellH = cellFm.getHeight() + cell_padding_y * 2 + 1;   
        tableHeadH = cellH * 2;   
    
        //计算Title以及其位置   
        String title = info;   
        g.setFont(title_font);   
        FontMetrics titleFm = g.getFontMetrics();   
        int titleW = titleFm.stringWidth(title);   
    
        //表底和表头文字属性   
        g.setFont(time_font);   
        FontMetrics btmFm = g.getFontMetrics();   
        FontMetrics timeFm = g.getFontMetrics();   
    
        //表格以上的Margin   
        double tableTopMargin = paper_offset_y + titleFm.getHeight() +   
                title_time_margin + timeFm.getHeight() + time_body_margin;   
    
        //表格每列的最大宽度   
        double[] cellColMaxWidths = caculateTableCellWidth(model, cellFm);   
    
        //当前Page的数据容量高度-不包括表头和表尾   
        double currentPageDataCapacityHeight = pageHeight - tableTopMargin -   
                tableHeadH - btmFm.getHeight() - body_btm_margin - 1;   
    
        //当前Page的数据容量   
        int currentPageBodyCapacityRows = (int) (currentPageDataCapacityHeight /   
                cellH);   
    
        //Y方向的分页数量   
        int pagesY = 0;   
        if (model.getRowCount() % currentPageBodyCapacityRows == 0) {   
            pagesY = (int) (model.getRowCount() /   
                    currentPageBodyCapacityRows);   
        } else {   
            pagesY = (int) (model.getRowCount() /   
                    currentPageBodyCapacityRows) +   
                    1;   
        }   
    
        //当前页数大于总页数时不打印   
        if (pageIndex + 1 > pagesY) {   
            return NO_SUCH_PAGE;   
        }   
    
        //绘制Title   
        g.setFont(title_font);   
        g.drawString(title, (int) (pageStartX +   
                (pageWidth - titleW) / 2), (int) (pageStartY +   
                paper_offset_y +   
                titleFm.getAscent()));   
    
        //绘制区域移动到新的(0,0)点   
        g.translate((int) (paper_offset_x + pageStartX), (int) (tableTopMargin +   
                pageStartY));   
        int currentX = 0, currentY = 0;   
    
        //绘制第一张表   
    
        //绘制表头   
        g.setFont(time_font);   
        String time = "表头: " + info;   
        g.drawString(time, currentX, currentY);   
        currentY += 5;   
        //绘制单一表头   
        for (int i = 0; i < model.getColumnCount(); i++) {   
            double width = cellColMaxWidths[i];   
            double height = tableHeadH;   
            String name = model.getColumnName(i);   
            drawCell(g, name, currentX, currentY, (int) width,   
                    (int) height, CENTER);   
            currentX += width;   
        }   
    
        //绘制数据   
        currentX = 0;   
        currentY = (int) tableHeadH;   
        //当前Page的数据容量   
        int rightCellX = 0;   
        int yIndex = pageIndex;   
        int startRow = currentPageBodyCapacityRows * yIndex;   
        int endRow = (currentPageBodyCapacityRows * (yIndex + 1)) >  //aa   
                totalRow   
                ? totalRow   
                : (currentPageBodyCapacityRows * (yIndex + 1));   
        for (int row = startRow; row < endRow; row++) {   
            //绘制单项表头下面的数据   
            for (int i = 0; i < model.getColumnCount(); i++) {   
                double width = cellColMaxWidths[i];   
                double height = body_cell_height;   
                Object value = model.getValueAt(row, i);   
                drawCell(g, value, currentX, currentY, (int) width,   
                        (int) height, AUTO);   
                currentX += width;   
                rightCellX = currentX;   
            }   
            currentX = 0;   
            currentY += cellH;   
        }   
    
        //绘制闭合线，下面和右侧两条   
        g.drawLine(currentX, currentY, rightCellX, currentY);   
        g.drawLine(rightCellX, 5, rightCellX, currentY);   
    
        drawBottomInfo(pageIndex, pagesY, currentY, g, (int) pageWidth);   
        return PAGE_EXISTS;   
    }   
    
    private void drawBottomInfo(int pageIndex, int pagesY,   
            int currentY, Graphics g, int pageWidth) {   
        if (pageIndex + 1 == pagesY) {   
            //绘制底部信息   
            int btmX = 0;   
            int btmY = currentY + 20;   
            g.drawString("负责人:", btmX, btmY);   
            g.drawString("制表:", pageWidth / 3, btmY);   
            FontMetrics fm = g.getFontMetrics();   
            int dataWidth = fm.stringWidth("日期: 2009/10/26");   
            g.drawString("日期:", pageWidth - dataWidth, btmY);   
        }   
    }   
    
    /**  
     * 计算最大列宽  
     * @param cellFm  
     * @return  
     */  
    private double[] caculateTableCellWidth(   
            TableModel model,   
            FontMetrics cellFm) {   
        //表格每列的最大宽度   
        double[] cellColMaxWidths = new double[model.getColumnCount()];   
    
        //计算表头每列最大宽度   
        double[] headerColMaxWidths = new double[model.getColumnCount()];   
    
        for (int i = 0; i < model.getColumnCount(); i++) {   
            String name = model.getColumnName(i);   
            headerColMaxWidths[i] = cellFm.stringWidth(name) + cell_padding_x *   
                    2 + 1;   
        }   
        //没有数据时，表头每列的最大宽度就是表格每列的最大宽度   
        cellColMaxWidths = headerColMaxWidths;   
    
        //算数据每列的最大宽度和表头每列最大宽度对比   
        for (int j = 0; j < model.getRowCount(); j++) {   
            for (int i = 0; i < model.getColumnCount(); i++) {   
                //做些数据类型的判断   
                Object value = model.getValueAt(j, i);   
                if (value instanceof BigDecimal) {   
                    value = ((BigDecimal) value).doubleValue();   
                }   
                String text = "";   
                if (value != null) {   
                    text = value.toString();   
                }   
                double temp = cellFm.stringWidth(text) + cell_padding_x * 2 + 1;   
                if (cellColMaxWidths[i] < temp) {   
                    cellColMaxWidths[i] = temp;   
                }   
            }   
        }   
        return cellColMaxWidths;   
    }   
    
    /**  
     * 绘制单元格及里面的文字  
     * @param g  
     * @param value  
     * @param x  
     * @param y  
     * @param width  
     * @param height  
     */  
    private static void drawCell(Graphics g, Object value, int x, int y,   
            int width,   
            int height, int locate) {   
    
        g.drawLine(x, y, x + width - 1, y);   
        g.drawLine(x, y, x, y + height - 1);   
        FontMetrics fm = g.getFontMetrics();   
        if (value == null) {   
            value = "";   
        }   
        switch (locate) {   
            case 0:   
                //居左   
                g.drawString(value.toString(), (int) (x + cell_padding_x), y +   
                        (height - fm.getHeight()) / 2 + fm.getAscent());   
            case 1:   
                //居右   
                g.drawString(value.toString(),   
                        (int) (x +   
                        (width - fm.stringWidth(value.toString()) + width -   
                        fm.stringWidth(value.toString()) - cell_padding_x) /   
                        2), y +   
                        (height - fm.getHeight()) / 2 + fm.getAscent());   
            case 2:   
                //居中   
                g.drawString(value.toString(), x + (width - fm.stringWidth(   
                        value.toString())) / 2, y + (height -   
                        fm.getHeight()) / 2 + fm.getAscent());   
            case 3:   
                //自动判断   
                NumberFormat formatter = NumberFormat.getNumberInstance();   
                formatter.setMinimumFractionDigits(2);   
                formatter.setMaximumFractionDigits(2);   
                //根据数据类型左对齐还是右对齐绘制还是居中对齐   
                if (value instanceof BigDecimal) {   
                    //居右   
                    value = ((BigDecimal) value).doubleValue();   
                    value = formatter.format(value);   
                    g.drawString(value.toString(),   
                            (int) (x +   
                            (width - fm.stringWidth(value.toString()) + width -   
                            fm.stringWidth(value.toString()) - cell_padding_x) /   
                            2), y +   
                            (height - fm.getHeight()) / 2 + fm.getAscent());   
                } else if (value instanceof Integer || value instanceof Long ||   
                        value instanceof Double) {   
                    //居右   
                    g.drawString(value.toString(),   
                            (int) (x +   
                            (width - fm.stringWidth(value.toString()) + width -   
                            fm.stringWidth(value.toString()) - cell_padding_x) /   
                            2), y +   
                            (height - fm.getHeight()) / 2 + fm.getAscent());   
                } else {   
                    //居中   
                    g.drawString(value.toString(), x + (width - fm.stringWidth(   
                            value.toString())) / 2, y + (height -   
                            fm.getHeight()) / 2 + fm.getAscent());   
                }   
        }   
    }   
    
    public static void test(String[] args) {   
        new TableDemo().printTable(testData(), "测试");   
    }   
    
    private static TableModel testData() {   
        final Object rows[][] = {   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", "Test1", "Test2", "Test3"},   
            {"five", "go - \u4E94", "Test1", "Test2", "Test3"},   
            {"six", "roku - \u516D", "Test1", "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", "Test1", "Test2", "Test3"},   
            {"eight", "hachi - \u516B", "Test1", "Test2", "Test3"},   
            {"nine", "kyu - \u4E5D", "Test1", "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", "Test1", "Test2", "Test3"},   
            {"five", "go - \u4E94", "Test1", "Test2", "Test3"},   
            {"six", "roku - \u516D", "Test1", "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", "Test1", "Test2", "Test3"},   
            {"eight", "hachi - \u516B", "Test1", "Test2", "Test3"},   
            {"nine", "kyu - \u4E5D", "Test1", "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", "Test1", "Test2", "Test3"},   
            {"five", "go - \u4E94", "Test1", "Test2", "Test3"},   
            {"six", "roku - \u516D", "Test1", "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", "Test1", "Test2", "Test3"},   
            {"eight", "hachi - \u516B", "Test1", "Test2", "Test3"},   
            {"nine", "kyu - \u4E5D", "Test1", "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", "Test1", "Test2", "Test3"},   
            {"five", "go - \u4E94", "Test1", "Test2", "Test3"},   
            {"six", "roku - \u516D", "Test1", "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", "Test1", "Test2", "Test3"},   
            {"eight", "hachi - \u516B", "Test1", "Tes12121t2", "Test3"},   
            {"nine", "kyu - \u4E5D", "Test1", "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", 12, "Test2", "Test3"},   
            {"five", "go - \u4E94", 121212, "Test2", "Test3"},   
            {"six", "roku - \u516D", 1212121212, "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", 12.01, "Test2", "Test3"},   
            {"eight", "hachi - \u516B", 135.12, "Test2", "Test3"},   
            {"nine", "kyu - \u4E5D", 93828.34, "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},   
            {"one", "ichi - \u4E00", "Test1", "Test2", "Test3"},   
            {"two", "ni - \u4E8C", "Test1", "Test2", "Test3"},   
            {"three", "san - \u4E09", "Test1", "Test2", "Test3"},   
            {"four", "shi - \u56DB", "Test1", "Test2", "Test3"},   
            {"five", "go - \u4E94", "Test1", "Test2", "Test3"},   
            {"six", "roku - \u516D", "Test1", "Test2", "Test3"},   
            {"seven", "shichi - \u4E03", "Test1", "Test2", "Test3"},   
            {"eight", "hachi - \u516B", "Test1", "Test2", "T1212121212est3"},   
            {"nine", "kyu - \u4E5D", "Test1", "Test2", "Test3"},   
            {"ten", "ju - \u5341", "Test1", "Test2", "Test3"},};   
        final Object headers[] = {"English", "Japanese", "Column1", "Column2",   
            "Column3"};   
        JTable table = new JTable(rows, headers);   
        return table.getModel();   
    }   
}