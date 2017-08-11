package org.ahstu.mi.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public class ZkDataWatcher implements Watcher {
    private static Watcher watcher = null;
    private static final ReentrantLock reentrantLock = new ReentrantLock();


    private ZkDataWatcher() {

    }

    public static final Watcher getInstance() {
        if (watcher == null) {
            try {
                reentrantLock.lock();
                synchronized (ZkDataWatcher.class) {
                    if (watcher == null) {
                        watcher = new ZkDataWatcher();
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

    }
}
