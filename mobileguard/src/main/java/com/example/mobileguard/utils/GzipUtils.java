
package com.example.mobileguard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import android.text.TextDirectionHeuristic;

/**
 * ClassName:GzipUtils <br/>
 * Function: 解压缩文件. <br/>
 * Date: 2016年8月12日 下午4:02:06 <br/>
 * 
 * @author dell
 * @version
 */
public class GzipUtils {

    public static void compress(String in, String out) {
        File inFile = new File(in);
        File outFile = new File(out);
        compress(inFile, outFile);
    }

    public static void compress(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            compress(fis, fos);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void compress(InputStream in, OutputStream out) {
        GZIPOutputStream gos = null;
        try {
            gos = new GZIPOutputStream(out);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1)
                gos.write(b, 0, len);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            StreamUtils.close(gos);
            StreamUtils.close(out);
            StreamUtils.close(in);
        }

    }

    public static void uncompress(String in, String out) {
        File inFile = new File(in);
        File outFile = new File(out);
        uncompress(inFile, outFile);
    }

    public static void uncompress(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            uncompress(fis, fos);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void uncompress(InputStream in, OutputStream out) {
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(in);
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = gis.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            StreamUtils.close(out);
            StreamUtils.close(gis);
            StreamUtils.close(in);

        }
    }
}
