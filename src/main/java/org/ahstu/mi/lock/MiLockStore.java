package org.ahstu.mi.lock;

import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.MiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiLockStore {

  private static Map<String,MiLock>  insistLockStore =new HashMap<String, MiLock>();

  public static void add(MiLock insistLock){

      if(insistLock==null || insistLock.getLockId()==null || insistLock.getLockId().isEmpty()
             ){

          throw new MiException(MiError.INSIST_ADDLOCK_ISNULL);
      }

      if(insistLockStore.get(insistLock.getLockId())!=null){
          throw new MiException(MiError.INSIST_ADDLOCK_EXIST);
      }

      insistLockStore.put(insistLock.getLockId(),insistLock);
  }


  public static MiLock get(String lockId){
      return insistLockStore.get(lockId);
  }

  public static void del(String lockId){
      insistLockStore.remove(lockId);
  }




}
