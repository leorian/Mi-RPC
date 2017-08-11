package org.ahstu.mi.dynamic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/6/13 0013.
 */
public class InsistServiceMethodDO implements Serializable {

    /**
     * 方法简短名称
     */
    private String simpleMethodName;

    /**
     * 方法全局名称
     */
    private String absoluteMethodName;

    /**
     * 返回值类型
     */
    private String returnType;

    /**
     * 参数类型
     */
    private List<InsistServiceMethodParameterDO> insistServiceMethodParameterDOList;

    public String getSimpleMethodName() {
        return simpleMethodName;
    }

    public void setSimpleMethodName(String simpleMethodName) {
        this.simpleMethodName = simpleMethodName;
    }

    public String getAbsoluteMethodName() {
        return absoluteMethodName;
    }

    public void setAbsoluteMethodName(String absoluteMethodName) {
        this.absoluteMethodName = absoluteMethodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<InsistServiceMethodParameterDO> getInsistServiceMethodParameterDOList() {
        return insistServiceMethodParameterDOList;
    }

    public void setInsistServiceMethodParameterDOList(List<InsistServiceMethodParameterDO> insistServiceMethodParameterDOList) {
        this.insistServiceMethodParameterDOList = insistServiceMethodParameterDOList;
    }
}
