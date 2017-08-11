package org.ahstu.mi.serialize;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by xiezg@317hu.com on 2017/8/11 0011.
 */
public class HessianSerializerTool {
    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T t) {
        if (t == null) {
            throw new NullPointerException();
        } else {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                HessianOutput ho = new HessianOutput(os);
                ho.writeObject(t);
                return os.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            throw new NullPointerException();
        } else {
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            HessianInput hessianInput = new HessianInput(is);
            try {
                return (T) hessianInput.readObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
