package org.ahstu.mi.dynamic;

import java.io.Serializable;

/**
 * Created by xiezg@317hu.com on 2017/6/13 0013.
 */
public class InsistServiceMethodParameterDO implements Serializable {
    private int parameterIndex;//参数索引
    private String parameterTypeName;//参数类型名称
    private String parameterExample;//参数类型示例
    private boolean isJsonObject;
    private ParameterHtmlDomType parameterHtmlDomType;//参数类型对应HTML的DOM元素

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    public String getParameterTypeName() {
        return parameterTypeName;
    }

    public void setParameterTypeName(String parameterTypeName) {
        this.parameterTypeName = parameterTypeName;
    }

    public ParameterHtmlDomType getParameterHtmlDomType() {
        return parameterHtmlDomType;
    }

    public void setParameterHtmlDomType(ParameterHtmlDomType parameterHtmlDomType) {
        this.parameterHtmlDomType = parameterHtmlDomType;
    }

    public String getParameterExample() {
        return parameterExample;
    }

    public void setParameterExample(String parameterExample) {
        this.parameterExample = parameterExample;
    }

    public boolean isJsonObject() {
        return isJsonObject;
    }

    public void setJsonObject(boolean jsonObject) {
        isJsonObject = jsonObject;
    }
}
