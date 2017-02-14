package com.eliokog.persister;

import com.eliokog.util.SystemPropertyUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.Map;

/**
 * Created by eliokog on 2017/2/7.
 */
public class FilerPersister implements Persister {
    PrintWriter printWriter;

    public FilerPersister(){
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(SystemPropertyUtil.getStringProperty("com.eliokog.crawleFile")), "UTF-8"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void persist(String s) {
        printWriter.write(s);
        printWriter.flush();
    }

    @Override
    public void destroy(){
        printWriter.close();
    }

}
