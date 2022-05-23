package com.mmt.record.mvp.model.mvp.util;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.view.SurfaceView;


import com.deep.dpwork.util.CountDownTimeTextUtil;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.FolderEntityDao;
import com.mmt.record.greendao.ManagerFactory;
import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.FolderEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import timber.log.Timber;

public class RecordManagerUtil {

    private MediaUtils mediaUtils;

    private long startTime;

    private Timer timer;

    public boolean isRecording;

    private File parentFile;
    private String targetName;
    private RecordEvent mRecordEvent;

    Activity activity;
    private static RecordManagerUtil recordManagerUtil;

    public static RecordManagerUtil getInstance() {
        if (recordManagerUtil == null) {
            recordManagerUtil = new RecordManagerUtil();
        }
        return recordManagerUtil;
    }


    public void init(Activity activity, SurfaceView surfaceView,RecordEvent mRecordEvent) {
        this.activity=activity;
        this.mRecordEvent=mRecordEvent;
        mediaUtils = new MediaUtils(activity);

        mediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);

        initDate();

        mediaUtils.setTargetDir(parentFile);

        mediaUtils.setSurfaceView(surfaceView);

        mediaUtils.setTargetName(targetName);
    }

    private String getTimeString(long timeLong) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(timeLong);
        return format.format(d1);
    }

    private void initDate() {
        long i= (long) (MediaUtil.INSTANCE. getSDAllSize()*0.05)/1024;
        long remaining= MediaUtil.INSTANCE. getSDFreeSize()/1024;
        if (remaining-i<=400){
            deleteFile();
        }
        // 文件路径
        File pathFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        String dateTime = getTimeString(System.currentTimeMillis());
        parentFile = new File(pathFile.getPath() + "/normal_storage/" + dateTime);
        if (parentFile.mkdirs()) {
            Timber.i("创建目录:" + parentFile.getPath());
        } else {
            Timber.i("创建目录:" + parentFile.getPath() + " 失败");
        }
      if (ManagerFactory.getInstance().getFolderEntityManager(activity).queryBuilder().where(FolderEntityDao.Properties.FolderPath.eq(parentFile.getPath())).build().list().size()<=0){
          ManagerFactory.getInstance().getFolderEntityManager(activity).save(new FolderEntity(parentFile.getPath(),System.currentTimeMillis()));
        }
        FolderEntity folderEntity=     ManagerFactory.getInstance().getFolderEntityManager(activity).queryBuilder().where(FolderEntityDao.Properties.FolderPath.eq(parentFile.getPath())).build().unique();
        targetName= "Dashcam001-" + nowTime().replace(':', '.').replace(' ', '_') + "-" + System.currentTimeMillis() + ".mp4";
        Timber.i("创建文件:" + targetName);
        ManagerFactory.getInstance().getFileEntityManager(activity).save(new FileEntity(targetName,System.currentTimeMillis(),  folderEntity.getId(),folderEntity.getId()));

       /* String dateOldTime = getTimeString(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7);
        File parentOldFile = new File( + "/" + dateOldTime);
        Timber.i("一个星期前时间文件夹:" + parentOldFile.getPath());
        if (parentOldFile.exists()) {
            if (deleteDirectory(parentOldFile.getPath())) {
                Timber.i("删除一个星期前的文件夹");
            } else {
                Timber.i("删除一个星期前的文件夹失败");
            }
        } else {
            Timber.i("不存在一个星期前的文件夹");
        }*/
    }
public void deleteFile(){
    File deleteFile=null;
    List<FolderEntity> folderEntityList= ManagerFactory.getInstance().getFolderEntityManager(activity).queryBuilder().orderAsc(FolderEntityDao.Properties.AddTime).list();
    List<FileEntity> fileEntities=null;
    if (folderEntityList.size()>0){
        fileEntities=    ManagerFactory.getInstance().getFileEntityManager(activity).queryBuilder().where(FileEntityDao.Properties.FolderEntityId.eq(folderEntityList.get(0).getId())).orderAsc(FileEntityDao.Properties.AddTime).list();
        if (fileEntities.size()>0){
            deleteFile= new File(folderEntityList.get(0).getFolderPath()+"/"+fileEntities.get(0).getFilePath());
        }
    }

    if (deleteFile!=null){

        ManagerFactory.getInstance().getFileEntityManager(activity).deleteByKey(fileEntities.get(0).getId());
        if (fileEntities.size()==1){
            ManagerFactory.getInstance().getFolderEntityManager(activity).delete(folderEntityList.get(0));
        }

        if (!deleteFile.delete()){
            deleteFile();
        }
    }
}
    public void startRecord(int minute) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        startTime = System.currentTimeMillis();
        mediaUtils.record();
        isRecording = mediaUtils.isRecording();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Timber.i("自动循环录制...");

                mediaUtils.stopRecordSave();
               if (mRecordEvent!=null){
                   Timber.i("11文件:"+parentFile+"/"+targetName+"11------文件是否存在："+new File(parentFile+"/"+targetName).isFile());
                   mRecordEvent.onComplete(parentFile,targetName);
               }
                Timber.i("自动循环录制保存成功");

                initDate();

                mediaUtils.setTargetDir(parentFile);

                mediaUtils.setTargetName(targetName);

                mediaUtils.record();

                Timber.i("自动循环录制重新开始");
            }
        }, minute * 1000 * 60, minute * 1000 * 60);
    }

    public void stopRecord() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        mediaUtils.stopRecordSave();
        isRecording = mediaUtils.isRecording();
        startTime = 0;
        if (mRecordEvent!=null){
            mRecordEvent.onStop(parentFile,targetName);
        }
    }
    public static String nowTime() {
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MMdd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }
    /**
     * 删除单个文件
     *
     * @param fileName：要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir：要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }
    public   interface  RecordEvent{
        void onComplete(File parentFile,String targetName);
        void onStop(File parentFile,String targetName);
    }
}
