package com.common.esimrfid.core.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.common.esimrfid.core.bean.InvDetail;

import java.util.List;

@Dao
public interface InvDetailDao extends BaseDao<InvDetail> {


    @Query("SELECT * FROM invdetail where invId = :orderId")
    public List<InvDetail> findInvDetailByOrderId(String orderId);

    @Query("DELETE from invdetail where invId =:orderId")
    public void deleteInvDetailByOrderId(String orderId);

    @Query("UPDATE invdetail SET invdtStatus = 1 where corpEpcCode = :epc and invId = :orderId")
    public void updateInvStatusByEpc(String epc, String orderId);

}
