package org.ahstu.mi.dynamic;

import org.ahstu.mi.common.MiDynamicDTO;

/**
 * Created by xiezhonggui on 2017/6/7.
 */
public interface MiDynamicCallService {

    /**
     * HTTP 转 RPC
     *
     * @param miDynamicDTO
     * @return
     */
    Object dynamicCallMethod(MiDynamicDTO miDynamicDTO);

    /**
     * 列举服务拥有的方法列表
     *
     * @param miDynamicDTO
     * @return
     */
    Object listInterfaceMethod(MiDynamicDTO miDynamicDTO);

}
