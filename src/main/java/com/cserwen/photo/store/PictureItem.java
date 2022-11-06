package com.cserwen.photo.store;

import java.util.ArrayList;
import java.util.List;

public class PictureItem {
    private List<String> names = new ArrayList<>();
    private final long uploadTime = System.currentTimeMillis();
    private String desc;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
