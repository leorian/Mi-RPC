package org.ahstu.mi.zk;

import org.ahstu.mi.common.StringUtil;
import org.ahstu.mi.common.MiConstants;
import org.ahstu.mi.common.MiLogger;
import org.ahstu.mi.consumer.manager.MiPullProvider;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public class ZkChildrenWatcher implements Watcher {
    private static Watcher watcher = null;
    private static final ReentrantLock reentrantLock = new ReentrantLock();


    private ZkChildrenWatcher() {

    }

    public static final Watcher getInstance() {
        if (watcher == null) {
            try {
                reentrantLock.lock();
                synchronized (ZkChildrenWatcher.class) {
                    if (watcher == null) {
                        watcher = new ZkChildrenWatcher();
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        return watcher;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        MiLogger.record(StringUtil.format("ZkChildrenWatcher.process watchedEvent path:%s type:%s stateName:%s !",
                watchedEvent.getPath(),
                watchedEvent.getType().name(),
                watchedEvent.getState().name()
        ));

        try {
            if(watchedEvent.getType().name().equals(Event.EventType.NodeChildrenChanged.name())
                    && StringUtil.isNotBlank(watchedEvent.getPath())){
              String[] pathArr =  watchedEvent.getPath().split(MiConstants.MI_ZK_SLASH);
              if(pathArr.length==7){
                   MiPullProvider.pull(watchedEvent.getPath());
               }

            }

        } catch (Throwable e) {
            MiLogger.record(StringUtil.format("ZkChildrenWatcher.process  MiPullProvider.pull error ! path:%s errorCode:%s",
                    watchedEvent.getPath(),
                    e.getMessage()), e);
        }finally {
            if(watchedEvent.getType().name().equals(Event.EventType.NodeChildrenChanged.name()) && StringUtil.isNotBlank(watchedEvent.getPath())){
                try {
                    MiZkClient.getInstance().addChildWatcher(watchedEvent.getPath(), this);
                }catch (Throwable e1){
                    MiLogger.record(StringUtil.format("ZkChildrenWatcher.process addChildWatcher error ! path:%s errorCode:%s",
                            watchedEvent.getPath(),
                            e1.getMessage()), e1);
                }
            }
        }

    }
}
