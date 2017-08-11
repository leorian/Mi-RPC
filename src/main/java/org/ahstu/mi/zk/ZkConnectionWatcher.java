package org.ahstu.mi.zk;

import org.ahstu.mi.common.StringUtil;
import org.ahstu.mi.common.MiLogger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public class ZkConnectionWatcher implements Watcher {
    private static Watcher watcher;
    private static volatile boolean connectSuccess = true;
    private static volatile boolean isLock = false;

    public static void unlock() {
        isLock = false;
    }

    private ZkConnectionWatcher() {

    }

    public static final Watcher getInstance() {
        if (watcher == null) {
            try {
                synchronized (ZkConnectionWatcher.class) {
                    if (watcher == null) {
                        watcher = new ZkConnectionWatcher();
                    }

                }
            } finally {
            }
        }
        return watcher;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        MiLogger.record(StringUtil.format("ZkConnectionWatcher.process watchedEvent path:%s type:%s stateName:%s !",
                watchedEvent.getPath(),
                watchedEvent.getType().name(),
                watchedEvent.getState().name()
        ));
        if (Event.KeeperState.Disconnected.name().equals(watchedEvent.getState().name())) {
            if (isLock) {
                MiLogger.record("ZkConnectionWatcher.process Disconnected reconnect isLock is true !");
                return;
            }

            connectSuccess = false;
            try {
                synchronized (this.getClass()) {
                    isLock = true;
                    if (connectSuccess) {
                        return;
                    }
                    MiZkClient.getInstance().reconnect();
                    connectSuccess = true;
                }
                MiLogger.record("ZkConnectionWatcher.process Disconnected reconnect success !");
            } catch (Throwable e) {
                MiLogger.getSysLogger().error(e.getMessage(), e);
            }finally {
                unlock();
            }
        } else if (Event.KeeperState.Expired.name().equals(watchedEvent.getState().name())) {
            try {
                MiZkClient.getInstance().reconnect();
                MiLogger.record("ZkConnectionWatcher.process Expired reconnect success !");
            } catch (Throwable e) {
                MiLogger.getSysLogger().error(e.getMessage(), e);
            }
        } else if (Event.KeeperState.SyncConnected.name().equals(watchedEvent.getState().name())) {
            MiLogger.record("ZkConnectionWatcher.process SyncConnected reconnect success !");
        }
    }
}
