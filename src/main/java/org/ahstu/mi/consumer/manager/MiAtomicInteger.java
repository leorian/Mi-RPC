package org.ahstu.mi.consumer.manager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by renyueliang on 17/5/16.
 */
public class MiAtomicInteger extends AtomicInteger {


    public MiAtomicInteger(int l){
        super(l);
    }

    public int addAndGetNew(int l){
        int result = 0;
        boolean flag=false;

        do {
            result = this.get();
            flag = this.compareAndSet(result, result + 1);
        } while (!flag);

        return  result;
    }



    public int decrementAndGetNew(int l){
        int result = 0;
        boolean flag=false;

        do {
            result = this.get();
            flag = this.compareAndSet(result, result - l);
        } while (!flag);

        return result ;
    }

}
