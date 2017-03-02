package com.eliokog.persister;

import com.eliokog.util.SystemPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by eliokog on 2017/2/14.
 * CSV perisitor
 */
public class CSVPersister implements Persister {

    private static final Logger logger = LoggerFactory.getLogger(CSVPersister.class);
    private FileOutputStream writer;

    public CSVPersister() throws IOException {
        writer = new FileOutputStream(SystemPropertyUtil.getStringProperty("com.eliokog.craeleExcel"), true);
        // fix the encoding issue
        byte[] bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        String head = Stream.of("\"编号", "小区", "单价", "挂牌总价", "户型", "楼层", "区县", "版块", "成交日期", "面积", "条件", "交通", "地铁站", "朝向", "装修", "描述", "链接", "CrawledFrom\"\r").collect(Collectors.joining("\",\""));
        writer.write(bom);
        writer.write((new String(head.getBytes(), "utf-8")).getBytes());
        writer.flush();
    }

    @Override
    public synchronized void persist(String s) {
        String data = Arrays.asList(StringUtils.split(s, "%")).stream().collect(Collectors.joining("\",\""));
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(data).append("\"\r");
        logger.debug("String to persist: {}", sb.toString());
        try {
            writer.write(sb.toString().getBytes());


            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
