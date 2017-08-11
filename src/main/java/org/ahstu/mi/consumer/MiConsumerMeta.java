package org.ahstu.mi.consumer;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.MiException;
import org.ahstu.mi.common.Meta;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiConsumerMeta extends Meta implements Serializable{

    private static final long serialVersionUID = 7310793253864310838L;
    private String id;
    private String version;
    private String group;
    private long clientTimeout;
    private int connectionNum;
    private String interfaceName;
    private String ip;
    private int port;
    private Class interfaceClass;
    private Set<String> classMethodsName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getClientTimeout() {
        return clientTimeout;
    }

    public void setClientTimeout(long clientTimeout) {
        this.clientTimeout = clientTimeout;
    }

    public int getConnectionNum() {
        return connectionNum;
    }

    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public boolean isThisClassMethod(String methodName){
        if(classMethodsName!=null && classMethodsName.contains(methodName)){
            return true;
        }

        return false;

    }

    public Set<String> getClassMethodsName() {
        if(classMethodsName==null){
            classMethodsName=new HashSet<String>();
        }

        return classMethodsName;
    }

    public void setClassMethodsName(Set<String> classMethodsName) {
        this.classMethodsName = classMethodsName;
    }

    @Override
    public void verification() {

        if(StringUtil.isBlank(id)
                ||
                StringUtil.isBlank(version) ||
                StringUtil.isBlank(group)  ||
                StringUtil.isBlank(interfaceName) ){
            throw new MiException(MiError.INSIST_ILLEGAL_ARGUMENT);
        }

        if(clientTimeout==0){
            clientTimeout=3000l;
        }

    }
}
