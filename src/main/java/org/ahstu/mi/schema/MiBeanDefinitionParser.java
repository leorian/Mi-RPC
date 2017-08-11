package org.ahstu.mi.schema;

import org.ahstu.mi.consumer.MiSpringConsumerBean;
import org.ahstu.mi.provider.MiSpringProviderBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 2017/5/16.
 */
public class MiBeanDefinitionParser implements BeanDefinitionParser {

    private final Class clazz;

    public MiBeanDefinitionParser(Class clazz) {
        this.clazz = clazz;
    }

    public BeanDefinition parse(Element element, ParserContext parserContext) {
        if (clazz == MiSpringProviderBean.class) {
            return parseProvider(element, parserContext);
        } else if (clazz == MiSpringConsumerBean.class) {
            return parseConsumer(element, parserContext);
        } else {
            throw new BeanDefinitionValidationException("Unknown class to definition " + clazz.getName());
        }
    }

    static class FieldDefenition {
        public FieldDefenition(String propName, boolean requred, boolean discard) {
            this.propName = propName;
            this.requred = requred;
            this.discard = discard;
        }

        public FieldDefenition(boolean discard) {
            this.discard = discard;
        }

        String propName;
        boolean requred;
        final boolean discard;

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public boolean isRequred() {
            return requred;
        }

        public void setRequred(boolean requred) {
            this.requred = requred;
        }

        public boolean isDiscard() {
            return discard;
        }
    }

    static final Map<String, FieldDefenition> providerFieldDefMap = new HashMap<String, FieldDefenition>();

    static {
        providerFieldDefMap.put("interface", new FieldDefenition("interfaceName", true, false));
//        providerFieldDefMap.put("timeout", new FieldDefenition("clientTimeout", false, false));
        providerFieldDefMap.put("version", new FieldDefenition("version", true, false));
        providerFieldDefMap.put("group", new FieldDefenition("group", true, false));
        providerFieldDefMap.put("ref", new FieldDefenition(true));
        providerFieldDefMap.put("id", new FieldDefenition("id",true,false));
    }

    private void parseAttr(Element element, RootBeanDefinition beanDef, Map<String, FieldDefenition> fieldDefMap) {
        //props
        NamedNodeMap attrMap = element.getAttributes();
        for (int i = 0; i < attrMap.getLength(); i++) {
            Attr attr = (Attr) attrMap.item(i);
            String name = attr.getName();
            if(null == name || name.isEmpty()) {
                continue;
            }
            FieldDefenition fieldDefenition = fieldDefMap.get(name);
            if(null == fieldDefenition) {
                setProperty(beanDef, element, name, name, false);
            } else {
                if(fieldDefenition.isDiscard()) {
                    continue;
                }
                setProperty(beanDef, element, name, fieldDefenition.getPropName(), fieldDefenition.isRequred());
            }
        }
    }

    public BeanDefinition parseProvider(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clazz);
        beanDef.setLazyInit(false);
        //target
        String targetRef = element.getAttribute("ref");
        checkAttr("ref", targetRef);
        beanDef.getPropertyValues().addPropertyValue("target", new RuntimeBeanReference(targetRef));

        //props
        parseAttr(element, beanDef, providerFieldDefMap);

