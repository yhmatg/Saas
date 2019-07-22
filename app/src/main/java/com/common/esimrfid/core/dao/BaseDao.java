package com.common.esimrfid.core.dao;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {
    @Insert
    public void insertItem(T item);//插入单条数据

    @Insert
    public void insertItems(List<T> items);//插入list

    @Delete
    public void deleteItem(T item);//删除
    @Update
    public void updateItem(T item);//更新
}
