package org.ahstu.mi.rpc;

import org.ahstu.mi.common.MiResult;
import org.ahstu.mi.common.MiSendDTO;

/**
 * Created by renyueliang on 17/5/17.
 */
public interface RpcCallExcutor {
    /**
     *
     * @param miSendDTO
     * @return
     */
    public MiResult remoteCall(MiSendDTO miSendDTO);
}
