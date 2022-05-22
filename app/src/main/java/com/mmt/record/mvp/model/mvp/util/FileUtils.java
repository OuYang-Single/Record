package com.mmt.record.mvp.model.mvp.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.mmt.record.mvp.model.entity.FileEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import timber.log.Timber;

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
     *
     */
    public static String stringToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            result = Base64.encodeToString(path.getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        ZipFiles(file.getParent()+ File.separator, file.getName(), outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
    }


    public static void ZipFolder(List<FileEntity> fileEntities, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        isFile=false;
        //创建文件

        //压缩
        ZipFile(fileEntities, outZip);
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {

        if (zipOutputSteam == null)
            return;

        File file = new File(folderString +"/"+ fileString);

        Timber.i("文件:"+file.getName()+"------文件是否存在："+file.isFile());
        ZipEntry zipEntry = new ZipEntry(fileString);
        FileInputStream inputStream = new FileInputStream(file);
        zipOutputSteam.putNextEntry(zipEntry);
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            zipOutputSteam.write(buffer, 0, len);
        }
        zipOutputSteam.closeEntry();
    }

    /**
     * 压缩文件
     *
     * @param fileEntities
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFile( List<FileEntity> fileEntities,ZipOutputStream zipOutputSteam) throws Exception {

        if (zipOutputSteam == null)
            return;
        for ( FileEntity fileEntity:fileEntities) {
            ZipFile(fileEntity.getFolderEntity().getFolderPath()+"/",fileEntity.getFilePath(), zipOutputSteam);
        }
    }
  public   static boolean isFile=false;
    private static void ZipFile(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {

        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);

        Timber.i("文件:"+file.getName()+"------文件是否存在："+file.isFile());
        if (!file.isFile()){
            return;
        }
        isFile=true;
        ZipEntry zipEntry = new ZipEntry(fileString);
        FileInputStream inputStream = new FileInputStream(file);
        zipOutputSteam.putNextEntry(zipEntry);
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            zipOutputSteam.write(buffer, 0, len);
        }
        zipOutputSteam.closeEntry();
    }
    /**
     * 获取设备号
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId;

        if (Build.VERSION.SDK_INT >= 29) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return "";
                }
            }
            assert mTelephony != null;
            if (mTelephony.getDeviceId() != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = mTelephony.getImei();
                } else {
                    deviceId = mTelephony.getDeviceId();
                }
            } else {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        return deviceId;
    }

}
