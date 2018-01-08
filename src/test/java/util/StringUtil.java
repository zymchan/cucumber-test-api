package util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

public class StringUtil {

    public static String getCurrentTime(String dateFormat){
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(new Date());
    }
    public static Properties readPropertiesFile(String filePath){
        Properties prop = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            prop.load(in);
            in.close();
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return prop;
    }

}
