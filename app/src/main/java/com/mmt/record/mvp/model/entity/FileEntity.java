package com.mmt.record.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import com.mmt.record.greendao.DaoSession;
import com.mmt.record.greendao.FolderEntityDao;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.GpsEntityDao;

/*文件类*/
@Entity
public class FileEntity {
    @Id
    private  Long id;
    private String filePath;

    private Long folderEntityId;
    @ToOne(joinProperty = "folderEntityId")
    private FolderEntity folderEntity;
    private Long gpsEntityId;
    @ToOne(joinProperty = "gpsEntityId")
    private GpsEntity gpsEntity;
    private long addTime;
    private boolean upload;
    @NotNull
    private Long folderId;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1194307080)
    private transient FileEntityDao myDao;
    @Generated(hash = 2020532681)
    private transient Long folderEntity__resolvedKey;
    @Generated(hash = 905661239)
    private transient Long gpsEntity__resolvedKey;



    public FileEntity(String filePath, long addTime, @NotNull Long folderId, Long folderEntityId) {
        this.filePath = filePath;
        this.addTime = addTime;
        this.folderId = folderId;
        this.folderEntityId = folderEntityId;
    }



    @Generated(hash = 568930200)
    public FileEntity(Long id, String filePath, Long folderEntityId, Long gpsEntityId, long addTime,
            boolean upload, @NotNull Long folderId) {
        this.id = id;
        this.filePath = filePath;
        this.folderEntityId = folderEntityId;
        this.gpsEntityId = gpsEntityId;
        this.addTime = addTime;
        this.upload = upload;
        this.folderId = folderId;
    }



    @Generated(hash = 1879603201)
    public FileEntity() {
    }



    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getFilePath() {
        return this.filePath;
    }



    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }



    public Long getFolderEntityId() {
        return this.folderEntityId;
    }



    public void setFolderEntityId(Long folderEntityId) {
        this.folderEntityId = folderEntityId;
    }



    public long getAddTime() {
        return this.addTime;
    }



    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }



    public boolean getUpload() {
        return this.upload;
    }



    public void setUpload(boolean upload) {
        this.upload = upload;
    }



    public Long getFolderId() {
        return this.folderId;
    }



    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }



    /** To-one relationship, resolved on first access. */
    @Generated(hash = 259996788)
    public FolderEntity getFolderEntity() {
        Long __key = this.folderEntityId;
        if (folderEntity__resolvedKey == null || !folderEntity__resolvedKey.equals(__key)) {
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
    @Generated(hash = 1431751175)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFileEntityDao() : null;
    }



    public Long getGpsEntityId() {
        return this.gpsEntityId;
    }



    public void setGpsEntityId(Long gpsEntityId) {
        this.gpsEntityId = gpsEntityId;
    }



    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1090568235)
    public GpsEntity getGpsEntity() {
        Long __key = this.gpsEntityId;
        if (gpsEntity__resolvedKey == null || !gpsEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GpsEntityDao targetDao = daoSession.getGpsEntityDao();
            GpsEntity gpsEntityNew = targetDao.load(__key);
            synchronized (this) {
                gpsEntity = gpsEntityNew;
                gpsEntity__resolvedKey = __key;
            }
        }
        return gpsEntity;
    }



    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 145618566)
    public void setGpsEntity(GpsEntity gpsEntity) {
        synchronized (this) {
            this.gpsEntity = gpsEntity;
            gpsEntityId = gpsEntity == null ? null : gpsEntity.getId();
            gpsEntity__resolvedKey = gpsEntityId;
        }
    }

}
