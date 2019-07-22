package com.common.esimrfid.core.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class InvDetail implements Serializable {
    @PrimaryKey(autoGenerate=true)
    @NonNull
    private int id;
    private String accName;
    private String corpEpcCode;
    private String corpAccount;
    private String corpName;
    private String cardCode;
    private String bankName;
    private String invId;
    private Date cardRecordDate;
    private int invdtStatus;


    public String getInvId() {
        return invId;
    }

    public void setInvId(String invId) {
        this.invId = invId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getCorpEpcCode() {
        return corpEpcCode;
    }

    public void setCorpEpcCode(String corpEpcCode) {
        this.corpEpcCode = corpEpcCode;
    }

    public String getCorpAccount() {
        return corpAccount;
    }

    public void setCorpAccount(String corpAccount) {
        this.corpAccount = corpAccount;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getCardRecordDate() {
        return cardRecordDate;
    }

    public void setCardRecordDate(Date cardRecordDate) {
        this.cardRecordDate = cardRecordDate;
    }

    public int getInvdtStatus() {
        return invdtStatus;
    }

    public void setInvdtStatus(int invdtStatus) {
        this.invdtStatus = invdtStatus;
    }
    public String getIvnStatusString(){
        String statusString="";
        switch(invdtStatus){
            case 0:
                statusString="未盘";
                break;
            case 1:
                statusString="已盘";
                break;
        }
        return statusString;
    }
}
