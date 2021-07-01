package com.common.esimrfid.ui.assetsearch;

import java.util.Objects;

public class AssetDetailItem {
    String name;
    String content;

    public AssetDetailItem(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssetDetailItem)) return false;
        AssetDetailItem that = (AssetDetailItem) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
