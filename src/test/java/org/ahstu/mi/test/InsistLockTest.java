package org.ahstu.mi.test;

import org.ahstu.mi.lock.MiLock;

import java.util.UUID;

/**
 * Created by renyueliang on 17/5/23.
 */
public class InsistLockTest {

    public static void main(String[] args) throws Throwable{

       final MiLock insistLock = new MiLock(UUID.randomUUID().toString());

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (insistLock){

                    try {
                        System.out.println(" test start lock ");
                        insistLock.lock();

                        System.out.println(" test end lock ");
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }


            }
        });
        thread.start();

        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {

                synchronized (insistLock){

                    try {
                        Thread.sleep(3000l);

                        System.out.println(" test start unlock ");
                        insistLock.unlock();

                        System.out.println(" test end unlock ");
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }


            }
        });
        thread1.start();

        System.in.read();

    }



}
