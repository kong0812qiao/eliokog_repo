package com.eliokog.persister;

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
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream("C:\\eliokog\\123\\crawler.log"), "UTF-8"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void append(String s) {
        printWriter.print(s);

    }


    public void destory(){
        printWriter.close();
    }

}
