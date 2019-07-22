package com.common.esimrfid.core.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.common.esimrfid.core.bean.InvOrder;

import java.util.List;

@Dao
public interface InvOrderDao extends BaseDao<InvOrder> {

    @Query("SELECT * FROM invorder order by createDate desc limit 1")
    public InvOrder findOneNewestOrder();

    @Query("SELECT * FROM invorder order by createDate desc limit 20")
    public List<InvOrder> findNewestOrder();

    @Query("SELECT * FROM invorder order by createDate desc limit :start ,:end")
    List<InvOrder> findNewestOrderMore(int start, int end);

    @Query("DELETE FROM invorder")
    void deleteAll();

}