        List list = new ArrayList();
        List<String> configCenterList = new ArrayList<String>();
        NodeList methodSpecials = element.getChildNodes();
        for (int i = 0; i < methodSpecials.getLength(); i++) {
            Node item = methodSpecials.item(i);
            if (item instanceof Element) {
                String localName = item.getLocalName();
                if (localName.equals("methodSpecials")) {
                    //methodSpecials
                    NodeList methodSpecialList = item.getChildNodes();
                    for (int j = 0; j < methodSpecialList.getLength(); j++) {
                        Node methodSpecial = methodSpecialList.item(j);
                        if (methodSpecial instanceof Element) {
                            String timeout = ((Element) methodSpecial).getAttribute("timeout");
                            String name = ((Element) methodSpecial).getAttribute("name");
                            String retries = ((Element) methodSpecial).getAttribute("retries");
//                            MethodSpecial ms = new MethodSpecial();
//                            ms.setClientTimeout(Long.valueOf(timeout));
//                            ms.setMethodName(name);
                            if (!retries.isEmpty()) {
//                                ms.setRetries(Integer.valueOf(retries));
                            }
//                            list.add(ms);
                        }
                    }
                } else if (localName.equals("configserverCenter")) {
                    //configserverCenter
                    NodeList centerList = item.getChildNodes();
                    for (int j = 0; j < centerList.getLength(); j++) {
                        Node center = centerList.item(j);
                        if (center instanceof Element) {
                            String name = ((Element) center).getAttribute("name");
                            if (!name.isEmpty()) {
                                configCenterList.add(name);
                            }
                        }
                    }
                }
            }
        }
        if (list.size() > 0) {
            beanDef.getPropertyValues().addPropertyValue("methodSpecials", list.toArray());
        }
        if (configCenterList.size() > 0) {
            beanDef.getPropertyValues().addPropertyValue("configserverCenter", configCenterList);
        }
        //name
        String id = getId(parserContext, element.getAttribute("id"));
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return beanDef;
    }

    private String getId(ParserContext parserContext, String id) {
        if(id == null || id.length() == 0) {
            id = this.clazz.getSimpleName();
        }
        int time = 3;
        while(parserContext.getRegistry().containsBeanDefinition(id) && time > 0) {
            id += time;
            time --;
        }
        if(parserContext.getRegistry().containsBeanDefinition(id)){
            throw new IllegalStateException("Duplicate spring bean id " + id);
        }
        return id;
    }

    private void setProperty(RootBeanDefinition beanDef, Element element, String attrName, String propertyName, boolean required) {
        String attr = element.getAttribute(attrName);
        if (required) {
            checkAttr(attrName, attr);
        }
        if (attr != null && !attr.isEmpty()) {
            beanDef.getPropertyValues().addPropertyValue(propertyName, attr);
        }
    }

    private void checkAttr(String attrName, String attr) {
        if (null == attr || attr.isEmpty()) {
            throw new BeanDefinitionValidationException("attribute " + attrName + " must set.");
        }
    }

    static final Map<String, FieldDefenition> consumerFieldDefMap = new HashMap<String, FieldDefenition>();

    static {
        consumerFieldDefMap.put("interface", new FieldDefenition("interfaceName", true, false));
        consumerFieldDefMap.put("version", new FieldDefenition("version", true, false));
        consumerFieldDefMap.put("group", new FieldDefenition("group", true, false));
        consumerFieldDefMap.put("id", new FieldDefenition("id",true,false));
    }

    public BeanDefinition parseConsumer(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(clazz);
        beanDef.setLazyInit(false);

        //props
        parseAttr(element, beanDef, consumerFieldDefMap);

        String callbackInvokerRef = element.getAttribute("callbackInvoker");
        if (!callbackInvokerRef.isEmpty()) {
            beanDef.getPropertyValues().addPropertyValue("callbackInvoker", new RuntimeBeanReference(callbackInvokerRef));
        }

        List list = new ArrayList();
        List<String> asyncallMethods = new ArrayList<String>();
        NodeList methodSpecials = element.getChildNodes();
        for (int i = 0; i < methodSpecials.getLength(); i++) {
            Node item = methodSpecials.item(i);
            if (item instanceof Element) {
                String localName = item.getLocalName();
                if (localName.equals("asyncallMethods")) {
                    //asyncallMethods
                    NodeList asyncallMethodsList = item.getChildNodes();
                    for (int j = 0; j < asyncallMethodsList.getLength(); j++) {
                        Node asyncallMethod = asyncallMethodsList.item(j);
                        if (!(asyncallMethod instanceof Element)) {
                            continue;
                        }
                        String name = ((Element) asyncallMethod).getAttribute("name");
                        String type = ((Element) asyncallMethod).getAttribute("type");
                        if (name.isEmpty() || type.isEmpty()) {
                            continue;
                        }
                        String listener = ((Element) asyncallMethod).getAttribute("listener");
                        StringBuilder sb = new StringBuilder();
                        sb.append("name:").append(name).append(";type:").append(type);
                        if (type.equals("callback")) {
                            sb.append(";listener:").append(listener);
                        }
                        asyncallMethods.add(sb.toString());
                    }
                } else if (localName.equals("methodSpecials")) {
                    //methodSpecials
                    NodeList methodSpecialList = item.getChildNodes();
                    for (int j = 0; j < methodSpecialList.getLength(); j++) {
                        Node methodSpecial = methodSpecialList.item(j);
                        if (methodSpecial instanceof Element) {
                            String timeout = ((Element) methodSpecial).getAttribute("timeout");
                            String name = ((Element) methodSpecial).getAttribute("name");
                            String retries = ((Element) methodSpecial).getAttribute("retries");
//                            MethodSpecial ms = new MethodSpecial();
//                            ms.setClientTimeout(Long.valueOf(timeout));
//                            ms.setMethodName(name);
                            if (!retries.isEmpty()) {
//                                ms.setRetries(Integer.valueOf(retries));
                            }
//                            list.add(ms);
                        }
                    }
                }
            }
        }
        if (list.size() > 0) {
            beanDef.getPropertyValues().addPropertyValue("methodSpecials", list.toArray());
        }
        if (asyncallMethods.size() > 0) {
            beanDef.getPropertyValues().addPropertyValue("asyncallMethods", asyncallMethods);
        }

        String id = getId(parserContext, element.getAttribute("id"));
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDef, id);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return beanDef;
    }

}
