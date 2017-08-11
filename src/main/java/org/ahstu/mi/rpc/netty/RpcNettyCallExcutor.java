package org.ahstu.mi.rpc.netty;

import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.consumer.manager.MiResultStore;
import org.ahstu.mi.lock.MiLock;
import org.ahstu.mi.lock.MiLockStore;
import org.ahstu.mi.rpc.RpcCallExcutor;
import org.ahstu.mi.rpc.netty.client.NettyClient;

import java.util.UUID;

/**
 * Created by renyueliang on 17/5/17.
 */
public class RpcNettyCallExcutor implements RpcCallExcutor {

    private static String RPC_NETTY_CREATE_LOCK = UUID.randomUUID().toString();

    private static RpcNettyCallExcutor rpcNettyCallExcutor = null;

    private RpcNettyCallExcutor() {

    }

    public static RpcNettyCallExcutor getInstance() {

        if (rpcNettyCallExcutor == null) {
            synchronized (RPC_NETTY_CREATE_LOCK) {
                if (rpcNettyCallExcutor == null) {
                    rpcNettyCallExcutor = new RpcNettyCallExcutor();
                }
            }
        }
        return rpcNettyCallExcutor;
    }


    @Override
    public MiResult remoteCall(MiSendDTO miSendDTO) {

        recordLog(miSendDTO,null,null);

        try {
            try {
                final NettyClient nettyClient = NettyClient.getInstance();
                MiLock miLock = new MiLock(miSendDTO.getRequestId());
                MiLockStore.add(miLock);
                synchronized (miLock) {
                    nettyClient.send(miSendDTO);
                    miLock.lock(miSendDTO.getClientTimeout());
                }
            } catch (Throwable e) {
                recordLog(miSendDTO,null,e);
                throw new MiException(e.getMessage(),e);
            } finally {
                MiLockStore.del(miSendDTO.getRequestId());
            }
            MiResult miResult = MiResultStore.getAndRemove(miSendDTO.getRequestId());
            if (miResult == null) {
                recordLog(miSendDTO, MiError.MI_RESULT_IS_NULL,null);
                throw new MiException(MiError.CLIENT_TIME_OUT);
            }
            return miResult;
        }  finally {
            MiResultStore.remove(miSendDTO.getRequestId());
        }
    }


    private void recordLog(MiSendDTO miSendDTO, MiError miError, Throwable e){
        if(e==null || miError==null) {
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute  ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s",
                    miSendDTO.getRequestId(),
                    miSendDTO.getInterfaceName(),
                    miSendDTO.getGroup(),
                    miSendDTO.getVersion(),
                    miSendDTO.getMethod(),
                    miSendDTO.getServerIp(),
                    miSendDTO.getPort()));
        }
        else if(e==null || miError!=null){
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute error ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s,errorCode:%s",
                    miSendDTO.getRequestId(),
                    miSendDTO.getInterfaceName(),
                    miSendDTO.getGroup(),
                    miSendDTO.getVersion(),
                    miSendDTO.getMethod(),
                    miSendDTO.getServerIp(),
                    miSendDTO.getPort(),
                    miError.getErrorCode()));
        }else{
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute error ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s,errorCode:%s",
                    miSendDTO.getRequestId(),
                    miSendDTO.getInterfaceName(),
                    miSendDTO.getGroup(),
                    miSendDTO.getVersion(),
                    miSendDTO.getMethod(),
                    miSendDTO.getServerIp(),
                    miSendDTO.getPort(),
                    e.getMessage()),e);
        }
    }
}
