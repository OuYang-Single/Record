package com.mmt.record.greendao;

import com.mmt.record.mvp.model.entity.FileEntity;
import com.mmt.record.mvp.model.entity.User;

import org.greenrobot.greendao.AbstractDao;

public class FileEntityManager extends BaseBeanManager<FileEntity, Long> {

    public FileEntityManager(AbstractDao dao) {
        super(dao);
    }
}

