package com.ddcommon.esimrfid.core.bean.update;

public class UpdateVersion {

    /**
     * app_download_url : www.esimtekiot.com/app/download/assets.apk
     * app_must_upgrade : 0
     * app_upgrade_message : 1.修复了已知BUG；2.优化了一些体验；3.新增了功能
     * app_version : v1.0.2
     * app_version_code : 1
     * create_date : 1577242474000
     * id : 1
     * update_date : 1577226825000
     */

    private String app_download_url;
    private int app_must_upgrade;
    private String app_upgrade_message;
    private String app_version;
    private int app_version_code;

    public String getApp_download_url() {
        return app_download_url;
    }

    public void setApp_download_url(String app_download_url) {
        this.app_download_url = app_download_url;
    }

    public int getApp_must_upgrade() {
        return app_must_upgrade;
    }

    public void setApp_must_upgrade(int app_must_upgrade) {
        this.app_must_upgrade = app_must_upgrade;
    }

    public String getApp_upgrade_message() {
        return app_upgrade_message;
    }

    public void setApp_upgrade_message(String app_upgrade_message) {
        this.app_upgrade_message = app_upgrade_message;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public int getApp_version_code() {
        return app_version_code;
    }

    public void setApp_version_code(int app_version_code) {
        this.app_version_code = app_version_code;
    }
}
