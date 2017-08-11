package org.ahstu.mi.zk.api;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;

import java.io.IOException;
import java.util.List;

/**
 * Created by xiezg@317hu.com on 2017/5/18 0018.
 */
public interface IZkClient {
    /**
     * 删除节点（路径）
     *
     * @param node
     */
    void deleteNode(String node) throws KeeperException, InterruptedException;

    /**
     * 链接zookeeper
     */
    void connect() throws IOException, InterruptedException, KeeperException;

    /**
     * 重新连接链接zookeeper
     * @throws IOException
     * @throws InterruptedException
     * @throws KeeperException
     */
    void reconnect()  throws IOException, InterruptedException, KeeperException;

    /**
     * 链接zookeeper
     *
     * @param zkHosts
     */
    void connect(String zkHosts) throws IOException, InterruptedException, KeeperException;

    /**
     * 创建 持久或者临时节点（路径）
     *
     * @param node
     * @param ephemeral
     */
    void addNode(String node, boolean ephemeral) throws KeeperException, InterruptedException;

    /**
     * 节点添加事件监听
     *
     * @param node
     * @param watcher
     * @param watcherType
     */
    void addWatcher(String node, Watcher watcher, WatcherType watcherType) throws KeeperException, InterruptedException;

    /**
     * 获取当前节点子节点
     *
     * @param node
     * @return
     */
    List<String> getNodeChildren(String node) throws KeeperException, InterruptedException;

    /**
     * 设置节点数据
     *
     * @param node
     * @param data
     * @param version
     */
    void setData(String node, byte[] data, int version) throws KeeperException, InterruptedException;

    /**
     * 获取节点数据
     *
     * @param node
     * @param version
     * @return
     */
    byte[] getData(String node, int version) throws KeeperException, InterruptedException;


    /**
     *
      * @param node
     * @param version
     * @return
     * @throws Exception
     */
    public String getDataForStr(String node, int version) throws Exception;

    /**
     *
     * @param node
     * @param data
     * @param version
     * @throws Exception
     */
    public void setDataForStr(String node, String data, int version) throws Exception;

    /**
     *
     * @param node
     * @param watcher
     * @throws KeeperException
     * @throws InterruptedException
     */
    void addExistsWatcher(String node, Watcher watcher) throws KeeperException, InterruptedException;

    /**
     *
     * @param node
     * @param watcher
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void addChildWatcher(String node, Watcher watcher) throws KeeperException, InterruptedException;

    /**
     *
     * @param node
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public boolean has(String node) throws KeeperException, InterruptedException;


    /**
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> getTreeForList(String path) throws Exception;

}
