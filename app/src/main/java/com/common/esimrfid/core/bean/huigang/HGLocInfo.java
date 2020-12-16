package com.common.esimrfid.core.bean.huigang;

public class HGLocInfo {

    /**
     * timestamp : 1607504806000
     * uid : testqqq-1607504806000
     * location : {"x":12681821,"y":2570746,"floor":22}
     * buildingId : 07552001
     */

    private long timestamp;
    private String uid;
    private Location location;
    private String buildingId;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public static class Location {
        /**
         * x : 12681821
         * y : 2570746
         * floor : 22
         */

        private int x;
        private int y;
        private int floor;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getFloor() {
            return floor;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }
    }
}
