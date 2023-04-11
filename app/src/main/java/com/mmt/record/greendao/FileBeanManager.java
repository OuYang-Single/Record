package com.mmt.record.greendao;

import com.mmt.record.mvp.model.entity.FileBean;
import com.mmt.record.mvp.model.entity.FolderBean;

import org.greenrobot.greendao.AbstractDao;


public class FileBeanManager extends BaseBeanManager<FileBean, Long> {

    public FileBeanManager(AbstractDao dao) {
        super(dao);
    }
}

