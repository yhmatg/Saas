package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.common.esimrfid.core.bean.nanhua.DbUser;

@Dao
public interface DbUserDao extends BaseDao<DbUser> {

    @Query("SELECT * FROM DbUser limit 1")
    public DbUser findDbUserInfo();
}
