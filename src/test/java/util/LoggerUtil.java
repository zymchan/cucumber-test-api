package util;

import org.apache.log4j.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoggerUtil {

    public static Logger getLog(Class<?> clazz) {

        Logger logger = Logger.getLogger(clazz);
        logger.removeAllAppenders();
        logger.setLevel(Level.INFO);
        logger.setAdditivity(true);

        FileAppender appender = new RollingFileAppender();
        PatternLayout layout = new PatternLayout();
        layout.setConversionPattern("[%d{yyyy-MM-dd HH:mm:ss}] %p %l : %m%n");
        appender.setLayout(layout);

        appender.setFile("./logs/"+getTime("yyyy-MM-dd-HH-mm-ss") + ".log");
        appender.setEncoding("UTF-8");
        appender.setAppend(true);
        appender.activateOptions();

        logger.addAppender(appender);
        return logger;
    }

    private static String getTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }
}
