package org.ahstu.mi.test;

import org.ahstu.mi.common.MiDynamicDTO;
import org.ahstu.mi.dynamic.MiDynamicCallService;
import org.ahstu.mi.test.client.DataCenterService;
import org.ahstu.mi.test.service.TradeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.UUID;

/**
 * Created by renyueliang on 17/5/23.
 */
public class MiClientTest {

    static ApplicationContext factory;

    static void init() {
        factory = new ClassPathXmlApplicationContext("spring-client.xml");
    }

    static <T> T getBean(String beanName) {
        return (T) factory.getBean(beanName);
    }

    public static void main(String[] args) throws Throwable {
        init();

        MiDynamicCallService miDynamicCallService = getBean("miDynamicCallServiceClient");
        MiDynamicDTO miDynamicDTO = new MiDynamicDTO();
        miDynamicDTO.setGroup("trade");
        // miDynamicDTO.setMethod("findTrade");
        miDynamicDTO.setMethod("findListString");
        miDynamicDTO.setVersion("1.0.0.daily");
        miDynamicDTO.setServiceName("com.bozhong.mi.test.service.TradeService");
        //miDynamicDTO.setParam("[\"helloworld\",[{\"id\":1,\"name\":\"xzgui01\",\"xzgSex\":\"FEMALE\"},{\"id\":2,\"name\":\"xzgui02\",\"xzgSex\":\"MALE\"}]]");
        miDynamicDTO.setParam("[[\"hello\",\"wolrd\",\"thankyou\"]]");
        miDynamicCallService.dynamicCallMethod(miDynamicDTO);
        System.in.read();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {

                        try {
                            Thread.sleep(3000l);
                            DataCenterService dataCenterService = getBean("dataCenterService");
                            String result = dataCenterService.find("renyl haha !" + UUID.randomUUID().toString());
                            System.out.println(" result :" + result);

                            System.out.println(" ******** trade ********** ");
                            TradeService tradeService = getBean("tradeServiceClient");
                            String resultStr = tradeService.findTrade("kk ll oo" + UUID.randomUUID().toString(), null);
                            System.out.println(" resultStr :" + resultStr);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                        //  break;


                    }


                }
            });

            thread.start();
        }

        System.in.read();
    }

}
