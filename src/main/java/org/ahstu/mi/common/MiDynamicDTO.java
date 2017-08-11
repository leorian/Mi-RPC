package org.ahstu.mi.common;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by xiezhonggui on 2017/6/7.
 */
public class MiDynamicDTO implements Serializable{
    /**
     * 组
     */
    private String group;

    /**
     * 版本
     */
    private String version;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 参数值
     */
    private String param;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
