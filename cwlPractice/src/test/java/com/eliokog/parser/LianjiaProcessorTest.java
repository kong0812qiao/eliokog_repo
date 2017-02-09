package com.eliokog.parser;

import com.eliokog.url.WebURL;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by eliokog on 2017/2/9.
 */
public class LianjiaProcessorTest {

    Logger logger = LoggerFactory.getLogger(LianjiaProcessorTest.class);

//    @Test
    public void getPriceHTMLInfoTest(){
        String html ="<div class=\"col-1 fl\"> \n" +
                "  <div class=\"other\"> \n" +
                "   <div class=\"con\"> \n" +
                "    <a href=\"/chengjiao/changning/\">长宁</a> \n" +
                "    <a href=\"/chengjiao/zhongshangongyuan/\">中山公园</a> \n" +
                "    <span>| </span>高区/24层 \n" +
                "    <span>| </span>朝南 \n" +
                "    <span>| </span>精装 \n" +
                "   </div> \n" +
                "  </div> ";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("div.con");
        String[] attrs = StringUtils.split(elements.text(), "");

        logger.info("text is : {}",elements.get(0).text());
        Arrays.stream(attrs[0].trim().split(" ")).forEach(logger::info);
        Arrays.stream(attrs).forEach(logger::info);
    }

//    @Test
    public void testExcelWrite() throws IOException {

        //Blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Lianjia Data");
        Row row = sheet.createRow(0);
        Stream.of("编号","小区", "区县", "地区","户型","楼层","装修","面积", "单价", "总价", "地铁", "站名").
                collect(Collectors.toList()).stream().reduce("0",(i,s)->{
                        row.createCell(Integer.parseInt(i)).setCellValue(s);
                return String.valueOf(Integer.parseInt(i)+1);
        });
//        //This data needs to be written (Object[])
//        Map<String, Object[]> data = new TreeMap<String, Object[]>();
//        data.put("1", new Object[] {"ID", "NAME", "LASTNAME"});
//        data.put("2", new Object[] {1, "Amit", "Shukla"});
//        data.put("3", new Object[] {2, "Lokesh", "Gupta"});
//        data.put("4", new Object[] {3, "John", "Adwards"});
//        data.put("5", new Object[] {4, "Brian", "Schultz"});
//
//        //Iterate over data and write to sheet
//        Set<String> keyset = data.keySet();
//        int rownum = 0;
//        for (String key : keyset)
//        {
//            Row row = sheet.createRow(rownum++);
//            Object [] objArr = data.get(key);
//            int cellnum = 0;
//            for (Object obj : objArr)
//            {
//                Cell cell = row.createCell(cellnum++);
//                if(obj instanceof String)
//                    cell.setCellValue((String)obj);
//                else if(obj instanceof Integer)
//                    cell.setCellValue((Integer)obj);
//            }
//        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("C:\\eliokog\\123\\crawler.xlsx)"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void getExcelStringTest() {
        String html = " <div class=\"info-panel clear\"> \n" +
                " <h2 class=\"clear\"> <a key=\"sh1825840\" target=\"_blank\" href=\"/chengjiao/sh1825840.html\">兆丰大厦 3室2厅 156.5平米</a> </h2> \n" +
                " <div class=\"col-1 fl\"> \n" +
                "  <div class=\"other\"> \n" +
                "   <div class=\"con\"> \n" +
                "    <a href=\"/chengjiao/changning/\">长宁</a> \n" +
                "    <a href=\"/chengjiao/zhongshangongyuan/\">中山公园</a> \n" +
                "    <span>| </span>高区/24层 \n" +
                "    <span>| </span>朝南 \n" +
                "    <span>| </span>精装 \n" +
                "   </div> \n" +
                "  </div> \n" +
                "  <div class=\"introduce\"> \n" +
                "   <span>满五 </span> \n" +
                "   <span>距离2号线江苏路站737米 </span> \n" +
                "  </div> \n" +
                " </div> \n" +
                " <div class=\"col-2 fr\"> \n" +
                "  <div class=\"dealType\"> \n" +
                "   <div class=\"fl\"> \n" +
                "    <div class=\"div-cun\">\n" +
                "     2016-11-10\n" +
                "    </div> \n" +
                "    <p>链家网签约</p> \n" +
                "   </div> \n" +
                "   <div class=\"fl\"> \n" +
                "    <div class=\"div-cun\">\n" +
                "     67092\n" +
                "     <span>元/平</span>\n" +
                "    </div> \n" +
                "    <p>挂牌单价</p> \n" +
                "   </div> \n" +
                "   <div class=\"fr\"> \n" +
                "    <div class=\"div-cun\">\n" +
                "     1050\n" +
                "     <span>万</span>\n" +
                "    </div> \n" +
                "    <p>挂牌总价</p> \n" +
                "   </div> \n" +
                "  </div> \n" +
                " </div> \n" +
                "</div>";
        Document doc = Jsoup.parse(html);
        LianjiaProcessor p = new LianjiaProcessor();
//        p.getExcelString(doc.select("div.info-panel"), new WebURL("http://sh.lianjia.com"));
    }
}
