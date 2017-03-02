package com.eliokog.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by win on 2017/3/2.
 */
public class FIleContentFilter {

    public static void main(String[] args) {
        try (   BufferedReader br = new BufferedReader(new FileReader(new File("C:\\\\eliokog\\\\123\\\\crawler.csv")));
                FileOutputStream bw = new FileOutputStream(new File("C:\\\\eliokog\\\\123\\\\crawler.csv1")); ){

            byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            String head = Stream.of("\"编号", "小区", "单价", "挂牌总价", "户型", "楼层", "区县", "版块", "成交日期", "面积", "条件", "交通", "地铁站", "朝向", "装修", "描述", "链接", "CrawledFrom\"\r").collect(Collectors.joining("\",\""));
            bw.write(bom);
            bw.write((head.getBytes()));
            bw.flush();
        while(br.ready()){
                String s;
                if(StringUtils.contains((s = br.readLine()), "商住")||StringUtils.contains(s,"公寓")){
                    bw.write(s.getBytes());
                    bw.write(System.lineSeparator().getBytes());
            }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
