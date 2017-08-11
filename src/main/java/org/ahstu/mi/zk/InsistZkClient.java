package org.ahstu.mi.zk;

import com.alibaba.fastjson.JSON;
import com.bozhong.common.util.StringUtil;
import org.ahstu.mi.common.*;
import org.ahstu.mi.consumer.manager.InsistPullProvider;
import org.ahstu.mi.consumer.manager.InsistPushConsumer;
import org.ahstu.mi.provider.manager.MiPushProvider;
import org.ahstu.mi.zk.api.IZkClient;
import org.ahstu.mi.zk.api.WatcherType;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public class InsistZkClient implements IZkClient {

    private static IZkClient iZkClient = null;

    private static final ReentrantLock reentrantLock = new ReentrantLock();

    private static final int SESSION_TIMEOUT = 1000;//会话延时

    private String zkHosts;

    private ZooKeeper zk = null;

    private CountDownLatch countDownLatch = new CountDownLatch(1);//同步计数器

    private InsistZkClient() {

    }

    public static final IZkClient getInstance() {
        if (iZkClient == null) {
            try {
                reentrantLock.lock();
                synchronized (InsistZkClient.class) {
                    if (iZkClient == null) {
                        iZkClient = new InsistZkClient();
                    }
                }
            } finally {
                reentrantLock.unlock();
            }
        }

        return iZkClient;
    }

    @Override
    public void deleteNode(String node) throws KeeperException, InterruptedException {
        MiUtil.firstAddChar(node);
        if (has(node)) {
            zk.delete(node, -1);
        }
    }

    @Override
    public void connect() throws IOException, InterruptedException, KeeperException {
        String zkHosts = HttpUtil.loadZkHosts();
        if (StringUtil.isNotBlank(zkHosts)) {
            this.zkHosts = zkHosts;
        }
      //  this.zkHosts="127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        connect(this.zkHosts);
    }

    @Override
    public void reconnect() throws IOException, InterruptedException, KeeperException {
        connect();
        InsistPullProvider.pullAll();
        MiPushProvider.pushAll();
        InsistPushConsumer.pushAll();
    }

    @Override
    public void connect(String zkHosts) throws IOException, InterruptedException, KeeperException {
        if (StringUtil.isBlank(zkHosts)) {
            throw new MiException(MiError.INSIST_ZK_HOST_ISNULL);
        }

        MiLogger.record("********** InsistZkClient.connect start zkHosts:" + zkHosts + " *****************");

        this.zkHosts = zkHosts;
        MiLogger.getSysLogger().warn(this.getClass().getSimpleName() + " connect is start ! ");
        zk = new ZooKeeper(zkHosts, SESSION_TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                MiLogger.getSysLogger().warn(this.getClass().getSimpleName() + " - process execute ! "
                        + JSON.toJSONString(watchedEvent));
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected
                        && watchedEvent.getType() == Event.EventType.None) {
                    countDownLatch.countDown();
                    countDownLatch = new CountDownLatch(1);
                    ZkConnectionWatcher.unlock();
                    MiLogger.getSysLogger().warn(this.getClass().getSimpleName() +
                            " - countDownLatch.countDown execute ! ");
                }
            }
        });

        MiLogger.getSysLogger().warn(this.getClass().getSimpleName() + " - connect await in ! ");
        countDownLatch.await();
        MiLogger.getSysLogger().warn(this.getClass().getSimpleName() + "- connect await out ! "
                + InsistZkClient.class.getSimpleName() + " connect zk is success ! ");

        createPersistent(MiConstants.INSIST_ZK_SLASH + MiConstants.INSIST_ROOT_PATH);
        addExistsWatcher(MiConstants.INSIST_ZK_SLASH + MiConstants.INSIST_ROOT_PATH, ZkConnectionWatcher.getInstance());
    }

    @Override
    public void addNode(String node, boolean ephemeral) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        if (ephemeral) {
            createEphemeral(node);
        } else {
            createPersistent(node);
        }
    }

    @Override
    public void addWatcher(String node, Watcher watcher, WatcherType watcherType) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        if (WatcherType.EXIST.name().equals(watcherType.name())) {
            addExistsWatcher(node, watcher);
        } else if (WatcherType.CHILDREN.name().equals(watcherType.name())) {
            addChildrenChangeWatcher(node, watcher);
        } else if (WatcherType.DATA.name().equals(watcherType.name())) {
            addDataChangeWathcer(node, watcher);
        }
    }

    @Override
    public List<String> getNodeChildren(String node) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        Stat stat = zk.exists(node, false);
        if (stat == null) {
            return new ArrayList<String>();
        }
        List<String> children = zk.getChildren(node, false);

        return children;
    }

    @Override
    public void setData(String node, byte[] data, int version) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        zk.setData(node, data, version);
    }

    @Override
    public byte[] getData(String node, int version) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        Stat stat = new Stat();
        stat.setAversion(version);
        return zk.getData(node, ZkDataWatcher.getInstance(), stat);
    }


    public String getDataForStr(String node, int version) throws Exception {
        byte[] bytes = getData(node, version);

        if (bytes == null) {
            return "";
        }

        return new String(bytes, "utf-8").toString();

    }

    public void setDataForStr(String node, String data, int version) throws Exception {

        node = MiUtil.firstAddChar(node);

        if (StringUtil.isBlank(data)) {
            return;
        }

        byte[] bytes = data.getBytes("utf-8");

        setData(node, bytes, version);

    }

    public boolean has(String node) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        return zk.exists(node, false) != null;
    }

    private void addChildrenChangeWatcher(String node, Watcher watcher) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        zk.getChildren(node, watcher);
    }

    public void addExistsWatcher(String node, Watcher watcher) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        zk.exists(node, watcher);
    }

    private void addDataChangeWathcer(String node, Watcher watcher) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        Stat stat = new Stat();
        stat.setAversion(-1);
        zk.getData(node, watcher, stat);
    }

    public void addChildWatcher(String node, Watcher watcher) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        zk.getChildren(node, watcher);
    }

    private void createPersistent(String node) throws KeeperException, InterruptedException {
        create(node, CreateMode.PERSISTENT);
    }

    private void createPersistentSequential(String node) throws KeeperException, InterruptedException {
        create(node, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private void createEphemeral(String node) throws KeeperException, InterruptedException {
        create(node, CreateMode.EPHEMERAL);
    }

    private void createEphemeralSequential(String node) throws KeeperException, InterruptedException {
        create(node, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private void create(String node, CreateMode createMode) throws KeeperException, InterruptedException {
        node = MiUtil.firstAddChar(node);
        Stat stat = zk.exists(node, true);
        String createNode = node;
        if (stat == null) {
            createNode = zk.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            MiLogger.getSysLogger().warn(this.getClass().getSimpleName() +
                    " register server created : " + createNode);
        } else {
            MiLogger.getSysLogger().warn(this.getClass().getSimpleName() +
                    " register server exits : " + createNode);
        }
        Stat stat1 = new Stat();
        stat1.setAversion(-1);

        zk.getData(createNode, ZkDataWatcher.getInstance(), stat);
    }


    public List<String> getTreeForList(String path) throws Exception {
        path = MiUtil.firstAddChar(path);
        Stat stat = zk.exists(path, false);
        if (stat == null) {
            return new ArrayList<String>();
        }
        List<String> dealList = new ArrayList<String>();
        dealList.add(path);
        int index = 0;
        while (index < dealList.size()) {
            String tempPath = dealList.get(index);
            if (!has(tempPath)) {
                continue;
            }
            try {
                List<String> children = zk.getChildren(tempPath, false);
                if (tempPath.equalsIgnoreCase("/") == false) {
                    tempPath = tempPath + "/";
                }
                Collections.sort(children);
                for (int i = children.size() - 1; i >= 0; i--) {
                    dealList.add(index + 1, tempPath + children.get(i));
                }
            } catch (Throwable e) {
                MiLogger.record("InsistZkClient.getTreeForList error ! path:"+path+ " tempPath:"+tempPath,e);
            }
            index++;
        }
        return dealList;
    }


}
