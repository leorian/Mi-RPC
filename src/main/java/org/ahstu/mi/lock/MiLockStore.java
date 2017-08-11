package org.ahstu.mi.lock;

import org.ahstu.mi.common.MiError;
import org.ahstu.mi.common.MiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renyueliang on 17/5/15.
 */
public class MiLockStore {

  private static Map<String,MiLock>  miLockStore =new HashMap<String, MiLock>();

  public static void add(MiLock miLock){

      if(miLock==null || miLock.getLockId()==null || miLock.getLockId().isEmpty()
             ){

          throw new MiException(MiError.MI_ADDLOCK_ISNULL);
      }

      if(miLockStore.get(miLock.getLockId())!=null){
          throw new MiException(MiError.MI_ADDLOCK_EXIST);
      }

      miLockStore.put(miLock.getLockId(),miLock);
  }


  public static MiLock get(String lockId){
      return miLockStore.get(lockId);
  }

  public static void del(String lockId){
      miLockStore.remove(lockId);
  }




}
