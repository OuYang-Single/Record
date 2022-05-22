package com.mmt.record.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.mmt.record.greendao.DaoSession;
import com.mmt.record.greendao.FolderEntityDao;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.GpsEntityDao;

@Entity
public class GpsEntity {
    @Id
    private  Long id;
    private String device_id;
    private  double longitude;
    private double latitude;
    private float speed;
    private float heading;
    private long gps_time;
    private boolean upload;
    private Long fileEntityId;
    private Long folderEntityId;
    @ToOne(joinProperty = "fileEntityId")
    private FileEntity mFileEntity;
    @ToOne(joinProperty = "folderEntityId")
    private FolderEntity folderEntity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1201113516)
    private transient GpsEntityDao myDao;
    @Generated(hash = 1920535660)
    public GpsEntity(Long id, String device_id, double longitude, double latitude,
            float speed, float heading, long gps_time, boolean upload,
            Long fileEntityId, Long folderEntityId) {
        this.id = id;
        this.device_id = device_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.speed = speed;
        this.heading = heading;
        this.gps_time = gps_time;
        this.upload = upload;
        this.fileEntityId = fileEntityId;
        this.folderEntityId = folderEntityId;
    }
    @Generated(hash = 1068679422)
    public GpsEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDevice_id() {
        return this.device_id;
    }
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public float getSpeed() {
        return this.speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public float getHeading() {
        return this.heading;
    }
    public void setHeading(float heading) {
        this.heading = heading;
    }
    public long getGps_time() {
        return this.gps_time;
    }
    public void setGps_time(long gps_time) {
        this.gps_time = gps_time;
    }
    public boolean getUpload() {
        return this.upload;
    }
    public void setUpload(boolean upload) {
        this.upload = upload;
    }
    public Long getFileEntityId() {
        return this.fileEntityId;
    }
    public void setFileEntityId(Long fileEntityId) {
        this.fileEntityId = fileEntityId;
    }
    public Long getFolderEntityId() {
        return this.folderEntityId;
    }
    public void setFolderEntityId(Long folderEntityId) {
        this.folderEntityId = folderEntityId;
    }
    @Generated(hash = 1239190016)
    private transient Long mFileEntity__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 985688440)
    public FileEntity getMFileEntity() {
        Long __key = this.fileEntityId;
        if (mFileEntity__resolvedKey == null
                || !mFileEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileEntityDao targetDao = daoSession.getFileEntityDao();
            FileEntity mFileEntityNew = targetDao.load(__key);
            synchronized (this) {
                mFileEntity = mFileEntityNew;
                mFileEntity__resolvedKey = __key;
            }
        }
        return mFileEntity;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 836345558)
    public void setMFileEntity(FileEntity mFileEntity) {
        synchronized (this) {
            this.mFileEntity = mFileEntity;
            fileEntityId = mFileEntity == null ? null : mFileEntity.getId();
            mFileEntity__resolvedKey = fileEntityId;
        }
    }
    @Generated(hash = 2020532681)
    private transient Long folderEntity__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 259996788)
    public FolderEntity getFolderEntity() {
        Long __key = this.folderEntityId;
        if (folderEntity__resolvedKey == null
                || !folderEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FolderEntityDao targetDao = daoSession.getFolderEntityDao();
            FolderEntity folderEntityNew = targetDao.load(__key);
            synchronized (this) {
                folderEntity = folderEntityNew;
                folderEntity__resolvedKey = __key;
            }
        }
        return folderEntity;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 263277269)
    public void setFolderEntity(FolderEntity folderEntity) {
        synchronized (this) {
            this.folderEntity = folderEntity;
            folderEntityId = folderEntity == null ? null : folderEntity.getId();
            folderEntity__resolvedKey = folderEntityId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 422749075)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGpsEntityDao() : null;
    }

}
