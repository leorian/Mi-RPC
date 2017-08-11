package org.ahstu.mi.dynamic;

import org.ahstu.mi.common.MiDynamicDTO;

/**
 * Created by xiezhonggui on 2017/6/7.
 */
public interface MiDynamicCallService {

    /**
     * HTTP 转 RPC
     *
     * @param insistDynamicDTO
     * @return
     */
    Object dynamicCallMethod(MiDynamicDTO insistDynamicDTO);

    /**
     * 列举服务拥有的方法列表
     *
     * @param insistDynamicDTO
     * @return
     */
    Object listInterfaceMethod(MiDynamicDTO insistDynamicDTO);

}
