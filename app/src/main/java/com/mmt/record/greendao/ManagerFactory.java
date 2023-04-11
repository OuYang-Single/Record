package com.mmt.record.greendao;

import android.content.Context;

import com.mmt.record.mvp.model.entity.GpsEntity;

public class ManagerFactory {
    /**
     * 每一个BeanManager都管理着数据库中的一个表，我将这些管理者在ManagerFactory中进行统一管理
     */
    UserManager mUserManager;
    FileEntityManager mFileEntityManager;
    FolderEntityManager mFolderEntityManager;
    GpsEntityManager mGpsEntityManager;
    FolderBeanManager mFolderBeanManager;
    FileBeanManager mFileBeanManager;


    private static ManagerFactory mInstance = null;

        /**
         * 获取DaoFactory的实例
         *
         * @return
         */
        public static ManagerFactory getInstance() {
            if (mInstance == null) {
                synchronized (ManagerFactory.class) {
                    if (mInstance == null) {
                        mInstance = new ManagerFactory();
                    }
                }
            }
            return mInstance;
        }

    public synchronized UserManager getStudentManager(Context context) {
            if (mUserManager == null){
                mUserManager = new UserManager(DaoManager.getInstance(context).getDaoSession().getUserDao());
            }
        return mUserManager;
    }
    public synchronized FileEntityManager getFileEntityManager(Context context) {
        if (mFileEntityManager == null){
            mFileEntityManager = new FileEntityManager(DaoManager.getInstance(context).getDaoSession().getFileEntityDao());
        }
        return mFileEntityManager;
    }

    public synchronized FolderEntityManager getFolderEntityManager(Context context) {
        if (mFolderEntityManager == null){
            mFolderEntityManager = new FolderEntityManager(DaoManager.getInstance(context).getDaoSession().getFolderEntityDao());
        }
        return mFolderEntityManager;
    }
    public synchronized GpsEntityManager getGpsEntityManager(Context context) {
        if (mGpsEntityManager == null){
            mGpsEntityManager = new GpsEntityManager(DaoManager.getInstance(context).getDaoSession().getGpsEntityDao());
        }
        return mGpsEntityManager;
    }
    public synchronized FolderBeanManager getFolderBeanManager(Context context) {
        if (mFolderBeanManager == null){
            mFolderBeanManager = new FolderBeanManager(DaoManager.getInstance(context).getDaoSession().getFolderBeanDao());
        }
        return mFolderBeanManager;
    }

    public synchronized FileBeanManager getFileBeanManager(Context context) {
        if (mFileBeanManager == null){
            mFileBeanManager = new FileBeanManager(DaoManager.getInstance(context).getDaoSession().getFileBeanDao());
        }
        return mFileBeanManager;
    }
}

