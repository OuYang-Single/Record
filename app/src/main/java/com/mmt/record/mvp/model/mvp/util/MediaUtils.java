package com.mmt.record.mvp.model.mvp.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProvider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.webkit.MimeTypeMap;


import com.luck.picture.lib.tools.ValueOf;
import com.mmt.record.mvp.model.entity.MediaExtraInfo;
import com.mmt.record.mvp.model.mvp.config.PictureMimeType;
import com.mmt.record.mvp.model.mvp.contract.OnCallbackListener;
import com.mmt.record.mvp.model.mvp.loader.PictureContentResolver;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by wanbo on 2017/1/18.
 */

public class MediaUtils implements SurfaceHolder.Callback {
    private static final String TAG = "MediaUtils";
    public static final int MEDIA_AUDIO = 0;
    public static final int MEDIA_VIDEO = 1;
    private Activity activity;
    private MediaRecorder mMediaRecorder;
    private CamcorderProfile profile;
    private Camera mCamera;
    private TextureView textureView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private File targetDir;
    private String targetName;
    private File targetFile;
    private int previewWidth, previewHeight;
    private int recorderType;
    private boolean isRecording;
    private GestureDetector mDetector;
    private boolean isZoomIn = false;
    private int or = 0;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    RecordManagerUtil.RecordEvent mRecordEvent;
    public MediaUtils(Activity activity, RecordManagerUtil.RecordEvent mRecordEvent) {
        this.activity = activity;
        this.mRecordEvent = mRecordEvent;
    }

    public void setRecorderType(int type) {
        this.recorderType = type;
    }

    public void setTargetDir(File file) {
        this.targetDir = file;
    }

    public void setTargetName(String name) {
        this.targetName = name;
    }

    public String getTargetFilePath() {
        return targetFile.getPath();
    }

    public boolean deleteTargetFile() {
        if (targetFile.exists()) {
            return targetFile.delete();
        } else {
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSurfaceView(SurfaceView view) {
        this.mSurfaceView = view;
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFixedSize(previewWidth, previewHeight);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
        mDetector = new GestureDetector(activity, new ZoomGestureListener());
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });
    }

//    public void setTextureView(AutoFitTextureView view) {
//        this.textureView = view;
//        initCamera();
//        mDetector = new GestureDetector(activity, new ZoomGestureListener());
//        this.textureView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mDetector.onTouchEvent(event);
//                return true;
//            }
//        });
//    }

    public int getPreviewWidth() {
        return previewWidth;
    }

