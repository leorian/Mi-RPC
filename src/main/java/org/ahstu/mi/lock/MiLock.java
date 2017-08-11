package org.ahstu.mi.lock;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiLock {

    private String lockId;

    private boolean isLock;


    public MiLock(String lockId) {
        this.lockId = lockId;
    }

    public boolean isLock() {
        return isLock;
    }

    public void lock() throws InterruptedException{
        this.isLock=true;
        this.wait();
    }

    public void lock(long timeOut) throws InterruptedException{
        this.isLock=true;
        this.wait(timeOut);
    }

    public void unlock(){
        this.notifyAll();
        this.isLock=false;
    }

    public String getLockId() {
        return lockId;
    }
}
