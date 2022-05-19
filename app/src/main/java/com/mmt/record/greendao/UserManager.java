package com.mmt.record.greendao;


import com.mmt.record.mvp.model.entity.User;

import org.greenrobot.greendao.AbstractDao;

public class UserManager extends BaseBeanManager<User, Long> {

    public UserManager(AbstractDao dao) {
        super(dao);
    }
}
