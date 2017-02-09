package com.eliokog.persister;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by win on 2017/2/9.
 */
public class ExcelPersitTest {
    private XSSFSheet sheet;
    private AtomicInteger rowNum = new AtomicInteger(0);
    @Test
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
}
