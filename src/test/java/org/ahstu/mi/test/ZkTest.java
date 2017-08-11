package org.ahstu.mi.test;

import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.zk.MiZkClient;

import java.util.List;

/**
 * Created by renyueliang on 17/5/23.
 */
public class ZkTest {


    public static void main(String[] args) throws  Throwable{

        System.out.println("/mi/consumer/forservice/user/com.bozhong.mi.test.service.UserService/1.0.0.daily".split("/").length);


        MiZkClient.getInstance().connect();
        // MiZkClient.getInstance().deleteNode("/mi/consumer/forservice/user/com.bozhong.mi.test.service.UserService/1.0.0.daily");
       // MiZkClient.getInstance().deleteNode("/mi/consumer/forservice/user");
        ///mi/prodiver/forservice/com.bozhong.mi.test.service.UserService/user/1.0.0.daily
        String path =  "mi/prodiver/forservice/user/com.bozhong.mi.test.service.UserService/1.0.0.daily";
        List<String> list = MiZkClient.getInstance().getTreeForList("mi/prodiver/forservice") ;

        System.out.println("************** service start********************");
        for(String sss : list){
            if(sss.split("/").length<7){
                continue;
            }
            System.out.println(sss);
        }
        System.out.println("************** service end ********************");

        List<String> consumerList = MiZkClient.getInstance().getTreeForList(MiUtil.getConsumerZkPath()) ;
        System.out.println("************** consumerList start********************");
        for(String consumer : consumerList){
            if(consumer.split("/").length<7){
                continue;
            }
            System.out.println(consumer);
        }
        System.out.println("************** consumerList end ********************");

    }
}
