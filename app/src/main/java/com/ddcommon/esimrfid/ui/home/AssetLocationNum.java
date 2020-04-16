package com.ddcommon.esimrfid.ui.home;

import java.util.Objects;

public class AssetLocationNum {
    private String location;
    private int number;
    private int progress;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetLocationNum)) return false;
        AssetLocationNum that = (AssetLocationNum) o;
        return getNumber() == that.getNumber() &&
                getLocation().equals(that.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocation(), getNumber());
    }
}
