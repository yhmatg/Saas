package com.common.esimrfid.core.bean.nanhua.jsonbeans;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.support.annotation.NonNull;

@Entity
public class MoreInventotyDetail {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer localId;
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded
    private InventoryDetail inventoryDetail;

    @NonNull
    public Integer getLocalId() {
        return localId;
    }

    public void setLocalId(@NonNull Integer localId) {
        this.localId = localId;
    }

    public InventoryDetail getInventoryDetail() {
        return inventoryDetail;
    }

    public void setInventoryDetail(InventoryDetail inventoryDetail) {
        this.inventoryDetail = inventoryDetail;
    }
}
