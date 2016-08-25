package com.example.mobileguard.utils;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by dell on 2016/8/25.
 */
public class Md5Utils {
    // 获取字符的MD5
    public static String encode(String in) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(in.getBytes());

            for (byte b : digest) {
                int c = b & 0xff;
                String s = Integer.toHexString(c);
                if (s.length() == 1) {
                    s = "0" + s;
                }
                result += s;
            }

        } catch (Exception cnse) {
        }
        return result;
    }

    // 获取文件的MD5
    public static String encode(InputStream in) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = new byte[8192];
            int len;
            while ((len = in.read(bytes)) > 0) {
                md.update(bytes, 0, len);
            }
            byte[] digest = md.digest();

            for (byte b : digest) {
                int c = b & 0xff;
                String s = Integer.toHexString(c);
                if (s.length() == 1) {
                    s = "0" + s;
                }
                result += s;
            }
        } catch (Exception cnse) {
        }
        return result;
    }
}
