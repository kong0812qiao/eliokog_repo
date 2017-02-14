package com.eliokog.persister;

import au.com.bytecode.opencsv.CSVWriter;
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
public class OpenCSVPersister implements Persister {

    private static final Logger logger = LoggerFactory.getLogger(OpenCSVPersister.class);
    CSVWriter writer;
    public OpenCSVPersister(){
//        writer = new CSVWraiter();
//        writer = new FileOutputStream(SystemPropertyUtil.getStringProperty("com.eliokog.craeleExcel"), true);


    }
    @Override
    public void persist(String s) {
        String data = Arrays.asList(StringUtils.split(s, "%")).stream().collect(Collectors.joining("\",\""));
        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(data).append("\"\r");
        logger.debug("String to persist: {}", sb.toString());
        try {
//            writer.write(sb.toString().getBytes());
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
