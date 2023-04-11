package com.mmt.record.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.mmt.record.greendao.DaoSession;
import com.mmt.record.greendao.FolderBeanDao;
import com.mmt.record.greendao.FileBeanDao;
@Entity
public class FileBean {
    @Id
    private  Long id;
    private String filePath;
    private String name;
    private Long folderEntityId;
    @ToOne(joinProperty = "folderEntityId")
    private FolderBean folderEntity;
    @NotNull
    private Long folderId;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 682641553)
    private transient FileBeanDao myDao;
    @Generated(hash = 224960527)
    public FileBean(Long id, String filePath, String name, Long folderEntityId,
            @NotNull Long folderId) {
        this.id = id;
        this.filePath = filePath;
        this.name = name;
        this.folderEntityId = folderEntityId;
        this.folderId = folderId;
    }
    public FileBean( String filePath, String name, Long folderEntityId,
                    @NotNull Long folderId) {
        this.filePath = filePath;
        this.name = name;
        this.folderEntityId = folderEntityId;
        this.folderId = folderId;
    }
    @Generated(hash = 1910776192)
    public FileBean() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getFolderEntityId() {
        return this.folderEntityId;
    }
    public void setFolderEntityId(Long folderEntityId) {
        this.folderEntityId = folderEntityId;
    }
    public Long getFolderId() {
        return this.folderId;
    }
    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
    @Generated(hash = 2020532681)
    private transient Long folderEntity__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 397003279)
    public FolderBean getFolderEntity() {
        Long __key = this.folderEntityId;
        if (folderEntity__resolvedKey == null
                || !folderEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FolderBeanDao targetDao = daoSession.getFolderBeanDao();
            FolderBean folderEntityNew = targetDao.load(__key);
            synchronized (this) {
                folderEntity = folderEntityNew;
                folderEntity__resolvedKey = __key;
            }
        }
        return folderEntity;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1392019234)
    public void setFolderEntity(FolderBean folderEntity) {
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
    @Generated(hash = 1469328431)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFileBeanDao() : null;
    }

}
