package com.eliokog.persister;

import com.eliokog.parser.LianjiaProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by win on 2017/2/9.
 */
public class ExcelPersitTest {
    private XSSFSheet sheet;
    private AtomicInteger rowNum = new AtomicInteger(0);

    @Before
    public void before(){
        System.setProperty("com.eliokog.craeleExcel", "C:\\eliokog\\123\\crawler.csv");
    }
//    @Test
    public void testExcel()throws Exception{

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
         sheet = workbook.createSheet("Lianjia Data");
        for(int j=0; j<10; j++) {
            Row row = sheet.createRow(rowNum.getAndIncrement());

            Stream.of("编号", "小区", "区县", "地区", "户型", "楼层", "装修", "面积", "单价", "总价", "地铁", "站名").
                    collect(Collectors.toList()).stream().reduce("0", (i, s) -> {
                row.createCell(Integer.parseInt(i)).setCellValue(s);
                return String.valueOf(Integer.parseInt(i) + 1);

            });

            FileOutputStream out = new FileOutputStream(new File("C:\\eliokog\\123\\crawler.csv"));
            workbook.write(out);
            out.close();
        }
        //Write the workbook in file system

    }
//    @Test
    public void testAppend() throws IOException {
     Persister persister = new ExcelPersister();
        for(int i=0; i<10000; i++)
     persister.persist("123123123123123123124 % 12331231231232131245 % 13123123123123123123123123123123122324");
    }
//    @Test
    public void testCSV() throws IOException {
        Persister persister = new CSVPersister();
        persister.persist("sh4150186%广裕小区%44800元/平%420万%2室2厅% 中区/24层 %虹口%凉城%2016-08-14%93.75平米%满五%N/A%N/A% 朝东南 %精装%N/A%http://sh.lianjia.com/chengjiao/sh4150186.html");
        persister.persist("123%123%");
    }
    @Test
    public void testGetStation(){
        LianjiaProcessor processor = new LianjiaProcessor();
        String s = "距离12号线试试广兰路站21312471米";
        int i = processor.findFirstNumber(s);
        System.out.println(s);
        System.out.println(s.indexOf("线"));
        String station = StringUtils.mid(s, s.indexOf("线")+1, i-1);
        System.out.println(i);

        System.out.println(station);
    }

}
