package com.eliokog.persister;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by eliokog on 2017/2/9.
 */
public class ExcelPersister implements Persister {
    private SXSSFWorkbook workbook;
    private  XSSFSheet sheet;
    private AtomicInteger rowNum = new AtomicInteger(0);
    private FileOutputStream out;

    public ExcelPersister() throws IOException {

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Create a blank sheet
        sheet = workbook.createSheet("Lianjia Data");
        Row row = sheet.createRow(rowNum.get());
        Stream.of("编号","小区", "区县", "地区","户型","楼层","装修","面积", "单价", "总价", "地铁", "站名").
                collect(Collectors.toList()).stream().reduce("0",(i, s)->{
            row.createCell(Integer.parseInt(i)).setCellValue(s);
            return String.valueOf(Integer.parseInt(i)+1);
        });

        //Write the workbook in file system
        FileOutputStream out = new FileOutputStream(new File(System.getProperty("com.eliokog.craeleExcel")));
        workbook.write(out);
    }

    @Override
    public void persist(String str){
        Row row = sheet.createRow(rowNum.get());
        Arrays.stream(StringUtils.splitByWholeSeparator(str, "%")).reduce("0",(i, s)->{
            row.createCell(Integer.parseInt(i)).setCellValue(s);
            return String.valueOf(Integer.parseInt(i)+1);
        });
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destory() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
