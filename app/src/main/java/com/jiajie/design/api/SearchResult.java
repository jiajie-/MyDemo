package com.jiajie.design.api;

/**
 * SearchResult
 * Created by jiajie on 16/7/7.
 */
public class SearchResult {

    private String ganhuo_id;
    private String desc;
    private String publishedAt;
    private String readability;
    private String type;
    private String url;
    private String who;

    public SearchResult() {
    }

    public String getGanhuo_id() {
        return ganhuo_id;
    }

    public void setGanhuo_id(String ganhuo_id) {
        this.ganhuo_id = ganhuo_id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getReadability() {
        return readability;
    }

    public void setReadability(String readability) {
        this.readability = readability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    @Override
    public String toString() {
        return "ganhuo_id:" + ganhuo_id
                + "\ndesc:" + desc
                + "\npublishedAt:" + publishedAt
                + "\nreadability:" + readability
                + "\ntype:" + type
                + "\nurl:" + url
                + "\nwho:" + who;
    }
}
