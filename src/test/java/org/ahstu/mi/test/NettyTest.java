package org.ahstu.mi.test;

import org.ahstu.mi.common.MiSendDTO;
import org.ahstu.mi.rpc.netty.client.NettyClient;

/**
 * Created by renyueliang on 17/5/23.
 */
public class NettyTest {

    //172.16.110.79,port:20080

    public static void main(String[] args)throws Throwable{

        MiSendDTO sendDTO=new MiSendDTO("242","5533","fff",null,null,"172.16.110.79",20081,"ttt",3000L,"24343","2424");

        NettyClient.getInstance().getChc(sendDTO);


        System.in.read();

    }

}
