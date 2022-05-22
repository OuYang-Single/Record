package com.mmt.record.greendao;

import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.FolderEntity;

import org.greenrobot.greendao.AbstractDao;

public class FolderEntityManager extends BaseBeanManager<FolderEntity, Long> {

    public FolderEntityManager(AbstractDao dao) {
        super(dao);
    }
}

