package com.common.esimrfid.core.bean;

import java.io.Serializable;
import java.util.List;

public class CorpInfo implements Serializable {

    private String corpAccount;

    private String corpAddress;

    private String corpEpcCode;

    private String corpId;

    private String corpName;

    private String corpPerson;

    private String corpPersonTel;

    private List<SignatureCard> signatureCards;

    public String getCorpAccount() {
        return corpAccount;
    }

    public void setCorpAccount(String corpAccount) {
        this.corpAccount = corpAccount;
    }

    public String getCorpAddress() {
        return corpAddress;
    }

    public void setCorpAddress(String corpAddress) {
        this.corpAddress = corpAddress;
    }

    public String getCorpEpcCode() {
        return corpEpcCode;
    }

    public void setCorpEpcCode(String corpEpcCode) {
        this.corpEpcCode = corpEpcCode;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpPerson() {
        return corpPerson;
    }

    public void setCorpPerson(String corpPerson) {
        this.corpPerson = corpPerson;
    }

    public String getCorpPersonTel() {
        return corpPersonTel;
    }

    public void setCorpPersonTel(String corpPersonTel) {
        this.corpPersonTel = corpPersonTel;
    }

    public List<SignatureCard> getSignatureCards() {
        return signatureCards;
    }

    public void setSignatureCards(List<SignatureCard> signatureCards) {
        this.signatureCards = signatureCards;
    }
}
