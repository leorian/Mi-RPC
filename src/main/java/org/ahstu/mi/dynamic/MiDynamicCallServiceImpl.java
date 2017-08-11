package org.ahstu.mi.dynamic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ahstu.mi.common.MiDynamicDTO;
import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.provider.InsistProviderMeta;
import org.ahstu.mi.provider.InsistProviderStore;
import org.apache.commons.beanutils.MethodUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * Created by xiezhonggui on 2017/6/7.
 */
public class MiDynamicCallServiceImpl implements MiDynamicCallService {
    private static final Map<String, String> inputType = new HashMap<String, String>();
    private static final Map<Class, Boolean> basicType = new HashMap<Class, Boolean>();

    static {

        inputType.put(Class.class.toString(), "类的全路径名(如):java.lang.String");
        inputType.put(String.class.toString(), "字符串(如):\"张三\"");
        inputType.put(int.class.toString(), "整型(如):1");
        inputType.put(long.class.toString(), "长整型(如):1");
        inputType.put(double.class.toString(), "双精度浮点数(如):1.23");
        inputType.put(float.class.toString(), "单精度浮点数(如):1.23");
        inputType.put(boolean.class.toString(), "布尔类型(如):true或者false");

        inputType.put(int[].class.toString(), "整型数组(如):[1,2,3,4]");
        inputType.put(long[].class.toString(), "长整型数组(如):[1,2,3,4]");
        inputType.put(double[].class.toString(), "双精度浮点数数组(如):[1.23,2.34,3.44,4.55]");
        inputType.put(float[].class.toString(), "单精度浮点数数组(如):[1.23,2.34,3.44,4.55]");
        inputType.put(boolean[].class.toString(), "布尔类型数组(如):[true,false,true" +
                "]");

        inputType.put(String[].class.toString(), "字符串数组/集合(如):[\"张三\",\"李四\"]");
        inputType.put(Integer[].class.toString(), "整型数组/集合(如):[1,2,3,4]");
        inputType.put(Long[].class.toString(), "长整型数组/集合(如):[1,2,3,4]");
        inputType.put(Double[].class.toString(), "双精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(Float[].class.toString(), "单精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(Boolean[].class.toString(), "布尔类型数组/集合(如):[true,false,true" +
                "]");

        inputType.put(List.class.getName() + "<" + String.class.getName() + ">", "字符串数组/集合(如):[\"张三\",\"李四\"]");
        inputType.put(List.class.getName() + "<" + Integer.class.getName() + ">", "整型数组/集合(如):[1,2,3,4]");
        inputType.put(List.class.getName() + "<" + Long.class.getName() + ">", "长整型数组/集合(如):[1,2,3,4]");
        inputType.put(List.class.getName() + "<" + Double.class.getName() + ">", "双精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(List.class.getName() + "<" + Float.class.getName() + ">", "单精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(List.class.getName() + "<" + Boolean.class.getName() + ">", "布尔类型数组/集合(如):[true,false,true" +
                "]");

        inputType.put(Set.class.getName() + "<" + String.class.getName() + ">", "字符串数组/集合(如):[\"张三\",\"李四\"]");
        inputType.put(Set.class.getName() + "<" + Integer.class.getName() + ">", "整型数组/集合(如):[1,2,3,4]");
        inputType.put(Set.class.getName() + "<" + Long.class.getName() + ">", "长整型数组/集合(如):[1,2,3,4]");
        inputType.put(Set.class.getName() + "<" + Double.class.getName() + ">", "双精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(Set.class.getName() + "<" + Float.class.getName() + ">", "单精度浮点数数组/集合(如):[1.23,2.34,3.44,4.55]");
        inputType.put(Set.class.getName() + "<" + Boolean.class.getName() + ">", "布尔类型数组/集合(如):[true,false,true" +
                "]");


        basicType.put(String.class, true);
        basicType.put(Integer.class, true);
        basicType.put(Long.class, true);
        basicType.put(Double.class, true);
        basicType.put(Float.class, true);
        basicType.put(Boolean.class, true);
    }

    @Override
    public Object dynamicCallMethod(MiDynamicDTO insistDynamicDTO) {
        System.out.println(insistDynamicDTO);
        String serviceName = insistDynamicDTO.getServiceName();//服务名称
        String group = insistDynamicDTO.getGroup();//组名
        String version = insistDynamicDTO.getVersion();//版本名称
        String methodName = insistDynamicDTO.getMethod();//方法名称
        String param = insistDynamicDTO.getParam();//参数
        JSONArray jsonArray = JSON.parseArray(param);
        InsistProviderMeta providerMeta =
                InsistProviderStore.get(MiUtil.serviceGroupVersionCreateKey(serviceName, group, version));
        Object provider = providerMeta.getRef();
        Class providerClass = provider.getClass();
        Method[] methods = providerClass.getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
            if (methodName.equals(m.getName())) {
                method = m;
            }
        }
        Type[] types = method.getGenericParameterTypes();
        Object[] objects = null;
        if (jsonArray == null) {
            objects = new Object[0];
        } else {
            objects = new Object[types.length];
        }

        int i = 0;
        for (Type type : types) {
            if (type.toString().equals(Class.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        try {
                            objects[i] = Class.forName(jsonArray.getString(i));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (type.toString().equals(String.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getString(i);
                    }
                }
            } else if (type.toString().equals(int.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getIntValue(i);
                    }
                }
            } else if (type.toString().equals(long.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getLongValue(i);
                    }
                }
            } else if (type.toString().equals(double.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getDoubleValue(i);
                    }
                }
            } else if (type.toString().equals(float.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getFloatValue(i);
                    }
                }
            } else if (type.toString().equals(boolean.class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (StringUtils.isEmpty(jsonArray.getString(i))) {
                        objects[i] = null;
                    } else {
                        objects[i] = jsonArray.getBooleanValue(i);
                    }
                }
            } else if (type.toString().equals(int[].class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (jsonArray.getJSONArray(i).size() == 0) {
                        objects[i] = null;
                    } else {
                        JSONArray subJsonArray = jsonArray.getJSONArray(i);
                        int[] ints = new int[subJsonArray.size()];
                        for (int j = 0; j < subJsonArray.size(); j++) {
                            ints[j] = subJsonArray.getIntValue(j);
                        }
                        objects[i] = ints;
                    }
                }
            } else if (type.toString().equals(long[].class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (jsonArray.getJSONArray(i).size() == 0) {
                        objects[i] = null;
                    } else {
                        JSONArray subJsonArray = jsonArray.getJSONArray(i);
                        long[] longs = new long[subJsonArray.size()];
                        for (int j = 0; j < subJsonArray.size(); j++) {
                            longs[j] = subJsonArray.getLongValue(j);
                        }
                        objects[i] = longs;
                    }
                }
            } else if (type.toString().equals(double[].class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (jsonArray.getJSONArray(i).size() == 0) {
                        objects[i] = null;
                    } else {
                        JSONArray subJsonArray = jsonArray.getJSONArray(i);
                        double[] doubles = new double[subJsonArray.size()];
                        for (int j = 0; j < subJsonArray.size(); j++) {
                            doubles[j] = subJsonArray.getDoubleValue(j);
                        }
                        objects[i] = doubles;
                    }
                }
            } else if (type.toString().equals(float[].class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (jsonArray.getJSONArray(i).size() == 0) {
                        objects[i] = null;
                    } else {
                        JSONArray subJsonArray = jsonArray.getJSONArray(i);
                        float[] floats = new float[subJsonArray.size()];
                        for (int j = 0; j < subJsonArray.size(); j++) {
                            floats[j] = subJsonArray.getFloatValue(j);
                        }
                        objects[i] = floats;
                    }
                }
            } else if (type.toString().equals(boolean[].class.toString())) {
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    if (jsonArray.getJSONArray(i).size() == 0) {
                        objects[i] = null;
                    } else {
                        JSONArray subJsonArray = jsonArray.getJSONArray(i);
                        boolean[] booleans = new boolean[subJsonArray.size()];
                        for (int j = 0; j < subJsonArray.size(); j++) {
                            booleans[j] = subJsonArray.getBooleanValue(j);
                        }
                        objects[i] = booleans;
                    }
                }
            } else if (type.toString().startsWith("class [L") && type.toString().endsWith(";")) {//对象数组
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    JSONArray subJsonArray = jsonArray.getJSONArray(i);
                    if (subJsonArray.size() == 0) {
                        objects[i] = null;
                    } else {
                        try {
                            Class cls = Class.forName(type.toString().replace("class [L", "").replace(";", "").trim());
                            Object objects1 = Array.newInstance(cls, subJsonArray.size());
                            objects[i] = JSON.parseArray(subJsonArray.toJSONString(), cls).toArray((Object[]) objects1);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (!type.toString().startsWith("class [") && type.toString().startsWith("class ")) {//单个对象
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.size() == 0) {
                        objects[i] = null;
                    } else {
                        try {
                            Class cls = Class.forName(type.toString().replace("class ", "").trim());
                            objects[i] = JSON.parseObject(jsonObject.toJSONString(), cls);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (type.toString().startsWith("java.util.List")) {//list集合
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    JSONArray subJsonArray = jsonArray.getJSONArray(i);
                    if (subJsonArray == null || subJsonArray.size() == 0) {
                        objects[i] = null;
                    } else {
                        try {
                            Class cls = Class.forName(type.toString().replace("java.util.List<", "").replace(">", "").trim());
                            objects[i] = JSON.parseArray(subJsonArray.toJSONString(), cls);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else if (type.toString().startsWith("java.util.Set")) {//set集合
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    JSONArray subJsonArray = jsonArray.getJSONArray(i);
                    if (subJsonArray == null || subJsonArray.size() == 0) {
                        objects[i] = null;
                    } else {
                        try {
                            Class cls = Class.forName(type.toString().replace("java.util.Set<", "").replace(">", "").trim());
                            objects[i] = new HashSet(JSON.parseArray(subJsonArray.toJSONString(), cls));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (type.toString().startsWith("java.util.Map")) {//map集合
                if (jsonArray.size() == 0) {
                    objects[i] = null;
                } else {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject == null || jsonObject.size() == 0) {
                        objects[i] = null;
                    } else {
                        String[] typeStr = type.toString().replace("java.util.Map<", "").replace(">", "").split(",");
                        try {
                            Class keyCls = Class.forName(typeStr[0].trim());
                            Class valueCls = Class.forName(typeStr[1].trim());
                            Map map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
                            Map keyValueMap = new HashMap();
                            for (Object key : map.keySet()) {
                                keyValueMap.put(parseObject(key, keyCls), parseObject(map.get(key), valueCls));
                            }
                            objects[i] = keyValueMap;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println(type.toString());
            i++;
        }

        Object target = null;
        try {
            target = MethodUtils.invokeExactMethod(provider,
                    methodName,
                    objects,
                    method.getParameterTypes());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (target == null) {
            return null;
        }

        return target;
    }

    @Override
    public Object listInterfaceMethod(MiDynamicDTO insistDynamicDTO) {
        System.out.println(insistDynamicDTO);
        String serviceName = insistDynamicDTO.getServiceName();//服务名称
        String group = insistDynamicDTO.getGroup();//组名
        String version = insistDynamicDTO.getVersion();//版本名称
        InsistProviderMeta providerMeta =
                InsistProviderStore.get(MiUtil.serviceGroupVersionCreateKey(serviceName, group, version));
        Object provider = providerMeta.getRef();
        Class providerClass = provider.getClass();
        try {
            providerClass = Class.forName(serviceName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<MiServiceMethodDO> insistServiceMethodDOList = new ArrayList<MiServiceMethodDO>();
        for (Method method : providerClass.getDeclaredMethods()) {
            MiServiceMethodDO insistServiceMethodDO = new MiServiceMethodDO();
            insistServiceMethodDO.setReturnType(method.getReturnType().getName());
            insistServiceMethodDO.setSimpleMethodName(method.getName());
            if (insistServiceMethodDO.getInsistServiceMethodParameterDOList() == null) {
                insistServiceMethodDO.setInsistServiceMethodParameterDOList(new ArrayList<MiServiceMethodParameterDO>());
            }

            String methodName = method.getReturnType().getName() + " " + method.getName() + "(";
            int i = 0;
            for (Type type : method.getGenericParameterTypes()) {
                if (i != 0) {
                    methodName += ",";
                }
                methodName += type.toString();
                MiServiceMethodParameterDO insistServiceMethodParameterDO = new MiServiceMethodParameterDO();
                insistServiceMethodParameterDO.setParameterIndex(i);
                insistServiceMethodParameterDO.setParameterTypeName(type.toString());
                if (inputType.get(type.toString()) != null) {
                    insistServiceMethodParameterDO.setParameterHtmlDomType(ParameterHtmlDomType.INPUT);
                    insistServiceMethodParameterDO.setParameterExample(inputType.get(type.toString()));
                } else {
                    if (!type.toString().startsWith("class [") && type.toString().startsWith("class ")) { //单个对象
                        try {
                            Class cls = Class.forName(type.toString().replace("class ", "").trim());
                            Map<String, String> map = new HashMap<String, String>();
                            Field[] fields = cls.getDeclaredFields();
                            for (Field field : fields) {
                                map.put(field.getName(), field.getGenericType().toString());
                            }
                            insistServiceMethodParameterDO.setParameterExample(JSON.toJSONString(map));
                            insistServiceMethodParameterDO.setJsonObject(true);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (type.toString().startsWith("java.util.List")) {/*对象可重复集合*/
                        try {
                            Class cls = Class.forName(type.toString().replace("java.util.List<", "").replace(">", "").trim());
                            Map<String, String> map = new HashMap<String, String>();
                            Field[] fields = cls.getDeclaredFields();
                            for (Field field : fields) {
                                map.put(field.getName(), field.getGenericType().toString());
                            }
                            List<Object> objectList = new ArrayList<Object>();
                            objectList.add(map);
                            objectList.add(map);
                            insistServiceMethodParameterDO.setParameterExample(JSON.toJSON(objectList).toString());
                            insistServiceMethodParameterDO.setJsonObject(true);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (type.toString().startsWith("java.util.Set")) {/*对象不重复集合*/
                        try {
                            Class cls = Class.forName(type.toString().replace("java.util.Set<", "").replace(">", "").trim());
                            Map<String, String> map = new HashMap<String, String>();
                            Field[] fields = cls.getDeclaredFields();
                            for (Field field : fields) {
                                map.put(field.getName(), field.getGenericType().toString());
                            }
                            List<Object> objectList = new ArrayList<Object>();
                            objectList.add(map);
                            objectList.add(map);
                            insistServiceMethodParameterDO.setParameterExample(JSON.toJSON(objectList).toString());
                            insistServiceMethodParameterDO.setJsonObject(true);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (type.toString().startsWith("class [L") && type.toString().endsWith(";")) {//对象数组
                        try {
                            Class cls = Class.forName(type.toString().replace("class [L", "").replace(";", "").trim());
                            Map<String, String> map = new HashMap<String, String>();
                            Field[] fields = cls.getDeclaredFields();
                            for (Field field : fields) {
                                map.put(field.getName(), field.getGenericType().toString());
                            }
                            List<Object> objectList = new ArrayList<Object>();
                            objectList.add(map);
                            objectList.add(map);
                            insistServiceMethodParameterDO.setParameterExample(JSON.toJSON(objectList).toString());
                            insistServiceMethodParameterDO.setJsonObject(true);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (type.toString().startsWith("java.util.Map")) {//map集合
                        String[] typeStr = type.toString().replace("java.util.Map<", "").replace(">", "").split(",");
                        try {
                            Class keyCls = Class.forName(typeStr[0].trim());
                            Class valueCls = Class.forName(typeStr[1].trim());
                            Map map = new HashMap();
                            if (basicType.get(keyCls) != null && basicType.get(valueCls) != null) {
                                map.put("key01(类型:" + keyCls.getName() + ")", "value01(类型:" + valueCls.getName() + ")");
                                map.put("key02(类型:" + keyCls.getName() + ")", "value02(类型:" + valueCls.getName() + ")");
                            } else {
                                int index = 0;
                                while (index++ <= 1) {
                                    Object ko = null;
                                    if (basicType.get(keyCls) != null) {
                                        ko = "key0" + index + "(类型:" + keyCls.getName() + ")";
                                    } else {
                                        Map<String, String> kMap = new HashMap<String, String>();
                                        Field[] kFields = keyCls.getDeclaredFields();
                                        for (Field field : kFields) {
                                            kMap.put(field.getName(), field.getGenericType().toString());
                                        }
                                        ko = "key0" + index + "(类型:" + JSON.toJSON(kMap).toString() + ")";
                                    }

                                    Object vo = null;
                                    if (basicType.get(valueCls) != null) {
                                        vo = "value0" + index + "(类型:" + valueCls.getName() + ")";
                                    } else {
                                        Map<String, String> vMap = new HashMap<String, String>();
                                        Field[] vFields = valueCls.getDeclaredFields();
                                        for (Field field : vFields) {
                                            vMap.put(field.getName(), field.getGenericType().toString());
                                        }
                                        vo = vMap;
                                    }

                                    map.put(ko, vo);
                                }
                            }


                            insistServiceMethodParameterDO.setParameterExample(JSON.toJSON(map).toString());
                            insistServiceMethodParameterDO.setJsonObject(true);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    insistServiceMethodParameterDO.setParameterHtmlDomType(ParameterHtmlDomType.TEXTAREA);
                }

                insistServiceMethodDO.getInsistServiceMethodParameterDOList().add(insistServiceMethodParameterDO);
                i++;
            }
            methodName += ")";
            insistServiceMethodDO.setAbsoluteMethodName(methodName);
            insistServiceMethodDOList.add(insistServiceMethodDO);
        }
        return insistServiceMethodDOList;
    }

    private Object parseObject(Object o, Class c) {
        if (c == String.class) {
            return String.valueOf(o);
        } else if (c == Integer.class) {
            return Integer.parseInt(o.toString());
        } else if (c == Long.class) {
            return Long.parseLong(o.toString());
        } else if (c == Double.class) {
            return Double.parseDouble(o.toString());
        } else if (c == Float.class) {
            return Float.parseFloat(o.toString());
        } else if (c == Boolean.class) {
            return Boolean.parseBoolean(o.toString());
        } else {
            if (o instanceof String) {
                return JSON.parseObject(o.toString(), c);
            } else {
                return JSON.parseObject(((JSONObject) o).toJSONString());
            }
        }
    }
}
