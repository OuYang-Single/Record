package com.mmt.record.greendao;

import com.mmt.record.mvp.model.entity.FolderEntity;
import com.mmt.record.mvp.model.entity.GpsEntity;

import org.greenrobot.greendao.AbstractDao;

public class GpsEntityManager extends BaseBeanManager<GpsEntity, Long> {

    public GpsEntityManager(AbstractDao dao) {
        super(dao);
    }
}

