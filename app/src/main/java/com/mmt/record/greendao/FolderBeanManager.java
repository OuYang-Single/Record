package com.mmt.record.greendao;

import com.mmt.record.mvp.model.entity.FolderBean;
import com.mmt.record.mvp.model.entity.FolderEntity;

import org.greenrobot.greendao.AbstractDao;

public class FolderBeanManager extends BaseBeanManager<FolderBean, Long> {

    public FolderBeanManager(AbstractDao dao) {
        super(dao);
    }
}

