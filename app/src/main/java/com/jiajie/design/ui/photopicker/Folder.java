package com.jiajie.design.ui.photopicker;

/**
 * FolderBean
 * Created by jiajie on 16/9/12.
 */
class Folder {

    private String dir;
    private String firstImagePath;
    private String name;
    private int count;

    String getDir() {
        return dir;
    }

    void setDir(String dir) {
        this.dir = dir;
        int lastIndexOf = this.dir.lastIndexOf("/");
        this.name = this.dir.substring(lastIndexOf + 1);
    }

    String getFirstImagePath() {
        return firstImagePath;
    }

    public String getName() {
        return name;
    }

    void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
