package com.mmt.record.mvp.model.mvp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    public void writeFile(Context mContext,long start,long end,  InputStream byteStream,File futureStudioIconFile){
        try {
            mContext.openFileInput(futureStudioIconFile.getPath());
            //mContext.openFileOutput()
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void writeFile(long start, long end, InputStream byteStream, File futureStudioIconFile) {
        try {

            //初始化输出流
            OutputStream outputStream = null;

            try {
                //设置每次读写的字节
                byte[] fileReader = new byte[1024*1024];
                long fileSizeDownloaded = start;
                int  fileSizeDownloadeds = 0;
                //创建输出流
                outputStream = new FileOutputStream(futureStudioIconFile);
                //进行读取操作
                // BufferedWriter bw = new BufferedWriter(body.byteStream());

                while((fileSizeDownloadeds = byteStream.read(fileReader))!=-1){
                    if (fileSizeDownloadeds>=start&&fileSizeDownloadeds<=end){
                        outputStream.write(fileReader, 0, fileSizeDownloadeds);
                    }

                }


                //刷新
                outputStream.flush();

            } catch (IOException e) {
                Log.w("", "");

            } finally {

                if (outputStream != null) {
                    //关闭输出流
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Log.w("", "");

        }
    }

    /**
     * 将图片转换成Base64编码的字符串
     */
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

}
