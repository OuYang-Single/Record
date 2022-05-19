package com.mmt.record.greendao;

import android.content.Context;

public class ManagerFactory {
    /**
     * 每一个BeanManager都管理着数据库中的一个表，我将这些管理者在ManagerFactory中进行统一管理
     */
    UserManager mUserManager;


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
}

