package com.vns.webstore.middleware.entity;

/**
 * Created by LAP10572-local on 8/18/2016.
 */
public class Article {
    private String id;
    private String title;
    private String shotDesc;
    private String imageUrl;
    private String url;
    private String strDate;
    private String fromWebSite;
    private String websiteAvatar;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShotDesc() {
        return shotDesc;
    }

    public void setShotDesc(String shotDesc) {
        this.shotDesc = shotDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public String getFromWebSite() {
        return fromWebSite;
    }

    public void setFromWebSite(String fromWebSite) {
        this.fromWebSite = fromWebSite;
    }

    public String getWebsiteAvatar() {
        return websiteAvatar;
    }

    public void setWebsiteAvatar(String websiteAvatar) {
        this.websiteAvatar = websiteAvatar;
    }
}
