package com.eliokog.persister;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by eliokog on 2017/2/9.
 */
public class ExcelPersister implements Persister {
    private final static Logger logger = LoggerFactory.getLogger(ExcelPersister.class);
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private AtomicInteger rowNum = new AtomicInteger(0);
    private FileOutputStream out;

    private int count =0;
    public ExcelPersister() throws IOException {

        //Blank workbook
        workbook = new XSSFWorkbook();
        //Create a blank sheet
        sheet = workbook.createSheet("Lianjia Data");
        Row row = sheet.createRow(rowNum.getAndIncrement());
        Stream.of("编号", "小区", "单价", "挂牌总价", "户型", "楼层", "区县", "版块", "成交日期", "面积", "条件", "交通", "地铁站", "朝向", "装修", "描述", "链接"
        ).collect(Collectors.toList()).stream().reduce("0", (i, s) -> {
            row.createCell(Integer.parseInt(i)).setCellValue(s);
            return String.valueOf(Integer.parseInt(i) + 1);
        });

        //Write the workbook in file system
        out = new FileOutputStream(new File(System.getProperty("com.eliokog.craeleExcel")));
        workbook.write(out);
        out.close();
    }

    @Override
    public void persist(String str) {
//  TODO refactor not to read the whole file everytime write
        try {
            logger.info("Persisting String {} ", str);
            Row row = sheet.createRow(rowNum.getAndIncrement());
            Arrays.stream(StringUtils.splitByWholeSeparator(str, "%")).reduce("0", (i, s) -> {
                row.createCell(Integer.parseInt(i)).setCellValue(s);
                return String.valueOf(Integer.parseInt(i) + 1);
            });
            if(count==19) {
                out = new FileOutputStream(new File(System.getProperty("com.eliokog.craeleExcel")));
                workbook.write(out);
                out.flush();
                out.close();
            }
            count++;
        } catch (IOException  e) {
            e.printStackTrace();
            try {
                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

//        Row row = sheet.createRow(rowNum.getAndIncrement());
//        Arrays.stream(StringUtils.splitByWholeSeparator(str, "%")).reduce("0", (i, s) -> {
//            row.createCell(Integer.parseInt(i)).setCellValue(s);
//            return String.valueOf(Integer.parseInt(i) + 1);
//        });
//        try {
//            out = new FileOutputStream(new File(System.getProperty("com.eliokog.craeleExcel")));
//            workbook.write(out);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void destroy() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
