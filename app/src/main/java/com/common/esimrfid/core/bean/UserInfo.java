package com.common.esimrfid.core.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * UserInfo
 *@author rylai
 *created at 2019/6/20 15:33
 */
public class UserInfo implements Serializable {
    /**
     * 主键id
     */
    private String id;

    /**
     * 登陆名
     */
    private String userName;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 员工编号
     */
    private String userCode;

    /**
     * 头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer userGender;

    /**
     * 年龄
     */
    private Byte userAge;

    /**
     * 用户状态
     */
    private Byte userStatus;

    /**
     * 创建时间
     */
    private Date createDate;

    private Date updateDate;

    /**
     * 0:普通用户 1:超级管理员用户
     */
    private Integer userIsadmin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getUserGender() {
        return userGender;
    }

    public void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    public Byte getUserAge() {
        return userAge;
    }

    public void setUserAge(Byte userAge) {
        this.userAge = userAge;
    }

    public Byte getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Byte userStatus) {
        this.userStatus = userStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUserIsadmin() {
        return userIsadmin;
    }

    public void setUserIsadmin(Integer userIsadmin) {
        this.userIsadmin = userIsadmin;
    }
}
