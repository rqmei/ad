package com.ad.tibi.lib.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import java.io.File;

/**
 * 打开文件
 */
public class FileUtil {
    /**
     * 打开文件
     * @param mContext
     * @param filePath 文件地址
     */
    public static void openFile1(Context mContext,String filePath) {
        try {
            Intent intent = new Intent();
            // 这是比较流氓的方法，绕过7.0的文件权限检查
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }
            File file = new File(filePath);
            // 声明需要的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // 动作，查看
            intent.setAction(Intent.ACTION_VIEW);
            // 设置类型
            intent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
            mContext.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(mContext,"请安装相关插件!",Toast.LENGTH_SHORT).show();
        }
    }

    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
                || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("doc") || end.equals("docx")) {
            type = "application/msword";
        } else if (end.equals("ppt") || end.equals("pot") || end.equals("pps")
                || end.equals("pptx")) {
            type = "application/vnd.ms-powerpoint";
        } else if (end.equals("xla") || end.equals("xlc") || end.equals("xlm")
                || end.equals("xls") || end.equals("xlt") || end.equals("xlw")
                || end.equalsIgnoreCase("xlsx")) {
            type = "application/vnd.ms-excel";
        } else if (end.equals("xll")) {
            type = "application/x-excel";
        } else if (end.equals("pdf")) {
            type = "application/pdf";
        } else if (end.equals("zip")) {
            type = "application/zip";
        } else if (end.equals("rar")) {
            type = "application/x-rar-compressed";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "/*";
        }
        return type;
    }
}
