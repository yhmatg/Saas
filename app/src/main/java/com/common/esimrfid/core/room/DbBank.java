package com.common.esimrfid.core.room;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.common.esimrfid.app.EsimAndroidApp;
import com.common.esimrfid.core.bean.InvDetail;
import com.common.esimrfid.core.bean.InvOrder;
import com.common.esimrfid.core.bean.nanhua.DbUser;
import com.common.esimrfid.core.dao.InvDetailDao;
import com.common.esimrfid.core.dao.InvOrderDao;
import com.common.esimrfid.core.dao.DbUserDao;

@Database(entities = {InvOrder.class, InvDetail.class, DbUser.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class DbBank extends RoomDatabase {
    public static final String DB_NAME = "bank.db";
    private static volatile DbBank instance;

    public static synchronized DbBank getInstance() {
        if (instance == null) {
            instance = createDb();
        }
        return instance;
    }

    private static DbBank createDb() {
        DbBank build = Room.databaseBuilder(
                EsimAndroidApp.getInstance(),
                DbBank.class,
                DB_NAME).addCallback(new RoomDatabase.Callback() {
            //第一次创建数据库时调用，但是在创建所有表之后调用的
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            //当数据库被打开时调用
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        }).allowMainThreadQueries().build();
        return build;
    }

    public abstract InvOrderDao getInvOrderDao();

    public abstract InvDetailDao getInvDetailDao();

    public abstract DbUserDao getDbUserDao();
}