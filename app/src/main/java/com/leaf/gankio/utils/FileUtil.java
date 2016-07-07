package com.leaf.gankio.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import okhttp3.ResponseBody;

/**
 * @author :  Leaf<br>
 * @date ： 2016/6/19 22:38
 * @TODO ： 下载保存到SD卡
 */

public class FileUtil {

    /**
     * 保存到sd卡
     *
     * @param context
     * @param body
     * @param fileName
     * @return
     */
    public static Uri writeResponseBodyToDisk(Context context, ResponseBody body, String fileName) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.OwnCacheDirectory + "MeiZhi" + File.separator + fileName);
            if (!futureStudioIconFile.exists()) {
                File dir = futureStudioIconFile.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            Log.d("FileUtil", "-----------writeResponseBodyToDisk path:" + futureStudioIconFile.getAbsolutePath());
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("FileUtil", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                Uri uri = Uri.fromFile(futureStudioIconFile);
                // 通知图库更新
                Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                context.sendBroadcast(scannerIntent);
                /*MediaScannerConnection.scanFile(context, new String[] { path }, null,
                        null);*/
                return uri;
            } catch (IOException e) {
                Log.d("FileUtil", "-----------writeResponseBodyToDisk Exception:" + e);
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.d("FileUtil", "-----------writeResponseBodyToDisk Exception :" + e);
            return null;
        }
    }

    /**
     * 获取文件
     *
     * @param file
     * @return //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
     * //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化大小
     *
     * @param size
     * @return
     */
    public static String getFormatFolderSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 删除文件
     * @param cacheFile
     */
    public static void deleteFolderFile(File cacheFile) {

        if (cacheFile.isFile()) {
            cacheFile.delete();
        } else if (cacheFile.isDirectory()) {
            File[] childFiles = cacheFile.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                deleteFolderFile(childFiles[i]);
            }
        }
    }

}
