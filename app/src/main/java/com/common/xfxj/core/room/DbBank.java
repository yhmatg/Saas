package com.common.xfxj.core.room;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.common.xfxj.app.EsimAndroidApp;
import com.common.xfxj.core.bean.nanhua.jsonbeans.AssetsAllInfo;
import com.common.xfxj.core.bean.nanhua.jsonbeans.InventoryDetail;
import com.common.xfxj.core.bean.nanhua.jsonbeans.ResultInventoryOrder;
import com.common.xfxj.core.bean.nanhua.xfxj.XfAssetsAllInfo;
import com.common.xfxj.core.bean.nanhua.xfxj.XfInventoryDetail;
import com.common.xfxj.core.bean.nanhua.xfxj.XfResultInventoryOrder;
import com.common.xfxj.core.dao.AssetsAllInfoDao;
import com.common.xfxj.core.dao.InventoryDetailDao;
import com.common.xfxj.core.dao.ResultInventoryOrderDao;
import com.common.xfxj.core.dao.XAssetsAllInfoDao;
import com.common.xfxj.core.dao.XInventoryDetailDao;
import com.common.xfxj.core.dao.XResultInventoryOrderDao;

@Database(entities = {
        InventoryDetail.class,
        ResultInventoryOrder.class,
        AssetsAllInfo.class,
        XfInventoryDetail.class,
        XfResultInventoryOrder.class,
        XfAssetsAllInfo.class,
        }
        , version = 2)
@TypeConverters(DateConverter.class)
public abstract class DbBank extends RoomDatabase {
    public static final String DB_NAME = "inventory.db";
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
        }).allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        return build;
    }

    public abstract InventoryDetailDao getInventoryDetailDao();

    public abstract ResultInventoryOrderDao getResultInventoryOrderDao();

    public abstract AssetsAllInfoDao getAssetsAllInfoDao();

    public abstract XInventoryDetailDao getXfInventoryDetailDao();

    public abstract XResultInventoryOrderDao getXfResultInventoryOrderDao();

    public abstract XAssetsAllInfoDao getXfAssetsAllInfoDao();


}