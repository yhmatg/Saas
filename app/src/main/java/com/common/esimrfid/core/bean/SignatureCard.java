package com.common.esimrfid.core.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity
public class SignatureCard implements Serializable {


    @PrimaryKey
    @NonNull
    private String id;

    private String accName;

    private String bankId;

    private String bankName;

    private String cardCode;

    private Date cardCreateDate;

    private String cardMailCode;

    private Date cardRecordDate;

    private String corpAccount;

    private String creatorName;

    private Date createDate;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public Date getCardCreateDate() {
        return cardCreateDate;
    }

    public void setCardCreateDate(Date cardCreateDate) {
        this.cardCreateDate = cardCreateDate;
    }

    public String getCardMailCode() {
        return cardMailCode;
    }

    public void setCardMailCode(String cardMailCode) {
        this.cardMailCode = cardMailCode;
    }

    public Date getCardRecordDate() {
        return cardRecordDate;
    }

    public void setCardRecordDate(Date cardRecordDate) {
        this.cardRecordDate = cardRecordDate;
    }

    public String getCorpAccount() {
        return corpAccount;
    }

    public void setCorpAccount(String corpAccount) {
        this.corpAccount = corpAccount;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
