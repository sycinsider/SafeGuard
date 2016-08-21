
package com.example.mobileguard.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * ClassName:StreamUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Date: 2016年8月5日 下午7:33:26 <br/>
 * 
 * @author dell
 * @version
 */
public class StreamUtils {
    public static void close(Closeable closeable) {
        // TODO Auto-generated method stub
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        closeable = null;
    }
}
