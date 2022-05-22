package com.mmt.record.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.mmt.record.greendao.DaoSession;
import com.mmt.record.greendao.FileEntityDao;
import com.mmt.record.greendao.FolderEntityDao;

/*文件夹类*/
@Entity
public class FolderEntity {
    @Id
    private  Long id;

    private String folderPath;

    private long addTime;
    @ToMany(referencedJoinProperty = "folderId")
    List<FileEntity>fileEntities;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1097204649)
    private transient FolderEntityDao myDao;
    @Generated(hash = 1880425172)
    public FolderEntity(Long id, String folderPath, long addTime) {
        this.id = id;
        this.folderPath = folderPath;
        this.addTime = addTime;
    }

    public FolderEntity( String folderPath, long addTime) {
        this.folderPath = folderPath;
        this.addTime = addTime;
    }
    @Generated(hash = 1378061393)
    public FolderEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFolderPath() {
        return this.folderPath;
    }
    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }
    public long getAddTime() {
        return this.addTime;
    }
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1982008036)
    public List<FileEntity> getFileEntities() {
        if (fileEntities == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileEntityDao targetDao = daoSession.getFileEntityDao();
            List<FileEntity> fileEntitiesNew = targetDao
                    ._queryFolderEntity_FileEntities(id);
            synchronized (this) {
                if (fileEntities == null) {
                    fileEntities = fileEntitiesNew;
                }
            }
        }
        return fileEntities;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1885993195)
    public synchronized void resetFileEntities() {
        fileEntities = null;
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
    @Generated(hash = 118105580)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFolderEntityDao() : null;
    }
}
