package org.ahstu.mi.zk.api;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public enum WatcherType {
    EXIST,/*节点存在事件*/
    DATA, /*节点数据改变事件*/
    CHILDREN/*节点子节点改动事件*/
}
