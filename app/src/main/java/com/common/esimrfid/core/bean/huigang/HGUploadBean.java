package com.common.esimrfid.core.bean.huigang;

import java.util.List;

public class HGUploadBean {

    /**
     * deviceId : testqqq
     * timestamp : 1607504806000
     * uid : testqqq-1607504806000
     * signal : [{"rssi":-74,"major":7,"minor":8,"power":-65,"accuracy":1.1,"uuid":"40D6F323-6732-4C7C-A438-C9A740D0BF15"},{"rssi":-70,"major":7,"minor":6,"power":-65,"accuracy":1.1,"uuid":"40D6F323-6732-4C7C-A438-C9A740D0BF15"},{"rssi":-65,"major":7,"minor":11,"power":-65,"accuracy":1.1,"uuid":"40D6F323-6732-4C7C-A438-C9A740D0BF15"}]
     */

    private String deviceId;
    private long timestamp;
    private String uid;
    private List<Signal> signal;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<Signal> getSignal() {
        return signal;
    }

    public void setSignal(List<Signal> signal) {
        this.signal = signal;
    }

    public static class Signal {
        /**
         * rssi : -74
         * major : 7
         * minor : 8
         * power : -65
         * accuracy : 1.1
         * uuid : 40D6F323-6732-4C7C-A438-C9A740D0BF15
         */

        private int rssi;
        private int major;
        private int minor;
        private int power;
        private double accuracy;
        private String uuid;

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public int getMajor() {
            return major;
        }

        public void setMajor(int major) {
            this.major = major;
        }

        public int getMinor() {
            return minor;
        }

        public void setMinor(int minor) {
            this.minor = minor;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(double accuracy) {
            this.accuracy = accuracy;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
