package lhy.lhylibrary.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.File;
import java.math.BigDecimal;


public class FileUtils {

    private FileUtils() {
    }

    public static boolean isSDCardEnabled() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取可用空间
     */
    public static String getAvailSpace(Context context, String path) {
        StatFs stat = new StatFs(path);

        long availableBlocks = stat.getAvailableBlocks();// 获取可用存储块数量
        long blockSize = stat.getBlockSize();// 每个存储块的大小

        // 可用存储空间 Integer.MAX_VALUE 可以表示2G大小, 2G太少, 需要用Long
        long availSize = availableBlocks * blockSize;
        return Formatter.formatFileSize(context, availSize);// 将字节转为带有相应单位(MB,G)的字符串
    }

    /**
     * SD卡可用，并且空间够，缓存放在SD卡，SK卡不可用就放在手机内存
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getCacheFile(Context context, String fileName) {
        File file = null;
        if (isSDCardEnabled()) {
            file = new File(context.getExternalCacheDir(), fileName);
        } else {
            file = new File(context.getCacheDir(), fileName);
        }
        return file;
    }

    /**
     * 新建外部缓存文件
     *
     * @param context
     * @param fileName 文件名
     * @return
     */
    public static File getExtraCacheFile(Context context, String fileName) {
        File file = null;
        if (isSDCardEnabled()) {
            file = new File(context.getExternalCacheDir(), fileName);
            if (!file.exists()) {
                //如果不存在就创建文件JIA,防止获取路径时发生异常
                file.mkdir();
            }
        }
        return file;
    }

    /**
     * 新建内部缓存文件
     *
     * @param context
     * @param fileName 文件名
     * @return
     */
    public static File getInternalCacheFile(Context context, String fileName) {
        File file = new File(context.getCacheDir(), fileName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }


    /**
     * 拍照保存图片的文件夹,拍照属于非本应用，所以访问不了本应用的data/data目录
     * 只能保存在SD卡中
     * 得到文件夹 path为文件夹的名称
     */

    public static String getDir(Context context, String path) {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(state)) {
            File dir = new File(Environment.getExternalStorageDirectory(), "Android/data/"
                    + context.getPackageName() + "/" + path);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        } else {
            File dir = new File(context.getFilesDir(), path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir.getAbsolutePath();
        }
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            //不到1KB返回0
            return 0 + "KB";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

}
