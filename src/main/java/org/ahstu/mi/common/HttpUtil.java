package org.ahstu.mi.common;

import com.bozhong.common.util.StringUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;


/**
 * Created by xiezg@317hu.com on 2017/5/24 0024.
 */
public class HttpUtil {
    public static final String INSIST_URL = "insist.317hu.com:8888";

    public static String loadZkHosts() {
        String zkHosts = null;
        HttpClient httpClient = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        PostMethod postMethod = new PostMethod("http://" + INSIST_URL + "/imanager/insist/zkHosts");
        try {
            httpClient.executeMethod(postMethod);
            zkHosts = postMethod.getResponseBodyAsString();
        } catch (Throwable e) {
            zkHosts = System.getProperty("insist.zkHosts");
        } finally {
            postMethod.releaseConnection();
        }

        if (StringUtil.isBlank(zkHosts)) {
            zkHosts = System.getProperty("insist.zkHosts");
        }

        return zkHosts;
    }
}
