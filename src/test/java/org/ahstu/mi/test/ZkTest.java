package org.ahstu.mi.test;

import org.ahstu.mi.common.MiUtil;
import org.ahstu.mi.zk.InsistZkClient;

import java.util.List;

/**
 * Created by renyueliang on 17/5/23.
 */
public class ZkTest {


    public static void main(String[] args) throws  Throwable{

        System.out.println("/insist/consumer/forservice/user/com.bozhong.insist.test.service.UserService/1.0.0.daily".split("/").length);


        InsistZkClient.getInstance().connect();
        // InsistZkClient.getInstance().deleteNode("/insist/consumer/forservice/user/com.bozhong.insist.test.service.UserService/1.0.0.daily");
       // InsistZkClient.getInstance().deleteNode("/insist/consumer/forservice/user");
        ///insist/prodiver/forservice/com.bozhong.insist.test.service.UserService/user/1.0.0.daily
        String path =  "insist/prodiver/forservice/user/com.bozhong.insist.test.service.UserService/1.0.0.daily";
        List<String> list = InsistZkClient.getInstance().getTreeForList("insist/prodiver/forservice") ;

        System.out.println("************** service start********************");
        for(String sss : list){
            if(sss.split("/").length<7){
                continue;
            }
            System.out.println(sss);
        }
        System.out.println("************** service end ********************");

        List<String> consumerList = InsistZkClient.getInstance().getTreeForList(MiUtil.getConsumerZkPath()) ;
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
