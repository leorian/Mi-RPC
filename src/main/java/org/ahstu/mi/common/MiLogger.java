package org.ahstu.mi.common;

import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.log4j.MiRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiLogger {


    private static Logger logger;
    private final static String INSIST_LOG_NAME = "insistlog";
    private final static String INSIST_LOG_FILE = "insist.log";
    //%-d{yyyy-MM-dd HH:mm:ss}  %m%n
    private final static String PATTERN_LAYOUT = "%-d{yyyy-MM-dd HH:mm:ss}  %m%n";

    private final static String MAC_LOG_FILE_PATH = "/tmp/log/insistlog/";
    private final static String LINUX_LOG_FILE_PATH = "/var/log/insistlog/";
    private final static String WINODWS_LOG_FILE_PATH = "C:/log/insistlog/";
    private final static int maxFileSize=3;

    public static String getLogFilePath() {

        if (isLinux()) {
            return LINUX_LOG_FILE_PATH;
        }

        if (isMac()) {
            return MAC_LOG_FILE_PATH;
        }

        if (isWindows()) {
            return WINODWS_LOG_FILE_PATH;
        }

        return LINUX_LOG_FILE_PATH;

    }

    public static boolean isLinux() {
        return !isWindows() && !isMac();
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("mac");
    }


    public static SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse };

    public static Logger getLogger() {
        return logger;
    }

    static {
        try {
            init();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        try {
            ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

            int yjh = 23; //小时
            int yjm = 59; //分
            int yjs = 59; // 秒
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            int h = cal.get(Calendar.HOUR_OF_DAY);
            int m = cal.get(Calendar.MINUTE);
            int s = cal.get(Calendar.SECOND);

            int jldsm = (yjh - h) * 3600 + (yjm - m) * 60 + yjs - s + 60 * 60 * 0;

            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    getSysLogger().warn(" InsistLogger start !");

                    try {
                       MiRollingFileAppender appender = (MiRollingFileAppender) logger.getAppender(INSIST_LOG_NAME);

                        if (appender != null) {
                            getSysLogger().warn("appendName:" + appender.getName());
                            appender.setNow();
                            appender.rollOver();
                        } else {
                            getSysLogger().warn("not find appender:");
                        }
                    } catch (Throwable e) {
                        getSysLogger().error(e.getMessage(), e);
                    }
                }
            }, jldsm, 60 * 60 * 24, TimeUnit.SECONDS);

        } catch (Throwable e) {
            getSysLogger().error(e.getMessage(), e);
        }

    }

    private static void init() throws Throwable {
        logger = Logger.getLogger(INSIST_LOG_NAME);

        logger.setAdditivity(false);
        //additivity
        MiRollingFileAppender appender = new MiRollingFileAppender();

        String logFilePath = getLogFilePath()+INSIST_LOG_FILE;
        //是否按照应用文件夹存放不同的日志文件：主要是解决多个应用放在同一个服务器上，日志在一个unifiedlog.log文件里面

        appender.setFile(logFilePath);
        appender.setDatePattern("'.'yyyy-MM-dd");

        PatternLayout layout = new PatternLayout(PATTERN_LAYOUT);
        appender.setMaxFileSize(maxFileSize);
        appender.setName(INSIST_LOG_NAME);
        appender.setLayout(layout);
        appender.setAppend(true);
        appender.activateOptions();
        appender.setNow();
        appender.rollOver();
        logger.addAppender(appender);
        logger.setLevel(Level.WARN);

    }

    public static Logger getSysLogger() {
        return Logger.getLogger(MiLogger.class);
    }

    public static void record(String message, Throwable e) {
        logger.error(message, e);
    }

    public static void record(String message) {
        logger.warn(message);
    }

}
