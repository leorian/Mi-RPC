package org.ahstu.mi.common;

 /*
 * Created by renyueliang on 17/5/16.
 */
public class MiConstants {
    public static final String MI_ROOT_PATH = "mi";
    //
    public final static String LOWER_HORIZONTAL_LINE="_";

     public final static String MI_ZK_SLASH="/";

     public final static String MI_ZK_PRODIVER="prodiver";

     public final static String MI_ZK_CONSUMER ="consumer";

     public final static String MI_ZK_FORSERVICE="forservice";


    //反序列化接收每次接收长度的大小
    public final static int MAX_OBJECT_SIZE=65536*1024*10;

    //连接数
    public final static int SO_BACK_LOG = 10240;

    //发送缓冲区
    public final static int SO_SND_BUF = 65536;
    //接收缓冲区
    public final static int SO_RCV_BUF = 65536;


    //
    public final static  String REMOTE_CLIENT_IP ="REMOTE_CLIENT_IP";

    public final static  String REQUEST_ID ="REQUEST_ID";




}
