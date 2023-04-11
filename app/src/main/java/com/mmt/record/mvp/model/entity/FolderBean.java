package com.mmt.record.mvp.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.mmt.record.greendao.DaoSession;
import com.mmt.record.greendao.FileBeanDao;
import com.mmt.record.greendao.FolderBeanDao;
@Entity
public class FolderBean {
    @Id
    private  Long id;
    private String name;
    private String firstImagePath;
    @ToMany(referencedJoinProperty = "folderId")
    private List<FileBean> data;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 438594612)
    private transient FolderBeanDao myDao;
    @Generated(hash = 99542781)
    public FolderBean(Long id, String name, String firstImagePath) {
        this.id = id;
        this.name = name;
        this.firstImagePath = firstImagePath;
    }
    public FolderBean( String name, String firstImagePath) {
        this.name = name;
        this.firstImagePath = firstImagePath;
    }
    @Generated(hash = 1368532233)
    public FolderBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getFirstImagePath() {
        return this.firstImagePath;
    }
    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2114816180)
    public List<FileBean> getData() {
        if (data == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FileBeanDao targetDao = daoSession.getFileBeanDao();
            List<FileBean> dataNew = targetDao._queryFolderBean_Data(id);
            synchronized (this) {
                if (data == null) {
                    data = dataNew;
                }
            }
        }
        return data;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1283600904)
    public synchronized void resetData() {
        data = null;
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
    @Generated(hash = 1153476043)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFolderBeanDao() : null;
    }
  

}
