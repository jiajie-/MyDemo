package com.jiajie.design.widgets.radar;

import java.util.List;

/**
 * RadarItem for RadarView
 * Created by jiajie on 16/9/6.
 */
public class RadarItem {

    int id;
    String name;
    List<Value> list;

    public RadarItem() {
        this.id = 0;
        this.name = "null";
        this.list = null;
    }

    public RadarItem(int id, String name, List<Value> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Value> getList() {
        return list;
    }

    public void setList(List<Value> list) {
        this.list = list;
    }
}