    public int getPreviewHeight() {
        return previewHeight;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void record() {
        if (isRecording) {
            try {
                mMediaRecorder.stop();  // stop the recording
            } catch (RuntimeException e) {
                // RuntimeException is thrown when stop() is called immediately after start().
                // In this case the output file is not properly constructed ans should be deleted.
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
                //noinspection ResultOfMethodCallIgnored
                targetFile.delete();
            }
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder
            isRecording = false;
        } else {
            startRecordThread();
        }
    }

    private boolean prepareRecord() {
        try {

            mMediaRecorder = new MediaRecorder();
            if (recorderType == MEDIA_VIDEO) {
                mCamera.unlock();
                mMediaRecorder.setCamera(mCamera);
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mMediaRecorder.setProfile(profile);
                // 实际视屏录制后的方向
                if (cameraPosition == 0) {
                    mMediaRecorder.setOrientationHint(270);
                } else {
                    mMediaRecorder.setOrientationHint(90);
                }

            } else if (recorderType == MEDIA_AUDIO) {
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            targetFile = new File(targetDir, targetName);
            mMediaRecorder.setOutputFile(targetFile.getPath());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MediaRecorder", "Exception prepareRecord: ");
            releaseMediaRecorder();
            return false;
        }
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d("MediaRecorder", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d("MediaRecorder", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void stopRecordSave() {
        Log.d("Recorder", "stopRecordSave");
        if (isRecording) {
            isRecording = false;
            try {
                mMediaRecorder.stop();
                Log.d("Recorder", targetFile.getPath());
            } catch (RuntimeException r) {
                Log.d("Recorder", "RuntimeException: stop() is called immediately after start()");
            } finally {
                releaseMediaRecorder();
            }
        }
    }

    public void stopRecordUnSave() {
        Log.d("Recorder", "stopRecordUnSave");
        if (isRecording) {
            isRecording = false;
            try {
                mMediaRecorder.stop();
            } catch (RuntimeException r) {
                Log.d("Recorder", "RuntimeException: stop() is called immediately after start()");
                if (targetFile.exists()) {
                    //不保存直接删掉
                    targetFile.delete();
                }
            } finally {
                releaseMediaRecorder();
            }
            if (targetFile.exists()) {
                //不保存直接删掉
                targetFile.delete();
            }
        }
    }

    private void startPreView(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
        if (mCamera != null) {
            mCamera.setDisplayOrientation(or);
            try {
                mCamera.setPreviewDisplay(holder);
                Camera.Parameters parameters = mCamera.getParameters();
                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
                List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
                Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                        mSupportedPreviewSizes, 4096, 2160);
                // Use the same size for recording profile.
                previewWidth = optimalSize.width;
                previewHeight = optimalSize.height;
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
                // 这里是重点，分辨率和比特率
                // 分辨率越大视频大小越大，比特率越大视频越清晰
                // 清晰度由比特率决定，视频尺寸和像素量由分辨率决定
                // 比特率越高越清晰（前提是分辨率保持不变），分辨率越大视频尺寸越大。
                profile.videoFrameWidth = optimalSize.width;
                profile.videoFrameHeight = optimalSize.height;
                // 这样设置 1080p的视频 大小在5M , 可根据自己需求调节
           //     profile.videoBitRate = (int) (2 * previewWidth * previewHeight);
                profile.videoCodec = MediaRecorder.VideoEncoder.H264;
                profile.videoBitRate = 2 * optimalSize.width * optimalSize.height;
                List<String> supportedFocusModes = parameters.getSupportedFocusModes();
                if (supportedFocusModes != null && supportedFocusModes.size() > 0) {
                    if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                }
                mCamera.setParameters(parameters);
                mCamera.startPreview();
                if (mRecordEvent!=null){
                    mRecordEvent.onStarts();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            Log.d("Recorder", "release Recorder");
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
            Log.d("Recorder", "release Camera");
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        startPreView(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            Log.d(TAG, "surfaceDestroyed: ");
            releaseCamera();
        }
        if (mMediaRecorder != null) {
            releaseMediaRecorder();
        }
    }

    private void startRecordThread() {
        if (prepareRecord()) {
            try {
                mMediaRecorder.start();
                isRecording = true;
                Log.d("Recorder", "Start Record");
            } catch (RuntimeException r) {
                releaseMediaRecorder();
                Log.d("Recorder", "RuntimeException: start() is called immediately after stop()");
            }
        }
    }

    private class ZoomGestureListener extends GestureDetector.SimpleOnGestureListener {
        //双击手势事件
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            super.onDoubleTap(e);
            Log.d(TAG, "onDoubleTap: 双击事件");
            if (!isZoomIn) {
                setZoom(20);
                isZoomIn = true;
            } else {
                setZoom(0);
                isZoomIn = false;
            }
            return true;
        }
    }

    private void setZoom(int zoomValue) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.isZoomSupported()) {
                int maxZoom = parameters.getMaxZoom();
                if (maxZoom == 0) {
                    return;
                }
                if (zoomValue > maxZoom) {
                    zoomValue = maxZoom;
                }
                parameters.setZoom(zoomValue);
                mCamera.setParameters(parameters);
            }
        }
    }

    private String getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return bitmap2File(media.getFrameAtTime());
    }

    private String bitmap2File(Bitmap bitmap) {
        File thumbFile = new File(targetDir,
                targetName);
        if (thumbFile.exists()) thumbFile.delete();
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(thumbFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            return null;
        }
        return thumbFile.getAbsolutePath();
    }

    public void switchCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    startPreView(mSurfaceHolder);
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    mCamera.stopPreview();//停掉原来摄像头的预览
                    mCamera.release();//释放资源
                    mCamera = null;//取消原来摄像头
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    startPreView(mSurfaceHolder);
                    cameraPosition = 1;
                    break;
                }
            }
        }
    }

    /**
     * get uri
     *
     * @param id
     * @return
     */
    public static String getRealPathUri(long id, String mimeType) {
        Uri contentUri;
        if (PictureMimeType.isHasImage(mimeType)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if (PictureMimeType.isHasVideo(mimeType)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if (PictureMimeType.isHasAudio(mimeType)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        } else {
            contentUri = MediaStore.Files.getContentUri("external");
        }
        return ContentUris.withAppendedId(contentUri, id).toString();
    }

    /**
     * 获取mimeType
     *
     * @param url
     * @return
     */
    public static String getMimeTypeFromMediaUrl(String url) {
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase());
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = getMimeType(new File(url));
        }
        return TextUtils.isEmpty(mimeType) ? PictureMimeType.MIME_TYPE_JPEG : mimeType;
    }

    /**
     * 获取mimeType
     *
     * @param file
     * @return
     */
    private static String getMimeType(File file) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file.getName());
    }

    /**
     * 是否是长图
     *
     * @param width  图片宽度
     * @param height 图片高度
     * @return
     */
    public static boolean isLongImage(int width, int height) {
        if (width <= 0 || height <= 0) {
            return false;
        }
        return height > width * 3;
    }

    /**
     * 生成BucketId
     *
     * @param context         上下文
     * @param cameraFile      拍照资源文件
     * @param outPutCameraDir 自定义拍照输出目录
     * @return
     */
    public static long generateCameraBucketId(Context context, File cameraFile, String outPutCameraDir) {
        long bucketId;
        if (TextUtils.isEmpty(outPutCameraDir)) {
            bucketId = getFirstBucketId(context, cameraFile.getParent());
        } else {
            if (cameraFile.getParentFile() != null) {
                bucketId = cameraFile.getParentFile().getName().hashCode();
            } else {
                bucketId = getFirstBucketId(context, cameraFile.getParent());
            }
        }
        return bucketId;
    }


    /**
     * 生成BucketId
     *
     * @param context        上下文
     * @param cameraFile     拍照资源文件
     * @param outPutAudioDir 自定义拍照输出目录
     * @return
     */
    public static long generateSoundsBucketId(Context context, File cameraFile, String outPutAudioDir) {
        long bucketId;
        if (TextUtils.isEmpty(outPutAudioDir)) {
            bucketId = getFirstBucketId(context,cameraFile.getParent());
        } else {
            if (cameraFile.getParentFile() != null) {
                bucketId = cameraFile.getParentFile().getName().hashCode();
            } else {
                bucketId = getFirstBucketId(context,cameraFile.getParent());
            }
        }
        return bucketId;
    }

    /**
     * 创建目录名
     *
     * @param absolutePath 资源路径
     * @return
     */
    public static String generateCameraFolderName(String absolutePath) {
        String folderName;
        File cameraFile = new File(absolutePath);
        if (cameraFile.getParentFile() != null) {
            folderName = cameraFile.getParentFile().getName();
        } else {
            folderName = PictureMimeType.CAMERA;
        }
        return folderName;
    }

    /**
     * get Local image width or height
     * <p>
     *
     *
     * @param url
     * @return
     */
    @Deprecated
    public static MediaExtraInfo getImageSize( Context context,String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        InputStream inputStream = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            if (PictureMimeType.isContent(url)) {
                inputStream = PictureContentResolver.getContentResolverOpenInputStream(context, Uri.parse(url));
            } else {
                inputStream = new FileInputStream(url);
            }
            BitmapFactory.decodeStream(inputStream, null, options);
            mediaExtraInfo.setWidth(options.outWidth);
            mediaExtraInfo.setHeight(options.outHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PictureFileUtils.close(inputStream);
        }
        return mediaExtraInfo;
    }

    /**
     * get Local image width or height
     *
     * @param url
     * @return
     */
    public static MediaExtraInfo getImageSizes(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        InputStream inputStream = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            if (PictureMimeType.isContent(url)) {
                inputStream = PictureContentResolver.getContentResolverOpenInputStream(context, Uri.parse(url));
            } else {
                inputStream = new FileInputStream(url);
            }
            BitmapFactory.decodeStream(inputStream, null, options);
            mediaExtraInfo.setWidth(options.outWidth);
            mediaExtraInfo.setHeight(options.outHeight);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PictureFileUtils.close(inputStream);
        }
        return mediaExtraInfo;
    }

    /**
     * get Local video width or height
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getVideoSize(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (PictureMimeType.isContent(url)) {
                retriever.setDataSource(context, Uri.parse(url));
            } else {
                retriever.setDataSource(url);
            }
            String orientation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
            int width, height;
            if (TextUtils.equals("90", orientation) || TextUtils.equals("270", orientation)) {
                height = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                width = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            } else {
                width = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                height = ValueOf.toInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            }
            mediaExtraInfo.setWidth(width);
            mediaExtraInfo.setHeight(height);
            mediaExtraInfo.setOrientation(orientation);
            mediaExtraInfo.setDuration(ValueOf.toLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return mediaExtraInfo;
    }

    /**
     * get Local video width or height
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getAudioSize(Context context, String url) {
        MediaExtraInfo mediaExtraInfo = new MediaExtraInfo();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (PictureMimeType.isContent(url)) {
                retriever.setDataSource(context, Uri.parse(url));
            } else {
                retriever.setDataSource(url);
            }
            mediaExtraInfo.setDuration(ValueOf.toLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return mediaExtraInfo;
    }

    /**
     * 删除部分手机 拍照在DCIM也生成一张的问题
     *
     * @param id
     */
    public static void removeMedia(Context context, int id) {
        try {
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Images.Media._ID + "=?";
            cr.delete(uri, selection, new String[]{Long.toString(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取DCIM文件下最新一条拍照记录
     *
     * @return
     */
    public static int getDCIMLastImageId(Context context,String absoluteDir) {
        Cursor data = null;
        try {
            //selection: 指定查询条件
            String selection = MediaStore.Images.Media.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {"%" + absoluteDir + "%"};
            if (SdkVersionUtils.isR()) {
                Bundle queryArgs = MediaUtils.createQueryArgsBundle(selection, selectionArgs, 1, 0,MediaStore.Files.FileColumns._ID + " DESC");
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, queryArgs, null);
            } else {
                String orderBy = MediaStore.Files.FileColumns._ID + " DESC limit 1 offset 0";
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection, selectionArgs, orderBy);
            }
            if (data != null && data.getCount() > 0 && data.moveToFirst()) {
                int id = data.getInt(data.getColumnIndex(MediaStore.Images.Media._ID));
                long date = data.getLong(data.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                int duration = DateUtils.dateDiffer(date);
                // 最近时间1s以内的图片，可以判定是最新生成的重复照片
                return duration <= 1 ? id : -1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (data != null) {
                data.close();
            }
        }
    }

    /**
     * 获取最新一条拍照记录
     *
     * @return
     */
    public static long getFirstBucketId(Context context,String absoluteDir) {
        Cursor data = null;
        try {
            //selection: 指定查询条件
            String selection = MediaStore.Files.FileColumns.DATA + " like ?";
            //定义selectionArgs：
            String[] selectionArgs = {"%" + absoluteDir + "%"};
            if (SdkVersionUtils.isR()) {
                Bundle queryArgs = MediaUtils.createQueryArgsBundle(selection, selectionArgs, 1, 0,MediaStore.Files.FileColumns._ID + " DESC");
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, queryArgs, null);
            } else {
                String orderBy = MediaStore.Files.FileColumns._ID + " DESC limit 1 offset 0";
                data = context.getApplicationContext().getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection, selectionArgs, orderBy);
            }
            if (data != null && data.getCount() > 0 && data.moveToFirst()) {
                return data.getLong(data.getColumnIndex("bucket_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (data != null) {
                data.close();
            }
        }
        return -1;
    }



    public static final String QUERY_ARG_SQL_LIMIT = "android:query-arg-sql-limit";

    /**
     * R  createQueryArgsBundle
     *
     * @param selection
     * @param selectionArgs
     * @param limitCount
     * @param offset
     * @return
     */
    public static Bundle createQueryArgsBundle(String selection, String[] selectionArgs, int limitCount, int offset, String orderBy) {
        Bundle queryArgs = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, orderBy);
            if (SdkVersionUtils.isR()) {
                // queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, limitCount + " offset " + offset);
            }
        }
        return queryArgs;
    }

    /**
     * 异步获取视频缩略图地址
     *
     * @param context
     * @param url
     * @param call
     * @return
     */
    public static void getAsyncVideoThumbnail(Context context, String url, OnCallbackListener<MediaExtraInfo> call) {
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<MediaExtraInfo>() {

            @Override
            public MediaExtraInfo doInBackground() {
                return getVideoThumbnail(context, url);
            }

            @Override
            public void onSuccess(MediaExtraInfo result) {
                PictureThreadUtils.cancel(this);
                if (call != null) {
                    call.onCall(result);
                }
            }
        });
    }

    /**
     * 获取视频缩略图地址
     *
     * @param context
     * @param url
     * @return
     */
    public static MediaExtraInfo getVideoThumbnail(Context context, String url) {
        Bitmap bitmap = null;
        ByteArrayOutputStream stream = null;
        FileOutputStream fos = null;
        MediaExtraInfo extraInfo = new MediaExtraInfo();
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            if (PictureMimeType.isContent(url)) {
                mmr.setDataSource(context, Uri.parse(url));
            } else {
                mmr.setDataSource(url);
            }
            bitmap = mmr.getFrameAtTime();
            if (bitmap != null && !bitmap.isRecycled()) {
                stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                String videoThumbnailDir = PictureFileUtils.getVideoThumbnailDir(context);
                File targetFile = new File(videoThumbnailDir, DateUtils.getCreateFileName("vid_") + "_thumb.jpg");
                fos = new FileOutputStream(targetFile);
                fos.write(stream.toByteArray());
                fos.flush();
                extraInfo.setVideoThumbnail(targetFile.getAbsolutePath());
                extraInfo.setWidth(bitmap.getWidth());
                extraInfo.setHeight(bitmap.getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            PictureFileUtils.close(stream);
            PictureFileUtils.close(fos);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        return extraInfo;
    }

    /**
     * delete camera PATH
     *
     * @param context Context
     * @param path    path
     */
    public static void deleteUri(Context context, String path) {
        try {
            if (PictureMimeType.isContent(path)) {
                context.getContentResolver().delete(Uri.parse(path), null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}