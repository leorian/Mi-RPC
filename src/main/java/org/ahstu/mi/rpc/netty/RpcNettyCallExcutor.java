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
    public MiResult remoteCall(MiSendDTO insistSendDTO) {

        recordLog(insistSendDTO,null,null);

        try {
            try {
                final NettyClient nettyClient = NettyClient.getInstance();
                MiLock insistLock = new MiLock(insistSendDTO.getRequestId());
                MiLockStore.add(insistLock);
                synchronized (insistLock) {
                    nettyClient.send(insistSendDTO);
                    insistLock.lock(insistSendDTO.getClientTimeout());
                }
            } catch (Throwable e) {
                recordLog(insistSendDTO,null,e);
                throw new MiException(e.getMessage(),e);
            } finally {
                MiLockStore.del(insistSendDTO.getRequestId());
            }
            MiResult insistResult = MiResultStore.getAndRemove(insistSendDTO.getRequestId());
            if (insistResult == null) {
                recordLog(insistSendDTO, MiError.MI_RESULT_IS_NULL,null);
                throw new MiException(MiError.CLIENT_TIME_OUT);
            }
            return insistResult;
        }  finally {
            MiResultStore.remove(insistSendDTO.getRequestId());
        }
    }


    private void recordLog(MiSendDTO insistSendDTO, MiError insistError, Throwable e){
        if(e==null || insistError==null) {
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute  ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s",
                    insistSendDTO.getRequestId(),
                    insistSendDTO.getInterfaceName(),
                    insistSendDTO.getGroup(),
                    insistSendDTO.getVersion(),
                    insistSendDTO.getMethod(),
                    insistSendDTO.getServerIp(),
                    insistSendDTO.getPort()));
        }
        else if(e==null || insistError!=null){
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute error ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s,errorCode:%s",
                    insistSendDTO.getRequestId(),
                    insistSendDTO.getInterfaceName(),
                    insistSendDTO.getGroup(),
                    insistSendDTO.getVersion(),
                    insistSendDTO.getMethod(),
                    insistSendDTO.getServerIp(),
                    insistSendDTO.getPort(),
                    insistError.getErrorCode()));
        }else{
            MiLogger.record(StringUtil.format("RpcNettyCallExcutor.remoteCall excute error ! requestId:%s,serviceName:%s,group:%s,version:%s,methodName:%s,ip:%s,port:%s,errorCode:%s",
                    insistSendDTO.getRequestId(),
                    insistSendDTO.getInterfaceName(),
                    insistSendDTO.getGroup(),
                    insistSendDTO.getVersion(),
                    insistSendDTO.getMethod(),
                    insistSendDTO.getServerIp(),
                    insistSendDTO.getPort(),
                    e.getMessage()),e);
        }
    }
}
